package com.codeper.ControlClientes.auth;

import java.io.IOException;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.codeper.ControlClientes.entity.Usuario;
import com.codeper.ControlClientes.repository.UsuarioRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component 
@RequiredArgsConstructor 
public class JwtAuthenticationFilter extends OncePerRequestFilter {
     //------- CONTROLAR NUESTRAS VALIDACION -----------//
     //Otra froma de inyeccion de depedendencias
     private final JwtService jwtService;

     private final UserDetailsService userDetailsService;

     private final UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
     //Aqui se implementaria la logica para extraer el token JWT , validarlo y demas  
       if(request.getServletPath().contains("/login")){
            filterChain.doFilter(request, response);
            return;
       }
       String authHeader= request.getHeader(HttpHeaders.AUTHORIZATION);
       if(authHeader==null|| !authHeader.startsWith("Bearer")){
            filterChain.doFilter(request, response);
            return ;
       }
       String token=authHeader.substring(7);
       String username= jwtService.extractUsername(token);
       Authentication authentication=SecurityContextHolder.getContext().getAuthentication();

       if(username==null || authentication !=null){
               filterChain.doFilter(request, response);
               return ;
       }

       UserDetails userDetails=userDetailsService.loadUserByUsername(username);
       Usuario usuario=usuarioRepository.findByPersonaGmail(username)
       .orElseThrow(()-> new RuntimeException("Usuario no encontrado"));

       if(jwtService.isTokenValid(token, usuario)){
          UsernamePasswordAuthenticationToken jwtAuthToken= new UsernamePasswordAuthenticationToken(
               userDetails,
               null,
               userDetails.getAuthorities()
          );
          jwtAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(jwtAuthToken);
       }
       filterChain.doFilter(request, response);
     }
}
