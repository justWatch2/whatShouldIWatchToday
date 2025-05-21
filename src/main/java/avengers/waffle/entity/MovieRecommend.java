package avengers.waffle.entity;

import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Table(name ="movie_recommend" )
@NoArgsConstructor
public class MovieRecommend implements Serializable {
    @Id
    @Column(name = "recommend_keyword)")
    private Integer recommend_keyword;


    @Type(JsonType.class)
    @Column(name = "id_list", columnDefinition = "JSON")
    private List<String> id_list;

    @Column(name = "recommend_name",length = 20)
    private String recommend_name;

    @Builder(toBuilder = true)
    public MovieRecommend(Integer recommend_keyword, List<String> id_list, String recommend_name) {
        this.recommend_keyword = recommend_keyword;
        this.id_list = id_list;
        this.recommend_name = recommend_name;
    }
}