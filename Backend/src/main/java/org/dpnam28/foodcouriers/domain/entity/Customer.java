package org.dpnam28.foodcouriers.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "customer_id")
    @ToString.Exclude
    private User user;

    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer totalOrders;

    @OneToMany(mappedBy = "customer")
    private List<Order> orders;
}
