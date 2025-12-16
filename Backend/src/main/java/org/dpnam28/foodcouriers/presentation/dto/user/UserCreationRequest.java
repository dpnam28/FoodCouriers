package org.dpnam28.foodcouriers.presentation.dto.user;

import jakarta.validation.constraints.Email;
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
public class UserCreationRequest {

    @NotBlank(message = "Email là bắt buộc")
    @Email(message = "Email không hợp lệ")
    private String email;
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
}
