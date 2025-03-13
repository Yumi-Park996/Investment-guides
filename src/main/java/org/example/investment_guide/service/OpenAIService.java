package org.example.investment_guide.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.investment_guide.model.dto.BaseLLMResponse;
import org.example.investment_guide.model.dto.ModelType;
import org.example.investment_guide.model.dto.OpenAIAPIParam;
import org.example.investment_guide.model.repository.OpenAIRepository;
import java.util.logging.Logger;

public class OpenAIService {
    private OpenAIService() {}
    private static final OpenAIService instance = new OpenAIService();
    private ObjectMapper objectMapper = new ObjectMapper();
    private final OpenAIRepository repository = OpenAIRepository.getInstance();
    private static final Logger logger = Logger.getLogger(OpenAIService.class.getName());

    public static OpenAIService getInstance() {
        return instance;
    }

    /**
     * 모델을 선택하여 OpenAI API를 호출하는 메서드
     * @param prompt 유저 입력 프롬프트
     * @param modelType 사용할 모델 타입 (Base, Reasoning, Image)
     * @param retrievalEnabled RAG 적용 여부
     * @return OpenAI LLM의 응답 텍스트
     * @throws JsonProcessingException JSON 파싱 예외
     */
    public String useModel(String prompt, ModelType modelType, boolean retrievalEnabled) throws JsonProcessingException {
        // ✅ 프롬프트 사전처리 (투자 관련 맞춤 설정)
        String promptPreProcessing = switch (modelType) {
            case BASE -> """
                    You are an expert investment mentor who helps users understand financial concepts, evaluate market opportunities, and make informed decisions. Your responses should be engaging, structured like a lesson, and highly adaptable to different types of investment-related questions.
                    
                    🔹 **How to Respond:**
                    1. First, **identify the core intent** of the user's question. \s
                       - Are they asking for **market trends**, **investment strategies**, **company evaluations**, **risk management**, or **technical/psychological insights**?
                       - Adjust the structure accordingly rather than forcing a rigid format.
                    
                    2. Provide a **clear, well-structured explanation** while keeping it engaging. \s
                       - Use simple language but maintain depth.
                       - Use **examples and analogies** to make complex ideas intuitive.
                    
                    3. **Encourage the user to think critically.** \s
                       - Don't just list facts—explain **why** something is important.
                       - Pose **follow-up questions** to help users refine their investment approach.
                    
                    🔹 **Response Flow (Flexible, Adaptable to Question Type)**
                    
                    ✅ **1. Understanding the User's Question & Context** \s
                    - Briefly restate or interpret the question to clarify the user’s intent. \s
                    - Provide **relevant background information** (if necessary). \s
                    
                    ✅ **2. Key Insights & Analysis** (Adaptable to the question type) \s
                    - If market trends: Explain economic conditions & their impact. \s
                    - If investment strategies: Compare options & explain pros/cons. \s
                    - If company evaluation: Discuss valuation metrics, competitive advantages, risks. \s
                    - If risk management: Explain risk factors, diversification, and mitigation strategies. \s
                    
                    ✅ **3. Thought-Provoking Questions & Actionable Next Steps** \s
                    - Offer **questions the user should consider** before making an investment. \s
                    - Provide **practical next steps** based on their experience level. \s
                    
                🔹 **User Question:**  
                %s
                """.formatted(prompt);

            case REASONING -> """
                    You are an advanced investment strategist who specializes in **critical thinking, risk assessment, and behavioral finance**. Your role is not just to provide information but to guide users in **developing a strong investment mindset**.
                    
               🔹 **How to Respond:**
               1. **Deconstruct the user's question logically.** \s
                  - What are the key variables affecting this decision? \s
                  - What potential biases or emotional factors might be involved? \s

               2. Provide a **structured but thought-provoking response.** \s
                  - Break down the decision-making process step by step. \s
                  - Present **different perspectives** rather than a single answer. \s

               3. Encourage the user to **question their assumptions & think deeper.** \s
                  - Use **Socratic questioning** (e.g., "What if the market condition changes?"). \s
                  - Offer **contrasting viewpoints** to highlight potential blind spots. \s

               📌 **User's Question:** \s
                %s
                """.formatted(prompt);
        };

        logger.info("🟢 OpenAIService.useModel() 실행됨");
        logger.info("📌 입력 프롬프트: " + prompt);
        logger.info("📌 모델 타입: " + modelType);
        logger.info("📌 RAG 활성화 여부: " + retrievalEnabled);

        // ✅ systemPrompt와 prompt를 합쳐서 OpenAI에게 보냄
        String responseText = repository.callAPI(new OpenAIAPIParam(promptPreProcessing, modelType), retrievalEnabled);

        logger.info("✅ OpenAI API 응답 받음: " + responseText);

        return objectMapper.readValue(responseText, BaseLLMResponse.class).choices().get(0).message().content();
    }

    // 개별 모델 호출 메서드 (편의성 제공)
    public String useBaseModel(String prompt, boolean retrievalEnabled) throws JsonProcessingException {
        return useModel(prompt, ModelType.BASE, retrievalEnabled);
    }

    public String useReasoning(String prompt) throws JsonProcessingException {
        return useModel(prompt, ModelType.REASONING, false);
    }
}
