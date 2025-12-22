package org.dpnam28.foodcouriers.domain.repository;

import org.dpnam28.foodcouriers.domain.entity.Customer;

public interface CustomerRepository {
    Customer save(Customer customer);

    Customer findById(Long id);
}
