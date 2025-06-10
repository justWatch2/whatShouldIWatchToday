package avengers.waffle.mapper;

import avengers.waffle.VO.recommendFriends.RecommendFriendsInfoDTO;
import avengers.waffle.VO.recommendFriends.RecommendMoviesInfoDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Mapper
public interface FriendMapper {
    List<RecommendFriendsInfoDTO> getFriendsInfoByMemberId(List<String> friendIds);

    //친구를 아무도 아직 선택하지 않았을떄는 userId 기준으로 추천을 해야된다.
    List<RecommendMoviesInfoDTO> getRecommendMovieByUserId(String userId);

    //친구를 아무도 선택하지 않았을때 userId 기준으로 찜목록과 유사한 작품 추천
    List<RecommendMoviesInfoDTO> getRecommendMovieByUserIdFromMovieWish(String userId);

    //친구 선택 x 일 경우 userId 기준으로 시청한 목록과 유사한 작품 추천
    List<RecommendMoviesInfoDTO> getRecommendMovieByUserIdFromMemberViewlist(String userId);

    //친구 포함 ---------------------------------------------------------------------------------------------------------
    //친구포함 기본 추천 버젼
    List<RecommendMoviesInfoDTO> getRecommendMovieByMemberIds( List<String> memberIds);

    //친구포함 찜목록만 비슷한 작품 추천
    List<RecommendMoviesInfoDTO> getRecommendMovieByMemberIdsFromWish(List<String> memberIds);

    //친구포함 시청한 작품만  비슷한 작품 추천
    List<RecommendMoviesInfoDTO> getRecommendMovieByMemberIdsFromViewlist( List<String> memberIds);

}
