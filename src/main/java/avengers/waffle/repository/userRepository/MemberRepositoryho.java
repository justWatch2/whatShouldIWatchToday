package avengers.waffle.repository.userRepository;

import avengers.waffle.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepositoryho extends JpaRepository<Member, Long> {

    Optional<Member> findByMemberId(String memberId);

}
