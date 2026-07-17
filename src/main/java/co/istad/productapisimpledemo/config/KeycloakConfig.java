package co.istad.productapisimpledemo.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
// @ConfigurationProperties(prefix = "keycloak")
public class KeycloakConfig {
    @Value("${keycloak.server-url}")
    private String serverUrl;
    @Value("${keycloak.client-id}")
    private String clientId;
    @Value("${keycloak.client-secret}")
    private String clientSecret;
    @Value("${keycloak.realm}")
    private String realm;

    // this object, will act as a client(keycloak) to manage or create user
    // inside keycloak but from spring app
    @Bean
    public Keycloak getInstance(){
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .realm(realm)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .build();
    }
}
