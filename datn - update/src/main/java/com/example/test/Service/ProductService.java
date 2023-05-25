package com.example.test.Service;

import com.example.test.Entity.Product;
import com.example.test.Exception.ProductNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {

    public List<Product> listAll();

    public boolean checkProduct(String name);

    public Product getProduct(Integer id) throws ProductNotFoundException;

    public Product saveProduct(Product product);

    public void deleteProduct(Integer id);

    public List<Product> findAllByProductName(String keyword);
}
