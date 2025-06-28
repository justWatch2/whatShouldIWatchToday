package avengers.waffle.repository.recommendationTVRepository;

import avengers.waffle.dto.querydslDTO.QTv_ShowsDTO;
import avengers.waffle.dto.querydslDTO.Tv_ShowsDTO;
import avengers.waffle.dto.searchDTO.ComplexSearchDTO;
import avengers.waffle.entity.QTvShows;
import avengers.waffle.entity.tv.QTvGenres;
import avengers.waffle.entity.tv.QTvGenresList;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@RequiredArgsConstructor
@Slf4j
public class RecommendationTvRepositoryImpl implements RecommendationTvRepository {
    private final JPAQueryFactory jpaQueryFactory;

    private final QTvShows tv = QTvShows.tvShows;
    private final QTvGenres tvGenres = QTvGenres.tvGenres;
    private final QTvGenresList tvGenresList = QTvGenresList.tvGenresList;

    @Override
    public List<Tv_ShowsDTO> findTVshowsByGenres(List<String> genres, boolean isDomestic) {
        BooleanBuilder builder = new BooleanBuilder();
        if(genres != null && !genres.isEmpty()) {
            // 서브쿼리 1: 입력된 장르 이름으로 genresId 조회
            JPQLQuery<Integer> genresIdSubQuery = JPAExpressions.select(tvGenresList.genresId)
                    .from(tvGenresList)
                    .where(tvGenresList.genres.in(genres));
                //여기까지가 장르id찾기
            JPQLQuery<Integer> tvShowIdSubQuery = JPAExpressions.select(tvGenres.tvShowsId)
                    .distinct()
                    .from(tvGenres)
                    .where(tvGenres.genresId.in(genresIdSubQuery));
            //여기까지가 장르id기반으로 tvid찾기
            builder.and(tv.id.in(tvShowIdSubQuery));
            //그리고 조건에 추가하기
        }
        if (isDomestic) {
            builder.and(tv.originalLanguage.eq("ko"));
        } else {
            builder.and(tv.originalLanguage.ne("ko"));
        }
        builder.and(tv.adult.eq(0));
        builder.and(tv.voteAverage.gt(5.0));
        builder.and(tv.voteCount.gt(10));
        return jpaQueryFactory
                .select(new QTv_ShowsDTO(
                        tv.id,
                        tv.koreanName,
                        tv.posterPath,
                        tv.backdropPath,
                        tv.overview,
                        tv.voteAverage
                        ))
                .from(tv)
                .where(builder)
                .orderBy(tv.voteCount.desc(), tv.voteAverage.desc())
                .limit(20)
                .fetch();
    }

    @Override
    public List<Tv_ShowsDTO> searchComplexTv(ComplexSearchDTO complexSearchDTO) {
        BooleanBuilder builder = new BooleanBuilder();


        //해외/국내여부
        if(complexSearchDTO.isDomestic()){
            builder.and(tv.originalLanguage.eq("ko"));
        }else {
            builder.and(tv.originalLanguage.ne("ko"));
        }
        //성인여부
        if(complexSearchDTO.isAdult()){
            builder.and(tv.adult.eq(1));
        }else {
            builder.and(tv.adult.eq(0));
        }
        builder.and(releaseDateCheck(complexSearchDTO.getReleaseYear()));
        builder.and(ratingsCheck(complexSearchDTO.getRating()));


        return jpaQueryFactory
                .select(new QTv_ShowsDTO(
                tv.id,
                tv.koreanName,
                tv.posterPath,
                tv.backdropPath,
                tv.overview,
                tv.voteAverage))
                .distinct()
                .from(tvGenresList)
                .join(tvGenres).on(tvGenres.genresId.eq(tvGenresList.genresId)
                        .and(tvGenresList.genres.in(complexSearchDTO.getSelectedGenres())))
                .join(tv).on(tv.id.eq(tvGenres.tvShowsId))
                .where(builder)
                .orderBy(tv.voteCount.desc(), tv.voteAverage.desc())
                .limit(20)
                .fetch();
    }



    private BooleanExpression releaseDateCheck(List<Integer> releaseDates) {
        NumberTemplate<Integer> releaseYear = Expressions.numberTemplate(
                Integer.class,
                "function('YEAR', {0})",
                tv.firstAirDate
        );
        return releaseYear.goe(releaseDates.get(0))
                .and(releaseYear.loe(releaseDates.get(1)));
    }

    private BooleanExpression ratingsCheck(List<Integer> ratings) {
        return tv.voteAverage.goe(ratings.get(0))
                .and(tv.voteCount.gt(ratings.get(1)));
    }


}
