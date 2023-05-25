package com.example.test.Controller;

import com.example.test.Entity.Custommer;
import com.example.test.Entity.Users;
import com.example.test.Service.ShoppingCartServiceTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ShoppingCartRestController {
    @Autowired
    private ShoppingCartServiceTest shoppingCartServiceTest;

    @PostMapping("/cart/add/{productId}/{quantity}")
    public String addProductToCart(@PathVariable ("productId") Integer productId,
                                   @PathVariable ("quantity") Integer quantity, HttpServletRequest request) {

        Integer updatedQuantity = shoppingCartServiceTest.addProduct(productId, quantity, new Users());
        return updatedQuantity + " item(s) of this product were added to your shopping cart";
    }



}
