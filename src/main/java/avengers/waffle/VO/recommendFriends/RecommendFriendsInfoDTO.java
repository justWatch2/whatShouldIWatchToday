package avengers.waffle.VO.recommendFriends;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RecommendFriendsInfoDTO {
    private String name;
    private Long numWish;
    private Long numViewedMovie;
    private String icon;

    @Builder
    public RecommendFriendsInfoDTO(String name, Long numWish, Long numViewedMovie, String icon) {
        this.name = name;
        this.numWish = numWish;
        this.numViewedMovie = numViewedMovie;
        this.icon = icon;
    }
}
