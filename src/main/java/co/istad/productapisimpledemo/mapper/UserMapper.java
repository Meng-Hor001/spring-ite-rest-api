package co.istad.productapisimpledemo.mapper;

import co.istad.productapisimpledemo.dto.auth.RegisterRequest;
import co.istad.productapisimpledemo.dto.auth.RegisterResponse;
import co.istad.productapisimpledemo.dto.user.CreateUserRequest;
import co.istad.productapisimpledemo.dto.user.UserResponse;
import co.istad.productapisimpledemo.entity.User;
import org.keycloak.representations.idm.UserRepresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "profileUrl", source = "profile.profileUrl")
    @Mapping(target="bio", source = "profile.bio")
    UserResponse toUserResponse(User user);
    User toUser(CreateUserRequest request);



    // for register
    RegisterResponse toRegisterResponse(UserRepresentation user);

    // entity to registerResponse
    @Mapping(target = "firstName", source = "profile.firstName")
    @Mapping(target ="lastName", source = "profile.lastName")
    @Mapping(target = "biography" , source="profile.bio")
    @Mapping(target = "gender", source = "profile.gender")
    RegisterResponse toRegisterResponse(User user);
}
