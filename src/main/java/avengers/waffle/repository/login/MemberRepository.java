package avengers.waffle.repository.login;

import avengers.waffle.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {

    boolean existsByMemberId(String id);
}
