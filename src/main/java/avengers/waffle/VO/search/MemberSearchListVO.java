package avengers.waffle.VO.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder(toBuilder = true)
public class MemberSearchListVO {
    private String title;
    private String memberId;
}
