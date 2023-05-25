package com.example.test.Service;

import com.example.test.Entity.Cart;
import com.example.test.Entity.CartItem;
import com.example.test.Entity.Custommer;
import com.example.test.Entity.Users;
import com.example.test.Repository.CartItemsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public interface ShoppingCartServiceTest {

//    public Integer addProduct(Integer productId, Integer quantity, Users users);


    int getCount();

    double getAmount();

    void clear();

    Collection<Cart> getCart();

    void remove(Cart item);

    void add(Cart item);

    void remove(Integer id);

}
