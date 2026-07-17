package co.istad.productapisimpledemo.service;

// Specification

import co.istad.productapisimpledemo.dto.user.CreateUserRequest;
import co.istad.productapisimpledemo.dto.user.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse createUser(CreateUserRequest request );
    List<UserResponse> getAllUsers();
    UserResponse getUserByKeycloakId(String keycloakId);
}
