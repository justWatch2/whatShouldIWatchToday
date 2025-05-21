package avengers.waffle.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Table(name="reply")
public class Reply implements Serializable {
    @Id
    @Column(name = "reply_num", length = 10)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int replyNum;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "no", referencedColumnName = "no", nullable = false)
    private Post post;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "member_id", referencedColumnName = "member_id", nullable = false)
    private MovieMember movieMember;

    @LastModifiedDate
    @Column(name = "indate")
    private LocalDateTime indate;

    @Column(name = "contents", nullable = false,length = 1000)
    private String contents;

    @Column(name = "like_count")
    @ColumnDefault("0")
    private int likeCount;


    @Builder(toBuilder = true)
    public Reply(Post post,MovieMember movieMember,String contents) {
        this.post = post;
        this.movieMember = movieMember;
        this.contents = contents;
    }
}
