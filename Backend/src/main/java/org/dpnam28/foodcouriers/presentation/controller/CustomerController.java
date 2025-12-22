package org.dpnam28.foodcouriers.presentation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dpnam28.foodcouriers.domain.dto.ApiResponse;
import org.dpnam28.foodcouriers.presentation.dto.customer.CustomerTotalOrdersRequest;
import org.dpnam28.foodcouriers.presentation.dto.customer.CustomerTotalOrdersResponse;
import org.dpnam28.foodcouriers.usecase.CustomerUseCase;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerUseCase customerUseCase;

    @GetMapping("/{id}/total-orders")
    public ApiResponse<CustomerTotalOrdersResponse> getTotalOrders(@PathVariable Long id) {
        Integer totalOrders = customerUseCase.getTotalOrders(id);
        return ApiResponse.apiResponseSuccess(
                "Get total orders succeeded",
                new CustomerTotalOrdersResponse(totalOrders)
        );
    }

    @PutMapping("/{id}/total-orders")
    public ApiResponse<CustomerTotalOrdersResponse> updateTotalOrders(
            @PathVariable Long id,
            @RequestBody @Valid CustomerTotalOrdersRequest request
    ) {
        Integer totalOrders = customerUseCase.updateTotalOrders(id, request.getTotalOrders());
        return ApiResponse.apiResponseSuccess(
                "Update total orders succeeded",
                new CustomerTotalOrdersResponse(totalOrders)
        );
    }
}
