package com.app.ecom_application.controller;

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
    public ResponseEntity<List<User>> getAllUsers(){
        return new ResponseEntity<>(userService.fetchAllUsers(), HttpStatus.OK);
//        return  ResponseEntity.ok(userService.fetchAllUsers()) ;
    }

    @GetMapping("{id}")
    public ResponseEntity<User> fetchUserById(@PathVariable Long id){

        return userService.fetchUser(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<String> addUser(@RequestBody User user){
        userService.createUser(user);
        return  ResponseEntity.ok("User added successfully") ;
    }

    @PutMapping("{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id,@RequestBody User user) {
        User users = userService.updateUserById(id, user);

        if (users == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok("User updated successfully");
    }
}
