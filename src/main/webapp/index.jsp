<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, java.util.Random, java.util.ArrayList, java.util.Collections, org.example.investment_guide.model.dto.FAQ, org.example.investment_guide.model.dto.YouTubeVideo" %>

<%
    List<FAQ> faqs = (List<FAQ>) application.getAttribute("faqs");
    if (faqs == null) {
        faqs = new ArrayList<>();
    }

    Random rand = new Random();
    List<FAQ> randomFAQs = new ArrayList<>(faqs);

    // ✅ 리스트 섞기 (shuffle) 후 상위 3개 선택
    Collections.shuffle(randomFAQs, rand);
    int maxDisplay = Math.min(3, randomFAQs.size());

    List<YouTubeVideo> recommendedVideos = (List<YouTubeVideo>) session.getAttribute("recommendedVideos");
    if (recommendedVideos == null) {
        recommendedVideos = new ArrayList<>();
    }

    String userQuestion = (String) session.getAttribute("question");
    String aiAnswer = (String) session.getAttribute("answer");

    boolean hasAnswer = (userQuestion != null && aiAnswer != null);
%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>초보자를 위한 해외주식 투자 가이드</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.0/css/bootstrap.min.css" rel="stylesheet">
    <link href="<%=request.getContextPath()%>/assets/style.css" rel="stylesheet">
    <meta property="og:title" content="초보 해외주식 투자자를 위한 가이드" />
    <meta property="og:description" content="궁금하면 들어오세요." />
    <meta property="og:image" content="<%=request.getContextPath()%>/assets/dog.png" />
    <meta property="og:url" content="<%=request.getContextPath()%>" />
</head>
<body>

<div class="container app-container">
    <!-- Intro Card -->
    <div class="card intro-card text-center mb-4">
        <h1 class="display-5 fw-bold mb-3">💰 초보자 해외주식 투자 가이드</h1>
        <p class="lead">해외주식 투자를 쉽고 안전하게 시작해보세요.</p>
        <div class="text-center">
            <img id="main-image" src="<%=request.getContextPath()%>/images/main.webp">
        </div>
        <div class="rotating-text mt-3">
            <h5 id="rotatingText">"주식은 어렵지 않아요! 차근차근 배워보세요"</h5>
        </div>
    </div>

    <!-- Question Form -->
    <div class="card question-form-card mb-4">
        <h3 class="mb-4 text-center">💡 해외주식 궁금한 점을 물어보세요</h3>
        <form id="questionForm" action="<%=request.getContextPath()%>/answer" method="post">
            <div class="mb-3">
                <input type="text" class="form-control" id="questionInput" name="question"
                       placeholder="예: 초보자가 투자하기 좋은 해외 주식은?" required>
            </div>
            <div class="mb-3">
                <label for="modelType">모델 선택:</label>
                <select class="form-select" id="modelType" name="modelType">
                    <option value="BASE" selected>기본 모델</option>
                    <option value="REASONING">추론 모델</option>
                </select>
            </div>
            <div class="text-center">
                <button id="submitButton" type="submit" class="btn btn-gradient">질문하기</button>
            </div>
            <!-- ✅ 로딩 스피너 -->
            <div id="loadingSpinner" class="text-center mt-3 d-none">
                <div class="spinner-border text-primary" role="status">
                    <span class="visually-hidden">Loading...</span>
                </div>
                <p class="mt-2">AI가 답변을 생성하는 중입니다...</p>
            </div>
        </form>
    </div>

    <!-- AI 답변 표시 -->
    <div class="card response-card <%=(session.getAttribute("answer") != null) ? "show" : "d-none"%>">
        <h4 class="mb-3">📢 AI 투자 가이드 답변:</h4>
        <% if (hasAnswer) { %>
        <p class="question-text"><strong>질문:</strong> <%= userQuestion %></p>
        <p id="answerText"><strong>답변:</strong> <%= aiAnswer.replace("\n", "<br>") %></p>
        <% } else { %>
        <p class="text-center">❌ AI가 답변을 생성하지 못했습니다. 다시 질문해 주세요.</p>
        <% } %>
        <!-- 유튜브 추천 영상 -->
        <% if (recommendedVideos != null && !recommendedVideos.isEmpty()) { %>
        <div class="youtube-section mt-4">
            <h4 class="mb-3 text-center">🎥 해외주식 관련 유튜브 랜덤 영상</h4>
            <h6>해외주식 키워드 검색으로 나온 유튜브 영상입니다.</h6>
            <h6>알고리즘의 오류로 잘못된 영상이 나올 수 있습니다. 놀람 주의.</h6>
            <div class="row">
                <% for (YouTubeVideo video : recommendedVideos) { %>
                <div class="col-md-6">
                    <div class="card p-3 text-center">
                        <h5>📌 <%= video.getTitle() %></h5>
                        <a href="<%= video.getUrl() %>" class="btn btn-outline-primary mt-2" target="_blank">영상 보기</a>
                    </div>
                </div>
                <% } // for 문 닫기 %>
            </div>
        </div>
        <% } else { %>  <%-- ⚠️ `else` 문이 없거나 `{}` 블록이 빠져 있으면 오류 발생 --%>
        <div class="text-center mt-3">
            <p>❌ 추천할 유튜브 영상이 없습니다.</p>
        </div>
        <% } %>  <%-- ⚠️ `if` 블록 닫기 --%>
        <div class="text-center mt-4">
            <button id="newQuestionBtn" class="btn btn-outline-primary">새로운 질문하기</button>
        </div>
    </div>


    <!-- 랜덤 질문 카드 -->
    <div class="faq-section mt-5">
        <h4 class="mb-3 text-center">📢 랜덤 투자 질문</h4>
        <div class="row">
            <% if (!faqs.isEmpty()) { %>
            <% for (int i = 0; i < maxDisplay; i++) { %>
            <div class="col-md-4">
                <div class="card p-3 text-center">
                    <h5>💡 <%= randomFAQs.get(i).getQuestion() %></h5>
                    <a href="faq.jsp?id=<%= i %>" class="btn btn-outline-primary mt-2">자세히 보기</a>
                </div>
            </div>
            <% } %>
            <% } else { %>
            <div class="text-center">
                <p>❌ 등록된 FAQ가 없습니다.</p>
            </div>
            <% } %>
        </div>
    </div>
</div>

<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.0/js/bootstrap.bundle.min.js"></script>
<script>
    // 햄톨이 텍스트 회전
    const rotatingTexts = [
        "해외 주식 투자를 쉽게 시작해보세요",
        "배당주로 안정적인 수익을 노려보세요",
        "환율이 투자에 미치는 영향을 알아보세요",
        "성장주와 가치주의 차이를 이해하세요"
    ];

    let currentTextIndex = 0;
    const rotatingTextElement = document.getElementById('rotatingText');

    setInterval(() => {
        currentTextIndex = (currentTextIndex + 1) % rotatingTexts.length;
        rotatingTextElement.style.opacity = 0;
        setTimeout(() => {
            rotatingTextElement.textContent = rotatingTexts[currentTextIndex];
            rotatingTextElement.style.opacity = 1;
        }, 500);
    }, 4000);

    document.querySelector("form").addEventListener("submit", function(event) {
        console.log("🟢 폼 제출됨");
        console.log("📌 입력된 질문: " + document.getElementById("questionInput").value);
        console.log("📌 선택된 모델: " + document.getElementById("modelType").value);
    });

    document.getElementById('newQuestionBtn').addEventListener('click', function() {
        document.querySelector('.response-card').classList.add('d-none');
    });

    document.getElementById("questionForm").addEventListener("submit", function (event) {
        // 스피너 표시
        document.getElementById("loadingSpinner").classList.remove("d-none");

        // 버튼 비활성화
        document.getElementById("submitButton").disabled = true;
    });
</script>
</body>
</html>