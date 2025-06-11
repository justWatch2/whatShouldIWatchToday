package avengers.waffle.realMovieEntity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "movies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MoviesEntity {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "title", length = 255)
    private String title;

    @Column(name = "vote_average", precision = 5, scale = 3)
    private BigDecimal voteAverage;

    @Column(name = "vote_count")
    private Integer voteCount;

    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Column(name = "revenue")
    private Long revenue;

    @Column(name = "runtime")
    private Integer runtime;

    @Column(name = "adult")
    private Boolean adult;

    @Column(name = "backdrop_path", length = 255)
    private String backdropPath;

    @Column(name = "budget")
    private Long budget;

    @Column(name = "homepage", length = 255)
    private String homepage;

    @Column(name = "imdb_id", length = 50)
    private String imdbId;

    @Column(name = "original_language", length = 10)
    private String originalLanguage;

    @Column(name = "original_title", length = 255)
    private String originalTitle;

    @Column(name = "overview", columnDefinition = "TEXT")
    private String overview;

    @Column(name = "popularity", precision = 10, scale = 3)
    private BigDecimal popularity;

    @Column(name = "poster_path", length = 255)
    private String posterPath;

    @Column(name = "tagline", length = 255)
    private String tagline;

    @Column(name = "genres", length = 255)
    private String genres;

    @Column(name = "production_companies", length = 255)
    private String productionCompanies;

    @Column(name = "production_countries", length = 255)
    private String productionCountries;

    @Column(name = "spoken_languages", length = 255)
    private String spokenLanguages;

    @Column(name = "keywords", columnDefinition = "TEXT")
    private String keywords;
}