package org.example.investment_guide.repository;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import io.github.cdimascio.dotenv.Dotenv;
import org.example.investment_guide.model.FAQ;
import org.example.investment_guide.model.YouTubeVideo;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class SupabaseRepository {
    private static final Logger logger = Logger.getLogger(SupabaseRepository.class.getName());
    private static final Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
    private static final String SUPABASE_URL = dotenv.get("SUPABASE_URL");
    private static final String SUPABASE_KEY = dotenv.get("SUPABASE_KEY");

    // ✅ 캐싱을 위한 필드 추가
    private static List<YouTubeVideo> youtubeVideos = new ArrayList<>();
    private static List<FAQ> faqs = new ArrayList<>();

    /** ✅ 유튜브 영상 데이터를 Supabase에서 가져오는 메서드 */
    public static void loadYouTubeVideos() {
        if (SUPABASE_URL == null || SUPABASE_KEY == null) {
            logger.severe("❌ Supabase 환경 변수가 설정되지 않음.");
            return;
        }

        String url = SUPABASE_URL + "/rest/v1/youtube_videos?select=title,url";
        HttpResponse<JsonNode> jsonResponse = Unirest.get(url)
                .header("apikey", SUPABASE_KEY)
                .header("Authorization", "Bearer " + SUPABASE_KEY)
                .header("Content-Type", "application/json")
                .asJson();

        if (jsonResponse.getStatus() != 200) {
            logger.severe("❌ 유튜브 데이터 요청 실패: " + jsonResponse.getStatusText());
            return;
        }

        youtubeVideos.clear(); // 기존 데이터 초기화
        JSONArray jsonArray = jsonResponse.getBody().getArray();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            String title = obj.optString("title", "제목 없음");
            String urlLink = obj.optString("url", "#");

            youtubeVideos.add(new YouTubeVideo(title, urlLink));
        }

        logger.info("✅ Supabase에서 유튜브 영상 " + youtubeVideos.size() + "개 로드 완료.");
    }

    /** ✅ FAQ 데이터를 Supabase에서 가져오는 메서드 */
    public static void loadFAQs() {
        if (SUPABASE_URL == null || SUPABASE_KEY == null) {
            logger.severe("❌ Supabase 환경 변수가 설정되지 않음.");
            return;
        }

        String url = SUPABASE_URL + "/rest/v1/investment_faqs?select=question,answer";
        HttpResponse<JsonNode> jsonResponse = Unirest.get(url)
                .header("apikey", SUPABASE_KEY)
                .header("Authorization", "Bearer " + SUPABASE_KEY)
                .header("Content-Type", "application/json")
                .asJson();

        if (jsonResponse.getStatus() != 200) {
            logger.severe("❌ FAQ 데이터 요청 실패: " + jsonResponse.getStatusText());
            return;
        }

        faqs.clear(); // 기존 데이터 초기화
        JSONArray jsonArray = jsonResponse.getBody().getArray();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            String question = obj.optString("question", "질문 없음");
            String answer = obj.optString("answer", "답변 없음");

            faqs.add(new FAQ(question, answer));
        }

        logger.info("✅ Supabase에서 FAQ " + faqs.size() + "개 로드 완료.");
    }

    /** ✅ 캐싱된 유튜브 데이터 반환 */
    public static List<YouTubeVideo> getYouTubeVideos() {
        return youtubeVideos;
    }

    /** ✅ 캐싱된 FAQ 데이터 반환 */
    public static List<FAQ> getFAQs() {
        return faqs;
    }
}
