package avengers.waffle.service.impl.recommendFriends;

import avengers.waffle.VO.recommendFriends.RecommendFriendsInfoDTO;
import avengers.waffle.VO.recommendFriends.RecommendInfoDTO;
import avengers.waffle.VO.recommendFriends.RecommendRequestDTO;
import avengers.waffle.mapper.FriendMapper;
import avengers.waffle.repository.friends.FriendsRepository;
import avengers.waffle.service.IF.recommendFriends.IF_RecommendFriendsService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendFriendsService implements IF_RecommendFriendsService {
    private final FriendMapper friendMapper;
    private final FriendsRepository friendsRepository;

    //친구 목록 && 친구별 영화 찜 수 시청한 작푹수 불러오기
    public List<RecommendFriendsInfoDTO> getFriendsInfoFromMovies(String memberId) {
        String friends = friendsRepository.getFriendsIdByMemberId(memberId);
        System.out.println("recommendFriends = " + friends);

        if (friends == null) {
            return null;
        }
        List<String> friendIds = Arrays.asList(friends.split(","));

        for (String friendId : friendIds) {
            System.out.println("리스트로 만든것들!! friendId = " + friendId);
        }

        //시간초 떄문에 변경

        return friendMapper.getFriendsInfoByMemberIdFromMovies(friendIds);
    }

    //친구 목록 && 친구별 tvShow 찜 수 시청한 작푹수 불러오기
    public List<RecommendFriendsInfoDTO> getFriendsInfoFromTvshow(String memberId) {
        String friends = friendsRepository.getFriendsIdByMemberId(memberId);
        System.out.println("recommendFriends = " + friends);

        if (friends == null) {
            return null;
        }
        List<String> friendIds = Arrays.asList(friends.split(","));

        for (String friendId : friendIds) {
            System.out.println("리스트로 만든것들!! friendId = " + friendId);
        }
        return friendMapper.getFriendsInfoByMemberIdFromTvshow(friendIds);
    }

    //영화 + 드라마  추천 기본 파트
    public List<RecommendInfoDTO> getMoviesInfo(String userId, RecommendRequestDTO recommendRequestDTO, boolean  isMovies) {
        //userId랑 recommendRequestDTO에 있는 체크박스에 체크된 친구들 가지고
        // 이게 auto 인지 liked, watched 인지 null 인제 ㅊ크 하기 !
        List<RecommendInfoDTO> moviesInfoDTOList = null;
        int recommendType = 0;

        if ("auto".equals(recommendRequestDTO.getRecommendOption())) {
                System.out.println(" 개인 + 친구포함 => 기본 추천 부분!!!!!!!!");
                List<String> memberIds = recommendRequestDTO.getMemberIds();
                memberIds.add(userId);
                recommendType = 1;
                if (isMovies) {
                    moviesInfoDTOList = friendMapper.getRecommendMovieByMemberIds(memberIds, recommendType);
                }else{
                    moviesInfoDTOList = friendMapper.getRecommendTvShowByMemberIds(memberIds, recommendType);
                }

//            }
        } else if ("liked".equals(recommendRequestDTO.getRecommendOption())) {
                System.out.println(" 개인 + 친구포함 => 찜목록만  추천 부분!!!!!!!!");

                List<String> memberIds = recommendRequestDTO.getMemberIds();
                memberIds.add(userId);
                recommendType = 2;
                if (isMovies) {
                    System.out.println("영화 클릭했다");
                    moviesInfoDTOList = friendMapper.getRecommendMovieByMemberIds(memberIds, recommendType);
                    System.out.println("moviesInfoDTOList = " + moviesInfoDTOList);
                    for ( RecommendInfoDTO moviesInfoDTOLists :  moviesInfoDTOList){
                        System.out.println(" 쿼리문 돌고나서 moviesInfoDTOLists.getTitle() = " + moviesInfoDTOLists.getTitle());
                    }
                }else{
                    System.out.println("드라마 클릭했다");
                    moviesInfoDTOList = friendMapper.getRecommendTvShowByMemberIds(memberIds, recommendType);
                }
        } else if ("watched".equals(recommendRequestDTO.getRecommendOption())) {
            System.out.println(" 개인 + 친구포함 => 시청 기록 목록만  추천 부분!!!!!!!!");

            List<String> memberIds = recommendRequestDTO.getMemberIds();
            memberIds.add(userId);
            recommendType = 3;
            if (isMovies) {
                moviesInfoDTOList = friendMapper.getRecommendMovieByMemberIds(memberIds, recommendType);
            }else{
                moviesInfoDTOList = friendMapper.getRecommendTvShowByMemberIds(memberIds, recommendType);
            }
        }

        
        System.out.println(" 친구 추천 부분에서 결과 값이 나왔다 bfs돌고 나서 " );
        return moviesInfoDTOList;
    }

    public List<RecommendInfoDTO> bfsRecommendForMovie(List<RecommendInfoDTO> candidates, int maxDepth, int recommendSize, int topNStartMovies) {
        if (candidates == null || candidates.isEmpty())
            return Collections.emptyList();

        System.out.println("bfs 시작");

        // 1. 영화 ID -> 객체 매핑
        Map<Integer, RecommendInfoDTO> idToMovie = new HashMap<>();
        for (RecommendInfoDTO movie : candidates) {
            if (movie != null) {
                idToMovie.put(movie.getMovieId(), movie);
            }
        }

        // 2. 역인덱스 구성: 키워드 -> 그 키워드를 가진 영화 ID 리스트
        Map<String, List<Integer>> keywordToMovies = new HashMap<>();
        for (RecommendInfoDTO movie : candidates) {
            List<String> keywords = movie.getKeywords();
            if (keywords == null)
                continue;
            for (String keyword : keywords) {
                keywordToMovies.computeIfAbsent(keyword, k -> new ArrayList<>()).add(movie.getMovieId());  // chek부분 : computeIfAbsent
            }
        }
        //역인덱스 사용이유
//        역인덱스는 키워드 → 영화 리스트를 미리 만들어서
//        공통 키워드를 가진 영화들끼리만 비교하도록 도와줌
//        결과적으로 전체 영화 쌍을 비교하는 것보다 훨씬 적은 연산으로 공통 키워드 기반 연결을 빠르게 찾을 수 있음
//        그래서 추천 시스템에서 역인덱스를 활용하면 시간 효율이 크게 개선됨


        // 3. 그래프 구성: movieId -> (이웃 movieId -> 공통 키워드 수)
        Map<Integer, Map<Integer, Integer>> graph = new HashMap<>();

        for (List<Integer> movieList : keywordToMovies.values()) {
            int size = movieList.size();
            for (int i = 0; i < size; i++) {
                int movieA = movieList.get(i);
                for (int j = i + 1; j < size; j++) {
                    int movieB = movieList.get(j);

                    graph.computeIfAbsent(movieA, k -> new HashMap<>())
                            .merge(movieB, 1, (oldValue, newValue) -> oldValue + newValue);
                    graph.computeIfAbsent(movieB, k -> new HashMap<>())
                            .merge(movieA, 1, (oldValue, newValue) -> oldValue + newValue);  // 여기서 value값으로 1은 가중치 +1해주는 부분

                    //map.merge(key, value, remappingFunction)
                    //키가 존재하지 않으면:
                    //해당 key에 아무 값도 없거나, 값이 null이면
                    //value를 그대로 저장함 (put(key, value)와 같음)

                    //키가 존재하면:
                    //현재 저장된 값(oldValue)과 새로 넣으려는 값(value)을 remappingFunction에 넘김
                    //remappingFunction.apply(oldValue, value)의 결과를 저장함
                }
            }
        }

        // 4. 시작 노드 N개 선정 (keywordsRanking 기준 상위 N개)
        List<RecommendInfoDTO> topNStarts = new ArrayList<>();

        // 4-1. null 아닌 영화만 골라서 리스트에 담기
        for (RecommendInfoDTO movie : candidates) {
            if (movie != null) {
                topNStarts.add(movie);
            }
        }

        // 4-2. keywordsRanking 기준 내림차순 정렬 => 리스트 정렬 의미함!
        Collections.sort(topNStarts, new Comparator<RecommendInfoDTO>() {
            @Override
            public int compare(RecommendInfoDTO a, RecommendInfoDTO b) {
                return Long.compare(b.getKeywordsRanking(), a.getKeywordsRanking());
            }
        });

        // 4-3. 리스트 크기가 topNStartMovies보다 크면 잘라내기
        if (topNStarts.size() > topNStartMovies) {
            topNStarts = topNStarts.subList(0, topNStartMovies);
        }


        // 5. 추천 탐색 수행 (중복 제거)
        Set<Integer> visitedGlobal = new HashSet<>();   //visitedGlobal	전체 추천 리스트에서 이미 추천한 영화 다시 추천하지 않게 방지

        List<RecommendInfoDTO> finalRecommendations = new ArrayList<>();

        int recommendPerStart = recommendSize / topNStarts.size();

        for (RecommendInfoDTO startMovie : topNStarts) {
            int startId = startMovie.getMovieId();
            System.out.println(" 시작 노드 돌아가는거 숫자 체크" );
            //편향 문제
            int currentMovieTotalKeywords = idToMovie.get(startId).getKeywords().size(); // 전체 키워드 수

            class Node {
                int movieId;
                int depth;
                double score;

                Node(int movieId, int depth, double score) {
                    this.movieId = movieId;
                    this.depth = depth;
                    this.score = score;
                }
            }

            PriorityQueue<Node> queue = new PriorityQueue<>((a, b) -> Double.compare(b.score, a.score));
            Set<Integer> visitedLocal = new HashSet<>();  //visitedLocal	현재 하나의 시작 영화(startMovie) 기준으로 중복 탐색 방지

            List<RecommendInfoDTO> localRecommendations = new ArrayList<>();  //여러 시작노드에서 각각노드의 결과 값들 저장

            queue.offer(new Node(startId, 0, Double.MAX_VALUE));

            while (!queue.isEmpty() &&
                    localRecommendations.size() < recommendPerStart &&
                    finalRecommendations.size() < recommendSize) {
                Node current = queue.poll();
                int movieId = current.movieId;
                int depth = current.depth;

                if (visitedLocal.contains(movieId) || visitedGlobal.contains(movieId))
                    continue;
                visitedLocal.add(movieId);

//                if (depth > 0) {
//                    finalRecommendations.add(idToMovie.get(movieId));
//                    visitedGlobal.add(movieId); // 전역 방문 처리
//                }
                if (depth > 0) {
                    RecommendInfoDTO recommended = idToMovie.get(movieId);
                    localRecommendations.add(recommended);
                    visitedGlobal.add(movieId);
                }

                if (depth >= maxDepth)
                    continue;

                Map<Integer, Integer> neighbors   = graph.getOrDefault(movieId, Collections.emptyMap());

                for (Map.Entry<Integer, Integer> entry : neighbors.entrySet()) {
                    int neighborId = entry.getKey();
                    int keywordWeight = entry.getValue();

                    if (!visitedLocal.contains(neighborId) && !visitedGlobal.contains(neighborId)) {
                        RecommendInfoDTO neighborMovie = idToMovie.get(neighborId);
//                        double score = keywordWeight * neighborMovie.getKeywordsRanking();   // 이부분에서 편향 부분 잡으면 된다
                        // 편향 완화 점수 계산 부분 시작

//                        double normalizedWeight = keywordWeight / (double) currentMovieTotalKeywords;  //정규화 부분
                        double avgKeywordCount = (currentMovieTotalKeywords + neighborMovie.getKeywords().size()) / 2.0;
                        double normalizedWeight = keywordWeight / avgKeywordCount;

                        double adjustedRanking = Math.log(1 + neighborMovie.getKeywordsRanking()); // log 보정

//                        double depthDecay = 1.0 / (depth + 1);  // 깊이 보정

//                        double score = normalizedWeight * adjustedRanking * depthDecay;
                        double score = normalizedWeight * adjustedRanking;

                        queue.offer(new Node(neighborId, depth + 1, score));
                    }
                }
            }

            finalRecommendations.addAll(localRecommendations);

            if (finalRecommendations.size() >= recommendSize)
                break;
        }

//    // 키워드 중복 많이된 순서로  내림차순 정렬
        finalRecommendations.sort((a, b) -> Long.compare(b.getKeywordsRanking(), a.getKeywordsRanking()));
        // 6. 최종 정렬 (평점 → 키워드 랭킹)
//    finalRecommendations.sort(Comparator
//            .comparingDouble(RecommendInfoDTO::getRating).reversed() // 평점 내림차순
//            .thenComparingLong(RecommendInfoDTO::getKeywordsRanking).reversed()); // 키워드랭킹 내림차순

        return finalRecommendations;
    }

    public List<RecommendInfoDTO> bfsRecommendForTvshow(List<RecommendInfoDTO> candidates, int maxDepth, int recommendSize, int topNStartMovies) {
        if (candidates == null || candidates.isEmpty())
            return Collections.emptyList();

        System.out.println("bfs 시작");

        // 1. 영화 ID -> 객체 매핑
        Map<Integer, RecommendInfoDTO> idToTvShow = new HashMap<>();
        for (RecommendInfoDTO tvShow : candidates) {
            if (tvShow != null) {
                idToTvShow.put(tvShow.getTvShowId(), tvShow);
            }
        }

        // 2. 역인덱스 구성: 키워드 -> 그 키워드를 가진 영화 ID 리스트
        Map<String, List<Integer>> keywordToTvShow = new HashMap<>();

        for (RecommendInfoDTO tvShow : candidates) {
            List<String> keywords = tvShow.getKeywords();
            if (keywords == null)
                continue;
            for (String keyword : keywords) {
                keywordToTvShow.computeIfAbsent(keyword, k -> new ArrayList<>()).add(tvShow.getTvShowId());  // chek부분 : computeIfAbsent
            }
        }
        //역인덱스 사용이유
//        역인덱스는 키워드 → 영화 리스트를 미리 만들어서
//        공통 키워드를 가진 영화들끼리만 비교하도록 도와줌
//        결과적으로 전체 영화 쌍을 비교하는 것보다 훨씬 적은 연산으로 공통 키워드 기반 연결을 빠르게 찾을 수 있음
//        그래서 추천 시스템에서 역인덱스를 활용하면 시간 효율이 크게 개선됨


        // 3. 그래프 구성: movieId -> (이웃 movieId -> 공통 키워드 수)
        Map<Integer, Map<Integer, Integer>> graph = new HashMap<>();

        for (List<Integer> tvShowList : keywordToTvShow.values()) {
            int size = tvShowList.size();
            for (int i = 0; i < size; i++) {
                int movieA = tvShowList.get(i);
                for (int j = i + 1; j < size; j++) {
                    int movieB = tvShowList.get(j);

                    graph.computeIfAbsent(movieA, k -> new HashMap<>())
                            .merge(movieB, 1, (oldValue, newValue) -> oldValue + newValue);
                    graph.computeIfAbsent(movieB, k -> new HashMap<>())
                            .merge(movieA, 1, (oldValue, newValue) -> oldValue + newValue);  // 여기서 value값으로 1은 가중치 +1해주는 부분

                    //map.merge(key, value, remappingFunction)
                    //키가 존재하지 않으면:
                    //해당 key에 아무 값도 없거나, 값이 null이면
                    //value를 그대로 저장함 (put(key, value)와 같음)

                    //키가 존재하면:
                    //현재 저장된 값(oldValue)과 새로 넣으려는 값(value)을 remappingFunction에 넘김
                    //remappingFunction.apply(oldValue, value)의 결과를 저장함
                }
            }
        }

        // 4. 시작 노드 N개 선정 (keywordsRanking 기준 상위 N개)
        List<RecommendInfoDTO> topNStarts = new ArrayList<>();

        // 4-1. null 아닌 영화만 골라서 리스트에 담기
        for (RecommendInfoDTO tvShow : candidates) {
            if (tvShow != null) {
                topNStarts.add(tvShow);
            }
        }

        // 4-2. keywordsRanking 기준 내림차순 정렬 => 리스트 정렬 의미함!
        Collections.sort(topNStarts, new Comparator<RecommendInfoDTO>() {
            @Override
            public int compare(RecommendInfoDTO a, RecommendInfoDTO b) {
                return Long.compare(b.getKeywordsRanking(), a.getKeywordsRanking());
            }
        });

        // 4-3. 리스트 크기가 topNStartMovies보다 크면 잘라내기
        if (topNStarts.size() > topNStartMovies) {
            topNStarts = topNStarts.subList(0, topNStartMovies);
        }


        // 5. 추천 탐색 수행 (중복 제거)
        Set<Integer> visitedGlobal = new HashSet<>();   //visitedGlobal	전체 추천 리스트에서 이미 추천한 영화 다시 추천하지 않게 방지

        List<RecommendInfoDTO> finalRecommendations = new ArrayList<>();

        int recommendPerStart = recommendSize / topNStarts.size();

        for (RecommendInfoDTO startTvShow : topNStarts) {
            int startId = startTvShow.getTvShowId();
            System.out.println(" 시작 노드 돌아가는거 숫자 체크" );
            //편향 문제
            int currentMovieTotalKeywords = idToTvShow.get(startId).getKeywords().size(); // 전체 키워드 수

            class Node {
                int tvShowId;
                int depth;
                double score;

                Node(int tvShowId, int depth, double score) {
                    this.tvShowId = tvShowId;
                    this.depth = depth;
                    this.score = score;
                }
            }

            PriorityQueue<Node> queue = new PriorityQueue<>((a, b) -> Double.compare(b.score, a.score));
            Set<Integer> visitedLocal = new HashSet<>();  //visitedLocal	현재 하나의 시작 영화(startTvShow) 기준으로 중복 탐색 방지

            List<RecommendInfoDTO> localRecommendations = new ArrayList<>();  //여러 시작노드에서 각각노드의 결과 값들 저장

            queue.offer(new Node(startId, 0, Double.MAX_VALUE));

            while (!queue.isEmpty() &&
                    localRecommendations.size() < recommendPerStart &&
                    finalRecommendations.size() < recommendSize) {
                Node current = queue.poll();
                int movieId = current.tvShowId;
                int depth = current.depth;

                if (visitedLocal.contains(movieId) || visitedGlobal.contains(movieId))
                    continue;
                visitedLocal.add(movieId);

//                if (depth > 0) {
//                    finalRecommendations.add(idToTvShow.get(movieId));
//                    visitedGlobal.add(movieId); // 전역 방문 처리
//                }
                if (depth > 0) {
                    RecommendInfoDTO recommended = idToTvShow.get(movieId);
                    localRecommendations.add(recommended);
                    visitedGlobal.add(movieId);
                }

                if (depth >= maxDepth)
                    continue;

                Map<Integer, Integer> neighbors   = graph.getOrDefault(movieId, Collections.emptyMap());

                for (Map.Entry<Integer, Integer> entry : neighbors.entrySet()) {
                    int neighborId = entry.getKey();
                    int keywordWeight = entry.getValue();

                    if (!visitedLocal.contains(neighborId) && !visitedGlobal.contains(neighborId)) {
                        RecommendInfoDTO neighborMovie = idToTvShow.get(neighborId);
//                        double score = keywordWeight * neighborMovie.getKeywordsRanking();   // 이부분에서 편향 부분 잡으면 된다
                        // 편향 완화 점수 계산 부분 시작

//                        double normalizedWeight = keywordWeight / (double) currentMovieTotalKeywords;  //정규화 부분
                        double avgKeywordCount = (currentMovieTotalKeywords + neighborMovie.getKeywords().size()) / 2.0;
                        double normalizedWeight = keywordWeight / avgKeywordCount;

                        double adjustedRanking = Math.log(1 + neighborMovie.getKeywordsRanking()); // log 보정

//                        double depthDecay = 1.0 / (depth + 1);  // 깊이 보정

//                        double score = normalizedWeight * adjustedRanking * depthDecay;
                        double score = normalizedWeight * adjustedRanking;

                        queue.offer(new Node(neighborId, depth + 1, score));
                    }
                }
            }

            finalRecommendations.addAll(localRecommendations);

            if (finalRecommendations.size() >= recommendSize)
                break;
        }

//    // 키워드 중복 많이된 순서로  내림차순 정렬
        finalRecommendations.sort((a, b) -> Long.compare(b.getKeywordsRanking(), a.getKeywordsRanking()));
        // 6. 최종 정렬 (평점 → 키워드 랭킹)
//    finalRecommendations.sort(Comparator
//            .comparingDouble(RecommendInfoDTO::getRating).reversed() // 평점 내림차순
//            .thenComparingLong(RecommendInfoDTO::getKeywordsRanking).reversed()); // 키워드랭킹 내림차순

        return finalRecommendations;
    }






}
