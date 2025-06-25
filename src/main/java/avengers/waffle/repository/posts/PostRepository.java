package avengers.waffle.repository.posts;

import avengers.waffle.VO.posts.Post4ListDTO;
import avengers.waffle.entity.Post;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    Page<Post> findAllByCategoryOrderByIndateDesc(String category, Pageable pageable);


    Optional<Post> findByNo(long no);
    @Query("SELECT new avengers.waffle.VO.posts.Post4ListDTO(p.no, p.title, p.member.memberId, p.indate, p.count) " +
            "FROM Post p WHERE p.category = :category ORDER BY p.count DESC LIMIT 5")
    List<Post4ListDTO> findTop5ByCategoryOrderByCountDesc(@Param("category") String category);
}
