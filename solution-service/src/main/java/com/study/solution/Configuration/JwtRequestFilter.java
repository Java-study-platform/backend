package com.study.solution.Configuration;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.JwkProviderBuilder;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.study.common.Exceptions.InternalServerException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.interfaces.RSAPublicKey;
import java.util.List;

import static com.study.common.Constants.Consts.USERNAME_CLAIM;

@Component
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

    private final String issuer;

    private final JwkProvider jwkProvider;

    public JwtRequestFilter(
            @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}") String jwk,
            @Value("${spring.security.oauth2.client.provider.keycloak.issuer-uri}") String issuer
    ) throws MalformedURLException {
        this.issuer = issuer;
        jwkProvider = new JwkProviderBuilder(new URL(jwk)).build();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String jwt;

        if (authHeader != null && authHeader.equals("Bearer null")) {
            authHeader = null;
        }

        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                jwt = authHeader.substring(7);

                DecodedJWT decodedJWT = JWT.decode(jwt);

                Jwk jwk = jwkProvider.get(decodedJWT.getKeyId());

                Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(), null);

                JWTVerifier verifier = JWT.require(algorithm)
                        .withIssuer(issuer)
                        .build();
                verifier.verify(decodedJWT);

                List<String> roles = decodedJWT.getClaim("spring_sec_roles").asList(String.class)
                        .stream()
                        .filter(role -> role.startsWith("ROLE_"))
                        .toList();

                List<SimpleGrantedAuthority> authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList();

                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(decodedJWT.getClaim(USERNAME_CLAIM), null, authorities));
            }
        } catch (JWTVerificationException e) {
            log.info(e.getMessage());
        } catch (Exception e) {
            throw new InternalServerException();
        }

        if (request.getRequestURL().toString().contains("http://localhost:8084/ws")) {
            response.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, Accept");
            response.setHeader("Authorization", request.getHeader("Authorization"));
        }

        if (request.getMethod().equals("OPTIONS")) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
