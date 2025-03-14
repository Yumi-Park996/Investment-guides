package org.example.investment_guide.service;

import org.example.investment_guide.repository.SupabaseRepository;
import org.example.investment_guide.model.FAQ;
import org.example.investment_guide.model.YouTubeVideo;

import java.util.List;
import java.util.logging.Logger;

public class SupabaseService {
    private static final Logger logger = Logger.getLogger(SupabaseService.class.getName());

    public static void loadSupabaseData() {
        logger.info("📡 Supabase 데이터 로드 시작...");

        // ✅ SupabaseRepository에서 데이터를 가져와 캐싱
        SupabaseRepository.loadYouTubeVideos();
        SupabaseRepository.loadFAQs();

        logger.info("✅ Supabase 데이터 로드 완료! 유튜브: "
                + SupabaseRepository.getYouTubeVideos().size() + "개, FAQ: "
                + SupabaseRepository.getFAQs().size() + "개");
    }

    public static List<YouTubeVideo> getYouTubeVideos() {
        return SupabaseRepository.getYouTubeVideos();  // ✅ SupabaseRepository에서 데이터 반환
    }

    public static List<FAQ> getFAQs() {
        return SupabaseRepository.getFAQs();  // ✅ SupabaseRepository에서 데이터 반환
    }
}
