package avengers.waffle.entity.tv;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
@Table(name = "tv_genres")
public class TvGenres {

    @Id
    private int num;

    @Column(name = "genres_id")
    private int genresId;

    @Column(name = "tv_shows_id")
    private int tvShowsId;
}
