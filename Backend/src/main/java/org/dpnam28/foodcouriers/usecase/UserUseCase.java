package org.dpnam28.foodcouriers.usecase;

import java.util.Objects;

import org.dpnam28.foodcouriers.domain.entity.Courier;
import org.dpnam28.foodcouriers.domain.entity.Customer;
import org.dpnam28.foodcouriers.domain.entity.Restaurant;
import org.dpnam28.foodcouriers.domain.entity.User;
import org.dpnam28.foodcouriers.domain.exception.AppException;
import org.dpnam28.foodcouriers.domain.exception.ErrorCode;
import org.dpnam28.foodcouriers.domain.repository.CourierRepository;
import org.dpnam28.foodcouriers.domain.repository.CustomerRepository;
import org.dpnam28.foodcouriers.domain.repository.LocationRepository;
import org.dpnam28.foodcouriers.domain.repository.RestaurantRepository;
import org.dpnam28.foodcouriers.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

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
        var location = locationRepository.findById(locationId);
        var savedUser = userRepository.save(user);
        savedUser.setLocation(location);
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

    public User updateUser(Long id, User user, Restaurant restaurant) {
        User userUpdate = userRepository.findById(id);
        userUpdate.setPassword(user.getPassword());
        userUpdate.setFullName(user.getFullName());
        userUpdate.setPhoneNumber(user.getPhoneNumber());
        userUpdate.setAddress(user.getAddress());
        Restaurant restaurantUpdate = restaurantRepository.findById(id);
        if(restaurantUpdate != null && Objects.equals(userUpdate.getRole(), "ROLE_RESTAURANT")){
            restaurantUpdate.setDescription(restaurant.getDescription());
            restaurantUpdate.setBannerImage(restaurant.getBannerImage());
            restaurantUpdate.setDeliveryFee(restaurant.getDeliveryFee());
            restaurantRepository.save(restaurantUpdate);
        }
        return userRepository.save(userUpdate);
    }

    public User findById(Long id) {
        return userRepository.findById(id);
    }
}
