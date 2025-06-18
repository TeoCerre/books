package it.uniroma3.siw.SiwBooks.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import it.uniroma3.siw.SiwBooks.model.User;
import it.uniroma3.siw.SiwBooks.service.UserService;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/default")
    public String redirectAfterLogin() {
        org.springframework.security.core.Authentication auth = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication();

        String username = auth.getName();
        User user = userService.findByUsername(username);

        if (user.getRole() == it.uniroma3.siw.SiwBooks.model.Role.ADMIN) {
            return "redirect:/admin/home";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
