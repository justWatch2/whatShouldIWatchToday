package avengers.waffle.repository;


import avengers.waffle.entity.MovieMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieMemberRepository extends JpaRepository<MovieMember, String> {
    MovieMember findByMemberId(String memberId);
}
