package it.uniroma3.siw.SiwBooks.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login";  // nome del template Thymeleaf login.html
    }
}

