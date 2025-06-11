//package avengers.waffle.entity;
//
//import jakarta.persistence.*;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import org.springframework.data.annotation.LastModifiedDate;
//import org.hibernate.annotations.Type;
//import com.vladmihalcea.hibernate.type.json.JsonType;
//
//import java.io.Serializable;
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Entity
//@NoArgsConstructor
//@Getter
//@Table(name = "search_list")
//public class SearchList implements Serializable {
//    @EmbeddedId
//    private SearchListPK searchListPK;
//    @LastModifiedDate
//    @Column(name = "search_time")
//    private LocalDateTime searchTime;
//    @Type(JsonType.class)
//    @Column(name = "result_list", columnDefinition = "JSON")
//    private List<String> resultList;
//
//    @Builder(toBuilder = true)
//    public SearchList(SearchListPK searchListPK, LocalDateTime searchTime, List<String> resultList) {
//        this.searchListPK = searchListPK;
//        this.searchTime = searchTime;
//        this.resultList = resultList;
//    }
//}
