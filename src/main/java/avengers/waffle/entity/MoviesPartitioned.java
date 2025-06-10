package avengers.waffle.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "movies_partitioned")
@Getter
@NoArgsConstructor
public class MoviesPartitioned {

    @Id
    private Integer id;

    private String title;
    private Float voteAverage;
    private Integer voteCount;
    private String status;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    private Long revenue;
    private Integer runtime;
    private Boolean adult;
    private String backdropPath;
    private Long budget;
    private String homepage;

    @Column(name = "imdb_id", unique = true)
    private String imdbId;

    private String originalLanguage;
    private String originalTitle;
    private String overview;
    private Float popularity;
    private String posterPath;
    private String tagline;
    private String genres;
    private String productionCompanies;
    private String productionCountries;
    private String spokenLanguages;
    private String keywords;


//    //1ㄷㅐ1이고 단방향일경우 이렇게 한쪽에서만 적어도 된다.
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "imdb_id", referencedColumnName = "tconst", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
//    private Ratings ratings;

//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "imdb_id", referencedColumnName = "tconst", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
//    private TitleAdult titleAdult;

    @Builder
    public MoviesPartitioned(Integer id, String title, Float voteAverage, Integer voteCount, String status, LocalDate releaseDate, Long revenue, Integer runtime, Boolean adult, String backdropPath, Long budget, String homepage, String imdbId, String originalLanguage, String originalTitle, String overview, Float popularity, String posterPath, String tagline, String genres, String productionCompanies, String productionCountries, String spokenLanguages, String keywords) {
        this.id = id;
        this.title = title;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
        this.status = status;
        this.releaseDate = releaseDate;
        this.revenue = revenue;
        this.runtime = runtime;
        this.adult = adult;
        this.backdropPath = backdropPath;
        this.budget = budget;
        this.homepage = homepage;
        this.imdbId = imdbId;
        this.originalLanguage = originalLanguage;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.popularity = popularity;
        this.posterPath = posterPath;
        this.tagline = tagline;
        this.genres = genres;
        this.productionCompanies = productionCompanies;
        this.productionCountries = productionCountries;
        this.spokenLanguages = spokenLanguages;
        this.keywords = keywords;
    }
}

