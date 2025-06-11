package avengers.waffle.VO.security;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberVO {
    private String memberId;
    private String memberPw;

    @Builder
    public MemberVO(String memberId, String memberPw) {
        this.memberId = memberId;
        this.memberPw = memberPw;
    }
}
