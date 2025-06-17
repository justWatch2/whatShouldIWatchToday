package avengers.waffle.service.IF.recommendFriends;

import avengers.waffle.VO.recommendFriends.RecommendFriendsInfoDTO;
import avengers.waffle.VO.recommendFriends.RecommendMoviesInfoDTO;
import avengers.waffle.VO.recommendFriends.RecommendRequestDTO;
import avengers.waffle.mapper.FriendMapper;
import avengers.waffle.repository.friends.FriendsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

public interface IF_RecommendFriendsService {


    //친구 목록 불러오기
    public List<RecommendFriendsInfoDTO> getFriendsInfo(String memberId);
    //영화 추천 기본 파트
    public List<RecommendMoviesInfoDTO> getMoviesInfo(String userId, RecommendRequestDTO recommendRequestDTO);
}
