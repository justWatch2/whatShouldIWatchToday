package avengers.waffle.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "movie_viewlist")
@Builder
@AllArgsConstructor
public class MovieViewList implements Serializable {
    @Id
    @Column(name = "no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer no;

    @ManyToOne
    @JoinColumn(name="movies_id", referencedColumnName = "id", nullable = false)
    private Movies movies;

    @ManyToOne
    @JoinColumn(name = "member_id", referencedColumnName = "member_id", nullable = false)
    private Member member;
}
