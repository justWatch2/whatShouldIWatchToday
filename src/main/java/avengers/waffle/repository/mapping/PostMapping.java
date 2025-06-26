package avengers.waffle.repository.mapping;

import avengers.waffle.utils.GetMemberId;

import java.time.LocalDateTime;


public interface PostMapping {
    long no = 0;
    String title = "";
    String memberId = "";
    LocalDateTime indate = null;
    int count = 0;


    long getNo();

    String getTitle(); 

    String getMemberId();

    LocalDateTime getIndate();

    int getCount();
}
