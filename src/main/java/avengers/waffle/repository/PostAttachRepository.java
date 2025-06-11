package avengers.waffle.repository;

import avengers.waffle.entity.PostAttach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostAttachRepository extends JpaRepository<PostAttach, String> {

}
