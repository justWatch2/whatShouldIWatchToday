package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name="movies_copy")
public class BeforeEntity {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "title", length = 255)
    private String title;

    @Column(name = "release_date")
    private LocalDate releaseDate;

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

