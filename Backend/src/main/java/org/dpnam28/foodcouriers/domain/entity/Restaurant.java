package org.dpnam28.foodcouriers.domain.entity;

import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "restaurants")
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

    @OneToMany(mappedBy = "restaurant")
    private List<Order> orders;
    
    @OneToMany(mappedBy = "restaurant")
    private List<Food> foods;
}
