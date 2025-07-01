package avengers.waffle.controller.search;

import avengers.waffle.VO.search.MemberSearchListVO;
import avengers.waffle.VO.search.SearchVO;
import avengers.waffle.service.IF.search.IF_SearchService;
import avengers.waffle.utils.GetMemberId;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class SearchController {

    private final IF_SearchService searchService;
    private final GetMemberId getMemberId;

    @GetMapping("api/non-member/movie/search")
    public ResponseEntity<Map<String, Object>> viewlist(SearchVO movie) {
//        System.out.println(movie.getTitle());@ModelAttribute
//        System.out.println(movie.getGenres());
//        System.out.println(movie.getYears());
//        System.out.println(movie.isKorean());
//        System.out.println(movie.isAdult());

        return ResponseEntity.ok(searchService.searchMovie(movie));
    }
    @GetMapping("api/non-member/TV_shows/search")
    public ResponseEntity<Map<String, Object>> viewlisttv(SearchVO movie) {
//        System.out.println(movie.getTitle());
//        System.out.println(movie.getGenres());
//        System.out.println(movie.getYears());
//        System.out.println(movie.isKorean());
//        System.out.println(movie.isAdult());

        return ResponseEntity.ok(searchService.searchTV(movie));
    }

    @GetMapping("api/searchlist")
    public ResponseEntity<List<String>> getSearchList(HttpServletRequest request) {
//        System.out.println(movie.getTitle());
//        System.out.println(movie.getGenres());
//        System.out.println(movie.getYears());
//        System.out.println(movie.isKorean());
//        System.out.println(movie.isAdult());
        String memberId=getMemberId.getMemberId(request.getHeader("Authorization"));
        return ResponseEntity.ok(searchService.getSearchList(memberId));
    }

    @PostMapping("api/searchlist")
    public ResponseEntity<List<String>> setSearchList(@RequestBody MemberSearchListVO vo, HttpServletRequest request) {
        String memberId=getMemberId.getMemberId(request.getHeader("Authorization"));
        System.out.println("---------------------"+vo.getTitle()+"---------------------"+memberId);

        return ResponseEntity.ok(searchService.setSerachList(memberId, vo.getTitle()));
    }

}
