package com.example.test.Service;

import com.example.test.Entity.Custommer;
import com.example.test.Exception.CustomerNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CustomerService {
    public List<Custommer> findAllByProductName(String keyword);

    public Custommer getCustomer(Integer id) throws CustomerNotFoundException;


}
