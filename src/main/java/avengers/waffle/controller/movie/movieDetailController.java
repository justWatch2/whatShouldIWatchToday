package avengers.waffle.controller.movie;

import avengers.waffle.VO.movieDetail.DetailRequestVO;
import avengers.waffle.VO.movieDetail.PostResultVO;
import avengers.waffle.service.IF.movieDetail.IF_DetailService;
import avengers.waffle.utils.GetMemberId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class movieDetailController {

    private final IF_DetailService service;
    private final GetMemberId getMemberId;

    @GetMapping("api/wish/{memberId}/{category}/{id}")
    public ResponseEntity<Boolean> getWish(
            @PathVariable String memberId,
            @PathVariable String category,
            @PathVariable Integer id) {
        //System.out.println(memberId+category+id+"getLike");

        return ResponseEntity.ok(service.getWish(memberId, category, id));
    }
    @GetMapping("api/view/{memberId}/{category}/{id}")
    public ResponseEntity<Boolean> getView(
            @PathVariable String memberId,
            @PathVariable String category,
            @PathVariable Integer id) {
//        System.out.println(memberId+category+id+"getWatch");

        return ResponseEntity.ok(service.getView(memberId, category, id));
    }
    @DeleteMapping("api/wish/{memberId}/{category}/{id}")
    public ResponseEntity<Void> delWish(
            @PathVariable String memberId,
//            HttpServletRequest request,
            @PathVariable String category,
            @PathVariable Integer id) {
//        System.out.println(memberId+category+id+"delView");
//        String authorizationHeader = request.getHeader("Authorization");
//        String memberId =  getMemberId.getMemberId(authorizationHeader);
        service.delWish(memberId, category, id);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("api/view/{memberId}/{category}/{id}")
    public ResponseEntity<Void> delView(
            @PathVariable String memberId,
            @PathVariable String category,
            @PathVariable Integer id) {
//        System.out.println(memberId+category+id+"delView");
        service.delView(memberId, category, id);
        return ResponseEntity.ok().build();
    }
    @PostMapping("api/wish")
    public ResponseEntity<Void> addWish(@RequestBody DetailRequestVO request) {
//        System.out.println(memberId+category+id+"addWish");
        service.addWish(request.getMemberId(), request.getCategory(), request.getId());
        return ResponseEntity.ok().build();
    }
    @PostMapping("api/view")
    public ResponseEntity<Void> addView(@RequestBody DetailRequestVO request) {
//        System.out.println(memberId+category+id+"addView");
        service.addView(request.getMemberId(), request.getCategory(), request.getId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/dependency/{title}/{korean_title}")
    public ResponseEntity<List<PostResultVO>> getWish(
            @PathVariable String title,
            @PathVariable String korean_title) {
        //System.out.println(memberId+category+id+"getLike");

        return ResponseEntity.ok(service.PostLoad(title,korean_title));
    }
}
