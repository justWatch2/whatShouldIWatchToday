package avengers.waffle.controller.movie;

import avengers.waffle.VO.movieDetail.DetailRequestVO;
import avengers.waffle.VO.movieDetail.PostResultVO;
import avengers.waffle.VO.movieDetail.VoteVO;
import avengers.waffle.service.IF.movieDetail.IF_DetailService;
import avengers.waffle.utils.GetMemberId;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class movieDetailController {

    private final IF_DetailService service;
    private final GetMemberId getMemberId;

    @GetMapping("api/non-member/vote/{imdb_id}")
    public ResponseEntity<VoteVO> getVote(
            @PathVariable String imdb_id) {
        System.out.println("votevotevotevotevotevotevotevotevotevotevotevotevotevotevotevotevotevotevotevotevotevotevotevotevotevotevotevotevotevotevotevote");
        try {
            VoteVO result = service.getVote(imdb_id);
            return ResponseEntity.ok(result);
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("api/detail/{category}/{id}")
    public ResponseEntity<Map<String,Boolean>> getDetail(
            HttpServletRequest request,
            @PathVariable String category,
            @PathVariable Integer id) {
        //System.out.println(memberId+category+id+"getLike");
        try {
            String memberId = getMemberId.getMemberId(request.getHeader("Authorization"));
            Map<String,Boolean> result = service.getDetail(memberId, category, id);
            return ResponseEntity.ok(result);
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("api/wish/{category}/{id}")
    public ResponseEntity<Void> delWish(
            HttpServletRequest request,
            @PathVariable String category,
            @PathVariable Integer id) {
//        System.out.println(memberId+category+id+"delView");
//        String authorizationHeader = request.getHeader("Authorization");
//        String memberId =  getMemberId.getMemberId(authorizationHeader);
        try{
            String memberId=getMemberId.getMemberId(request.getHeader("Authorization"));
            service.delWish(memberId, category, id);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }
    @DeleteMapping("api/view/{category}/{id}")
    public ResponseEntity<Void> delView(
            HttpServletRequest request,
            @PathVariable String category,
            @PathVariable Integer id) {
        try{
            String memberId=getMemberId.getMemberId(request.getHeader("Authorization"));
            service.delView(memberId, category, id);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }
    @PostMapping("api/wish")
    public ResponseEntity<Void> addWish(HttpServletRequest sevletRequest,@RequestBody DetailRequestVO request) {
//        System.out.println(memberId+category+id+"addWish");
        try{
            String memberId=getMemberId.getMemberId(sevletRequest.getHeader("Authorization"));
            service.addWish(memberId, request.getCategory(), request.getId());
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }
    @PostMapping("api/view")
    public ResponseEntity<Void> addView(HttpServletRequest sevletRequest,@RequestBody DetailRequestVO request) {
//        System.out.println(memberId+category+id+"addView");
        try{
            String memberId=getMemberId.getMemberId(sevletRequest.getHeader("Authorization"));
            service.addView(memberId, request.getCategory(), request.getId());
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/api/non-member/dependency/{title}/{korean_title}")
    public ResponseEntity<List<PostResultVO>> getWish(
            @PathVariable String title,
            @PathVariable String korean_title) {
        //System.out.println(memberId+category+id+"getLike");
        try {
            List<PostResultVO> result=service.PostLoad(title, korean_title);
            return ResponseEntity.ok(result);
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }
}
