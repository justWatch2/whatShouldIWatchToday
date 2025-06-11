package avengers.waffle.repository;

import avengers.waffle.entity.MovieMember;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendsRepository extends JpaRepository<MovieMember,String> {
    @Query("SELECT m.friendList FROM MovieMember m WHERE m.memberId = :memberId")
    String getFriendsIdByMemberId(@Param("memberId") String memberId);
}
