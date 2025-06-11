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

//    // Ratings는 관계를 몰라도 된다면 이대로 유지 (단방향)
//    // 양방향 관계를 원하면 아래처럼 추가 가능
//    @OneToOne(mappedBy = "ratings", fetch = FetchType.LAZY)
//    private MoviesPartitioned movie;

    @Builder
    public Ratings(String tconst, Float averageRating, Integer numVotes) {
        this.tconst = tconst;
        this.averageRating = averageRating;
        this.numVotes = numVotes;
    }
}
