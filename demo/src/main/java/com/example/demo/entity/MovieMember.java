package com.example.demo.entity;

import com.example.demo.entity.Icon;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.Type;
import com.vladmihalcea.hibernate.type.json.JsonType;

import java.util.List;


@Entity
@NoArgsConstructor
@Getter
@Table(name = "movie_member")
public class MovieMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id" , length = 20)
    private String memberId;

    @Column(name = "member_pw", length = 80)
    private String memberPw;

    @Column(name = "member_name", length = 20)
    private String memberName;

    @Column(name = "provider", length = 10)
    private String provider;

    @Column(name = "provider_id", length = 30)
    private String providerId;

    @Column(name = "roles", length = 15)
    private String roles;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="icon_num")
    private Icon iconNum;


    @Type(JsonType.class)
    @Column(name = "friend_list" , columnDefinition = "JSON")
    private List<String> friendList;

    @Type(JsonType.class)
    @Column(name = "OTT_list" , columnDefinition = "JSON")
    private List<String> OTTList;



}