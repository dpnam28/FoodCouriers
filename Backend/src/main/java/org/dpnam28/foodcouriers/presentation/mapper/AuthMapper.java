package org.dpnam28.foodcouriers.presentation.mapper;

import org.dpnam28.foodcouriers.domain.entity.User;
import org.dpnam28.foodcouriers.presentation.dto.auth.AuthLoginResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthMapper {
    AuthLoginResponse toAuthLoginResponse(User user);
}
