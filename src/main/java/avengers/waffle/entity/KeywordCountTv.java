package avengers.waffle.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="keyword_count_tv")
public class KeywordCountTv {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name="keyword_count")
    private int keywordCount;
    @Column(name="wish_keyword_count")
    private int wishKeywordCount;
    @Column(name="view_keyword_count")
    private int viewKeywordCount;
    @Column(name="member_id")
    private String memberId;
    @Column(name="keywords_id")
    private int keywordsId;
}
