//package avengers.waffle.VO.recommendFriends;
//
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//import java.util.List;
//
//@Getter
//@NoArgsConstructor
//public class RecommendInfoDTO {
//    private int movieId;
//    private String title;
//    private String poster;
//    private Long rating;
//    private String description;
//    private List<String> keywords;
//    private int keywordsRanking;
//    // 문자열로 받아온 키워드 원본
//    private String keywordsRaw;
//
//    @Builder
//    public RecommendInfoDTO(int movieId, String title, String poster, Long rating, String description,
//                                  String keywordsRaw, int keywordsRanking) {
//        this.movieId = movieId;
//        this.title = title;
//        this.poster = poster;
//        this.rating = rating;
//        this.description = description;
//        this.keywordsRaw = keywordsRaw;
//        this.keywordsRanking = keywordsRanking;
//
//        // 문자열을 List<String>으로 변환
//        this.keywords = keywordsRaw == null || keywordsRaw.isEmpty() ?
//                List.of() :
//                List.of(keywordsRaw.split(",")); // split
//        System.out.println("DEBUG >>> keywordsRaw = " + keywordsRaw);
//        this.keywords = keywordsRaw == null || keywordsRaw.isEmpty() ?
//                List.of() :
//                List.of(keywordsRaw.split(","));
//    }
//}
package avengers.waffle.VO.recommendFriends;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class RecommendInfoDTO {
    private int movieId;
    private int tvShowId;
    private String title;
    private String poster;
    private Long rating;
    private String description;
    private String backdropPath;
    private List<String> keywords;  // 변환된 리스트
    private int keywordsRanking;

    // SQL에서 받아오는 원본 키워드 문자열
    private String keywordsRaw;

    public void setKeywordsRaw(String keywordsRaw) {
        this.keywordsRaw = keywordsRaw;
        System.out.println("DEBUG >>> keywordsRaw = " + keywordsRaw);
        if (keywordsRaw == null || keywordsRaw.isEmpty()) {
            this.keywords = List.of();
        } else {
            this.keywords = Arrays.stream(keywordsRaw.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
        }
    }
}
