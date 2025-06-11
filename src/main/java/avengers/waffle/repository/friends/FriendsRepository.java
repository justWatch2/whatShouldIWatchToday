package avengers.waffle.repository.friends;

import avengers.waffle.entity.Member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendsRepository extends JpaRepository<Member,String> {
    @Query("SELECT m.friendList FROM Member m WHERE m.memberId = :memberId")
    String getFriendsIdByMemberId(@Param("memberId") String memberId);
}
