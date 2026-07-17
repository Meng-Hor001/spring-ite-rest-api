package co.istad.productapisimpledemo.config;


// Write the code to determine/config the security of spring
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {
    @Value("${keycloak.client-id}")
    private String clientId;
    // SecurityFilterChain
    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        // 1. disable csrf  -> for stateless application
        http.csrf(AbstractHttpConfigurer::disable);
        // 2. disable form login
        http.formLogin(AbstractHttpConfigurer::disable);
        // 3. make it become stateless for REST constraint
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // to be able to work with keycloak authorization server
        http.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

        //http.httpBasic(Customizer.withDefaults());
        // endpoint to be allowed or protected
        http.authorizeHttpRequests(
                request->
           request
                   // enable scalar
                   .requestMatchers("/api/v1/admin","/api/v1/admin/**").hasRole("ADMIN")
                   .requestMatchers("/api/v1/auth/register", "/api/v1/test/forgotpassword/**").permitAll()
                   .requestMatchers("/scalar/**", "/v3/api-docs/**").permitAll()
                   .requestMatchers("/api/v1/files/**","/files/**").permitAll()
                   .requestMatchers(HttpMethod.GET, "/api/v1/categories/**").permitAll()
                   // file uploads

            .requestMatchers(HttpMethod.GET, "/api/v1/products/**","/api/v1/tags/**").permitAll()
              // login successfully first to access it
                .anyRequest().authenticated()
        );
        return http.build();
    }


    // USAGE: convert jwt to security object in spring
    // granted-authority: role -> what can authenticated user can do

    @Bean
    @SuppressWarnings("unchecked")
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        // 1. created a converter object
        Converter<Jwt, Collection<GrantedAuthority>> converter = jwt -> {
            // 1. get  List of granted-authories
            // get claim from resource_access
//            "resource_access": {
//                "spring-boot-app": {
//                    "roles": [
//                    "CUSTOMER"
//      ]
//                }
            Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
            if(resourceAccess == null) { return Collections.emptySet(); }
            var clientAccess = (Map<String, Object>) resourceAccess.get(clientId); // get role from spring-boot-app = client id
            if(clientAccess == null) { return Collections.emptySet(); }
            // get role object
            Object rolesObj = clientAccess.get("roles");

            // check the type of the role
            // if it's a collection , we can use it with stream
            if (!(rolesObj instanceof Collection<?> roles)) {
                return Collections.emptySet();
            }
            // CUSTOMER, SELLER -> ROLE_CUSTOMER, ROLE_SELLER
            return roles.stream()
                    .map(Object::toString)
                    .map(role -> {
                        if (role.contains(":")) {
                            return new SimpleGrantedAuthority(role); }
                    return new SimpleGrantedAuthority("ROLE_" + role);})
                    .collect(Collectors.toSet());
        };

        var jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(converter );

        return jwtAuthenticationConverter;
    }

    // TODO: configure cors for allowing specific client to access

}
