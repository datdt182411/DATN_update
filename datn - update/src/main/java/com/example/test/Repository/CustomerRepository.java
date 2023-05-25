package com.example.test.Repository;

import com.example.test.Entity.Custommer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Custommer,Integer> {
    List<Custommer> findAllByNameContaining(String keyword);
}
