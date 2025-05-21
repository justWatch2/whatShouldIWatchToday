//package com.example.demo.controller;
//
//import com.example.demo.entity.UserEntity;
//import com.example.demo.service.UserService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@RequestMapping("/api/v1/user")
//@RestController
//@RequiredArgsConstructor
//public class UserController {
//
//    private final UserService userService;
//
//    @PostMapping("/users")
//    public ResponseEntity<Object> selectUserList(){
//        List<UserEntity> userEntityList = userService.userList();
//        return new ResponseEntity<>(userEntityList, HttpStatus.OK);
//    }
//
//    @PostMapping("/user")
//    public ResponseEntity<Object> insertUser(@RequestBody UserEntity userEntity){
//        UserEntity result = userService.saveUser(userEntity);
//        return new ResponseEntity<>(result, HttpStatus.OK);
//    }
//}
