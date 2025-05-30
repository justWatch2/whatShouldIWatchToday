package avengers.waffle.entity;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "movie_wish")
public class MovieWish implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int no;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id")
    private Movie movieId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="")
    private MovieMember memberId;

    @Builder(toBuilder = true)
    public MovieWish(int no, Movie movieId, MovieMember memberId) {
        this.no = no;
        this.movieId = movieId;
        this.memberId = memberId;
    }
}
