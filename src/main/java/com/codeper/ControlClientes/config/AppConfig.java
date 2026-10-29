package com.codeper.ControlClientes.config;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.codeper.ControlClientes.entity.Usuario;
import com.codeper.ControlClientes.repository.UsuarioRepository;

@Configuration
public class AppConfig {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TransactionTemplate transactionTemplate;

    // CONFIGURACION DE CORS
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*");
            }
        };
    }

    // Relaciona los usuarios de la BD con UserDetailsService (el username es el gmail)
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> transactionTemplate.execute(status -> {
            Usuario usuario = usuarioRepository.findByPersonaGmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

            List<GrantedAuthority> authorities = usuario.getRoles().stream()
                    .flatMap(rol -> {
                        Stream.Builder<GrantedAuthority> builder = Stream.builder();
                        builder.add(new SimpleGrantedAuthority("ROLE_" + rol.getNombre()));
                        rol.getPermisos().stream()
                                .map(permiso -> new SimpleGrantedAuthority(permiso.getNombre()))
                                .forEach(builder::add);
                        return builder.build();
                    })
                    .collect(Collectors.toList());

            return User.builder()
                    .username(usuario.getPersona().getGmail())
                    .password(usuario.getPasswprd())
                    .authorities(authorities)
                    .build();
        });
    }

    // ENCRIPTADO DE CONTRASEÑA
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}