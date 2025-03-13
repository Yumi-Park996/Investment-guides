package org.example.investment_guide.listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import org.example.investment_guide.model.repository.SupabaseRepository;
import org.example.investment_guide.model.dto.FAQ;
import org.example.investment_guide.model.dto.YouTubeVideo;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@WebListener
public class SupabaseInitializer implements ServletContextListener {
    private static final Logger logger = Logger.getLogger(SupabaseInitializer.class.getName());
    private static final Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
    private static final String SUPABASE_URL = dotenv.get("SUPABASE_URL");
    private static final String SUPABASE_KEY = dotenv.get("SUPABASE_KEY");

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("📡 [SupabaseLoader] 서버 시작 시 Supabase에서 데이터 로드 시작...");

        // ✅ 유튜브 영상 및 FAQ 데이터 로드
        List<YouTubeVideo> youtubeVideos = loadYouTubeVideos();
        List<FAQ> faqs = loadFAQs();

        // ✅ 로드된 데이터 개수 로그 추가
        logger.info("✅ Supabase에서 로드된 FAQ 개수: " + faqs.size());

        // ✅ SupabaseClient에 데이터 저장
        SupabaseRepository.setYouTubeVideos(youtubeVideos);
        SupabaseRepository.setFAQs(faqs);

        // ✅ JSP에서 사용할 수 있도록 application에 저장
        sce.getServletContext().setAttribute("faqs", faqs);

        logger.info("✅ [SupabaseLoader] 데이터 로드 완료!");
    }

    /** ✅ Supabase에서 유튜브 영상 불러오기 */
    private List<YouTubeVideo> loadYouTubeVideos() {
        List<YouTubeVideo> youtubeVideos = new ArrayList<>();
        if (SUPABASE_URL == null || SUPABASE_KEY == null) {
            logger.severe("❌ Supabase 환경 변수가 설정되지 않음.");
            return youtubeVideos;
        }

        String url = SUPABASE_URL + "/rest/v1/youtube_videos?select=title,url";
        HttpResponse<JsonNode> jsonResponse = Unirest.get(url)
                .header("apikey", SUPABASE_KEY)
                .header("Authorization", "Bearer " + SUPABASE_KEY)
                .header("Content-Type", "application/json")
                .asJson();

        if (jsonResponse.getStatus() != 200) {
            logger.severe("❌ API 요청 실패: " + jsonResponse.getStatusText());
            return youtubeVideos;
        }

        JSONArray jsonArray = jsonResponse.getBody().getArray();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            String title = obj.optString("title", "제목 없음");
            String urlLink = obj.optString("url", "#");

            youtubeVideos.add(new YouTubeVideo(title, urlLink));
        }

        return youtubeVideos;
    }

    /** ✅ Supabase에서 FAQ 불러오기 */
    private List<FAQ> loadFAQs() {
        List<FAQ> faqs = new ArrayList<>();
        if (SUPABASE_URL == null || SUPABASE_KEY == null) {
            logger.severe("❌ Supabase 환경 변수가 설정되지 않음.");
            return faqs;
        }

        String url = SUPABASE_URL + "/rest/v1/investment_faqs?select=question,answer";
        HttpResponse<JsonNode> jsonResponse = Unirest.get(url)
                .header("apikey", SUPABASE_KEY)
                .header("Authorization", "Bearer " + SUPABASE_KEY)
                .header("Content-Type", "application/json")
                .asJson();

        if (jsonResponse.getStatus() != 200) {
            logger.severe("❌ API 요청 실패: " + jsonResponse.getStatusText());
            return faqs;
        }

        JSONArray jsonArray = jsonResponse.getBody().getArray();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            String question = obj.optString("question", "질문 없음");
            String answer = obj.optString("answer", "답변 없음");

            faqs.add(new FAQ(question, answer));
        }

        return faqs;
    }
}
