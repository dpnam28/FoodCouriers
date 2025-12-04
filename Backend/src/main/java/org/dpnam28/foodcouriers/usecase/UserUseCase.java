package org.dpnam28.foodcouriers.usecase;

import org.dpnam28.foodcouriers.domain.entity.User;
import org.dpnam28.foodcouriers.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserUseCase {
    private final UserRepository userRepository;

    public UserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        return null;
    }

    public User updateUser(User user) {
        return null;
    }
}
