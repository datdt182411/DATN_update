package com.example.test.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Repair implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Nationalized
    private String failure;
    private String failureDate;
    private String fixedDate;
    private Double price;
    private String description;

    @ManyToOne (cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private Product product;

//    private Order order;

}
