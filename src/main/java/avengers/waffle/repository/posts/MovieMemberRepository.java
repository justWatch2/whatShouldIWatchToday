package avengers.waffle.repository.posts;


import avengers.waffle.entity.Member;
import avengers.waffle.repository.mapping.MemberMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MovieMemberRepository extends JpaRepository<Member, String> {
    MemberMapping findMemberNameByMemberId(String memberId);
    Member findByMemberId(String memberId);
    boolean existsByMemberId(String id);
    boolean existsByMemberName(String name);
}
