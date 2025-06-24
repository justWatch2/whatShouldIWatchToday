package avengers.waffle.entity;

import jakarta.persistence.*;
import lombok.*;

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
    @Column(name = "icon_num", length = 10)
    private int iconNo;

    @Column(name = "img_url", length = 200)
    private String imgUrl;

    @OneToOne
    @JoinColumn(name="member_id", referencedColumnName = "member_id", nullable = false)
    private Member member;
}