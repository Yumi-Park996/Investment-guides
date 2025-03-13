package org.example.investment_guide.model.repository;

import org.example.investment_guide.model.dto.FAQ;
import org.example.investment_guide.model.dto.YouTubeVideo;

import java.util.List;

public class SupabaseRepository {
    private static List<YouTubeVideo> youtubeVideos;
    private static List<FAQ> faqs;

    // ✅ SupabaseLoader에서 데이터를 설정할 수 있도록 Setter 추가
    public static void setYouTubeVideos(List<YouTubeVideo> videos) {
        youtubeVideos = videos;
    }

    public static void setFAQs(List<FAQ> faqList) {
        faqs = faqList;
    }

    /** ✅ 미리 로드된 유튜브 영상 리스트 반환 */
    public static List<YouTubeVideo> getYouTubeVideos() {
        return youtubeVideos;
    }

    /** ✅ 미리 로드된 FAQ 리스트 반환 */
    public static List<FAQ> getFAQs() {
        return faqs;
    }
}
