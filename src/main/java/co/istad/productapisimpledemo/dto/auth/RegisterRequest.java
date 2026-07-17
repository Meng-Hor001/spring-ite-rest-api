package co.istad.productapisimpledemo.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record RegisterRequest(
        @NotBlank(message = "username is required! ")
        @Size(min = 3,max=255)
        String username ,

        @NotBlank(message = "Password is required! ")
        @Size(min = 8,max=255)
        String password,

        @NotBlank(message = "Confirmed password is required!")
        @Size(min = 8,max=255)
        String confirmedPassword,

        @NotBlank(message = "Email is required ")
        @Email
        @Size(max=255)
        String email,
        String firstName,
        String lastName,

        @NotBlank(message = "Gender is required")
        String gender,
        String biography
//
//    @NotNull(message = "Role is required ")
//    UserRole role
) {
}

