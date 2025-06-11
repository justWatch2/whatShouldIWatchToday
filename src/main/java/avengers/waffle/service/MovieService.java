package avengers.waffle.service;


import avengers.waffle.VO.MovieSearch;
import avengers.waffle.VO.PostVO;
import avengers.waffle.entity.Movies;
import avengers.waffle.entity.Post;
import avengers.waffle.mapper.MovieMapper;
import avengers.waffle.repository.PostJpaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final PostJpaRepository postJpaRepository;
    private final MovieMapper movieMapper;

    @Transactional
    public List<PostVO> viewlist() {
        List<Post> post = postJpaRepository.findAll();
        List<PostVO> postVOList = new ArrayList<>();
        for (Post p : post) {
            postVOList.add(PostVO.builder()
                .no(p.getNo())
                    .title(p.getTitle())
                    .name(p.getMovieMember().getMemberName())
                    .indate(p.getIndate())
                    .count(p.getCount())
                    .build());

        }
        return postVOList;
    }

    @Transactional
    public int getCount(String category) {
        return postJpaRepository.countByCategory(category);
    }

    @Transactional
    public List<Movies> searchMovie(MovieSearch movieSearch) {
        return movieMapper.search(movieSearch);
    }

}
