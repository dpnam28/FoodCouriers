package org.dpnam28.foodcouriers.usecase;

import lombok.RequiredArgsConstructor;
import org.dpnam28.foodcouriers.domain.entity.Customer;
import org.dpnam28.foodcouriers.domain.exception.AppException;
import org.dpnam28.foodcouriers.domain.exception.ErrorCode;
import org.dpnam28.foodcouriers.domain.repository.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerUseCase {

    private final CustomerRepository customerRepository;

    public Integer getTotalOrders(Long customerId) {
        Customer customer = customerRepository.findById(customerId);
        if (customer == null) {
            throw new AppException(ErrorCode.CUSTOMER_NOT_FOUND);
        }
        return customer.getTotalOrders();
    }

    public Integer updateTotalOrders(Long customerId, Integer totalOrders) {
        Customer customer = customerRepository.findById(customerId);
        if (customer == null) {
            throw new AppException(ErrorCode.CUSTOMER_NOT_FOUND);
        }
        customer.setTotalOrders(totalOrders);
        return customerRepository.save(customer).getTotalOrders();
    }
}
