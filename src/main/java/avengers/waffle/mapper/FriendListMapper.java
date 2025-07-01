package avengers.waffle.mapper;

import avengers.waffle.dto.userDTO.FriendDTO;
import io.lettuce.core.dynamic.annotation.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;


@Mapper
public interface FriendListMapper {
    List<FriendDTO> selectFriendsByMemberId(Map<String, Object> params);
    String selectFriendList(@Param("memberId") String memberId);
    int deleteFriend(Map<String, Object> params);
}
