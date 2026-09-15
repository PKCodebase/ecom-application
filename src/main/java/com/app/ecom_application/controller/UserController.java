package com.app.ecom_application.controller;

import com.app.ecom_application.dto.UserRequest;
import com.app.ecom_application.dto.UserResponse;
import com.app.ecom_application.model.User;
import com.app.ecom_application.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers(){
        return new ResponseEntity<>(userService.fetchAllUsers(), HttpStatus.OK);
//        return  ResponseEntity.ok(userService.fetchAllUsers()) ;
    }

    @GetMapping("{id}")
    public ResponseEntity<UserResponse> fetchUserById(@PathVariable Long id){

        return userService.fetchUser(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<String> addUser(@RequestBody UserRequest userRequest){
        userService.createUser(userRequest);
        return  ResponseEntity.ok("User added successfully") ;
    }

    @PutMapping("{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody UserRequest updateUserRequest) {
        boolean updated = userService.updateUserById(id, updateUserRequest);

        if (updated) {
            return ResponseEntity.ok("User updated successfully");
        }

        return ResponseEntity.notFound().build();
    }
}
