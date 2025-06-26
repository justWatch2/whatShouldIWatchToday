package avengers.waffle.service.IF.recommendFriends;

import avengers.waffle.VO.recommendFriends.RecommendFriendsInfoDTO;
import avengers.waffle.VO.recommendFriends.RecommendInfoDTO;
import avengers.waffle.VO.recommendFriends.RecommendRequestDTO;

import java.util.List;

public interface IF_RecommendFriendsService {


    //친구 목록 불러오기
    public List<RecommendFriendsInfoDTO> getFriendsInfo(String memberId);
    //영화 추천 기본 파트
    public List<RecommendInfoDTO> getMoviesInfo(String userId, RecommendRequestDTO recommendRequestDTO, boolean isMovies);
    // movie bfs 추천 서비스
    public List<RecommendInfoDTO> bfsRecommendForMovie(List<RecommendInfoDTO> candidates, int maxDepth, int recommendSize, int topNStartMovies);
    // tvShow bfs 추천 서비스
    public List<RecommendInfoDTO> bfsRecommendForTvshow(List<RecommendInfoDTO> candidates, int maxDepth, int recommendSize, int topNStartMovies);



}
