package avengers.waffle.repository.posts;

import avengers.waffle.entity.PostLikeList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostLikeListRepository extends JpaRepository<PostLikeList, Integer> {
    void deleteByMember_memberIdAndPost_no(String memberId, int postNo);

    boolean existsByMember_memberIdAndPost_no(String memberId, int postNo);
}
