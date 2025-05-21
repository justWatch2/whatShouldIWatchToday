package avengers.waffle.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "icon")
public class Icon implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "icon_num", length = 2)
    private int iconNum;

    @Column(name = "img_url", length = 70)
    private String imgUrl;

    @Builder(toBuilder = true)
    public Icon(String imgUrl) {
        this.imgUrl = imgUrl;
    }

}