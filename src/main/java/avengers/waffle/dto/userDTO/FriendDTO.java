package avengers.waffle.dto.userDTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FriendDTO {
    private String memberId;
    private String memberName;
    private String imgUrl;
}
