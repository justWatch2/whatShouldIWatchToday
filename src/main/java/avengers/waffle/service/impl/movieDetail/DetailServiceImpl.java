package avengers.waffle.service.impl.movieDetail;

import avengers.waffle.VO.movieDetail.PostResultVO;
import avengers.waffle.entity.*;
import avengers.waffle.mapper.DetailMapper;
import avengers.waffle.repository.MovieDetail.MovieViewListRepository;
import avengers.waffle.repository.MovieDetail.MovieWishListRepository;
import avengers.waffle.repository.MovieDetail.TvShowViewListRepository;
import avengers.waffle.repository.MovieDetail.TvShowWishListRepository;
import avengers.waffle.service.IF.movieDetail.IF_DetailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DetailServiceImpl implements IF_DetailService {
    private final MovieWishListRepository movieWishListRepository;
    private final MovieViewListRepository movieViewListRepository;
    private final TvShowViewListRepository tvShowViewListRepository;
    private final TvShowWishListRepository tvShowWishListRepository;

    private final DetailMapper detailMapper;

    @Override
    public boolean getWish(String memberId, String category, Integer id) {
        boolean result;
        if(category.equals("movie")){
            result=movieWishListRepository.existsByMovies_IdAndMember_MemberId(id,memberId);

        }else{
            result=tvShowWishListRepository.existsByTvshows_IdAndMember_MemberId(id,memberId);

        }
        return result;
    }

    @Override
    public void delWish(String memberId, String category, Integer id) {
        if(category.equals("movie")){
            movieWishListRepository.deleteAllByMovies_IdAndMember_MemberId(id,memberId);
            detailMapper.updateKcm(id,memberId);
            detailMapper.deleteKcm(id,memberId);
        }else{
            tvShowWishListRepository.deleteAllByTvshows_IdAndMember_MemberId(id,memberId);
            detailMapper.updateKct(id,memberId);
            detailMapper.deleteKct(id,memberId);
        }
    }

    @Override
    public void addWish(String memberId, String category, Integer id) {
        if(category.equals("movie")){
            movieWishListRepository.save(MovieWishList.builder()
                    .member(Member.builder().memberId(memberId).build())
                    .movies(Movies.builder().id(id).build())
                    .build());
            detailMapper.addKcm(id,memberId,1);
        }else{
            tvShowWishListRepository.save(TvShowWishList.builder()
                    .member(Member.builder().memberId(memberId).build())
                    .tvshows(TvShows.builder().id(id).build())
                    .build());
            detailMapper.addKct(id,memberId,1);
        }
    }


    @Override
    public boolean getView(String memberId, String category, Integer id) {
        boolean result;
        if(category.equals("movie")){
            result=movieViewListRepository.existsByMovies_IdAndMember_MemberId(id,memberId);
        }else{
            result=tvShowViewListRepository.existsByTvshows_IdAndMember_MemberId(id,memberId);
        }
        return result;
    }

    @Override
    public void delView(String memberId, String category, Integer id) {
        if(category.equals("movie")){
            movieViewListRepository.deleteAllByMovies_IdAndMember_MemberId(id,memberId);
            detailMapper.updateKcm(id,memberId);
            detailMapper.deleteKcm(id,memberId);
        }else{
            tvShowViewListRepository.deleteAllByTvshows_IdAndMember_MemberId(id,memberId);
            detailMapper.updateKct(id,memberId);
            detailMapper.deleteKct(id,memberId);
        }
    }

    @Override
    public void addView(String memberId, String category, Integer id) {
        if(category.equals("movie")){
            movieViewListRepository.save(MovieViewList.builder()
                    .member(Member.builder().memberId(memberId).build())
                    .movies(Movies.builder().id(id).build())
                    .build());
            detailMapper.addKcm(id,memberId,2);
        }else{
            tvShowViewListRepository.save(TvShowViewList.builder()
                    .member(Member.builder().memberId(memberId).build())
                    .tvshows(TvShows.builder().id(id).build())
                    .build());
            detailMapper.addKct(id,memberId,2);
        }
    }

    @Override
    public List<PostResultVO> PostLoad(String title, String korean_title) {

        String find=String.join("* ",title, korean_title+"*");
        return detailMapper.postLoad(find);
    }

}
