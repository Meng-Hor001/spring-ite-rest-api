package co.istad.productapisimpledemo.restcontrollers;


import co.istad.productapisimpledemo.dto.auth.UserUpdateRequest;
import co.istad.productapisimpledemo.dto.user.UserResponse;
import co.istad.productapisimpledemo.repository.UserRepository;
import co.istad.productapisimpledemo.service.AuthService;
import co.istad.productapisimpledemo.service.UserService;
import jakarta.validation.Valid;
import jakarta.ws.rs.POST;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/test")
@Slf4j
public class TestController {
    private final UserService userService;

    // for testing the update profile feature
    private final AuthService authService;

    // Note : should use
    @PostMapping("/forgot-password/{email}")
    public String forgotPassword(@PathVariable  String email){
        authService.forgotPassword(email);
        return "Reset Password link has successfully send to associate account !";
    }
    @GetMapping("/profile")
    public UserResponse getProfile(@AuthenticationPrincipal Jwt jwt, @RequestBody UserUpdateRequest request) {
        String keycloakId = (String) jwt.getSubject();

        // calling to service
        log.info("Profile keycloakId: {}", keycloakId);
        return authService.updateUser(keycloakId, request);

    }

    @PutMapping("/profile")
    public UserResponse updateProfile(@AuthenticationPrincipal Jwt jwt,
                                   @RequestBody UserUpdateRequest request){
        String keycloakId = (String) jwt.getSubject();

        // calling to service
        return authService.updateUser(keycloakId, request);
    }
    // only customer user can access this !
    //@PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_CUSTOMER')")
    @GetMapping("/customer")
    public String customer() {
        return "Hello! I am the customer ";
    }

    // role -> authorities
    // CUSTOMER -> product:create, product:view , product:delete
    // only seller user can access this
    @PreAuthorize("hasRole('ROLE_SELLER')")
    @GetMapping("/seller")
    public String seller() {
        return "Hello! I am the seller ";
    }
}
