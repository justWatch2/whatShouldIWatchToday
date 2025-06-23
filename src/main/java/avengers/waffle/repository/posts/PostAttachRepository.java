package avengers.waffle.repository.posts;

import avengers.waffle.entity.PostAttach;
import avengers.waffle.repository.mapping.attachMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostAttachRepository extends JpaRepository<PostAttach, Long> {
    List<attachMapping> findAllByPost_no(long no);

    void deleteByPost_no(long postNo);

    boolean existsByFileUrl(String existingFileUrl1);

    void deleteByFileUrl(String existFile);
}
