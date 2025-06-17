package avengers.waffle.controller.recommendFriends;

import avengers.waffle.VO.recommendFriends.RecommendFriendsInfoDTO;
import avengers.waffle.VO.recommendFriends.RecommendMoviesInfoDTO;
import avengers.waffle.VO.recommendFriends.RecommendRequestDTO;

import avengers.waffle.service.IF.recommendFriends.IF_RecommendFriendsService;
import avengers.waffle.utils.GetMemberId;

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
    public ResponseEntity<List<RecommendMoviesInfoDTO>> recommendMovies(@RequestBody RecommendRequestDTO recommendRequestDTO) {
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

        //만약에 카테고리가 드라마 라면!
        if ("드라마".equals(recommendRequestDTO.getCategory())) {
            // 드라마 부분은 우선 고려 아직 안함
            List<RecommendMoviesInfoDTO> recommendMoviesInfoDTOList = null;
        }
        //드마가 아니라면 영화!
        List<RecommendMoviesInfoDTO> recommendMoviesInfoDTOList = recommendFriendsService.getMoviesInfo(userId, recommendRequestDTO);

        //bfs로 영화 추천 로직
        List<RecommendMoviesInfoDTO> result = recommendFriendsService.bfsRecommend(recommendMoviesInfoDTOList, 2, 50, 3);  // 영화 리스트, 깊이, 추천결과 개수

        //만약에 recommendMoviesInfoDTOList가 null 이라면 처리를 따로 장르로 다시 추천을 돌리던가 처리를 해줘야된다. 우선 대기

        return ResponseEntity.ok(result);
    }



}
