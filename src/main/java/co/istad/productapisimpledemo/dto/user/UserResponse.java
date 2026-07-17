package co.istad.productapisimpledemo.dto.user;


import lombok.Builder;

@Builder
public record UserResponse(
        Long id ,
        String email,
        String profileUrl,
        String bio
) {
}
