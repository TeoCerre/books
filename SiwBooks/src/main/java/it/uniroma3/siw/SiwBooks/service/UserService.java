package it.uniroma3.siw.SiwBooks.service;

import org.springframework.beans.factory.annotation.Autowired;

import it.uniroma3.siw.SiwBooks.repository.UserRepository;

public class UserService {
    @Autowired 
    private UserRepository userRepository;
}
