package org.dpnam28.foodcouriers.presentation.dto.user;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dpnam28.foodcouriers.presentation.dto.location.LocationResponse;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String email;
    private String fullName;
    private String phoneNumber;
    private String address;
    private String profileImage;
    private String role;
    private LocationResponse location;
}
