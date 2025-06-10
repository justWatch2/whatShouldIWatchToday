package avengers.waffle.VO.kakao;

import lombok.Data;

import java.util.List;

@Data
public class KakaoDTO {
    private List<Element> elements;

    @Data
    public static class Element {
        private Long id;
        private String uuid;
        private String profile_nickname;
        private String profile_thumbnail_image;
    }
}