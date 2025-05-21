package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
public class MemberSearchList implements Serializable {
    @Id
    @Column(name="no")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String no;
    @ManyToOne
    @JoinColumn(name="member_id", referencedColumnName = "member_id", nullable = false)
    MovieMember movieMember;
    @Column(name="search_word", nullable = false, length =20)
    private String searchWord;
    @LastModifiedDate
    @Column(name="search_time", nullable = false)
    private LocalDateTime searchTime;
    @Builder(toBuilder = true)
    public MemberSearchList(MovieMember movieMember, String searchWord, LocalDateTime searchTime) {
        this.movieMember = movieMember;
        this.searchWord = searchWord;
        this.searchTime = searchTime;
    }
}
