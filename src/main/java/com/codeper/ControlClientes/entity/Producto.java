package com.codeper.ControlClientes.entity;

import java.math.BigDecimal;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor
@NoArgsConstructor 
@Entity 
@Builder 
@Table (name="pproducto")
public class Producto {
    
    @Id 
    @GeneratedValue (strategy=GenerationType.IDENTITY)
    private Integer id;

    @Column (nullable = false, length = 50)
    private String nombre;


    @Column (nullable = false, length = 200)
    private String foto;
    @Column (nullable = false)
    private BigDecimal precioVenta;

    @Column (nullable = false)
    private BigDecimal precioCompra;
    
    //Relacion N a 1 (Categoria)
    @ManyToOne 
    @JoinColumn (name = "categoria_id", nullable = false)
    private Categoria categoria;
    

}
