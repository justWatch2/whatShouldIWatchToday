package avengers.waffle.service.impl.recommendFriends;

import avengers.waffle.VO.recommendFriends.RecommendFriendsInfoDTO;
import avengers.waffle.VO.recommendFriends.RecommendMoviesInfoDTO;
import avengers.waffle.VO.recommendFriends.RecommendRequestDTO;
import avengers.waffle.mapper.FriendMapper;
import avengers.waffle.repository.friends.FriendsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendFriendsService {
    private final FriendMapper friendMapper;
    private final FriendsRepository friendsRepository;

    //친구 목록 불러오기
    public List<RecommendFriendsInfoDTO> getFriendsInfo(String memberId) {
        String friends = friendsRepository.getFriendsIdByMemberId(memberId);
        System.out.println("recommendFriends = " + friends);

        List<String> friendIds = Arrays.asList(friends.split(","));

        for (String friendId : friendIds) {
            System.out.println("리스트로 만든것들!! friendId = " + friendId);
        }
        return friendMapper.getFriendsInfoByMemberId(friendIds);
    }

    //영화 추천 기본 파트
    public List<RecommendMoviesInfoDTO> getMoviesInfo(String userId, RecommendRequestDTO recommendRequestDTO) {
        //userId랑 recommendRequestDTO에 있는 체크박스에 체크된 친구들 가지고
        // 이게 auto 인지 liked, watched 인지 null 인제 ㅊ크 하기 !
        List<RecommendMoviesInfoDTO> moviesInfoDTOList;

        if ("auto".equals(recommendRequestDTO.getRecommendOption())) {
            //친구를 아무도 선택하지 않았으면 유저 기준으로 추천을 해야된다.
            if (recommendRequestDTO.getMemberIds().isEmpty()) {
                System.out.println(" 시청한 작품 목록 + 찜한 작품 목록 모두 키워드 가져와서 상위 2개만 골라서 추천  + 시청한 목록에 있는 작품은 제외= 기본 추천");
                moviesInfoDTOList = friendMapper.getRecommendMovieByUserId(userId);

            } else {
                System.out.println(" 친구 포함 기본 추천 부분!!!!!!!!");
                List<String> memberIds = recommendRequestDTO.getMemberIds();
                memberIds.add(userId);

                System.out.println("친구 포함 추천 할때 memberIds = " + memberIds);

                moviesInfoDTOList = friendMapper.getRecommendMovieByMemberIds(memberIds);
            }
        } else if ("liked".equals(recommendRequestDTO.getRecommendOption())) {
            //친구를 아무도 선택하지 않았으면 유저 기준으로 추천을 해야된다.
            if (recommendRequestDTO.getMemberIds().isEmpty()) {
                System.out.println(" 찜목록과 유사한 작품 추천 로직에 들어가나? 유저혼자 기준");
                moviesInfoDTOList = friendMapper.getRecommendMovieByUserIdFromMovieWish(userId);

            } else {
                List<String> memberIds = recommendRequestDTO.getMemberIds();
                memberIds.add(userId);

                moviesInfoDTOList = friendMapper.getRecommendMovieByMemberIdsFromWish(memberIds);
            }
        } else if ("watched".equals(recommendRequestDTO.getRecommendOption())) {
            //친구를 아무도 선택하지 않았으면 유저 기준으로 추천을 해야된다.
            if (recommendRequestDTO.getMemberIds().isEmpty()) {
                System.out.println(" 시청한목록 유사한 작품 추천 로직에 들어가나? 유저혼자 기준");
                moviesInfoDTOList = friendMapper.getRecommendMovieByUserIdFromMemberViewlist(userId);

            } else {
                List<String> memberIds = recommendRequestDTO.getMemberIds();
                memberIds.add(userId);

                moviesInfoDTOList = friendMapper.getRecommendMovieByMemberIdsFromViewlist(memberIds);
            }
        } else {
            // null인 경우나 다른거일 경우
            moviesInfoDTOList = null;
        }

        //어떤게 추천되는지 체크
//        for (RecommendMoviesInfoDTO moviesInfoDTO : moviesInfoDTOList){
//            System.out.println("moviesInfoDTO.getTitle() = " + moviesInfoDTO.getTitle());
//            System.out.println("moviesInfoDTO.getDescription() = " + moviesInfoDTO.getDescription());
//            System.out.println("moviesInfoDTO.getPoster() = " + moviesInfoDTO.getPoster());
//            System.out.println("moviesInfoDTO.getKeywords() = " + moviesInfoDTO.getKeywords());
//        }
//        System.out.println("이게 마지막 부분인데 이전에 추천되는 영화가 나와야된다." );


        return moviesInfoDTOList;
    }

    //bfs로 추천 로직 구현 부분
//    public List<RecommendMoviesInfoDTO> bfsRecommend(List<RecommendMoviesInfoDTO> candidates, int maxDepth, int recommendSize) {
//
//        System.out.println(" bfs 시작!!!!" );
//        // 1. 시작 노드 선정
//        RecommendMoviesInfoDTO start = null;
//        int maxRanking = Integer.MIN_VALUE;
//
//        for (RecommendMoviesInfoDTO candidate : candidates) {
//            if (candidate != null && candidate.getKeywordsRanking() > maxRanking) {
//                maxRanking = candidate.getKeywordsRanking();
//                start = candidate;
//            }
//        }
//
//        if (start == null)
//            return Collections.emptyList();
//
//        int startId = start.getMovieId();
//        System.out.println("bfs 로직에서 !!!!! startId = " + startId);
//        // 2. 영화 id -> idToMovie 에  매핑
//        Map<Integer, RecommendMoviesInfoDTO> idToMovie = new HashMap<>();
//
//        for (RecommendMoviesInfoDTO movie : candidates) {
//            if (movie != null) {
//                idToMovie.put(movie.getMovieId(), movie);
//            }
//        }
//
//        // 3. 그래프 구성 (movieId -> 연결된 movieId들 + 가중치)
//        Map<Integer, Map<Integer, Integer>> graph = new HashMap<>();
//
//        for (RecommendMoviesInfoDTO a : candidates) {
//            for (RecommendMoviesInfoDTO b : candidates) {
//                if (a.getMovieId() == b.getMovieId())
//                    continue;
//                List<String> keywordsAList = a.getKeywords(); //List로 하면 중복 허용 + 순서가 중요 → 의도치 않게 잘못된 결과가 나올 수 있어요.
//                List<String> keywordsBList = b.getKeywords();
//
//                if (keywordsAList == null || keywordsBList == null)
//                    continue; // null이면 skip
//
//                Set<String> keywordsA = new HashSet<>(keywordsAList); //List → Set으로 바꾸는 이유는 교집합을 빠르게, 정확하게 구하기 위해서
//                Set<String> keywordsB = new HashSet<>(keywordsBList);
//                keywordsA.retainAll(keywordsB);    // keywordsB와 공통된 요소만 남기고 나머지는 모조리 keywordsA 에서 제거
//                                                    // Set은 내부적으로 **Hash 기반 (O(1) 조회)
//                // List<String> listA = List.of("spy", "spy", "mind");
//                //List<String> listB = List.of("spy", "time");
//                //listA.retainAll(listB); // 결과: ["spy", "spy"]  -> 리스트로 하게 된다면 이렇게 중복 체크가 안된다.
//
//                if (!keywordsA.isEmpty()) {
//                    graph.computeIfAbsent(a.getMovieId(), k -> new HashMap<>())
//                            .put(b.getMovieId(), keywordsA.size());
//                }
//            }
//        }
//
//            // 4. BFS 탐색
//            Set<Integer> visited = new HashSet<>();  // 탐색이 된곳을 체크 할때
//            Queue<int[]> queue = new LinkedList<>(); // int[]{movieId, depth}
//            List<RecommendMoviesInfoDTO> recommended = new ArrayList<>();  // 결과저장용
//
//            queue.offer(new int[]{startId, 0});
//            visited.add(startId);
//
//            while (!queue.isEmpty()) {
//                int[] current = queue.poll();
//                int movieId = current[0];
//                int depth = current[1];
//
//                if (depth > 0) {
//                    recommended.add(idToMovie.get(movieId));
//                }
//                if (depth >= maxDepth) continue;
//
//                Map<Integer, Integer> neighbors = graph.getOrDefault(movieId, Collections.emptyMap());
//                for (Integer neighborId : neighbors.keySet()) {
//                    if (!visited.contains(neighborId)) {
//                        visited.add(neighborId);
//                        queue.offer(new int[]{neighborId, depth + 1});
//                    }
//                }
//            }
//
//            // 5. 정렬 기준 적용 (ex: 평점 높은 순)
//        // 평점 내림차순 정렬
//        recommended.sort((a, b) -> Long.compare(b.getKeywordsRanking(), a.getKeywordsRanking()));
//
//        // 추천 개수 제한
//        int endIndex = Math.min(recommendSize, recommended.size());
//
//        return new ArrayList<>(recommended.subList(0, endIndex));
//        }

//    public List<RecommendMoviesInfoDTO> bfsRecommend(List<RecommendMoviesInfoDTO> candidates, int maxDepth, int recommendSize) {
//        System.out.println("bfs 시작!!!!");
//
//        // 영화 ID → 영화 DTO 매핑
//        Map<Integer, RecommendMoviesInfoDTO> idToMovie = new HashMap<>();
//        for (RecommendMoviesInfoDTO movie : candidates) {
//            if (movie != null) idToMovie.put(movie.getMovieId(), movie);
//        }
//
//        // 그래프 구성
//        Map<Integer, Map<Integer, Integer>> graph = new HashMap<>();
//        for (RecommendMoviesInfoDTO a : candidates) {
//            for (RecommendMoviesInfoDTO b : candidates) {
//                if (a.getMovieId() == b.getMovieId()) continue;
//                Set<String> keywordsA = new HashSet<>(Optional.ofNullable(a.getKeywords()).orElse(List.of()));
//                Set<String> keywordsB = new HashSet<>(Optional.ofNullable(b.getKeywords()).orElse(List.of()));
//                keywordsA.retainAll(keywordsB);
//                if (!keywordsA.isEmpty()) {
//                    graph.computeIfAbsent(a.getMovieId(), k -> new HashMap<>())
//                            .put(b.getMovieId(), keywordsA.size());
//                }
//            }
//        }
//
//        // 시작 노드를 여러 개: keywordsRanking 상위 N개 (ex. 3개)
//        int numStartNodes = 3;
//        List<RecommendMoviesInfoDTO> sortedCandidates = candidates.stream()
//                .filter(Objects::nonNull)
//                .sorted(Comparator.comparingInt(RecommendMoviesInfoDTO::getKeywordsRanking).reversed())
//                .limit(numStartNodes)
//                .toList();
//
//        // BFS 시작
//        Set<Integer> visited = new HashSet<>();
//        Queue<int[]> queue = new LinkedList<>();
//        List<RecommendMoviesInfoDTO> recommended = new ArrayList<>();
//
//        for (RecommendMoviesInfoDTO start : sortedCandidates) {
//            if (start != null && visited.add(start.getMovieId())) {
//                queue.offer(new int[]{start.getMovieId(), 0});
//            }
//        }
//
//        while (!queue.isEmpty()) {
//            int[] current = queue.poll();
//            int movieId = current[0];
//            int depth = current[1];
//
//            if (depth > 0) recommended.add(idToMovie.get(movieId));
//            if (depth >= maxDepth) continue;
//
//            Map<Integer, Integer> neighbors = graph.getOrDefault(movieId, Map.of());
//            for (Integer neighborId : neighbors.keySet()) {
//                if (visited.add(neighborId)) {
//                    queue.offer(new int[]{neighborId, depth + 1});
//                }
//            }
//        }
//
//        recommended.sort((a, b) -> Long.compare(b.getRating(), a.getRating()));
//        return recommended.subList(0, Math.min(recommendSize, recommended.size()));
//    }
//public List<RecommendMoviesInfoDTO> bfsRecommend(List<RecommendMoviesInfoDTO> candidates, int maxDepth, int recommendSize) {
//    if (candidates == null || candidates.isEmpty()) return Collections.emptyList();
//
//    System.out.println(" bfs 시작");
//    // 1. 시작 노드 선정: 가장 공통된 키워드를 많이 가진  영화
//    RecommendMoviesInfoDTO start = null;
//    long maxRanking = Long.MIN_VALUE;
//    for (RecommendMoviesInfoDTO candidate : candidates) {
//        if (candidate != null && candidate.getKeywordsRanking() > maxRanking) {
//            maxRanking = candidate.getKeywordsRanking();
//            start = candidate;
//        }
//    }
//
//    if (start == null) return Collections.emptyList();
//
//    int startId = start.getMovieId();
//
//    // 2. 영화 ID → 객체 매핑
//    Map<Integer, RecommendMoviesInfoDTO> idToMovie = new HashMap<>();
//    for (RecommendMoviesInfoDTO movie : candidates) {
//        if (movie != null) {
//            idToMovie.put(movie.getMovieId(), movie);
//        }
//    }
//
//    // 3. 그래프 구성: movieId -> (연결된 movieId -> 공통 키워드 수)
//    Map<Integer, Map<Integer, Integer>> graph = new HashMap<>();
//    for (RecommendMoviesInfoDTO a : candidates) {
//        for (RecommendMoviesInfoDTO b : candidates) {
//            if (a.getMovieId() == b.getMovieId()) continue;
//
//            List<String> keywordsAList = a.getKeywords();
//            List<String> keywordsBList = b.getKeywords();
//            if (keywordsAList == null || keywordsBList == null) continue;
//
//            Set<String> keywordsA = new HashSet<>(keywordsAList);
//            Set<String> keywordsB = new HashSet<>(keywordsBList);
//            keywordsA.retainAll(keywordsB);
//
//            if (!keywordsA.isEmpty()) {
//                graph.computeIfAbsent(a.getMovieId(), k -> new HashMap<>())
//                        .put(b.getMovieId(), keywordsA.size());
//            }
//        }
//    }
//
//    // 4. 가중치 기반 우선순위 탐색
//    class Node {
//        int movieId;
//        int depth;
//        double score;
//
//        Node(int movieId, int depth, double score) {
//            this.movieId = movieId;
//            this.depth = depth;
//            this.score = score;
//        }
//    }
//
//    PriorityQueue<Node> queue = new PriorityQueue<>((a, b) -> Double.compare(b.score, a.score));
//    Set<Integer> visited = new HashSet<>();
//    List<RecommendMoviesInfoDTO> recommended = new ArrayList<>();
//
//    queue.offer(new Node(startId, 0, Double.MAX_VALUE)); // 시작 노드는 무조건 최고점
//
//    while (!queue.isEmpty() && recommended.size() < recommendSize) {
//        Node current = queue.poll();
//        int movieId = current.movieId;
//        int depth = current.depth;
//
//        if (visited.contains(movieId)) continue;
//        visited.add(movieId);
//
//        if (depth > 0) { // 시작 노드는 제외
//            recommended.add(idToMovie.get(movieId));
//        }
//
//        if (depth >= maxDepth) continue;
//
//        Map<Integer, Integer> neighbors = graph.getOrDefault(movieId, Collections.emptyMap());
//        for (Map.Entry<Integer, Integer> entry : neighbors.entrySet()) {
//            int neighborId = entry.getKey();
//            int keywordWeight = entry.getValue();
//
//            if (!visited.contains(neighborId)) {
//                RecommendMoviesInfoDTO neighborMovie = idToMovie.get(neighborId);
//                double score = keywordWeight * neighborMovie.getKeywordsRanking(); // 예: 공통 키워드 수 × 평점
//                queue.offer(new Node(neighborId, depth + 1, score));
//            }
//        }
//    }
//    // ✅ 평점 기준 정렬 (내림차순)
//    recommended.sort((a, b) -> Long.compare(b.getRating(), a.getRating()));
//
//
//    return recommended;
//}

//    public List<RecommendMoviesInfoDTO> bfsRecommend(List<RecommendMoviesInfoDTO> candidates, int maxDepth, int recommendSize) {
//        if (candidates == null || candidates.isEmpty()) return Collections.emptyList();
//
//        System.out.println("bfs 시작");
//
//        // 1. 시작 노드 선정: 가장 높은 keywordsRanking 가진 영화
//        RecommendMoviesInfoDTO start = null;
//        long maxRanking = Long.MIN_VALUE;
//        for (RecommendMoviesInfoDTO candidate : candidates) {
//            if (candidate != null && candidate.getKeywordsRanking() > maxRanking) {
//                maxRanking = candidate.getKeywordsRanking();
//                start = candidate;
//            }
//        }
//        if (start == null) return Collections.emptyList();
//
//        int startId = start.getMovieId();
//
//        // 2. 영화 ID -> 객체 매핑
//        Map<Integer, RecommendMoviesInfoDTO> idToMovie = new HashMap<>();
//        for (RecommendMoviesInfoDTO movie : candidates) {
//            if (movie != null) {
//                idToMovie.put(movie.getMovieId(), movie);
//            }
//        }
//
//        // 3. 역인덱스 구성: 키워드 -> 그 키워드를 가진 영화 ID 리스트
//        Map<String, List<Integer>> keywordToMovies = new HashMap<>();
//        for (RecommendMoviesInfoDTO movie : candidates) {
//            List<String> keywords = movie.getKeywords();
//            if (keywords == null) continue;
//
//            for (String keyword : keywords) {
//                keywordToMovies.computeIfAbsent(keyword, k -> new ArrayList<>()).add(movie.getMovieId());
//            }
//        }
//
//        // 4. 그래프 구성: movieId -> (이웃 movieId -> 공통 키워드 수)
//        Map<Integer, Map<Integer, Integer>> graph = new HashMap<>();
//        for (List<Integer> movieList : keywordToMovies.values()) {
//            int size = movieList.size();
//            // 같은 키워드를 가진 영화들끼리 쌍을 만든다
//            for (int i = 0; i < size; i++) {
//                int movieA = movieList.get(i);
//                for (int j = i + 1; j < size; j++) {
//                    int movieB = movieList.get(j);
//
//                    // movieA -> movieB
//                    graph.computeIfAbsent(movieA, k -> new HashMap<>())
//                            .merge(movieB, 1, Integer::sum);
//
//                    // movieB -> movieA (양방향)
//                    graph.computeIfAbsent(movieB, k -> new HashMap<>())
//                            .merge(movieA, 1, Integer::sum);
//                }
//            }
//        }
//
//        // 5. 가중치 기반 우선순위 탐색 (BFS 변형)
//        class Node {
//            int movieId;
//            int depth;
//            double score;
//
//            Node(int movieId, int depth, double score) {
//                this.movieId = movieId;
//                this.depth = depth;
//                this.score = score;
//            }
//        }
//
//        PriorityQueue<Node> queue = new PriorityQueue<>((a, b) -> Double.compare(b.score, a.score));
//        Set<Integer> visited = new HashSet<>();
//        List<RecommendMoviesInfoDTO> recommended = new ArrayList<>();
//
//        queue.offer(new Node(startId, 0, Double.MAX_VALUE)); // 시작 노드 최고 점수
//
//        while (!queue.isEmpty() && recommended.size() < recommendSize) {
//            Node current = queue.poll();
//            int movieId = current.movieId;
//            int depth = current.depth;
//
//            if (visited.contains(movieId)) continue;
//            visited.add(movieId);
//
//            if (depth > 0) { // 시작 노드 제외
//                recommended.add(idToMovie.get(movieId));
//            }
//
//            if (depth >= maxDepth) continue;
//
//            Map<Integer, Integer> neighbors = graph.getOrDefault(movieId, Collections.emptyMap());
//            for (Map.Entry<Integer, Integer> entry : neighbors.entrySet()) {
//                int neighborId = entry.getKey();
//                int keywordWeight = entry.getValue();
//
//                if (!visited.contains(neighborId)) {
//                    RecommendMoviesInfoDTO neighborMovie = idToMovie.get(neighborId);
//                    double score = keywordWeight * neighborMovie.getKeywordsRanking();
//                    queue.offer(new Node(neighborId, depth + 1, score));
//                }
//            }
//        }
//
//        // 평점 기준 내림차순 정렬

    /// /        recommended.sort((a, b) -> Long.compare(b.getRating(), a.getRating()));
//
//        return recommended;
//    }
    public List<RecommendMoviesInfoDTO> bfsRecommend(List<RecommendMoviesInfoDTO> candidates, int maxDepth, int recommendSize, int topNStartMovies) {
        if (candidates == null || candidates.isEmpty())
            return Collections.emptyList();

        System.out.println("bfs 시작");

        // 1. 영화 ID -> 객체 매핑
        Map<Integer, RecommendMoviesInfoDTO> idToMovie = new HashMap<>();
        for (RecommendMoviesInfoDTO movie : candidates) {
            if (movie != null) {
                idToMovie.put(movie.getMovieId(), movie);
            }
        }

        // 2. 역인덱스 구성: 키워드 -> 그 키워드를 가진 영화 ID 리스트
        Map<String, List<Integer>> keywordToMovies = new HashMap<>();
        for (RecommendMoviesInfoDTO movie : candidates) {
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
        List<RecommendMoviesInfoDTO> topNStarts = new ArrayList<>();

        // 4-1. null 아닌 영화만 골라서 리스트에 담기
        for (RecommendMoviesInfoDTO movie : candidates) {
            if (movie != null) {
                topNStarts.add(movie);
            }
        }

        // 4-2. keywordsRanking 기준 내림차순 정렬 => 리스트 정렬 의미함!
        Collections.sort(topNStarts, new Comparator<RecommendMoviesInfoDTO>() {
            @Override
            public int compare(RecommendMoviesInfoDTO a, RecommendMoviesInfoDTO b) {
                return Long.compare(b.getKeywordsRanking(), a.getKeywordsRanking());
            }
        });

        // 4-3. 리스트 크기가 topNStartMovies보다 크면 잘라내기
        if (topNStarts.size() > topNStartMovies) {
            topNStarts = topNStarts.subList(0, topNStartMovies);
        }


        // 5. 추천 탐색 수행 (중복 제거)
        Set<Integer> visitedGlobal = new HashSet<>();   //visitedGlobal	전체 추천 리스트에서 이미 추천한 영화 다시 추천하지 않게 방지

        List<RecommendMoviesInfoDTO> finalRecommendations = new ArrayList<>();

        int recommendPerStart = recommendSize / topNStarts.size();

        for (RecommendMoviesInfoDTO startMovie : topNStarts) {
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

            List<RecommendMoviesInfoDTO> localRecommendations = new ArrayList<>();  //여러 시작노드에서 각각노드의 결과 값들 저장

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
                    RecommendMoviesInfoDTO recommended = idToMovie.get(movieId);
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
                        RecommendMoviesInfoDTO neighborMovie = idToMovie.get(neighborId);
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

//    // 평점 기준 내림차순 정렬
        finalRecommendations.sort((a, b) -> Long.compare(b.getKeywordsRanking(), a.getKeywordsRanking()));
        // 6. 최종 정렬 (평점 → 키워드 랭킹)
//    finalRecommendations.sort(Comparator
//            .comparingDouble(RecommendMoviesInfoDTO::getRating).reversed() // 평점 내림차순
//            .thenComparingLong(RecommendMoviesInfoDTO::getKeywordsRanking).reversed()); // 키워드랭킹 내림차순

        return finalRecommendations;
    }


}
