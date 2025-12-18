package org.dpnam28.foodcouriers.domain.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    INTERNAL_SERVER_ERROR("Lỗi máy chủ nội bộ", 500),
    USER_NOT_FOUND("Người dùng không tồn tại", 404),
    USER_ALREADY_EXISTS("Người dùng đã tồn tại", 409),
    LOCATION_NOT_FOUND("Vị trí không tồn tại", 404),
    INVALID_PASSWORD("Mật khẩu không hợp lệ", 400),
    EMAIL_NOT_VALID("Email không hợp lệ", 400),
    ARGUMENT_IS_REQUIRED("{arg} là bắt buộc", 400),
    RESTAURANT_NOT_FOUND("Nhà hàng không tồn tại", 404),
    CUSTOMER_NOT_FOUND("Khách hàng không tồn tại", 404),
    COURIER_NOT_FOUND("Nhân viên giao hàng không tồn tại", 404),
    CATEGORY_NOT_FOUND("Danh mục không tồn tại", 404),
    CATEGORY_ALREADY_EXISTS("Danh mục đã tồn tại", 409),
    FOOD_NOT_FOUND("Món ăn không tồn tại", 404),
    CART_ITEM_NOT_FOUND("Sản phẩm trong giỏ hàng không tồn tại", 404),
    ORDER_NOT_FOUND("Đơn hàng không tồn tại", 404),
    UNAUTHORIZED_ACTION("Không có quyền thực hiện thao tác này", 403),
    UPLOAD_FAILED("Tải ảnh thất bại", 500),
    USER_NOT_OWN_CART_ITEM("Người dùng không sở hữu sản phẩm trong giỏ hàng", 403),
    RESTAURANT_NOT_OWN_ITEM("Nhà hàng không sở hữu sản phẩm", 403)
    ;
    private final String message;
    private final int code;

    ErrorCode(String message, int code) {
        this.message = message;
        this.code = code;
    }

    public static ErrorCode fromMessage(String message) {
        if (message == null) {
            throw new IllegalArgumentException("Message must not be null");
        }
        for (ErrorCode errorCode : values()) {
            if (errorCode.message.equalsIgnoreCase(message.trim())) {
                return errorCode;
            }
        }
        throw new IllegalArgumentException("No matching ErrorCode for message: " + message);
    }

    public String formatMessage(String arg) {
        if (arg == null) {
            return message;
        }
        return message.replace("{arg}", arg.trim());
    }
}
