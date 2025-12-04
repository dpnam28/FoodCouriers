package org.dpnam28.foodcouriers.domain.repository;

import org.dpnam28.foodcouriers.domain.entity.User;

public interface UserRepository {
    User save(User user);
    User update(User user);
}
