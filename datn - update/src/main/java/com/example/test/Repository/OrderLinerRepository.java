package com.example.test.Repository;

import com.example.test.Entity.OrderLine;
import org.springframework.data.jpa.repository.JpaRepository;
public interface OrderLinerRepository extends JpaRepository<OrderLine, Integer> {
//    INSERT INTO `medical`.`order_line` (`order_id`, `product_id`, `quantity`) VALUES ('1', '1', '1');

}
