package avengers.waffle.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "member_viewlist")
public class MemberViewList implements Serializable {
    @Id
    @Column(name = "no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int no;

    //    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="id")
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)) // 외래키 제약 없음
//    private MoviesPartitioned movieId;
    @Column(name = "id") // 단순 ID 값
    private Integer movieId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MovieMember memberId;

    @Builder(toBuilder = true)
    public MemberViewList(int no, MovieMember memberId) {
        this.no = no;
        this.memberId = memberId;
    }
}
