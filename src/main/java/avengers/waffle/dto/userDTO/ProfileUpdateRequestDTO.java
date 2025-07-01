package avengers.waffle.dto.userDTO;

import lombok.Data;

@Data
public class ProfileUpdateRequestDTO {

    private String currentPassword;
    private String memberName;
    private String password;

    // 이 DTO는 프론트엔드에서 오는 데이터를 받는 용도이므로,
    // 특별한 변환 메소드는 필요 없습니다.
}