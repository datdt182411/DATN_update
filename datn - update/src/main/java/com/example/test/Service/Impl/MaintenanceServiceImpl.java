package com.example.test.Service.Impl;

import com.example.test.Entity.Maintenance;
import com.example.test.Entity.Product;
import com.example.test.Exception.MaintenanceNotFoundException;
import com.example.test.Repository.MaintenanceRepository;
import com.example.test.Service.MaintenanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MaintenanceServiceImpl implements MaintenanceService {
    @Autowired
    MaintenanceRepository maintenanceRepository;
    @Override
    public Maintenance getMaintenance(Integer id) throws MaintenanceNotFoundException {
        Optional<Maintenance> result = maintenanceRepository.findById(id);
        if(result.isPresent()){
            return result.get();
        }
        throw new MaintenanceNotFoundException("Could not find any product with ID " + id);
    }

    @Override
    public Maintenance saveMaintenance(Maintenance maintenance) {
        return maintenanceRepository.save(maintenance);
    }

}
