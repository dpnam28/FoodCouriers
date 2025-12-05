package org.dpnam28.foodcouriers.usecase;

import lombok.RequiredArgsConstructor;
import org.dpnam28.foodcouriers.domain.entity.User;
import org.dpnam28.foodcouriers.domain.repository.LocationRepository;
import org.dpnam28.foodcouriers.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserUseCase {
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    public User createUser(User user, Long locationId) {
        user.setLocation(locationRepository.findById(locationId)
                .orElseThrow(() -> new RuntimeException("Location not found")));
        return userRepository.save(user);
    }

    public User updateUser(Long id, User user, Long locationId) {
        user.setLocation(locationRepository.findById(locationId)
                .orElseThrow(() -> new RuntimeException("Location not found")));
        return userRepository.update(id, user);
    }
}
