package com.UserService.Service;

import com.UserService.Entity.UserModel;
import com.UserService.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserModel createUser(UserModel user){
        return userRepository.save(user);
    }

    public UserModel getUserByUsername(String username){
        return userRepository.getUserByUsername(username);
    }

    public boolean setRoomToUser(String username, String roomId){
        UserModel user = getUserByUsername(username);
        if(user != null) {
            user.getRoomsId().add(roomId);
            userRepository.save(user);
            return true;
        }

        return false;
    }

    public List<String> getAllRoomsOfTheUser(String username) {
        UserModel user = userRepository.getUserByUsername(username);

        return user.getRoomsId();
    }
}
