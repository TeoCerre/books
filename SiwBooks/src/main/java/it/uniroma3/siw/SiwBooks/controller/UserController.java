package it.uniroma3.siw.SiwBooks.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import it.uniroma3.siw.SiwBooks.repository.*;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

}