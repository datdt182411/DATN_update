package com.example.test.Service.Impl;

import com.example.test.Entity.Custommer;
import com.example.test.Exception.CustomerNotFoundException;
import com.example.test.Repository.CustomerRepository;
import com.example.test.Service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    CustomerRepository customerRepository;
    @Override
    public List<Custommer> findAllByProductName(String keyword) {
        return customerRepository.findAllByNameContaining(keyword);
    }

    @Override
    public Custommer getCustomer(Integer id) throws CustomerNotFoundException {
        Optional<Custommer> result = customerRepository.findById(id);
        if(result.isPresent()){
            return result.get();
        }
        throw new CustomerNotFoundException("Could not find any users with ID " + id);
    }
}
