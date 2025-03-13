package org.example.investment_guide.model.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.investment_guide.model.dto.APIClientParam;
import org.example.investment_guide.model.dto.BaseLLMBody;
import org.example.investment_guide.model.dto.OpenAIAPIParam;
import org.example.investment_guide.util.APIClient;

import java.util.List;

public class OpenAIRepository implements APIClient {
    private OpenAIRepository() {}
    private static final OpenAIRepository instance = new OpenAIRepository();
    public static OpenAIRepository getInstance() {
        return instance;
    }

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String callAPI(OpenAIAPIParam param, boolean retrievalEnabled) throws JsonProcessingException {
        String token = dotenv.get("OPENAI_KEY");
        String url = "https://api.openai.com/v1/chat/completions";
        String method = "POST";

        String[] headers = {
                "Authorization", "Bearer " + token,
                "Content-Type", "application/json"
        };

        // ✅ RAG 적용 여부에 따라 시스템 메시지 결정
        String systemMessage = retrievalEnabled ?
                "You have access to a database of high-quality YouTube videos. Utilize retrieved information to recommend the most useful and relevant videos in Korean." :
                "Recommend videos based on general knowledge. Do not assume information beyond well-known content, and focus on Korean user preferences.";

        BaseLLMBody requestBody = new BaseLLMBody(
                "gpt-4-turbo",
                List.of(
                        new BaseLLMBody.Message("system", systemMessage),
                        new BaseLLMBody.Message("user", param.prompt())
                )
        );

        String body = objectMapper.writeValueAsString(requestBody);

        return APIClient.super.callAPI(new APIClientParam(url, method, body, headers));
    }
}
