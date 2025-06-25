package avengers.waffle.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name="movies")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Movies {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "imdb_id", length = 20)
    private String imdbId;

    @Column(name = "title", length = 255)
    private String title;

    @Column(name = "runtime")
    private Integer runtime;

    @Column(name = "backdrop_path", columnDefinition = "TEXT")
    private String backdropPath;

    @Column(name = "original_language", length = 10)
    private String originalLanguage;

    @Column(name = "original_title", length = 255)
    private String originalTitle;

    @Column(name = "overview", columnDefinition = "TEXT")
    private String overview;

    @Column(name = "poster_path", columnDefinition = "TEXT")
    private String posterPath;

    @Column(name = "tagline", columnDefinition = "TEXT")
    private String tagline;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Column(name = "korean_title", length = 255)
    private String koreanTitle;

    @Column(name = "genres", columnDefinition = "TEXT")
    private String genres;

    @Column(name = "keywords", columnDefinition = "TEXT")
    private String keywords;

}
