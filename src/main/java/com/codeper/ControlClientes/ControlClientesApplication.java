package com.codeper.ControlClientes;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.codeper.ControlClientes.repository.UsuarioRepository;

@SpringBootApplication(scanBasePackages = {"com.codeper.ControlClientes", "validator"})
public class ControlClientesApplication {
    public static void main(String[] args) {
        SpringApplication.run(ControlClientesApplication.class, args);
    }
     @Bean
    CommandLineRunner encriptarPasswords(UsuarioRepository repo, PasswordEncoder encoder) {
        return args -> repo.findAll().forEach(u -> {
            String p = u.getPasswprd();
            if (p != null && !p.startsWith("$2")) {
                u.setPasswprd(encoder.encode(p));
                repo.save(u);
            }
        });
    }
}
