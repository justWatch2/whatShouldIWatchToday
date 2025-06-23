package avengers.waffle.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "post_like")
@Builder
@DynamicUpdate
@AllArgsConstructor
public class PostLikeList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int no;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id", referencedColumnName = "member_id")
    private Member member;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="post_no", referencedColumnName = "no")
    private Post post;
}
