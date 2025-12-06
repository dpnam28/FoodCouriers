package org.dpnam28.foodcouriers.usecase;

import lombok.RequiredArgsConstructor;
import org.dpnam28.foodcouriers.domain.entity.User;
import org.dpnam28.foodcouriers.domain.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthUseCase {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public User login(String email, String password){
        User user = userRepository.findByEmail(email);
        if(!encoder.matches(password, user.getPassword())){
            throw new RuntimeException("Invalid password");
        }
        return user;
    }
}
