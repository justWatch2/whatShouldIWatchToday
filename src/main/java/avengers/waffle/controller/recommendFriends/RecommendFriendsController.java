package avengers.waffle.controller.recommendFriends;

import avengers.waffle.VO.recommendFriends.RecommendFriendsInfoDTO;
import avengers.waffle.VO.recommendFriends.RecommendInfoDTO;
import avengers.waffle.VO.recommendFriends.RecommendRequestDTO;


import avengers.waffle.service.IF.recommendFriends.IF_RecommendFriendsService;

import avengers.waffle.utils.GetMemberId;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class RecommendFriendsController {
    private final IF_RecommendFriendsService recommendFriendsService;
    private final GetMemberId getMemberId;
//    private final ThreadService threadService;


    @GetMapping("/friend/getList")
    public ResponseEntity<List<RecommendFriendsInfoDTO>> getFriendList() {
        //토큰 안에 있는  아이디를 확인해서 친구목록 불러온다.
//        String authorizationHeader = request.getHeader("Authorization");
//        String memberId =  getMemberId.getMemberId(authorizationHeader);

        //임시로 넣어둠
        String userId = "aa";
        List<RecommendFriendsInfoDTO> recommendFriendsInfoDTOList = recommendFriendsService.getFriendsInfo(userId);

        // 잘들어갔는지 체크
        recommendFriendsInfoDTOList.stream().forEach((recommendMoviesInfoDTO) -> {
            System.out.println("recommendMoviesInfoDTO = " + recommendMoviesInfoDTO.getIcon());
        });
        return  ResponseEntity.ok(recommendFriendsInfoDTOList);
    }

    //영화, 드라마  데이터 추천 부분 상황에따라 실시간으로 처리
    @PostMapping("/recommend/movies")
    public ResponseEntity<List<RecommendInfoDTO>> recommendMovies(@RequestBody RecommendRequestDTO recommendRequestDTO) {
        //토큰 안에 있는  아이디를 확인해서 친구목록 불러온다.
//        String authorizationHeader = request.getHeader("Authorization");
//        String memberId =  getMemberId.getMemberId(authorizationHeader);

        //임시로 넣어둠
        String userId = "aa";

        System.out.println("recommendRequestDTO.getCategory() = " + recommendRequestDTO.getCategory());
        System.out.println("recommendRequestDTO.getRecommendOption() = " + recommendRequestDTO.getRecommendOption());
        for(String memberId : recommendRequestDTO.getMemberIds()) {
            System.out.println("memberId = " + memberId);
        }

        boolean isMovies = true;
        //만약에 카테고리가 드라마 라면!
        if ("드라마".equals(recommendRequestDTO.getCategory())) {
            isMovies = false;
        }

        //기존 방법
        List<RecommendInfoDTO> recommendInfoDTOList = recommendFriendsService.getMoviesInfo(userId, recommendRequestDTO, isMovies);

        //쓰레드 6개로 병렬 처리
//        List<RecommendInfoDTO> recommendInfoDTOList = threadService.threadStart(userId, recommendRequestDTO, isMovies);

        List<RecommendInfoDTO> result = null;

        //bfs추천 로직
        if(isMovies){
            //bfs로 영화 추천 로직
            result = recommendFriendsService.bfsRecommendForMovie(recommendInfoDTOList, 2, 50, 3);  // 영화 리스트, 깊이, 시작노드 개수
        }else{
            //bfs로 tv쇼 추천 로직
            result = recommendFriendsService.bfsRecommendForTvshow(recommendInfoDTOList, 2, 50, 3);  // tvshow 리스트, 깊이, 시작노드 개수
        }

        result.stream().forEach((recommendInfoDTO) -> {
            System.out.println("recommendInfoDTO.getTitle() = " + recommendInfoDTO.getTitle());
        });

        //만약에 recommendMoviesInfoDTOList가 null 이라면 처리를 따로 장르로 다시 추천을 돌리던가 처리를 해줘야된다. 우선 대기

        return ResponseEntity.ok(result);
    }



}
