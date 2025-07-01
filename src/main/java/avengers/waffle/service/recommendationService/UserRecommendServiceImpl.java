package avengers.waffle.service.recommendationService;

import avengers.waffle.dto.mybatis.UserRecommendMovieResultDTO;
import avengers.waffle.dto.mybatis.UserRecommendTVResultDTO;
import avengers.waffle.dto.requestDTO.UserRecommendRequestDTO;
import avengers.waffle.dto.responseDTO.UserRecommendResponseDTO;
import avengers.waffle.dto.searchDTO.UserRecommendSearchDTO;
import avengers.waffle.repository.recommendationMovieRepository.RecommendationMovieRepository;
import avengers.waffle.repository.recommendationTVRepository.UserRecommendTvRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserRecommendServiceImpl implements UserRecommendService {

    private final RecommendationMovieRepository movieRepository;
    private final UserRecommendTvRepository tvRepository;

    @Override
    public UserRecommendResponseDTO memberRecommend(UserRecommendRequestDTO requestDTO) {


        System.out.println("emfdjhdhasd123123!@#!@#!2312#!@$!@$!@$!@$");
        UserRecommendSearchDTO userRecommendSearchDTO = UserRecommendSearchDTO.builder()
                .userId(requestDTO.getUserId())
                .isDomestic(requestDTO.getRegion().equals("domestic"))
                .adult(requestDTO.getAgeRating().equals("adult"))
                .selectedGenres(requestDTO.getSelectedGenres())
                .build();

        int maxDepth = 3;        // BFS 탐색 최대 깊이
        int recommendSize = 30;  // 최종 추천할 아이템 개수
        int topNStartItems = 5; // BFS 탐색을 시작할 상위 아이템 개수 (keywordsRanking 기준)

        UserRecommendResponseDTO.UserRecommendResponseDTOBuilder responseBuilder = UserRecommendResponseDTO.builder();

        if (requestDTO.getMediaType().equals("movie")) {
            List<UserRecommendMovieResultDTO> candidateMovies = movieRepository.getMovieCandidates(userRecommendSearchDTO);
            System.out.println("candidate arrive");
            List<UserRecommendMovieResultDTO> finalUserRecommend = bfsRecommendForMovies(candidateMovies, maxDepth, recommendSize, topNStartItems);
            responseBuilder.userSelectedList(finalUserRecommend); // 영화 목록
        } else if (requestDTO.getMediaType().equals("tv")) {
            // !!! TV 추천 로직 추가 !!!
            log.info("userRecommendSearchDTO={}", userRecommendSearchDTO);
            List<UserRecommendTVResultDTO> candidateTvs = tvRepository.getTvCandidates(userRecommendSearchDTO); // TvMapper 사용
            log.info("11111111111111111111111111111111111");
            log.info("Found {} tv", candidateTvs.size());
            List<UserRecommendTVResultDTO> finalUserRecommend = bfsRecommendForTvs(candidateTvs, maxDepth, recommendSize, topNStartItems);
            responseBuilder.userSelectedList(finalUserRecommend); // TV 목록
        }
        return responseBuilder.build();
    }


    private List<UserRecommendMovieResultDTO> bfsRecommendForMovies(
            List<UserRecommendMovieResultDTO> candidates,
            int maxDepth,
            int recommendSize,
            int topNStartMovies) {

        if (candidates == null || candidates.isEmpty()) {
            return Collections.emptyList();
        }

        // 1. 영화 ID (int) -> 영화 객체 (UserRecommendResultDTO) 매핑: 빠른 조회를 위함
        Map<Integer, UserRecommendMovieResultDTO> idToMovie = candidates.stream()
                .collect(Collectors.toMap(UserRecommendMovieResultDTO::getMovieId, movie -> movie, (oldValue, newValue) -> oldValue));

        // 2. 역인덱스 구성: 키워드 (String) -> 해당 키워드를 가진 영화 ID (int) 리스트
        Map<String, List<Integer>> keywordToMovies = new HashMap<>();
        for (UserRecommendMovieResultDTO movie : candidates) {
            List<String> keywords = movie.getKeywords();
            if (keywords == null) continue;
            for (String keyword : keywords) {
                keywordToMovies.computeIfAbsent(keyword, k -> new ArrayList<>()).add(movie.getMovieId());
            }
        }

        // 3. 그래프 구성: 영화 ID (int) -> (이웃 영화 ID (int) -> 공통 키워드 수 (Integer)) (가중치 그래프)
        Map<Integer, Map<Integer, Integer>> graph = new HashMap<>();
        for (List<Integer> movieList : keywordToMovies.values()) {
            int size = movieList.size();
            for (int i = 0; i < size; i++) {
                int movieA = movieList.get(i);
                for (int j = i + 1; j < size; j++) {
                    int movieB = movieList.get(j);
                    // 양방향 그래프: movieA -> movieB, movieB -> movieA
                    graph.computeIfAbsent(movieA, k -> new HashMap<>()).merge(movieB, 1, Integer::sum);
                    graph.computeIfAbsent(movieB, k -> new HashMap<>()).merge(movieA, 1, Integer::sum);
                }
            }
        }

        // 4. BFS 탐색 시작 노드 N개 선정 (keywordsRanking 기준 상위 N개)
        // 만약 후보군이 topNStartMovies보다 적으면, 가능한 모든 영화를 시작점으로 사용
        List<UserRecommendMovieResultDTO> topNStartMoviesList = candidates.stream()
                .sorted(Comparator.comparing(UserRecommendMovieResultDTO::getKeywordsRanking, Comparator.nullsLast(Integer::compareTo)).reversed())
                .limit(topNStartMovies)
                .collect(Collectors.toList());

        // 5. 추천 탐색 수행 (BFS 기반) 및 중복 제거
        Set<Integer> visitedGlobal = new HashSet<>(); // 전체 BFS 탐색에서 이미 추천된 영화 ID들을 추적
        List<UserRecommendMovieResultDTO> finalRecommendations = new ArrayList<>(); // 최종 추천 목록

        for (UserRecommendMovieResultDTO startMovie : topNStartMoviesList) {
            int startMovieId = startMovie.getMovieId();

            // 만약 시작 영화 자체가 이미 전역적으로 추천되었다면 건너뜀 (중복 방지)
            if (visitedGlobal.contains(startMovieId)) {
                continue;
            }

            // BFS 탐색을 위한 Node 클래스 정의 (여기서 내부 클래스로 정의)
            class Node {
                int movieId;
                int depth;
                double score; // BFS 탐색 중 해당 노드의 누적 추천 점수

                Node(int movieId, int depth, double score) {
                    this.movieId = movieId;
                    this.depth = depth;
                    this.score = score;
                }
            }

            // PriorityQueue를 사용하여 점수가 높은 노드를 먼저 탐색 (최적화)
            PriorityQueue<Node> queue = new PriorityQueue<>((n1, n2) -> Double.compare(n2.score, n1.score));
            Set<Integer> visitedLocal = new HashSet<>(); // 현재 BFS 경로에서 방문한 영화 ID 추적 (순환 방지)

            // 시작 노드 추가: 깊이 0, 초기 점수는 임의로 높게 설정하여 우선 탐색되도록 함
            queue.offer(new Node(startMovieId, 0, Double.MAX_VALUE));
            visitedLocal.add(startMovieId); // 시작 노드도 방문 처리

            while (!queue.isEmpty() && finalRecommendations.size() < recommendSize) {
                Node current = queue.poll();
                int currentMovieId = current.movieId;
                int currentDepth = current.depth;
                // double currentScore = current.score; // 현재는 사용되지 않지만, 복잡한 점수 계산에 활용 가능

                // 이미 전역적으로 추천된 영화는 더 이상 탐색하거나 추가하지 않음
                if (visitedGlobal.contains(currentMovieId) && currentMovieId != startMovieId) {
                    continue;
                }

                // 현재 영화를 최종 추천 목록에 추가
                // depth > 0 조건은 시작 영화 자체가 아닌 이웃 영화부터 추천 목록에 추가하기 위함.
                // 만약 시작 영화도 추천 목록에 포함하고 싶다면 이 조건을 제거하거나,
                // BFS 시작 전에 `finalRecommendations.add(startMovie);`를 수행하고 visitedGlobal에 추가
                if (currentDepth > 0 || !visitedGlobal.contains(currentMovieId)) { // 시작 영화도 한 번은 포함되도록 조건 조정
                    UserRecommendMovieResultDTO recommendedMovie = idToMovie.get(currentMovieId);
                    if (recommendedMovie != null) {
                        finalRecommendations.add(recommendedMovie);
                        visitedGlobal.add(currentMovieId); // 전역 방문 처리
                        if (finalRecommendations.size() >= recommendSize) {
                            break; // 필요한 추천 개수를 채우면 종료
                        }
                    }
                }

                // 최대 탐색 깊이에 도달했으면 더 이상 이 노드의 이웃을 탐색하지 않음
                if (currentDepth >= maxDepth) {
                    continue;
                }

                // 현재 영화의 이웃 영화들 탐색
                Map<Integer, Integer> neighbors = graph.getOrDefault(currentMovieId, Collections.emptyMap());
                UserRecommendMovieResultDTO currentMovieData = idToMovie.get(currentMovieId);
                // currentMovieKeywordsCount 계산 시 NullPointerException 방지
                int currentMovieKeywordsCount = (currentMovieData != null && currentMovieData.getKeywords() != null) ?
                        currentMovieData.getKeywords().size() : 1; // 0으로 나누기 방지

                for (Map.Entry<Integer, Integer> neighborEntry : neighbors.entrySet()) {
                    int neighborMovieId = neighborEntry.getKey();
                    int commonKeywordWeight = neighborEntry.getValue(); // 이웃과의 공통 키워드 수

                    // 현재 BFS 경로 내에서 방문하지 않았고, 전역적으로도 아직 추천되지 않은 영화만 고려
                    if (!visitedLocal.contains(neighborMovieId) && !visitedGlobal.contains(neighborMovieId)) {
                        UserRecommendMovieResultDTO neighborMovieData = idToMovie.get(neighborMovieId);
                        if (neighborMovieData == null) continue;

                        // 추천 점수 계산 (다양한 요소를 복합적으로 반영)
                        // 1. 공통 키워드 가중치 정규화: (공통 키워드 수) / (현재 영화의 총 키워드 수)
                        double normalizedWeight = (double) commonKeywordWeight / currentMovieKeywordsCount;
                        // 2. 이웃 영화 자체의 키워드 랭킹 (사용자 선호도와 얼마나 일치하는지)
                        // null 처리 및 기본값 설정
                        double neighborKeywordsRank = (neighborMovieData.getKeywordsRanking() != null) ?
                                (double) neighborMovieData.getKeywordsRanking() : 0.0;
                        double adjustedRankingFactor = Math.log(1 + neighborKeywordsRank); // 로그 스케일 적용 (너무 큰 값 방지)
                        // 3. 깊이 감쇠: 시작점에서 멀어질수록 점수 감소 (너무 관련 없는 영화는 배제)
                        double depthDecayFactor = 1.0 / (currentDepth + 1); // 깊이가 0일 때 1.0, 1일 때 0.5, 2일 때 0.33...

                        // 최종 점수 = 정규화된 가중치 * 조정된 랭킹 요소 * 깊이 감쇠 요소
                        double calculatedScore = normalizedWeight * adjustedRankingFactor * depthDecayFactor;

                        // 큐에 추가
                        queue.offer(new Node(neighborMovieId, currentDepth + 1, calculatedScore));
                        visitedLocal.add(neighborMovieId); // 현재 BFS 경로 내에서 방문 처리
                    }
                }
            }
        }

        // 최종 추천 목록을 평점과 키워드 랭킹 기준으로 다시 정렬 (최종 품질 보증)
        finalRecommendations.sort(
                Comparator.comparing(UserRecommendMovieResultDTO::getAverageRating, Comparator.nullsLast(Double::compareTo)).reversed()
                        .thenComparing(UserRecommendMovieResultDTO::getKeywordsRanking, Comparator.nullsLast(Integer::compareTo)).reversed()
        );

        // 최종 추천 개수 제한 (만약 BFS가 recommendSize보다 더 많은 영화를 찾았다면)
        if (finalRecommendations.size() > recommendSize) {
            return finalRecommendations.subList(0, recommendSize);
        }

        return finalRecommendations;
    }

    private List<UserRecommendTVResultDTO> bfsRecommendForTvs(
            List<UserRecommendTVResultDTO> candidates,
            int maxDepth,
            int recommendSize,
            int topNStartTvs) {
        // 위 bfsRecommendForMovies 로직을 그대로 복사하되,
        // 모든 UserRecommendMovieResultDTO -> UserRecommendTvResultDTO로 변경
        // movieId -> tvId, getMovieId() -> getTvId() 등으로 변경
        // ...
        if (candidates == null || candidates.isEmpty()) {
            return Collections.emptyList();
        }

        Map<Integer, UserRecommendTVResultDTO> idToTv = candidates.stream()
                .collect(Collectors.toMap(UserRecommendTVResultDTO::getId, tv -> tv, (oldValue, newValue) -> oldValue));

        Map<String, List<Integer>> keywordToItems = new HashMap<>();
        for (UserRecommendTVResultDTO tv : candidates) {
            List<String> keywords = tv.getKeywords();
            if (keywords == null) continue;
            for (String keyword : keywords) {
                keywordToItems.computeIfAbsent(keyword, k -> new ArrayList<>()).add(tv.getId());
            }
        }

        Map<Integer, Map<Integer, Integer>> graph = new HashMap<>();
        for (List<Integer> itemList : keywordToItems.values()) {
            int size = itemList.size();
            for (int i = 0; i < size; i++) {
                int itemA = itemList.get(i);
                for (int j = i + 1; j < size; j++) {
                    int itemB = itemList.get(j);
                    graph.computeIfAbsent(itemA, k -> new HashMap<>()).merge(itemB, 1, Integer::sum);
                    graph.computeIfAbsent(itemB, k -> new HashMap<>()).merge(itemA, 1, Integer::sum);
                }
            }
        }

        List<UserRecommendTVResultDTO> topNStartItemsList = candidates.stream()
                .sorted(Comparator.comparing(UserRecommendTVResultDTO::getKeywordsRanking, Comparator.nullsLast(Integer::compareTo)).reversed())
                .limit(topNStartTvs)
                .collect(Collectors.toList());

        Set<Integer> visitedGlobal = new HashSet<>();
        List<UserRecommendTVResultDTO> finalRecommendations = new ArrayList<>();

        for (UserRecommendTVResultDTO startItem : topNStartItemsList) {
            int startItemId = startItem.getId();

            if (visitedGlobal.contains(startItemId)) {
                continue;
            }

            class Node {
                int itemId;
                int depth;
                double score;

                Node(int itemId, int depth, double score) {
                    this.itemId = itemId;
                    this.depth = depth;
                    this.score = score;
                }
            }

            PriorityQueue<Node> queue = new PriorityQueue<>((n1, n2) -> Double.compare(n2.score, n1.score));
            Set<Integer> visitedLocal = new HashSet<>();

            queue.offer(new Node(startItemId, 0, Double.MAX_VALUE));
            visitedLocal.add(startItemId);

            while (!queue.isEmpty() && finalRecommendations.size() < recommendSize) {
                Node current = queue.poll();
                int currentItemId = current.itemId;
                int currentDepth = current.depth;

                if (visitedGlobal.contains(currentItemId) && currentItemId != startItemId) {
                    continue;
                }

                if (currentDepth > 0 || !visitedGlobal.contains(currentItemId)) {
                    UserRecommendTVResultDTO recommendedItem = idToTv.get(currentItemId);
                    if (recommendedItem != null) {
                        finalRecommendations.add(recommendedItem);
                        visitedGlobal.add(currentItemId);
                        if (finalRecommendations.size() >= recommendSize) {
                            break;
                        }
                    }
                }

                if (currentDepth >= maxDepth) {
                    continue;
                }

                Map<Integer, Integer> neighbors = graph.getOrDefault(currentItemId, Collections.emptyMap());
                UserRecommendTVResultDTO currentItemData = idToTv.get(currentItemId);
                int currentItemKeywordsCount = (currentItemData != null && currentItemData.getKeywords() != null) ?
                        currentItemData.getKeywords().size() : 1;

                for (Map.Entry<Integer, Integer> neighborEntry : neighbors.entrySet()) {
                    int neighborItemId = neighborEntry.getKey();
                    int commonKeywordWeight = neighborEntry.getValue();

                    if (!visitedLocal.contains(neighborItemId) && !visitedGlobal.contains(neighborItemId)) {
                        UserRecommendTVResultDTO neighborItemData = idToTv.get(neighborItemId);
                        if (neighborItemData == null) continue;

                        double normalizedWeight = (double) commonKeywordWeight / currentItemKeywordsCount;
                        double neighborKeywordsRank = (neighborItemData.getKeywordsRanking() != null) ?
                                (double) neighborItemData.getKeywordsRanking() : 0.0;
                        double adjustedRankingFactor = Math.log(1 + neighborKeywordsRank);
                        double depthDecayFactor = 1.0 / (currentDepth + 1);

                        double calculatedScore = normalizedWeight * adjustedRankingFactor * depthDecayFactor;

                        queue.offer(new Node(neighborItemId, currentDepth + 1, calculatedScore));
                        visitedLocal.add(neighborItemId);
                    }
                }
            }

            finalRecommendations.sort(
                    Comparator.comparing(UserRecommendTVResultDTO::getVoteAverage, Comparator.nullsLast(Double::compareTo)).reversed()
                            .thenComparing(UserRecommendTVResultDTO::getKeywordsRanking, Comparator.nullsLast(Integer::compareTo)).reversed()
            );

            if (finalRecommendations.size() > recommendSize) {
                return finalRecommendations.subList(0, recommendSize);
            }
        }
        return finalRecommendations;
}
}
