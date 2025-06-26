package avengers.waffle.mapper;

import avengers.waffle.VO.posts.Post4ListDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PostMapper {
    List<Post4ListDTO> top5Ranking(String category);
}
