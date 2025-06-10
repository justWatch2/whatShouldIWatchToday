package avengers.waffle.entity;

import jakarta.persistence.*;
import lombok.Builder;
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

    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private MovieMember memberId;

    @Builder(toBuilder = true)
    public MemberViewList(int no, Integer id, MovieMember memberId) {
        this.no = no;
        this.id = id;
        this.memberId = memberId;
    }
}
