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
public class Courier {

    @Id
    @Column(name = "courier_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "courier_id")
    private User user;

    @OneToMany(mappedBy = "courier")
    private List<Order> orders;

    private Boolean isAvailable;
}
