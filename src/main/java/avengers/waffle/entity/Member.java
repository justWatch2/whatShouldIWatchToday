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

    @OneToOne
    @JoinColumn(name="icon_num" , referencedColumnName = "no")
    private Icon icon;

    @Type(JsonType.class)
    @Column(name = "friend_list" , columnDefinition = "JSON")
    private List<String> friendList;

    @Type(JsonType.class)
    @Column(name = "OTT_list" , columnDefinition = "JSON")
    private List<String> OTTList;

    public List<String> getRoleList(){           //role이 여러개 있을 경우 이런식으로 만들어 둔다. 하나만 있으면 안만들어도 된다.
        if (!this.roles.isEmpty()){
            return Arrays.asList(this.roles.split(","));
        }
        return new ArrayList<>();
    }

    public List<String> getFriendList() {
        if (this.friendList == null) {
            this.friendList = new ArrayList<>();
        }
        return this.friendList;
    }
}
