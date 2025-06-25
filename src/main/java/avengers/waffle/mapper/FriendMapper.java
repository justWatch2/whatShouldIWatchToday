package avengers.waffle.mapper;

import avengers.waffle.VO.recommendFriends.RecommendFriendsInfoDTO;
import avengers.waffle.VO.recommendFriends.RecommendInfoDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


import java.util.List;

@Mapper
public interface FriendMapper {

    List<RecommendFriendsInfoDTO> getFriendsInfoByMemberId(List<String> friendIds);

    //친구 포함 ---------------------------------------------------------------------------------------------------------
    //친구 + 개인 영화 추천 repository
    List<RecommendInfoDTO> getRecommendMovieByMemberIds(@Param("memberIds") List<String> memberIds,
                                                        @Param("recommendType") int recommendType);

    //친구 + 개인 tvShow 추천 repository
    List<RecommendInfoDTO> getRecommendTvShowByMemberIds(@Param("memberIds") List<String> memberIds,
                                                         @Param("recommendType") int recommendType);

    List<RecommendInfoDTO> threadStart(@Param("memberIds") List<String> memberIds,
                                       @Param("recommendType") int recommendType);

}
