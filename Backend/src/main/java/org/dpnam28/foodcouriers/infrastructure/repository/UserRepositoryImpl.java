package org.dpnam28.foodcouriers.infrastructure.repository;

import lombok.RequiredArgsConstructor;

import org.dpnam28.foodcouriers.domain.entity.User;
import org.dpnam28.foodcouriers.domain.repository.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

interface JpaUserRepository extends JpaRepository<User, Long> {
}

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final JpaUserRepository jpaUserRepository;

    @Override
    public User save(User user) {
        return null;
    }

    @Override
    public User update(User user) {
        return null;
    }
}
