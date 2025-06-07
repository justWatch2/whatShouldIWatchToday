package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="movie_keywords")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Keywords {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long no;

    @Column(name="id")
    private int id;

    @Column(name="keyword")
    private String keyword;
}
