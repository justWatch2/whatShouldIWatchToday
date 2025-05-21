package avengers.waffle.configuration.security.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.security.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String password;
    private String email;
    private String roles;

    private String provider;
    private String providerId;
    @CreationTimestamp
    private LocalDateTime createDate;

    @Builder
    public User(int id, String username, String password, String email, String roles, String provider, String providerId, LocalDateTime createDate) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.roles = roles;
        this.provider = provider;
        this.providerId = providerId;
        this.createDate = createDate;
    }


    public List<String> getRoleList(){           //role이 여러개 있을 경우 이런식으로 만들어 둔다. 하나만 있으면 안만들어도 된다.
        if (!this.roles.isEmpty()){
            return Arrays.asList(this.roles.split(","));
        }
        return new ArrayList<>();
    }
}
