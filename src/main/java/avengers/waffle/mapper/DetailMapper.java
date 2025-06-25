package avengers.waffle.mapper;

import avengers.waffle.VO.movieDetail.PostResultVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DetailMapper {
    List<PostResultVO> postLoad(String title);
    void addKcm(Integer id,String memberId,Integer recommendType);
    void addKct(Integer id,String memberId,Integer recommendType);
    void updateKcm(Integer id,String memberId);
    void updateKct(Integer id,String memberId);
    void deleteKcm(Integer id,String memberId);
    void deleteKct(Integer id,String memberId);
}
