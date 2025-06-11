package avengers.waffle.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serializable;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Builder
@Table(name="movies")
@NoArgsConstructor
public class Movies implements Serializable {
    @EmbeddedId
    private MoviesPK moviesPK;

    @Column(name = "title", length = 255)
    private String title;

    @Column(name = "runtime")
    private Integer runtime;

    @Column(name = "backdrop_path", length = 255)
    private String backdropPath;

    @Column(name = "imdb_id", length = 50)
    private String imdbId;

    @Column(name = "original_language", length = 10)
    private String originalLanguage;

    @Column(name = "original_title", length = 255)
    private String originalTitle;

    @Column(name = "overview", columnDefinition = "TEXT")
    private String overview;

    @Column(name = "poster_path", length = 255)
    private String posterPath;

    @Column(name = "tagline", length = 255)
    private String tagline;

    @Column(name = "genres", length = 255)
    private String genres;

    @Column(name = "keywords", columnDefinition = "TEXT")
    private String keywords;

    @Column(name = "korean_title", length = 255)
    private String koreanTitle;
}