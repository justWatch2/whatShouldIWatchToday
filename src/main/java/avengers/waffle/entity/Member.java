package avengers.waffle.entity;

import jakarta.persistence.*;
import lombok.*;

import org.hibernate.annotations.Type;
import com.vladmihalcea.hibernate.type.json.JsonType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Entity
@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
@Table(name = "member")
public class Member implements Serializable{
    @Id
    @Column(name = "member_id" , length = 100)
    private String memberId;

    @Column(name = "member_pw", length = 80)
    private String memberPw;

    @Column(name = "member_name", length = 20)
    private String memberName;

    @Column(name = "provider", length = 10)
    private String provider;

    @Column(name = "provider_id", length = 30)
    private String providerId;

    @Column(name = "roles", length = 15)
    private String roles;

    @Column(name="img_url" , length = 70)
    private String imgUrl;

    @Column(name = "friend_list" , columnDefinition = "Text")
    private String friendList;

    public List<String> getRoleList(){  //role이 여러개 있을 경우 이런식으로 만들어 둔다. 하나만 있으면 안만들어도 된다.
        if (!this.roles.isEmpty()){
            return Arrays.asList(this.roles.split(","));
        }
        return new ArrayList<>();
    }

    public void updateProfile(String newMemberName, String newPassword, String newImgUrl) {

        this.memberName = newMemberName;
        this.memberPw = newPassword;
        this.imgUrl = newImgUrl;
    }

}
