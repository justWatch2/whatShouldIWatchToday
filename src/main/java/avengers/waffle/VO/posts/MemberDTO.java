package avengers.waffle.VO.posts;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
public class MemberDTO {
    private String id;
    private String pw;


    public MemberDTO(String id, String pw) {
        this.id = id;
        this.pw = pw;
    }
}
