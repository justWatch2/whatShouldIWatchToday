package avengers.waffle.entity;

import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Getter
@Table(name = "movie")
@NoArgsConstructor
public class Movie implements Serializable {
    @Id
    @Column(name = "id", length = 8)
    private String id;

    @Column(name = "poster_path", length = 100)
    private String posterPath;

    @Column(name = "title",  length = 1000)
    private String title;

    @Column(name = "adult")
    private Boolean adult;

    @Column(name = "vote_count")
    private Integer voteCount;


    @Column(name = "vote_average")
    private Float voteAverage;

    @Builder(toBuilder = true)
    public Movie(String id, String posterPath, String title, Boolean adult, Integer voteCount, Float voteAverage) {
        this.id = id;
        this.posterPath = posterPath;
        this.title = title;
        this.adult = adult;
        this.voteCount = voteCount;
        this.voteAverage = voteAverage;
    }
}