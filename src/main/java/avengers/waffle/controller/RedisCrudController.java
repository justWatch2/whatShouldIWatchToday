package avengers.waffle.controller;

import avengers.waffle.service.RedisCrudService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/redis")
@RequiredArgsConstructor
public class RedisCrudController {

    private final RedisCrudService redisCrudService;

    @PostMapping("/save")
    public String save(@RequestParam String key, @RequestParam String value) {
        redisCrudService.save(key, value);
        return "저장 완료";
    }

    @GetMapping("/get")
    public String get(@RequestParam String key) {
        return redisCrudService.get(key);
    }

    @PostMapping("/update")
    public String update(@RequestParam String key, @RequestParam String value) {
        redisCrudService.update(key, value);
        return "업데이트 완료";
    }

    @DeleteMapping("/delete")
    public String delete(@RequestParam String key) {
        redisCrudService.delete(key);
        return "삭제 완료";
    }

    @GetMapping("/exists")
    public String exists(@RequestParam String key) {
        return redisCrudService.exists(key) ? "존재함" : "존재하지 않음";
    }
}
