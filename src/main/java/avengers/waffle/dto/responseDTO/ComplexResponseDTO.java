package avengers.waffle.dto.responseDTO;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ComplexResponseDTO {
    private List<?> selectedList;
    private String mediatype;
}
