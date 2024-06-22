//package com.study.core.handlers;
//
//import com.auth0.jwk.Jwk;
//import com.auth0.jwk.JwkException;
//import com.auth0.jwk.JwkProvider;
//import com.auth0.jwk.JwkProviderBuilder;
//import com.auth0.jwt.JWT;
//import com.auth0.jwt.algorithms.Algorithm;
//import com.auth0.jwt.exceptions.JWTVerificationException;
//import com.auth0.jwt.interfaces.DecodedJWT;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.server.ServerHttpRequest;
//import org.springframework.http.server.ServerHttpResponse;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.WebSocketHandler;
//import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;
//
//import java.net.URL;
//import java.security.interfaces.RSAPublicKey;
//import java.util.Map;
//
//@Component
//@Slf4j
//public class CustomHandShakeHandler extends HttpSessionHandshakeInterceptor {
//    private final String jwkUri;
//    private final String issuer;
//
//    public CustomHandShakeHandler(@Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}") String jwkUri,
//                                  @Value("${spring.security.oauth2.client.provider.keycloak.issuer-uri}") String issuer) {
//        this.jwkUri = jwkUri;
//        this.issuer = issuer;
//    }
//
//    @Override
//    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
//        String authHeader = request.getHeaders().getFirst("Authorization");
//
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            String token = authHeader.substring(7);
//            log.info("Handler принял токен: " + token);
//            DecodedJWT decodedJWT = verifyToken(token);
//            log.info("Токен верифицирован");
//
//            attributes.put("userId", decodedJWT.getSubject());
//            attributes.put("username", decodedJWT.getClaim("username").asString());
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    private DecodedJWT verifyToken(String token) throws Exception {
//        JwkProvider jwkProvider = new JwkProviderBuilder(new URL(jwkUri)).build();
//        DecodedJWT decodedJWT;
//
//        try {
//            decodedJWT = JWT.decode(token);
//            Jwk jwk = jwkProvider.get(decodedJWT.getKeyId());
//            Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(), null);
//
//            JWT.require(algorithm)
//                    .withIssuer(issuer)
//                    .build()
//                    .verify(decodedJWT);
//        } catch (JWTVerificationException | JwkException e) {
//            throw new RuntimeException("Failed to verify JWT token", e);
//        }
//
//        return decodedJWT;
//    }
//}
