package org.dpnam28.foodcouriers.usecase;

import lombok.RequiredArgsConstructor;
import org.dpnam28.foodcouriers.domain.entity.Courier;
import org.dpnam28.foodcouriers.domain.entity.Customer;
import org.dpnam28.foodcouriers.domain.entity.Restaurant;
import org.dpnam28.foodcouriers.domain.entity.User;
import org.dpnam28.foodcouriers.domain.exception.AppException;
import org.dpnam28.foodcouriers.domain.exception.ErrorCode;
import org.dpnam28.foodcouriers.domain.repository.*;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserUseCase {
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final CustomerRepository customerRepository;
    private final CourierRepository courierRepository;
    private final RestaurantRepository restaurantRepository;

    public User createUser(User user, Long locationId) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new AppException(ErrorCode.USER_ALREADY_EXISTS);
        }
        String role = user.getRole();
        var savedUser = userRepository.save(user);
        switch (role) {
            case "ROLE_CUSTOMER" -> createCustomerProfile(savedUser);
            case "ROLE_RESTAURANT" -> createRestaurantProfile(savedUser);
            case "ROLE_COURIER" -> createCourierProfile(savedUser);
        }

        return savedUser;
    }

    private void createCourierProfile(User savedUser) {
        courierRepository.save(Courier.builder()
                .user(savedUser)
                .isAvailable(true)
                .build());
    }

    private void createRestaurantProfile(User savedUser) {
        restaurantRepository.save(Restaurant.builder()
                .user(savedUser)
                .deliveryFee(0.0)
                .build());
    }

    private void createCustomerProfile(User savedUser) {
        customerRepository.save(Customer.builder()
                .user(savedUser)
                .totalOrders(0)
                .build());
    }

    public User updateUser(Long id, User user, Restaurant restaurant, Long locationId) {
        User userUpdate = userRepository.findById(id);
        userUpdate.setLocation(locationRepository.findById(locationId));
        userUpdate.setPassword(user.getPassword());
        userUpdate.setFullName(user.getFullName());
        userUpdate.setPhoneNumber(user.getPhoneNumber());
        userUpdate.setAddress(user.getAddress());
        Restaurant restaurantUpdate = restaurantRepository.findById(id);
        if(restaurantUpdate != null && Objects.equals(userUpdate.getRole(), "ROLE_RESTAURANT")){
            restaurantUpdate.setDescription(restaurant.getDescription());
            restaurantUpdate.setBannerImage(restaurant.getBannerImage());
            restaurantUpdate.setDeliveryFee(restaurant.getDeliveryFee());
        }
        return userRepository.save(userUpdate);
    }
}
