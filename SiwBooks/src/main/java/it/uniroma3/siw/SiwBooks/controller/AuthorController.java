package it.uniroma3.siw.SiwBooks.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import it.uniroma3.siw.SiwBooks.repository.AuthorRepository;

@Controller
public class AuthorController {

    @Autowired
    private AuthorRepository authorRepository;

    // TO-DO: Add endpoints
}