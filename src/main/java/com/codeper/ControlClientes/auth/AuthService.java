package com.codeper.ControlClientes.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.codeper.ControlClientes.entity.Usuario;
import com.codeper.ControlClientes.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;


@Service 
@RequiredArgsConstructor  
public class AuthService {
    @Value ("${application.jwt.access-token-expiration}")
    private Long expiration;
    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;
    private final AuthenticationManager uthenticationManager;

    public AuthResponse authenticate(AuthRequest authRequest){
        try {
            uthenticationManager.authenticate(
                  new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
            Usuario usuario=usuarioRepository.findByPersonaGmail((authRequest.getUsername()))
            .orElseThrow(()-> new RuntimeException("Usuario no encontrado"));

            String accessToken= jwtService.generateAccessToken(usuario);
            String refreshToken=jwtService.generateRefreshToken(usuario);
            return AuthResponse.builder()
                    .accesToken(accessToken)
                    .refreshToken(refreshToken)
                    .identifier(usuario.getId())
                    .expiration(expiration)
                    .build();
        } catch (Exception e) {
            throw e;
        }
    }

    public AuthResponse refreshToken(String authentication){
        try {
            if (authentication == null || !authentication.startsWith("Bearer ")) {
                throw new RuntimeException("Token invalido");
            }
            String token= authentication.substring(7);
            String username= jwtService.extractUsername(token);
            Usuario usuario= usuarioRepository.findByPersonaGmail(username)
                .orElseThrow(()->new RuntimeException("Usuario no encontrado"));
            String accessToken= jwtService.generateAccessToken(usuario);
            String refreshToken=jwtService.generateRefreshToken(usuario);
            return AuthResponse.builder()
                    .accesToken(accessToken)
                    .refreshToken(refreshToken)
                    .identifier(usuario.getId())
                    .expiration(expiration)
                    .build();
        } catch (Exception e) {
            throw e;
        }
    }
}
