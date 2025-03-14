package org.example.investment_guide.listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.example.investment_guide.service.SupabaseService;

import java.util.logging.Logger;

@WebListener
public class SupabaseInitializer implements ServletContextListener {
    private static final Logger logger = Logger.getLogger(SupabaseInitializer.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("📡 [SupabaseInitializer] 서버 시작 시 데이터 로드...");

        SupabaseService.loadSupabaseData();  // ✅ 서비스에서 데이터 로드

        // ✅ JSP에서 사용할 수 있도록 application에 저장
        sce.getServletContext().setAttribute("faqs", SupabaseService.getFAQs());
        sce.getServletContext().setAttribute("youtubeVideos", SupabaseService.getYouTubeVideos());

        logger.info("✅ [SupabaseInitializer] 데이터 로드 완료!");
    }
}
