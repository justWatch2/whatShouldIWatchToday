package com.example.demo.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor
@Table(name = "movie_wish")
public class MovieWish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String no;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id")
    private Movie movieId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private MovieMember memberId;
}