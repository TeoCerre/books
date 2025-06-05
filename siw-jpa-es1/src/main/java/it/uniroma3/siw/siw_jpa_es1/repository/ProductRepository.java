package it.uniroma3.siw.siw_jpa_es1.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.uniroma3.siw.siw_jpa_es1.model.Product;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {
}