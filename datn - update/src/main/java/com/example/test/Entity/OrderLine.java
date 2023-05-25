package com.example.test.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderLine {
    private int quantity;

    //    @EmbeddedId
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private OrderLineKey id;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;
    @ManyToOne
//    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
//    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;
//    @Embeddable
//    @EqualsAndHashCode
//    @Setter
//    @Getter
//    public class OrderLineKey implements Serializable {
//        @Column(name = "order_id")
//        Integer orderId;
//
//        @Column(name = "product_id")
//        Integer productId;
//
//        // standard constructors, getters, and setters
//        // hashcode and equals implementation
//    }

    @Override
    public String toString() {
        return "OrderLine{" +
                "quantity=" + quantity +
                ", id=" + id +
                ", order=" + order +
                ", product=" + product +
                '}';
    }
}
