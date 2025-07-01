package avengers.waffle.dto.mybatis;


import lombok.Data;
import lombok.ToString;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
@ToString
public class UserRecommendTVResultDTO {
    private int id;
    private String name;
    private String overview;
    private String posterPath;
    private String backdropPath;
    private double voteAverage;
    private Integer keywordsRanking;
    private String keywordsRaw;
    private List<String> keywords;


    public void setKeywordsRaw(String keywordsRaw) {
        this.keywordsRaw = keywordsRaw;
        this.keywords = keywordsRaw == null || keywordsRaw.isEmpty()
                ? List.of()
                : Arrays.stream(keywordsRaw.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }
}