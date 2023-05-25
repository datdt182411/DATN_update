package com.example.test.Service;

import com.example.test.Entity.CartItem;
import org.springframework.stereotype.Service;

import java.util.Collection;


@Service
public interface ShoppingCartService {

	int getCount();

	double getAmount();

	void clear();

	Collection<CartItem> getCartItems();

	void remove(CartItem item);

	void add(CartItem item);

	void remove(Integer id);

}
