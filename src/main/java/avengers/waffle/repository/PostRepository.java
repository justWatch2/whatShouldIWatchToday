package avengers.waffle.repository;

import avengers.waffle.entity.Post;
import avengers.waffle.repository.mapping.PostMapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByCategoryOrderByIndateDesc(String category, Pageable pageable);
    int CountByCategory(String category);


}
