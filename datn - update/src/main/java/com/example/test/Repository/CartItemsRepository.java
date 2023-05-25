package com.example.test.Repository;

import com.example.test.Entity.Cart;
import com.example.test.Entity.Custommer;
import com.example.test.Entity.Product;
import com.example.test.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemsRepository extends JpaRepository<Cart, Integer> {
    public List<Cart> findByUsers(Users users);

    public Cart findByUsersAndProduct(Users users, Product product);

    @Modifying
    @Query("UPDATE Cart c SET c.quantity = ?1 where c.users.id = ?2 AND c.product.id = ?3")
    public void updateQuantity(Integer quantity, Integer userId, Integer productId);

    @Modifying
    @Query("DELETE FROM Cart c WHERE c.users.id = ?1 AND c.product.id = ?2")
    public void deleteByUsersAndProduct(Integer userId, Integer productId);
}
