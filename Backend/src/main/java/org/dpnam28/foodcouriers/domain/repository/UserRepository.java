package org.dpnam28.foodcouriers.domain.repository;

import org.dpnam28.foodcouriers.domain.entity.User;

public interface UserRepository {
    User save(User user);

    User findByEmail(String email);

    User findById(Long id);

    boolean existsById(Long id);
}
