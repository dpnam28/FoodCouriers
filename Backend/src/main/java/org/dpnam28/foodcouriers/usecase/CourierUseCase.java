package org.dpnam28.foodcouriers.usecase;

import lombok.RequiredArgsConstructor;
import org.dpnam28.foodcouriers.domain.entity.Courier;
import org.dpnam28.foodcouriers.domain.exception.AppException;
import org.dpnam28.foodcouriers.domain.exception.ErrorCode;
import org.dpnam28.foodcouriers.domain.repository.CourierRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourierUseCase {

    private final CourierRepository courierRepository;

    public Boolean setAvailable(Long courierId, Boolean available) {
        Courier courier = courierRepository.findById(courierId);
        if (courier == null) {
            throw new AppException(ErrorCode.COURIER_NOT_FOUND);
        }
        courier.setIsAvailable(available);
        return courierRepository.save(courier).getIsAvailable();
    }
}
