package com.example.test.Repository;

import com.example.test.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    //    @Query(value = "EXEC bookedTicketForUser :accountID", nativeQuery = true)
//    List<Object> getList(@Param("accountID") Integer accountID);
//
//    @Query(value = "EXEC UserHistory ?1, ?2, ?3", nativeQuery = true)
//    List<Object> getHistory(Integer accountID, Date fromDate, Date toDate);
    @Query(value = "CALL manager_order(:manager_order);", nativeQuery = true)
    List<Object> managerOrder(@Param("manager_order") Integer year_in);

    @Query(value = "SELECT * from  orders where orders.order_date BETWEEN ?1 AND ?2" ,nativeQuery = true)
    List<Order> orders_by_time(String date1,String date2);

    @Query(value = "CALL manager_order_by_month(:years);" ,nativeQuery = true)
    List<Object> orders_by_month(@Param("years") Integer years);



}
