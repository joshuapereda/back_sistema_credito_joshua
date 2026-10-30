package com.codeper.ControlClientes.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.codeper.ControlClientes.entity.Cliente;
import com.codeper.ControlClientes.entity.Permiso;
import com.codeper.ControlClientes.entity.Persona;
import com.codeper.ControlClientes.entity.Rol;
import com.codeper.ControlClientes.entity.Usuario;
import com.codeper.ControlClientes.repository.ClienteRepository;
import com.codeper.ControlClientes.repository.PermisoRepository;
import com.codeper.ControlClientes.repository.PersonaRepository;
import com.codeper.ControlClientes.repository.RolRepository;
import com.codeper.ControlClientes.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Component 
@RequiredArgsConstructor 
public class DataSeeding  implements ApplicationRunner {
    //Inyeccion de dependencias
    private final UsuarioRepository usuarioRepository;
    private final PersonaRepository personaRepository;
    private final ClienteRepository clienteRepository;
    private final RolRepository rolRepository;
    private final PermisoRepository permisoRepository;
    private final PasswordEncoder passwordEncoder;

    
    public void run(ApplicationArguments args) throws Exception{
        List<String>acciones=List.of("crear","actualizar","listar","eliminar");
        List<String>recursos=List.of("USUARIOS" ,"ROLES", "RECURSOS", "CLIENTES" , "PRODUCTOS" ,"CATEGORIAS", 
        "COMPRAS", "VENTAS","PAGOS","DEUDAS","REPORTES" ,"CAJA","PROVEEDORES", "PERSONAS","PERMISOS", "DETALLE_COMPRAS",
        "DETALLE_VENTAS", "MOVIMIENTOS_CAJA","CUENTAS_POR_COBRAR" ,"CONFIGURACION");

        List<Permiso>permisos=new ArrayList<>();

        if(permisoRepository.count()==0){
            for(String recurso:recursos){
                for(String accion:acciones){
                    Permiso permiso=Permiso.builder()
                    .nombre(accion.toUpperCase()+"_"+recurso.toUpperCase())
                    .recurso(recurso)
                    .accion(accion)
                    .build();
                    permisos.add(permisoRepository.save(permiso));
                }
            }
        }
        List<Rol>roles=new ArrayList<>();

        if(rolRepository.count()==0){
                Rol adminRol=Rol.builder()
                    .nombre("Admin")
                    .descripcion("Administrador general")
                    .build();
                    roles.add(rolRepository.save(adminRol));
             
        }
        if(personaRepository.count()==0 || clienteRepository.count()==0||usuarioRepository.count()==0){
        Persona persona=Persona.builder()
                .nombre("ADMIN")
                .gmail("Admin2026@gmail.com")
                .telefono("991723222")
                .direccion("lima")
                .build();
                personaRepository.save(persona);
                
        Cliente cliente=Cliente.builder()
                .docIdentidad("77435114")
                .persona(persona)
                .build();
                clienteRepository.save(cliente);
        
       if (usuarioRepository.count() == 0) {
                Usuario usuarioAdmin = Usuario.builder()
                .username("Admin2026")
                .passwprd(passwordEncoder.encode("Admin2026"))
                .persona(persona)  
                .build();
            usuarioRepository.save(usuarioAdmin);
        }
    }
}
}
