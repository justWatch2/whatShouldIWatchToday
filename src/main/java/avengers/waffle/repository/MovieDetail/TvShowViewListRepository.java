package avengers.waffle.repository.MovieDetail;

import avengers.waffle.entity.TvShowViewList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TvShowViewListRepository extends JpaRepository<TvShowViewList, String> {
    boolean existsByTvshows_IdAndMember_MemberId(Integer tvShowId, String memberId);
    void deleteAllByTvshows_IdAndMember_MemberId(Integer tvShowId, String memberId);
}
