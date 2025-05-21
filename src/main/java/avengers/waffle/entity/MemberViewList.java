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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private MovieMember id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MovieMember member_id;

    @Builder(toBuilder = true)
    public MemberViewList(int no, MovieMember id, MovieMember member_id) {
        this.no = no;
        this.id = id;
        this.member_id = member_id;
    }
}
