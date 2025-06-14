package it.uniroma3.siw.SiwBooks.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.SiwBooks.model.User;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
}