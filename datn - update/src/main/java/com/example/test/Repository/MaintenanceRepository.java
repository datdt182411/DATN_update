package com.example.test.Repository;

import com.example.test.Entity.Maintenance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MaintenanceRepository extends JpaRepository<Maintenance,Integer> {
    @Query(value = "CALL get_maintenance()", nativeQuery = true)
    List<Object> findMaintenace();
}
