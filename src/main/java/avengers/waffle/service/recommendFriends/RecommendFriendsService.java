package avengers.waffle.service.recommendFriends;

import avengers.waffle.VO.recommendFriends.RecommendFriendsInfoDTO;
import avengers.waffle.VO.recommendFriends.RecommendMoviesInfoDTO;
import avengers.waffle.VO.recommendFriends.RecommendRequestDTO;
import avengers.waffle.mapper.FriendMapper;
import avengers.waffle.repository.FriendsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendFriendsService {
    private final FriendMapper friendMapper;
    private final FriendsRepository friendsRepository;

    //친구 목록 불러오기
    public List<RecommendFriendsInfoDTO> getFriendsInfo(String memberId){
        String friends = friendsRepository.getFriendsIdByMemberId(memberId);
        System.out.println("recommendFriends = " + friends);

        List<String> friendIds = Arrays.asList(friends.split(","));

        for(String friendId : friendIds){
            System.out.println("리스트로 만든것들!! friendId = " + friendId);
        }
        return  friendMapper.getFriendsInfoByMemberId(friendIds);
    }

    //영화 추천 기본 파트
    public List<RecommendMoviesInfoDTO> getMoviesInfo(String userId, RecommendRequestDTO recommendRequestDTO){
        //userId랑 recommendRequestDTO에 있는 체크박스에 체크된 친구들 가지고
        // 이게 auto 인지 liked, watched 인지 null 인제 ㅊ크 하기 !
        List<RecommendMoviesInfoDTO> moviesInfoDTOList;

        if ("auto".equals(recommendRequestDTO.getRecommendOption())){
            //친구를 아무도 선택하지 않았으면 유저 기준으로 추천을 해야된다.
            if (recommendRequestDTO.getMemberIds().isEmpty()){
                System.out.println(" 시청한 작품 목록 + 찜한 작품 목록 모두 키워드 가져와서 상위 2개만 골라서 추천  + 시청한 목록에 있는 작품은 제외= 기본 추천");
                moviesInfoDTOList = friendMapper.getRecommendMovieByUserId(userId);

            }else{
                System.out.println(" 친구 포함 기본 추천 부분!!!!!!!!");
                List<String> memberIds = recommendRequestDTO.getMemberIds();
                memberIds.add(userId);

                moviesInfoDTOList = friendMapper.getRecommendMovieByMemberIds(memberIds);
            }
        }else if ("liked".equals(recommendRequestDTO.getRecommendOption())){
            //친구를 아무도 선택하지 않았으면 유저 기준으로 추천을 해야된다.
            if (recommendRequestDTO.getMemberIds().isEmpty()){
                System.out.println(" 찜목록과 유사한 작품 추천 로직에 들어가나? 유저혼자 기준");
                moviesInfoDTOList = friendMapper.getRecommendMovieByUserIdFromMovieWish(userId);

            }else{
                List<String> memberIds = recommendRequestDTO.getMemberIds();
                memberIds.add(userId);

                moviesInfoDTOList = friendMapper. getRecommendMovieByMemberIdsFromWish(memberIds);
            }
        }else if ("watched".equals(recommendRequestDTO.getRecommendOption())){
            //친구를 아무도 선택하지 않았으면 유저 기준으로 추천을 해야된다.
            if (recommendRequestDTO.getMemberIds().isEmpty()){
                System.out.println(" 시청한목록 유사한 작품 추천 로직에 들어가나? 유저혼자 기준");
                moviesInfoDTOList = friendMapper.getRecommendMovieByUserIdFromMemberViewlist(userId);

            }else{
                List<String> memberIds = recommendRequestDTO.getMemberIds();
                memberIds.add(userId);

                moviesInfoDTOList = friendMapper.getRecommendMovieByMemberIdsFromViewlist(memberIds);
            }
        }else {
            // null인 경우나 다른거일 경우
            moviesInfoDTOList = null;
        }

        //어떤게 추천되는지 체크
        for (RecommendMoviesInfoDTO moviesInfoDTO : moviesInfoDTOList){
            System.out.println("moviesInfoDTO.getTitle() = " + moviesInfoDTO.getTitle());
            System.out.println("moviesInfoDTO.getDescription() = " + moviesInfoDTO.getDescription());
            System.out.println("moviesInfoDTO.getPoster() = " + moviesInfoDTO.getPoster());
        }
        System.out.println("이게 마지막 부분인데 이전에 추천되는 영화가 나와야된다." );
        return moviesInfoDTOList;
    }
}
