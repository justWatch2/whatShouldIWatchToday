package avengers.waffle.repository.posts;

import avengers.waffle.entity.ReplyLikeList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReplyLikeListRepository extends JpaRepository<ReplyLikeList, Integer> {
    void deleteByReply_replyNumAndMember_memberId(int replyNo, String memberId);
    boolean existsByReply_replyNumAndMember_memberId(int no, String id);
}
