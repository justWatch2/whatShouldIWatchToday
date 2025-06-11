package avengers.waffle.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "titles_adult")
@Getter
@NoArgsConstructor
public class TitleAdult {

    @Id
    @Column(name = "tconst")
    private String tconst;

    @Column(name = "is_adult")
    private Boolean isAdult;



    @Builder
    public TitleAdult(String tconst, Boolean isAdult) {
        this.tconst = tconst;
        this.isAdult = isAdult;
    }
}
