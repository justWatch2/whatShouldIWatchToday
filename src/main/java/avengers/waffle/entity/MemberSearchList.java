package avengers.waffle.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "member_search_list")
@Builder
@AllArgsConstructor
public class MemberSearchList extends BaseEntity implements Serializable {
    @Id
    @Column(name="no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer no;
    @ManyToOne
    @JoinColumn(name="member_id", referencedColumnName = "member_id", nullable = false)
    Member member;
    @Column(name="search_word", nullable = false, length =255)
    private String searchWord;
}

