package org.example.investment_guide.database;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import java.util.List;
import java.util.Map;

public class SupabaseClient {
    private static final String SUPABASE_URL = "https://yjteaicrczirwmsfejeo.supabase.co";
    private static final String SUPABASE_KEY = "Bearer YOUR_SUPABASE_SERVICE_ROLE_KEY";

    public static List<Map<String, Object>> fetchFAQs() {
        HttpResponse<JsonNode> response = Unirest.get(SUPABASE_URL + "/rest/v1/investment_faqs")
                .header("apikey", SUPABASE_KEY)
                .header("Authorization", SUPABASE_KEY)
                .header("Content-Type", "application/json")
                .asJson();

        return response.getBody().getArray().toList();
    }
}
