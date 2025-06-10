package avengers.waffle.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Embeddable
@NoArgsConstructor
@Getter
public class MoviesPK implements Serializable {
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Builder(toBuilder = true)
    public MoviesPK(Integer id,LocalDate releaseDate) {
        this.id = id;
        this.releaseDate = releaseDate;
    }
}
