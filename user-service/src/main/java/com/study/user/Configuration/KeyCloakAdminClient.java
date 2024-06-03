package com.study.user.Configuration;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.authorization.client.AuthzClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class KeyCloakAdminClient {
    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.admin-client-secret}")
    private String clientSecret;

    @Value("${keycloak.server-url}")
    private String serverUrl;

    @Value("${keycloak.admin-client-id}")
    private String clientId;

    @Bean
    public Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .clientSecret(clientSecret)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId(clientId)
                .build();
    }

    @Bean
    public AuthzClient keycloakAuthzClient() {
        return AuthzClient.create(new org.keycloak.authorization.client.Configuration(
                serverUrl,
                realm,
                clientId,
                Map.of("secret", clientSecret,"provider", "secret"),
                null
        ));
    }
}
