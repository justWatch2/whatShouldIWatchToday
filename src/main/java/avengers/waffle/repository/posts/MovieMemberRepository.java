package avengers.waffle.repository.posts;


import avengers.waffle.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieMemberRepository extends JpaRepository<Member, String> {
    Member findByMemberId(String memberId);
    boolean existsByMemberId(String id);
}
