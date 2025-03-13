<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AI 투자 가이드 답변</title>
    <link href="<%= request.getContextPath() %>/assets/style.css" rel="stylesheet">
</head>
<body>
<div class="container app-container">
    <div class="card response-card show">
        <h4 class="mb-3">📢 AI 투자 가이드 답변:</h4>

        <%
            int id = Integer.parseInt(request.getParameter("id"));
            List<String[]> faqs = (List<String[]>) application.getAttribute("faqs");
            String question = faqs.get(id)[0];
            String answer = faqs.get(id)[1];
        %>

        <p class="question-text"><strong>질문:</strong> <%= question %></p>
        <p id="answerText"><strong>답변:</strong> <%= answer.replace("\n", "<br>") %></p>

        <div class="text-center mt-4">
            <a href="<%= request.getContextPath() %>/" class="btn btn-outline-primary">새로운 질문 보기</a>
        </div>
    </div>
</div>
</body>
</html>
