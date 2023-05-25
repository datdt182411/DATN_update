package com.example.test.Service;

import com.example.test.Entity.Maintenance;
import com.example.test.Exception.MaintenanceNotFoundException;
import org.springframework.stereotype.Service;

@Service
public interface MaintenanceService {
    public Maintenance getMaintenance(Integer id) throws MaintenanceNotFoundException;

    public Maintenance saveMaintenance(Maintenance maintenance);
}
