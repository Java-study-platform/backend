package com.study.core.config;

import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.JwkProviderBuilder;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.RSAKeyProvider;
import com.study.common.DTO.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class WebSocketInterceptor implements ChannelInterceptor {
    private final String issuer;

    private final JwkProvider jwkProvider;

    public WebSocketInterceptor(
            @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}") String jwk,
            @Value("${spring.security.oauth2.client.provider.keycloak.issuer-uri}") String issuer
    ) throws MalformedURLException {
        this.issuer = issuer;
        jwkProvider = new JwkProviderBuilder(new URL(jwk)).build();
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        log.info("Headers: {}", accessor);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authHeader = accessor.getFirstNativeHeader("Authorization");
            String jwt;

            if (authHeader != null && authHeader.equals("Bearer null")) {
                authHeader = null;
            }

            try {
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    jwt = authHeader.substring(7);
                    DecodedJWT decodedJWT = JWT.decode(jwt);

                    RSAKeyProvider keyProvider = new RSAKeyProvider() {
                        @Override
                        public RSAPublicKey getPublicKeyById(String keyId) {
                            try {
                                return (RSAPublicKey) jwkProvider.get(keyId).getPublicKey();
                            } catch (JwkException e) {
                                throw new RuntimeException(e);
                            }
                        }

                        @Override
                        public RSAPrivateKey getPrivateKey() {
                            return null;
                        }

                        @Override
                        public String getPrivateKeyId() {
                            return null;
                        }
                    };

                    Algorithm algorithm = Algorithm.RSA256(keyProvider);
                    JWTVerifier verifier = JWT.require(algorithm)
                            .withIssuer(issuer)
                            .build();
                    verifier.verify(decodedJWT);

                    UserDto userDto = new UserDto();
                    userDto.setKeyCloakId(decodedJWT.getSubject());
                    userDto.setEmail(decodedJWT.getClaim("email").asString());
                    userDto.setRoles(decodedJWT.getClaim("spring_sec_roles").asList(String.class)
                            .stream()
                            .filter(role -> role.startsWith("ROLE_"))
                            .collect(Collectors.toList()));
                    userDto.setUsername(decodedJWT.getClaim("preferred_username").asString());
                    userDto.setFirstName(decodedJWT.getClaim("given_name").asString());
                    userDto.setLastName(decodedJWT.getClaim("family_name").asString());

                    List<SimpleGrantedAuthority> authorities = userDto.getRoles().stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());

                    Authentication auth = new UsernamePasswordAuthenticationToken(userDto, null, authorities);
                    accessor.setUser(auth);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            } catch (JWTVerificationException e) {
                System.out.println("JWT Verification Exception: " + e.getMessage());
            } catch (Exception e) {
                throw new RuntimeException("Internal server exception");
            }
        }
        return message;
    }
}
