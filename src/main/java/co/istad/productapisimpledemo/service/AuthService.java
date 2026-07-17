package co.istad.productapisimpledemo.service;

import co.istad.productapisimpledemo.dto.auth.RegisterRequest;
import co.istad.productapisimpledemo.dto.auth.RegisterResponse;
import co.istad.productapisimpledemo.dto.auth.UserUpdateRequest;
import co.istad.productapisimpledemo.dto.user.UserResponse;

public interface AuthService {

    // register the new user
    RegisterResponse register(RegisterRequest request );

    UserResponse updateUser(String keycloakId, UserUpdateRequest request);

    void forgotPassword(String email);
}
