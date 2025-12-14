package org.dpnam28.foodcouriers.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.dpnam28.foodcouriers.domain.entity.Customer;
import org.dpnam28.foodcouriers.domain.repository.CustomerRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

interface JpaCustomerRepository extends JpaRepository<Customer, Long> {
}

@Repository
@RequiredArgsConstructor
public class CustomerRepositoryImpl implements CustomerRepository {

    private final JpaCustomerRepository jpaCustomerRepository;
    @Override
    public Customer save(Customer customer) {
        return jpaCustomerRepository.save(customer);
    }
}
