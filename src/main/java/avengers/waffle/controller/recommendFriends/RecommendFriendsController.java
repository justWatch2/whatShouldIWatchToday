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


    @GetMapping("/friend/getList")
    public ResponseEntity<List<RecommendFriendsInfoDTO>> getFriendListFromMovies(HttpServletRequest request) {
//        토큰 안에 있는  아이디를 확인해서 친구목록 불러온다.
        String authorizationHeader = request.getHeader("Authorization");
        String memberId =  getMemberId.getMemberId(authorizationHeader);

        System.out.println("memberId = " + memberId);
        List<RecommendFriendsInfoDTO> recommendFriendsInfoDTOList = recommendFriendsService.getFriendsInfoFromMovies(memberId);

//        if (recommendFriendsInfoDTOList == null) {
//            return ResponseEntity.status()
//        }
        return  ResponseEntity.ok(recommendFriendsInfoDTOList);
    }

    @GetMapping("/friend/getDramaList")
    public ResponseEntity<List<RecommendFriendsInfoDTO>> getFriendListFromTvshow(HttpServletRequest request) {
//        토큰 안에 있는  아이디를 확인해서 친구목록 불러온다.
        String authorizationHeader = request.getHeader("Authorization");
        String memberId =  getMemberId.getMemberId(authorizationHeader);

        System.out.println("memberId = " + memberId);
        List<RecommendFriendsInfoDTO> recommendFriendsInfoDTOList = recommendFriendsService.getFriendsInfoFromTvshow(memberId);

        if (recommendFriendsInfoDTOList == null) {
            return ResponseEntity.ok(null);
        }
        return  ResponseEntity.ok(recommendFriendsInfoDTOList);
    }


    //영화, 드라마  데이터 추천 부분 상황에따라 실시간으로 처리
    @PostMapping("/recommend/movies")
    public ResponseEntity<List<RecommendInfoDTO>> recommendMovies(@RequestBody RecommendRequestDTO recommendRequestDTO, HttpServletRequest request) {
        //토큰 안에 있는  아이디를 확인해서 친구목록 불러온다.
        String authorizationHeader = request.getHeader("Authorization");
        String memberId =  getMemberId.getMemberId(authorizationHeader);

        System.out.println("recommendRequestDTO.getCategory() = " + recommendRequestDTO.getCategory());
        System.out.println("recommendRequestDTO.getRecommendOption() = " + recommendRequestDTO.getRecommendOption());
        for(String testMemberId : recommendRequestDTO.getMemberIds()) {
            System.out.println("controller 부분 memberId 체크  = " + testMemberId);
        }

        boolean isMovies = true;
        //만약에 카테고리가 드라마 라면!
        if ("드라마".equals(recommendRequestDTO.getCategory())) {
            isMovies = false;
        }

        //기존 방법
        List<RecommendInfoDTO> recommendInfoDTOList = recommendFriendsService.getMoviesInfo(memberId, recommendRequestDTO, isMovies);
        List<RecommendInfoDTO> result = null;

        recommendInfoDTOList.stream().forEach((nn) ->
                        System.out.println("bfs돌기전 영화나 드라마 자료자료 " + nn.getTitle())
                );
        //bfs추천 로직
        if(isMovies){
            //bfs로 영화 추천 로직
            result = recommendFriendsService.bfsRecommendForMovie(recommendInfoDTOList, 2, 30, 3);  // 영화 리스트, 깊이, 시작노드 개수
        }else{
            //bfs로 tv쇼 추천 로직
            result = recommendFriendsService.bfsRecommendForTvshow(recommendInfoDTOList, 2, 30, 3);  // tvshow 리스트, 깊이, 시작노드 개수
        }

        result.stream().forEach((recommendInfoDTO) -> {
            System.out.println("recommendInfoDTO.getTitle() = " + recommendInfoDTO.getTitle());
        });

        return ResponseEntity.ok(result);
    }



}
