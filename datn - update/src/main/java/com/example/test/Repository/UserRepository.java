package com.example.test.Repository;

import com.example.test.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users,Integer> {
    Users getUsersByUsername(String name);
    Users getUsersById(Integer id);
    public Users findByEmail(String email);
    public Users findByVerificationCode(String code);
}
