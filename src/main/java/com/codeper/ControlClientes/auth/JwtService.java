package com.codeper.ControlClientes.auth;

import java.util.Date;
import java.util.Map;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.codeper.ControlClientes.entity.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service 
public class JwtService {
    //acceder a las propiedades que declaramos en el application
    @Value ("${application.jwt.secret-key}")
    private String secretKey;

    @Value ("${application.jwt.access-token-expiration}")
    private Long jwtExpiration;

    @Value ("${application.jwt.refresh-token-expiration}")
    private Long refreshExpiration;

    //2-Verificar que el usuario del token es el usuario registrado en el sitema 
    public String extractUsername(String token){
        return Jwts.parser()
                    .verifyWith(getSignKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
    }

    //3-Generar token como nuestro Refresj token 
    public String buildToken(Usuario usuario,Long expiration){
            return Jwts.builder()
                    .subject(usuario.getPersona().getGmail())
                    .claims(Map.of("uid","_"+usuario.getPersona().getNombre()))
                    .issuedAt(new Date(System.currentTimeMillis()))
                    .expiration(new Date(System.currentTimeMillis()+expiration))
                    .signWith(getSignKey())
                    .compact();
    }

    //3.1-generacion de acces token 
    public  String generateAccessToken(Usuario usuario){
        return  buildToken(usuario, jwtExpiration);
    }
    //3.2-generacion de refresh token 
    public String generateRefreshToken(Usuario usuario){
        return  buildToken(usuario, refreshExpiration);
    }

    //4-validacion del token 
    public boolean isTokenExpired(String token){
        Date expiration =Jwts.parser()
            .verifyWith(getSignKey())
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getExpiration();
        return expiration.before(new Date());
    }
    //5- validar si coresponde al asuario
    public boolean isTokenValid(String token, Usuario usuario){
        String username=extractUsername(token);
        return (username.equals(((usuario.getPersona().getGmail())))) && !isTokenExpired(token);
    }
    //1-definir el manejo de nuestro secretKey
    private SecretKey  getSignKey(){
        byte[] keyBites= Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBites);
    }

}
