package avengers.waffle.entity.tv;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "tv_genres_list")
@Data
public class TvGenresList {

    @Id
    @Column(name = "genres_id")
    private int genresId;

    @Column(name = "genres")
    private String genres;
}
