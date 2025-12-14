package org.dpnam28.foodcouriers.infrastructure.repository;

import lombok.RequiredArgsConstructor;

import org.dpnam28.foodcouriers.domain.entity.User;
import org.dpnam28.foodcouriers.domain.exception.AppException;
import org.dpnam28.foodcouriers.domain.exception.ErrorCode;
import org.dpnam28.foodcouriers.domain.repository.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.Optional;

interface JpaUserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final JpaUserRepository jpaUserRepository;
    private final PasswordEncoder encoder;

    @Override
    public User save(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return jpaUserRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return jpaUserRepository.findByEmail(email)
                .orElse(null);
    }

    @Override
    public User findById(Long id) {
        return jpaUserRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }
}
