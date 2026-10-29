package com.codeper.ControlClientes.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.codeper.ControlClientes.auth.JwtAuthenticationFilter;

@Configuration 
@EnableWebSecurity 
@EnableMethodSecurity 
public class SecurityConfig {
    @Autowired 
    private AuthenticationProvider authemticationProvider;

    //Inyectamos de la otra forma
    @Autowired 
    private  JwtAuthenticationFilter jwtAuthenticationFilter;
    @Bean 
    public SecurityFilterChain securityFilterChain(HttpSecurity http){
        try {   
            http
                .csrf(csrf ->csrf.disable())
                .authorizeHttpRequests(auth -> auth
                    //Dar seguridad a las peticiones
                    //permitAll - permite pones que es una ruta avierta cualquiera púede ingresar
                    .requestMatchers("/api/auth/**", "/error").permitAll()
                    .requestMatchers("/productos").hasAnyAuthority("VER_PRODUCTOS","ROLE_ENCARGADO_ALMACEN")
                    .requestMatchers("/ventas").hasAnyAuthority("VER_VENTAS","ROLE_VENDEDOR")
                    //con esto va indicar que el resto de las ruta no va pedir  ni roles ni permisos 
                    //solo va pedir token osea que esten autenticados
                    .anyRequest().authenticated()
                )
                    .cors(Customizer.withDefaults())
                    .sessionManagement(sesseion -> sesseion.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .authenticationProvider(authemticationProvider)
                    .addFilterBefore(jwtAuthenticationFilter,UsernamePasswordAuthenticationFilter.class);
        return  http.build();
            
        } catch (Exception e) {
            throw new RuntimeException("Error al configurar el Filter Chain");
        }
    }

}
