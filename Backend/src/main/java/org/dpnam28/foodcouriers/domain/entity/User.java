package org.dpnam28.foodcouriers.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false, columnDefinition = "VARCHAR(255) CHECK (role IN ('ROLE_COURIER', 'ROLE_CUSTOMER', 'ROLE_RESTAURANT'))")
    private String role;

    @ManyToOne
    private Location location;

    @OneToOne(mappedBy = "user")
    private Courier courier;

    @OneToOne(mappedBy = "user")
    private Restaurant restaurant;

    @OneToOne(mappedBy = "user")
    private Customer customer;
}
