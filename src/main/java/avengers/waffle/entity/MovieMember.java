package avengers.waffle.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.Type;
import com.vladmihalcea.hibernate.type.json.JsonType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Entity
@NoArgsConstructor
@Getter
@Table(name = "movie_member")
public class MovieMember implements Serializable {
    @Id
    @Column(name = "member_id" , length = 20)
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="icon_num")
    private Icon iconNum;


    @Type(JsonType.class)
    @Column(name = "friend_list" , columnDefinition = "JSON")
    private List<String> friendList;

    @Type(JsonType.class)
    @Column(name = "OTT_list" , columnDefinition = "JSON")
    private List<String> OTTList;

    @Builder(toBuilder = true)
    public MovieMember(String memberId, String memberPw, String memberName, String provider, String providerId, String roles, Icon iconNum, List<String> friendList, List<String> OTTList) {
        this.memberId = memberId;
        this.memberPw = memberPw;
        this.memberName = memberName;
        this.provider = provider;
        this.providerId = providerId;
        this.roles = roles;
        this.iconNum = iconNum;
        this.friendList = friendList;
        this.OTTList = OTTList;
    }

    public List<String> getRoleList(){           //role이 여러개 있을 경우 이런식으로 만들어 둔다. 하나만 있으면 안만들어도 된다.
        if (!this.roles.isEmpty()){
            return Arrays.asList(this.roles.split(","));
        }
        return new ArrayList<>();
    }
}
