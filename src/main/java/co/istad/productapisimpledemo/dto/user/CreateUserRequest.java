package co.istad.productapisimpledemo.dto.user;


import lombok.Builder;

@Builder
public record CreateUserRequest(
        String email,
        String password,
        String profileUrl ,
        String bio

) {
}
