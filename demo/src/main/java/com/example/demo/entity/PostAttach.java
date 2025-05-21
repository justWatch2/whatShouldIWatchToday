package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@NoArgsConstructor
@Getter
public class PostAttach implements Serializable {
    @Id
    @Column(name = "attach_no")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long attachNo;
    @ManyToOne
    @JoinColumn(name = "no", referencedColumnName = "no", nullable = false)
    Post post;
    @Column(name = "file_url", nullable = false, length = 70)
    private String fileUrl;

    @Builder(toBuilder = true)
    public PostAttach(Post post, String fileUrl) {
        this.post = post;
        this.fileUrl = fileUrl;
    }
}
