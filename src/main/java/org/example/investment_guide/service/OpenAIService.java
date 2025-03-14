package org.example.investment_guide.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.investment_guide.dto.BaseLLMResponse;
import org.example.investment_guide.common.ModelType;
import org.example.investment_guide.dto.OpenAIAPIParam;
import org.example.investment_guide.repository.OpenAIRepository;
import org.example.investment_guide.util.PromptFactory;
import java.util.logging.Logger;

public class OpenAIService {
    private static final OpenAIService instance = new OpenAIService();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final OpenAIRepository repository = OpenAIRepository.getInstance();
    private static final Logger logger = Logger.getLogger(OpenAIService.class.getName());

    private OpenAIService() {}

    public static OpenAIService getInstance() {
        return instance;
    }

    /**
     * OpenAI 모델을 호출하는 메서드
     * @param prompt 유저 질문
     * @param modelType 사용할 모델 타입 (Base, Reasoning)
     * @return OpenAI LLM의 응답 텍스트
     * @throws JsonProcessingException JSON 파싱 예외
     */
    public String useModel(String prompt, ModelType modelType) throws JsonProcessingException {
        logger.info("🟢 OpenAIService.useModel() 실행됨");
        logger.info("📌 입력 프롬프트: " + prompt);
        logger.info("📌 모델 타입: " + modelType);

        // ✅ 프롬프트 생성 (PromptFactory 사용)
        String processedPrompt = modelType == ModelType.BASE
                ? PromptFactory.generatePrompt(prompt, modelType)  // gpt-4-turbo
                : "[Advanced Analysis Mode]\n" + PromptFactory.generatePrompt(prompt, modelType);  // gpt-4

        // ✅ OpenAI API 호출
        String responseText = repository.callAPI(new OpenAIAPIParam(processedPrompt, modelType));

        logger.info("✅ OpenAI API 응답 받음: " + responseText);

        BaseLLMResponse llmResponse = objectMapper.readValue(responseText, BaseLLMResponse.class);

        if (llmResponse.choices() == null || llmResponse.choices().isEmpty()) {
            logger.severe("❌ OpenAI API 응답 오류: choices가 비어 있음.");
            return "❌ AI가 유효한 응답을 생성하지 못했습니다. 다시 시도해 주세요.";
        }

        return llmResponse.choices().get(0).message().content();
    }

    public String useBaseModel(String prompt) throws JsonProcessingException {
        return useModel(prompt, ModelType.BASE);
    }

    public String useReasoningModel(String prompt) throws JsonProcessingException {
        return useModel(prompt, ModelType.REASONING);
    }
}
