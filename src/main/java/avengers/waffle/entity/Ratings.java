package avengers.waffle.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ratings")
@Getter
@NoArgsConstructor
public class Ratings {

    @Id
    @Column(name = "tconst")
    private String tconst;

    private Float averageRating;
    private Integer numVotes;

    @Builder
    public Ratings(String tconst, Float averageRating, Integer numVotes) {
        this.tconst = tconst;
        this.averageRating = averageRating;
        this.numVotes = numVotes;
    }
}
