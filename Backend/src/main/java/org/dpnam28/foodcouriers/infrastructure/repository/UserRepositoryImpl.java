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
        return jpaUserRepository.save(user);
    }

    @Override
    public User update(Long id, User user) {
        User userToUpdate = jpaUserRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        userToUpdate.setPassword(user.getPassword());
        userToUpdate.setFullName(user.getFullName());
        userToUpdate.setPhoneNumber(user.getPhoneNumber());
        userToUpdate.setAddress(user.getAddress());
        userToUpdate.setProfileImage(user.getProfileImage());
        userToUpdate.setRole(user.getRole());
        return jpaUserRepository.save(userToUpdate);
    }
}
