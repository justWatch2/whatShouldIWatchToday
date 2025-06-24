package avengers.waffle.repository.MovieDetail;

import avengers.waffle.entity.MovieWishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieWishListRepository extends JpaRepository<MovieWishList, String> {
    boolean existsByMovies_IdAndMember_MemberId(Integer MovieId, String MemberId);
    void deleteAllByMovies_IdAndMember_MemberId(Integer MovieId, String MemberId);
}
