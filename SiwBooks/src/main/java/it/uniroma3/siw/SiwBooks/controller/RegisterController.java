package it.uniroma3.siw.SiwBooks.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.ui.Model;
import it.uniroma3.siw.SiwBooks.model.User;
import it.uniroma3.siw.SiwBooks.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import it.uniroma3.siw.SiwBooks.model.Role; // Add this import if Role is an enum or class in your model package

@Controller
public class RegisterController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register"; // Thymeleaf page
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.REGISTERED);
        userRepository.save(user);
        return "redirect:/login";
    }
}

