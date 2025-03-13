package org.example.investment_guide.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.investment_guide.model.repository.SupabaseRepository;
import org.example.investment_guide.service.OpenAIService;
import org.example.investment_guide.model.dto.YouTubeVideo;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

@WebServlet("/answer")
public class AnswerController extends HttpServlet {
    private static final Logger logger = Logger.getLogger(AnswerController.class.getName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("🟢 AnswerController.doPost() 실행됨 (질문하기 버튼 클릭됨)");

        String userQuestion = request.getParameter("question");
        String modelType = request.getParameter("modelType");
        boolean retrievalEnabled = true; // 기본값 설정

        logger.info("🟢 AnswerController.doPost() 실행됨 (질문하기 버튼 클릭됨)");
        logger.info("📌 입력된 질문: " + userQuestion);
        logger.info("📌 선택된 모델: " + modelType);

        try {
            String aiAnswer = OpenAIService.getInstance().useBaseModel(userQuestion, true);
            request.getSession().setAttribute("question", userQuestion);
            request.getSession().setAttribute("answer", aiAnswer);
            request.getSession().setAttribute("modelType", modelType);

            // ✅ 무조건 랜덤 유튜브 영상 2개 추천
            List<YouTubeVideo> allVideos = SupabaseRepository.getYouTubeVideos();

            if (allVideos == null || allVideos.isEmpty()) {
                logger.warning("⚠️ Supabase에서 유튜브 영상 데이터를 가져오지 못함.");
                request.getSession().setAttribute("recommendedVideos", null);
            } else {
                Collections.shuffle(allVideos, new Random());
                List<YouTubeVideo> randomVideos = allVideos.subList(0, Math.min(2, allVideos.size()));

                request.getSession().setAttribute("recommendedVideos", randomVideos);
                logger.info("✅ 랜덤 유튜브 영상 2개 추천 완료: " + randomVideos.size() + "개 선택됨");
            }

        } catch (Exception e) {
            logger.severe("❌ AI 답변 생성 중 오류 발생: " + e.getMessage());
            request.getSession().setAttribute("answer", "❌ AI가 답변을 생성하지 못했습니다. 다시 시도해 주세요.");
        }

        // 응답 페이지로 리디렉트
        response.sendRedirect(request.getContextPath() + "/index.jsp");
    }
}
