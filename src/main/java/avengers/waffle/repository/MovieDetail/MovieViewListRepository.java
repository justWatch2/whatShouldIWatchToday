package avengers.waffle.repository.MovieDetail;

import avengers.waffle.entity.MovieViewList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieViewListRepository extends JpaRepository<MovieViewList, String> {
    boolean existsByMovies_IdAndMember_MemberId(Integer MovieId, String MemberId);
    void deleteAllByMovies_IdAndMember_MemberId(Integer MovieId, String MemberId);
}
