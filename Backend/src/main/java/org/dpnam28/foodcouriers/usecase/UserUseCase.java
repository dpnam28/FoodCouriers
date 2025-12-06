package org.dpnam28.foodcouriers.usecase;

import lombok.RequiredArgsConstructor;
import org.dpnam28.foodcouriers.domain.entity.User;
import org.dpnam28.foodcouriers.domain.exception.AppException;
import org.dpnam28.foodcouriers.domain.exception.ErrorCode;
import org.dpnam28.foodcouriers.domain.repository.LocationRepository;
import org.dpnam28.foodcouriers.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserUseCase {
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    public User createUser(User user, Long locationId) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new AppException(ErrorCode.USER_ALREADY_EXISTS);
        }
        user.setLocation(locationRepository.findById(locationId)
                .orElseThrow(() -> new AppException(ErrorCode.LOCATION_NOT_FOUND)));
        return userRepository.save(user);
    }

    public User updateUser(Long id, User user, Long locationId) {
        user.setLocation(locationRepository.findById(locationId)
                .orElseThrow(() -> new AppException(ErrorCode.LOCATION_NOT_FOUND)));
        return userRepository.update(id, user);
    }
}
