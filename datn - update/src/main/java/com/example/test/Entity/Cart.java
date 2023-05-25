package com.example.test.Entity;

import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
@Table(name = "cart_items")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private Custommer custommer;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users users;
    }
