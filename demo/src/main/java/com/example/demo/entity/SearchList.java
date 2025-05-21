package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;
import org.hibernate.annotations.Type;
import com.vladmihalcea.hibernate.type.json.JsonType;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class SearchList implements Serializable {
    @EmbeddedId
    private SearchListPK searchListPK;
    @LastModifiedDate
    @Column(name = "search_time")
    private LocalDateTime searchTime;
    @Type(JsonType.class)
    @Column(name = "result_list", columnDefinition = "JSON")
    private List<String> resultList;
}
