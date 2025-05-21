package com.example.demo.service.impl;

import com.example.demo.entity.UserEntity;
import com.example.demo.repository.UserJpaRepository;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserJpaRepository userJpaRepository;


    @Override
    @Transactional(readOnly = true)
    public List<UserEntity> userList() {
        return userJpaRepository.findAll();
    }

    @Override
    @Transactional
    public UserEntity saveUser(UserEntity userEntity) {
        return userJpaRepository.save(userEntity);
    }

    @Override
    @Transactional
    public UserEntity updateUser(UserEntity userEntity) {
        UserEntity user = userJpaRepository.findById(userEntity.getUserSq()).get();
        UserEntity resultEntity = UserEntity.builder().build();
        if (Objects.nonNull(user.getUserNm()) && !"".equals(user.getUserNm())) {
            resultEntity = UserEntity.builder().userNm(user.getUserNm()).build();
        }
        if (Objects.nonNull(user.getUserEmail()) && !"".equals(user.getUserEmail())) {
            resultEntity = UserEntity.builder().userEmail(user.getUserEmail()).build();
        }
        if (Objects.nonNull(user.getUserSt()) && !"".equals(user.getUserSt())) {
            resultEntity = UserEntity.builder().userSt(user.getUserSt()).build();
        }
        return userJpaRepository.save(resultEntity);
    }

    @Override
    public void deleteUserByUserSq(long userSq) {
        userJpaRepository.deleteById(userSq);
    }
}
