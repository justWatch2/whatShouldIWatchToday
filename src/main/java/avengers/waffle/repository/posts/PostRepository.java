package avengers.waffle.repository.posts;

import avengers.waffle.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByCategoryOrderByIndateDesc(String category, Pageable pageable);
    int countByCategory(String category);
    Optional<Post> findByNo(long no);
}
