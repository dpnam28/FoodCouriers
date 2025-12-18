package org.dpnam28.foodcouriers.presentation.dto.user;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequest {

    private String password;
    @NotBlank(message = "Tên là bắt buộc")
    private String fullName;
    @NotBlank(message = "Số điện thoại là bắt buộc")
    private String phoneNumber;
    @NotBlank(message = "Địa chỉ là bắt buộc")
    private String address;
    private String description;
}
