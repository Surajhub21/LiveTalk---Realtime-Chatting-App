package com.ChatService.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class WebPageController {

    @GetMapping("/")
    public String signinPage(){
        return "signin";
    }

    @GetMapping("/signup")
    public String signupPage(){
        return "signup";
    }

    @GetMapping("/home")
    public String home(){
        return "home";
    }

    @GetMapping("/chat/{room}")
    public String chatRoom(@PathVariable String room) {
        return "room";
    }
}
