package org.dpnam28.foodcouriers.presentation.dto.auth;

import org.dpnam28.foodcouriers.presentation.dto.location.LocationResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthLoginResponse {
    private Long id;
    private String email;
    private String fullName;
    private String phoneNumber;
    private String address;
    private String role;
    private LocationResponse location;
}
