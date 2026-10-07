package com.codeper.ControlClientes.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
@Table (name ="tipo_compra")
public class TipoCompra {
    @Id 
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private Integer id;

    @Column (nullable = false, length = 50)
    private String nombre;

    @Column (length = 200)
    private String descripcion;


}
