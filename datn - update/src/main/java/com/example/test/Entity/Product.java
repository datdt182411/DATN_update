package com.example.test.Entity;

import lombok.*;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Nationalized
    @Column(unique = true)
    @NonNull
    private String name;
    @NonNull
    private int status;
    @Min(0)
    @NonNull
    private double price;
    @Min(0)
    @NonNull
    private int quantity;
    @NonNull
    private String photo;

    public Product(Integer id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "type_id")
    private Type type;
    @ManyToOne
    @JoinColumn(name = "vendor_id")
    private Vendor vendor;

    @OneToMany(mappedBy = "product" )
    private List<Repair> repairList;

    @OneToMany(mappedBy = "product")
    private List<Maintenance> maintenanceList;

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status +
                '}';
    }
    @OneToMany(mappedBy = "product")
    List<OrderLine> orderLineList;
}
