<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>주부를 위한 해외주식 투자 가이드</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.0/css/bootstrap.min.css" rel="stylesheet">
    <link href="<%= request.getContextPath() %>/assets/style.css" rel="stylesheet">
</head>
<body>

<!-- Main App Container -->
<div class="container app-container">
    <!-- Intro Card -->
    <div class="card intro-card text-center mb-4">
        <h1 class="display-5 fw-bold mb-3">💰 주부 해외주식 투자 가이드</h1>
        <p class="lead">해외 주식 투자를 쉽고 안전하게 시작해보세요.</p>

        <!-- Investment Image -->
        <div class="text-center">
            <img id="main-image" src="<%= request.getContextPath() %>/images/main.webp">
        </div>

        <div class="rotating-text mt-3">
            <h5 id="rotatingText">"주식은 어렵지 않아요! 차근차근 배워보세요"</h5>
        </div>
    </div>

    <!-- Question Form -->
    <div class="card question-form-card mb-4">
        <h3 class="mb-4 text-center">💡 해외주식 궁금한 점을 물어보세요</h3>
        <form action="<%= request.getContextPath() %>/" method="post">
            <div class="mb-3">
                <input type="text" class="form-control" id="questionInput" name="question"
                       placeholder="예: 초보자가 투자하기 좋은 해외 주식은?" required>
            </div>
            <div class="text-center">
                <button type="submit" class="btn btn-gradient">질문하기</button>
            </div>
        </form>
    </div>

    <!-- Response Card (hidden initially) -->
    <div class="card response-card" id="responseCard">
        <h4 class="mb-3">📢 AI 투자 가이드 답변:</h4>
        <div id="responseText" class="mb-4"></div>
        <div class="text-center mt-4">
            <button id="newQuestionBtn" class="btn btn-outline-primary">새로운 질문하기</button>
        </div>
    </div>

    <!-- Features Section -->
    <div class="features-section">
        <div class="feature-item text-center">
            <div class="feature-icon">📈</div>
            <h5>기본 개념</h5>
            <p>해외 주식의 기초 개념과 ETF, 배당주, 환율 영향 이해하기</p>
        </div>

        <div class="feature-item text-center">
            <div class="feature-icon">📊</div>
            <h5>투자 전략</h5>
            <p>안정적인 투자 방법 및 리스크 관리</p>
        </div>

        <div class="feature-item text-center">
            <div class="feature-icon">🌍</div>
            <h5>실시간 정보</h5>
            <p>해외 주식 뉴스 & 추천 유튜브 채널 제공</p>
        </div>
    </div>
</div>

<!-- Bootstrap Bundle with Popper -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.0/js/bootstrap.bundle.min.js"></script>

<script>
    // Sample rotating texts
    const rotatingTexts = [
        "해외 주식 투자를 쉽게 시작해보세요",
        "배당주로 안정적인 수익을 노려보세요",
        "환율이 투자에 미치는 영향을 알아보세요",
        "성장주와 가치주의 차이를 이해하세요"
    ];

    let currentTextIndex = 0;
    const rotatingTextElement = document.getElementById('rotatingText');

    // Rotate texts every 4 seconds
    setInterval(() => {
        currentTextIndex = (currentTextIndex + 1) % rotatingTexts.length;
        rotatingTextElement.style.opacity = 0;

        setTimeout(() => {
            rotatingTextElement.textContent = rotatingTexts[currentTextIndex];
            rotatingTextElement.style.opacity = 1;
        }, 500);
    }, 4000);

    // New question button
    document.getElementById('newQuestionBtn').addEventListener('click', function() {
        const responseCard = document.getElementById('responseCard');
        const questionInput = document.getElementById('questionInput');

        responseCard.classList.remove('show');
        setTimeout(() => {
            questionInput.value = '';
            questionInput.focus();
        }, 500);
    });

    // Typing effect function
    function typeWriter(element, text, i, speed) {
        if (i < text.length) {
            element.innerHTML = text.substring(0, i + 1) + '<span class="cursor">|</span>';
            setTimeout(() => {
                typeWriter(element, text, i + 1, speed);
            }, speed);
        } else {
            element.innerHTML = text;
            element.classList.add('animate-fadeInUp');
        }
    }
</script>

</body>
</html>
