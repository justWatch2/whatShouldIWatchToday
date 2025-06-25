package avengers.waffle.repository.search;

import avengers.waffle.entity.MemberSearchList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberSearchListRepository extends JpaRepository<MemberSearchList, Long> {
    List<MemberSearchList> findByMember_MemberIdOrderByIndate(String memberId);
}
