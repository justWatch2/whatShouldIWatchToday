package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="movie_genres")
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Getter
public class Genres {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int no;

    @Column(name="id")
    private int id;

    @Column(name="genre")
    private String genre;
}
