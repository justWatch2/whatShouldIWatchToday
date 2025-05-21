package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
public class Post implements Serializable { // 게시글
    @Id
    @Column(name = "no")
    @GeneratedValue(strategy = GenerationType.AUTO) // 글 번호
    private long no;
    @Column(name="category", nullable = false, length = 10)
    private String category;
    @ManyToOne
    @JoinColumn(name = "member_id", referencedColumnName = "member_id", nullable = false) // 작성자 아이디
    MovieMember movieMember;
    @Column(name = "title", nullable = false, length = 50) // 제목
    private String title;
    @Column(name = "indate", nullable = false) // 날짜
    @LastModifiedDate
    private LocalDateTime indate;
    @Lob
    @Column(name = "contents", nullable = false, length = 256) // 내용
    private String contents;
    @Column(name = "count", nullable = false, length = 5) // 조회수
    private int count;
    @Column(name = "like", nullable = false, length = 5) // 좋아요
    private int like;

    @Builder(toBuilder = true)
    public Post(String category, MovieMember movieMember, String title, LocalDateTime indate, String contents, int count, int like) {
        this.category = category;
        this.movieMember = movieMember;
        this.title = title;
        this.indate = indate;
        this.contents = contents;
        this.count = count;
        this.like = like;
    }
}
