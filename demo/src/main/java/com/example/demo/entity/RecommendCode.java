package com.example.demo.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "recommend_code")
public class RecommendCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recommend_keyword" , length = 3)
    private int recommendKeyword;

    @Column(name = "recommend_name", length = 20)
    private String  recommendName;
}