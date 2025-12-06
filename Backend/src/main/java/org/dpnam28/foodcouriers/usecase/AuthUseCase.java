package org.dpnam28.foodcouriers.usecase;

import lombok.RequiredArgsConstructor;
import org.dpnam28.foodcouriers.domain.entity.User;
import org.dpnam28.foodcouriers.domain.exception.AppException;
import org.dpnam28.foodcouriers.domain.exception.ErrorCode;
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
        if(user == null){
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        if(!encoder.matches(password, user.getPassword())){
            throw new AppException(ErrorCode.INVALID_PASSWORD);
        }
        return user;
    }
}
