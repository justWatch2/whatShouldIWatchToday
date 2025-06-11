package avengers.waffle.repository;

import avengers.waffle.entity.PostAttach;
import avengers.waffle.repository.mapping.attachMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostAttachRepository extends JpaRepository<PostAttach, Long> {
    List<attachMapping> findAllByPost_no(long no);
}
