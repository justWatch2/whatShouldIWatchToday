package avengers.waffle.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Table(name="reply")
@Builder
@AllArgsConstructor
public class Reply extends BaseEntity implements Serializable {
    @Id
    @Column(name = "reply_num", length = 10)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long replyNum;

    @ManyToOne
    @JoinColumn(name = "post_no", referencedColumnName = "no", nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "member_id", referencedColumnName = "member_id", nullable = false)
    private Member member;

    @Column(name = "contents", nullable = false, columnDefinition = "TEXT")
    private String contents;

    @Column(name = "like_count")
    @ColumnDefault("0")
    private int likeCount;

}
