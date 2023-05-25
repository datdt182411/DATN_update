package com.example.test.Repository;

import com.example.test.Entity.Product;
import com.example.test.Entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findAllByNameContaining(String keyword);
    List<Product> findAllByName(String keyword);

    List<Product> findAllByType(Type type);
//    List<Product> findAll( Pageable pageable);

    public boolean existsByName(String name);
}
