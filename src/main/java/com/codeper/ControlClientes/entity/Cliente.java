package com.codeper.ControlClientes.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
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
@Table (name ="cliente")
public class Cliente {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 150)
    private String docIdentidad;

    //Relacion 1 a 1 (Persona) 
    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "persona_id", nullable = false)
    private Persona persona;


    //BIDIRECCIONALIDAD 
    //Permite tener los ids en ambas tablas sin necesidad de relacionarlo 
    @OneToMany(mappedBy = "cliente")
    private List<Compra> compras = new ArrayList<>();


}
