package com.codeper.ControlClientes.entity;


import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor 
@NoArgsConstructor 
@Builder 
@Entity 
@Table (name ="usuario")
public class Usuario {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 50)
    private String username;

    @Column(nullable = false, length = 20)
    private String passwprd;

    //Relacion 1 a 1 (Persona) 
    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "persona_id", nullable = false)
    private Persona persona;

    //Relacion N a M (Rol)
    @ManyToMany 
    @JoinTable (
        //Name de la tabla y sus campos
        name = "usuario_rol",
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "rol_id")
    )
    private List<Rol> roles;
}
