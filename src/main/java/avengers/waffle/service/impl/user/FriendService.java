package avengers.waffle.service.impl.user;

import avengers.waffle.dto.userDTO.FriendDTO;
import avengers.waffle.mapper.FriendListMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendService {
    private final FriendListMapper friendListMapper;

    public List<FriendDTO> getFriends(String memberId) {
        String friendList = friendListMapper.selectFriendList(memberId);
        System.out.println(friendList);
        List<String> friendIds = (friendList != null) ? Arrays.asList(friendList.split(",")) : Collections.emptyList();
        if (friendIds.isEmpty()) {
            return Collections.emptyList(); // DB 조회 없이 즉시 종료
        }
        System.out.println(friendIds.size());
        Map<String, Object> params = new HashMap<>();
        params.put("memberId", memberId);
        params.put("friendIds", friendIds);


        return friendListMapper.selectFriendsByMemberId(params);
    }

    @Transactional
    public boolean deleteFriend(String memberId, String friendId) {
        try {
            // 현재 친구 목록 조회
            List<FriendDTO> friends = getFriends(memberId);
            List<String> currentFriendIds = friends.stream()
                    .map(FriendDTO::getMemberId)
                    .toList();

            // friendId를 제외한 새로운 리스트 생성
            List<String> updatedFriendList = currentFriendIds.stream()
                    .filter(id -> !id.equals(friendId))
                    .collect(Collectors.toList());

            // DB 업데이트
            Map<String, Object> params = new HashMap<>();
            params.put("memberId", memberId);
            params.put("updatedFriendList", updatedFriendList);

            int rowsAffected = friendListMapper.deleteFriend(params);
            return rowsAffected > 0;
        } catch (Exception e) {
            System.out.println("Delete Friend Error: " + e.getMessage());
            return false;
        }
    }
}