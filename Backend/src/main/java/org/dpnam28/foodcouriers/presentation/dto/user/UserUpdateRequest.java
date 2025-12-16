package org.dpnam28.foodcouriers.presentation.dto.user;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequest {

    @NotBlank(message = "Mật khẩu là bắt buộc")
    private String password;
    @NotBlank(message = "Tên là bắt buộc")
    private String fullName;
    @NotBlank(message = "Số điện thoại là bắt buộc")
    private String phoneNumber;
    @NotBlank(message = "Địa chỉ là bắt buộc")
    private String address;
    @NotBlank(message = "Vai trò là bắt buộc")
    private String role;
    @NotNull(message = "Vị trí là bắt buộc")
    private Long locationId;
    private String description;
    private String bannerImage;
    private Double deliveryFee;
}
