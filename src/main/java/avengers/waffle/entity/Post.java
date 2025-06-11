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
@Setter
@Table(name = "post")
@Builder
@AllArgsConstructor
public class Post extends BaseEntity implements Serializable { // 게시글
    @Id
    @Column(name = "no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long no;

    @Column(name="category", nullable = false, length = 10)
    private String category;

    @ManyToOne
    @JoinColumn(name = "member_id", referencedColumnName = "member_id", nullable = false) // 작성자 아이디
    Member member;

    @Column(name = "title", nullable = false, length = 50) // 제목
    private String title;

    @Column(name = "contents", nullable = false, columnDefinition = "TEXT") // 내용
    private String contents;

    @Column(name = "count", nullable = false, length = 5) // 조회수
    @ColumnDefault("0")
    private int count;

    @Column(name = "like_count", nullable = false, length = 5) // 좋아요
    @ColumnDefault("0")
    private int likeCount;

}
