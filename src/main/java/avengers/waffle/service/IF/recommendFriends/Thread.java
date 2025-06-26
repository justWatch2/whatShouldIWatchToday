package avengers.waffle.service.IF.recommendFriends;

import avengers.waffle.VO.recommendFriends.RecommendInfoDTO;
import avengers.waffle.VO.recommendFriends.RecommendRequestDTO;

import java.util.List;

public interface Thread {
    //쓰레드로 병렬 처리 추천
    public List<RecommendInfoDTO> threadStart(String userId, RecommendRequestDTO recommendRequestDTO, boolean isMovies);
}
