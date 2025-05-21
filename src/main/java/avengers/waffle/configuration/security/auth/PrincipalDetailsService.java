package avengers.waffle.configuration.security.auth;



import avengers.waffle.entity.MovieMember;
import avengers.waffle.repository.MovieMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final MovieMemberRepository movieMemberRepository;

    @Override
    public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {
        MovieMember userEntity = movieMemberRepository.findByMemberId(memberId);

        if (userEntity == null) {
            throw new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다: " + memberId);
        }
        //db에서 찾은 userEntity객체를 PrincipalDetails로 감싸서 반환한다.
        // 이렇게 감싸야 Spring Security 가 내부에서 사용자 권한, 비밀번호 등을 처리할 수 있다.
        return new PrincipalDetails(userEntity);
    }
}
