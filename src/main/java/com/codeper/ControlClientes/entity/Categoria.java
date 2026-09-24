package com.codeper.ControlClientes.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table (name ="categoria")
public class Categoria {

    @Id 
    @GeneratedValue (strategy=GenerationType.IDENTITY)
    private Integer id;

    @Column (nullable = false, length = 50)
    private String nombre;

    @Column (length = 200)
    private String descripcion;

}
