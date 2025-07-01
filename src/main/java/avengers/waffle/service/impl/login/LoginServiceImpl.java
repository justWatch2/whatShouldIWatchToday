package avengers.waffle.service.impl.login;

import avengers.waffle.VO.login.MemberDTO;
import avengers.waffle.entity.Member;
import avengers.waffle.repository.posts.MovieMemberRepository;
import avengers.waffle.service.IF.login.IF_LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements IF_LoginService {
    private final MovieMemberRepository movieMemberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public void saveMember(MemberDTO memberDTO, String s) {
        movieMemberRepository.save(Member.builder()
                .memberId(memberDTO.getId())
                .roles("ROLE_USER")
                .memberPw(passwordEncoder.encode(memberDTO.getPass()))
                .memberName(memberDTO.getName())
                .imgUrl(s)
                .build());

    }

    @Override
    public String getImgUrl(String id) {
        String ImgUrl = movieMemberRepository.findByMemberId(id).getImgUrl();
        System.out.println("ImgUrl: " + ImgUrl);

        return ImgUrl;
    }
}
