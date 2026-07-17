package co.istad.productapisimpledemo.dto.auth;

import lombok.Builder;

// note : please use the validation package
@Builder
public record UserUpdateRequest(
        String firstName,
        String lastName,
        String gender,
        String biography,
        String profileUrl

        // can be more
) {
}
