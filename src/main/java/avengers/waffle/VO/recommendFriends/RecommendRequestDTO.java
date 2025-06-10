package avengers.waffle.VO.recommendFriends;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class RecommendRequestDTO {
    private String category;
    private List<String> memberIds;
    private String recommendOption;

    @Builder
    public RecommendRequestDTO(String category, List<String> memberIds, String recommendOption) {
        this.category = category;
        this.memberIds = memberIds;
        this.recommendOption = recommendOption;
    }
}
