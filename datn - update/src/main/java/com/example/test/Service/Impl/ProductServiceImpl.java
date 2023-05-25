package com.example.test.Service.Impl;

import com.example.test.Entity.Product;
import com.example.test.Exception.ProductNotFoundException;
import com.example.test.Repository.ProductRepository;
import com.example.test.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;

    @Override
    public List<Product> listAll() {
        return (List<Product>) productRepository.findAll();
    }

    @Override
    public boolean checkProduct(String name) {
        return productRepository.existsByName(name);
    }

    @Override
    public Product getProduct(Integer id) throws ProductNotFoundException{
        Optional<Product> result = productRepository.findById(id);
        if(result.isPresent()){
            return result.get();
        }
        throw new ProductNotFoundException("Could not find any product with ID " + id);
    }

    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Integer id) {
        productRepository.delete(productRepository.getOne(id));
    }

    @Override
    public List<Product> findAllByProductName(String keyword) {
        return productRepository.findAllByNameContaining(keyword);
    }
}
