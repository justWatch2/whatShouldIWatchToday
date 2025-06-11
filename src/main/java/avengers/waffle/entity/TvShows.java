package avengers.waffle.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "tvshows")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TvShows {
    @Id
    @Column(name = "id") // INT(11) NOT NULL
    private Integer id;

    @Column(name = "name") // VARCHAR(255)
    private String name;

    @Column(name = "number_of_seasons") // INT(11)
    private Integer numberOfSeasons;

    @Column(name = "number_of_episodes") // INT(11)
    private Integer numberOfEpisodes;

    @Column(name = "original_language") // VARCHAR(10)
    private String originalLanguage;

    @Column(name = "vote_count") // INT(11)
    private Integer voteCount;

    @Column(name = "vote_average") // FLOAT
    private Float voteAverage;

    @Column(name = "overview", columnDefinition = "TEXT") // TEXT
    private String overview;

    @Column(name = "adult") // TINYINT(1)
    private Boolean adult;

    @Column(name = "backdrop_path") // VARCHAR(255)
    private String backdropPath;

    @Column(name = "first_air_date") // DATE
    @Temporal(TemporalType.DATE)
    private LocalDate firstAirDate;

    @Column(name = "last_air_date") // DATE
    @Temporal(TemporalType.DATE)
    private LocalDate lastAirDate;

    @Column(name = "original_name") // VARCHAR(255)
    private String originalName;

    @Column(name = "popularity") // FLOAT
    private Float popularity;

    @Column(name = "poster_path") // VARCHAR(255)
    private String posterPath;

    @Column(name = "type") // VARCHAR(50)
    private String type;

    @Column(name = "status") // VARCHAR(50)
    private String status;

    @Column(name = "tagline") // VARCHAR(255)
    private String tagline;

    @Column(name = "genres", columnDefinition = "TEXT") // TEXT
    private String genres;

    @Column(name = "created_by", columnDefinition = "TEXT") // TEXT
    private String createdBy;

    @Column(name = "languages") // VARCHAR(100)
    private String languages;

    @Column(name = "origin_country") // VARCHAR(10)
    private String originCountry;

    @Column(name = "episode_run_time") // INT(11)
    private String episodeRunTime;

    @Column(name = "korean_name") // VARCHAR(100)
    private String koreanName;

    @Column(name = "keywords", columnDefinition = "TEXT")
    private String keywords;
}
