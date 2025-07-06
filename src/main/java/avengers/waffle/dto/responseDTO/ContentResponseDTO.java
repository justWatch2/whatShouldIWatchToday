package avengers.waffle.dto.responseDTO;


import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class ContentResponseDTO {

    private int no;
    private int id;
    private String title;
    private String posterPath;
    private Double rating;

}
