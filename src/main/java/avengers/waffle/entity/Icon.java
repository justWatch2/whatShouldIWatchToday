package avengers.waffle.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "icon")
@Builder
@AllArgsConstructor
public class Icon implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "no", length = 10)
    private int iconNo;

    @Column(name = "img_url", length = 70)
    private String imgUrl;

    @OneToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name="member_id", referencedColumnName = "member_id", nullable = false)
    private Member member;
}