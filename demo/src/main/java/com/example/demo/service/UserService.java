package com.example.demo.service;

import com.example.demo.entity.UserEntity;

import java.util.List;

public interface UserService {
    List<UserEntity> userList();                                // 사용자 리스트 조회

    UserEntity saveUser(UserEntity userEntity);                 // 사용자 등록

    UserEntity updateUser(UserEntity userEntity);               // 사용자 수정

    void deleteUserByUserSq(long userSq);                       // 사용자 삭제

}
