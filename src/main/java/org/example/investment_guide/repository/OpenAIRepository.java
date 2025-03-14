package org.example.investment_guide.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;
import org.example.investment_guide.dto.APIClientParam;
import org.example.investment_guide.dto.BaseLLMBody;
import org.example.investment_guide.dto.OpenAIAPIParam;
import org.example.investment_guide.util.APIClient;
import org.example.investment_guide.util.PromptFactory;

import java.util.List;

public class OpenAIRepository implements APIClient {
    private static final OpenAIRepository instance = new OpenAIRepository();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
    private static final String API_KEY = dotenv.get("OPENAI_KEY");
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    private OpenAIRepository() {}

    public static OpenAIRepository getInstance() {
        return instance;
    }

    public String callAPI(OpenAIAPIParam param) throws JsonProcessingException {
        String[] headers = {
                "Authorization", "Bearer " + API_KEY,
                "Content-Type", "application/json"
        };

        String modelName = switch (param.modelType()) {
            case BASE -> "gpt-4-turbo";
            case REASONING -> "gpt-4";
        };

        String systemMessage = PromptFactory.generateSystemMessage(param.modelType());

        BaseLLMBody requestBody = new BaseLLMBody(
                modelName,
                List.of(
                        new BaseLLMBody.Message("system", systemMessage),
                        new BaseLLMBody.Message("user", param.prompt())
                )
        );

        APIClientParam request = new APIClientParam(API_URL, "POST",
                objectMapper.writeValueAsString(requestBody), headers);

        return APIClient.super.callAPI(request);
    }
}
