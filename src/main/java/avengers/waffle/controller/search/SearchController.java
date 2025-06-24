package avengers.waffle.controller.search;

import avengers.waffle.VO.search.MemberSearchListVO;
import avengers.waffle.VO.search.SearchVO;
import avengers.waffle.service.IF.search.IF_SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class SearchController {

    private final IF_SearchService searchService;

    @GetMapping("api/movie/search")
    public ResponseEntity<Map<String, Object>> viewlist(SearchVO movie) {
//        System.out.println(movie.getTitle());@ModelAttribute
//        System.out.println(movie.getGenres());
//        System.out.println(movie.getYears());
//        System.out.println(movie.isKorean());
//        System.out.println(movie.isAdult());

        return ResponseEntity.ok(searchService.searchMovie(movie));
    }
    @GetMapping("api/TV_shows/search")
    public ResponseEntity<Map<String, Object>> viewlisttv(SearchVO movie) {
//        System.out.println(movie.getTitle());
//        System.out.println(movie.getGenres());
//        System.out.println(movie.getYears());
//        System.out.println(movie.isKorean());
//        System.out.println(movie.isAdult());

        return ResponseEntity.ok(searchService.searchTV(movie));
    }

    @GetMapping("api/searchlist")
    public ResponseEntity<List<String>> getSearchList(String memberId) {
//        System.out.println(movie.getTitle());
//        System.out.println(movie.getGenres());
//        System.out.println(movie.getYears());
//        System.out.println(movie.isKorean());
//        System.out.println(movie.isAdult());

        return ResponseEntity.ok(searchService.getSearchList(memberId));
    }

    @PostMapping("api/searchlist")
    public ResponseEntity<List<String>> setSearchList(@RequestBody MemberSearchListVO search) {
        System.out.println("들어옴");
        return ResponseEntity.ok(searchService.setSerachList(search.getMemberId(), search.getTitle()));
    }

}
