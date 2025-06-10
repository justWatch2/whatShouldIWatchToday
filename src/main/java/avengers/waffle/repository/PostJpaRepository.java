package avengers.waffle.repository;

import avengers.waffle.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostJpaRepository extends JpaRepository<Post, String> {
    int countByCategory(String category);
    Page<Post> findAllByCategoryOrderByIndateDesc(String category, Pageable pageable);
}
