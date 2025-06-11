package avengers.waffle.VO.recommendFriends;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RecommendMoviesInfoDTO {
    private int movieId;
    private String title;
    private String poster;
    private Long rating;
    private String description;

    @Builder
    public RecommendMoviesInfoDTO(String title, String poster, Long rating, int movieId, String description) {
        this.title = title;
        this.poster = poster;
        this.rating = rating;
        this.movieId = movieId;
        this.description = description;
    }
}
