<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, org.example.investment_guide.model.FAQ" %>
<%
    // 📌 애플리케이션 스코프에서 FAQ 리스트 가져오기
    List<FAQ> faqs = (List<FAQ>) application.getAttribute("faqs");

    // 📌 요청된 질문 ID 가져오기 (예외 처리 포함)
    String idParam = request.getParameter("id");
    int id = -1;
    try {
        id = (idParam != null) ? Integer.parseInt(idParam) : -1;
    } catch (NumberFormatException e) {
        System.out.println("❌ [faq.jsp] 잘못된 id 값: " + idParam);
    }

    // 📌 유효한 ID인지 확인 후 FAQ 데이터 가져오기
    FAQ selectedFaq = null;
    if (id >= 0 && id < faqs.size()) {
        selectedFaq = faqs.get(id);
    }
%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AI 투자 가이드</title>
    <link href="<%= request.getContextPath() %>/assets/style.css" rel="stylesheet">
</head>
<body>
<div class="container app-container">
    <h4 class="mb-3">📢 AI 투자 가이드 FAQ</h4>

    <% if (selectedFaq != null) { %>
    <div class="card response-card show">
        <h4 class="question-text"><strong>질문:</strong> <%= selectedFaq.getQuestion() %></h4>
        <p id="answerText"><strong>답변:</strong> <%= selectedFaq.getAnswer().replace("\n", "<br>") %></p>
    </div>
    <% } else { %>
    <p>❌ 질문을 찾을 수 없습니다.</p>
    <% } %>

    <div class="text-center mt-4">
        <a href="<%= request.getContextPath() %>/" class="btn btn-outline-primary">새로운 질문 보기</a>
    </div>
</div>
</body>
</html>
