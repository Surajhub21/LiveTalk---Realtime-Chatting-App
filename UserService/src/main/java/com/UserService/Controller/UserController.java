package com.UserService.Controller;

import com.UserService.Entity.UserModel;
import com.UserService.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //testing Purpose
    @GetMapping("/h")
    public String user(){
        return "<h1>User-Service Running...</h1>";
    }
    @GetMapping("/{username}")
    public UserModel getUserByUsername(@PathVariable String username){
        return userService.getUserByUsername(username);
    }

    @PostMapping
    public UserModel createUser(@RequestBody UserModel userModel){
        return userService.createUser(userModel);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserModel userModel) {
        UserModel user = userService.getUserByUsername(userModel.getUsername());

        if (user == null || !user.getPassword().equals(userModel.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        return ResponseEntity.ok(user);
    }

    @PostMapping("/{username}/roomId/{roomId}")
    public boolean setRoomToUser(@PathVariable String username , @PathVariable String roomId){
        return userService.setRoomToUser(username , roomId);
    }

    @GetMapping("/{username}/rooms")
    public List<String> getAllTheRooms(@PathVariable String username){
        return userService.getAllRoomsOfTheUser(username);
    }

}
