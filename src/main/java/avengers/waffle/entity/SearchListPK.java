//package avengers.waffle.entity;
//
//import jakarta.persistence.Column;
//import jakarta.persistence.Embeddable;
//import jakarta.persistence.Table;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//import java.io.Serializable;
//
//@Embeddable
//@NoArgsConstructor
//@Getter
//public class SearchListPK implements Serializable {
//    @Column(name = "search_word", nullable = false, length = 20)
//    private String searchWord;
//    @Column(name = "page", nullable = false, length = 4)
//    private int page;
//
//    @Builder(toBuilder = true)
//    public SearchListPK(String searchWord, int page) {
//        this.searchWord = searchWord;
//        this.page = page;
//    }
//}