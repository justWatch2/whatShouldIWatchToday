package avengers.waffle.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "reply_like")
@Builder
@DynamicUpdate
@AllArgsConstructor
public class ReplyLikeList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int no;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id", referencedColumnName = "member_id")
    private Member member;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="reply_no", referencedColumnName = "no")
    private Reply reply;
}
