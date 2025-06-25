package it.uniroma3.siw.SiwBooks.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.SiwBooks.repository.UserRepository;
import it.uniroma3.siw.SiwBooks.model.Role;
import it.uniroma3.siw.SiwBooks.model.User;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void register(User user, String password) {
        if (userRepository.findByUsername(user.getUsername()) != null ||
                userRepository.findByEmail(user.getEmail()) != null) {
            throw new IllegalArgumentException("UserExists");
        }
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(Role.REGISTERED);
        userRepository.save(user);

    }

}
