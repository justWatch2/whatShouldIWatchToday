package avengers.waffle.dto.responseDTO;

import avengers.waffle.dto.mybatis.MoviesDTO;
import avengers.waffle.dto.querydslDTO.Tv_ShowsDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ResponseDTO {
    private List<MoviesDTO> domesticMovies;
    private List<MoviesDTO> internationalMovies;
    private List<Tv_ShowsDTO> domesticTV;
    private List<Tv_ShowsDTO> internationalTV;

}
