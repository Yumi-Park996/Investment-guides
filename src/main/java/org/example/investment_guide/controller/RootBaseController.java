package org.example.investment_guide.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.investment_guide.common.ModelType;
import org.example.investment_guide.service.OpenAIService;

import java.io.IOException;

@WebServlet ("/")
public class RootBaseController extends BaseController {
    final static OpenAIService OPEN_AI_SERVICE = OpenAIService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html");
        view(req, resp, "index");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String question = req.getParameter("question");
        String modelTypeParam = req.getParameter("modelType"); // 프론트엔드에서 모델 선택 받기

        HttpSession session = req.getSession();

        if (question == null || question.isEmpty()) {
            session.setAttribute("message", "질문이 비어 있습니다!");
            view(req, resp, "index");
            return;
        }

        // 모델 타입 변환 (기본값: BASE)
        ModelType modelType;
        try {
            modelType = ModelType.valueOf(modelTypeParam.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            modelType = ModelType.BASE;
        }

        session.setAttribute("message", null);
        session.setAttribute("question", question);
        session.setAttribute("modelType", modelType.name()); // 모델 정보 저장

        try {
            // 선택된 모델 실행
            String answer = OPEN_AI_SERVICE.useModel(question, modelType);
            session.setAttribute("answer", answer);
        } catch (Exception e) {
            session.setAttribute("answer", "답변을 생성하는 도중 오류가 발생했습니다.");
        }

        resp.sendRedirect(req.getContextPath() + "/answer");
    }
}