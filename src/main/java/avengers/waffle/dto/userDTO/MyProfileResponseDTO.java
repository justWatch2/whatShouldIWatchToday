package avengers.waffle.dto.userDTO;


import avengers.waffle.entity.Member;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MyProfileResponseDTO {
    private String memberId;
    private String memberName;
    private String img_url;

    // Member(Entity)를 MyProfileResponseDTO로 변환하는 똑똑한 변환기입니다.
    public static MyProfileResponseDTO fromEntity(Member member) {
        return MyProfileResponseDTO.builder()
                .memberId(member.getMemberId())
                .memberName(member.getMemberName())
                .img_url(member.getImgUrl())
                .build();
    }
}
