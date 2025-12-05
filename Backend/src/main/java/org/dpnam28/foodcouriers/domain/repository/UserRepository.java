package org.dpnam28.foodcouriers.domain.repository;

import org.dpnam28.foodcouriers.domain.entity.User;
import org.dpnam28.foodcouriers.presentation.dto.request.UserCreationRequest;

public interface UserRepository {
    User save(User user);
    User update(Long id, User user);
}
