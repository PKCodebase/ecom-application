package com.app.ecom_application.service;

import com.app.ecom_application.model.User;
import com.app.ecom_application.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UserService {

    private final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

//    private List<User> userList = new ArrayList<>();

//    private Long nextId = 1L;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> fetchAllUsers(){
        return userRepository.findAll();
    }


    public  void createUser(User user){
//        user.setId(nextId++);
        userRepository.save(user);

    }


    public Optional<User> fetchUser(Long id) {
        logger.info("Fetching user by ID: {}", id);

        return userRepository.findById(id);
    }


    public User updateUserById(Long id,User updateUser ){
        return userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setFirstName(updateUser.getFirstName());
                    existingUser.setLastName(updateUser.getLastName());
                    return userRepository.save(existingUser);
                })
                .orElse(null);
    }

}
