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
public class Restaurant {

    @Id
    @Column(name = "restaurant_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "restaurant_id")
    @ToString.Exclude
    private User user;

    private String description;

    private String bannerImage;

    @Column(nullable = false)
    private Double deliveryFee;

    @OneToMany(mappedBy = "restaurant")
    private List<Order> orders;
    
    @OneToMany(mappedBy = "restaurant")
    private List<Food> foods;
}
