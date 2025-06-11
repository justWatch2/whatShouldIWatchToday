package avengers.waffle.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
<<<<<<< HEAD
=======
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
>>>>>>> 717ac2530a1f92c433767ce7361f6046b03b8ead

import java.io.Serializable;

@Entity
@NoArgsConstructor
@Getter
@Table(name="reply")
public class Reply extends BaseEntity implements Serializable {
    @Id
    @Column(name = "reply_num", length = 10)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long replyNum;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "no", referencedColumnName = "no", nullable = false)
    private Post post;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "member_id", referencedColumnName = "member_id", nullable = false)
    private MovieMember movieMember;

<<<<<<< HEAD
//    @LastModifiedDate
//    @Column(name = "indate")
//    private LocalDateTime indate;
=======
    @LastModifiedDate
    @CreatedDate
    @Column(name = "indate")
    private LocalDateTime indate;
>>>>>>> 717ac2530a1f92c433767ce7361f6046b03b8ead

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
