package com.app.ecom_application.service;

import com.app.ecom_application.dto.AddressDTO;
import com.app.ecom_application.dto.UserRequest;
import com.app.ecom_application.dto.UserResponse;
import com.app.ecom_application.model.Address;
import com.app.ecom_application.model.User;
import com.app.ecom_application.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class UserService {

    private final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

//    private List<User> userList = new ArrayList<>();

//    private Long nextId = 1L;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

//    public List<UserResponse> fetchAllUsers(){
//        return userRepository.findAll();
//    }

    public List<UserResponse> fetchAllUsers(){
        return userRepository.findAll()
                .stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }


    public  void createUser(UserRequest userRequest){
//        user.setId(nextId++);
        User user = new User();
        updateUserFromRequest(user,userRequest);
        userRepository.save(user);

    }

    private void updateUserFromRequest(User user, UserRequest userRequest) {
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPhone(userRequest.getPhone());
        if(userRequest.getAddress() != null){
            Address address = new Address();
            address.setCity(userRequest.getAddress().getCity());
            address.setStreet(userRequest.getAddress().getStreet());
            address.setState(userRequest.getAddress().getState());
            address.setCountry(userRequest.getAddress().getCountry());
            address.setZipCode(userRequest.getAddress().getZipCode());
            user.setAddress(address);
        }
    }


    public Optional<UserResponse> fetchUser(Long id) {
        logger.info("Fetching user by ID: {}", id);

        return userRepository.findById(id)
                .map(this::mapToUserResponse);
    }


    public boolean updateUserById(Long id,UserRequest updateUser ){
        return userRepository.findById(id)
                .map(existingUser -> {
                    updateUserFromRequest(existingUser, updateUser);
                    userRepository.save(existingUser);
                    return true;
                })
                .orElse(false);
    }

    private UserResponse mapToUserResponse(User user){
        UserResponse response = new UserResponse();
        response.setId(String.valueOf(user.getId()));
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setRole(user.getRole());

        if(user.getAddress() != null){
            AddressDTO addressDTO = new AddressDTO();
            addressDTO.setStreet(user.getAddress().getStreet());
            addressDTO.setCity(user.getAddress().getCity());
            addressDTO.setState(user.getAddress().getState());
            addressDTO.setCountry(user.getAddress().getCountry());
            addressDTO.setZipCode(user.getAddress().getZipCode());
            response.setAddress(addressDTO);
        }
        return response;
    }

}
