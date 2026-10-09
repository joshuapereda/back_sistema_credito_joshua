package com.codeper.ControlClientes.entity;

import java.util.List;
import jakarta.persistence.Entity;
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
@Table (name ="detalleCompra")
public class DetalleCompra {

    private Integer id;
    private Integer cantidad;
    private  double subtotal;

    @ManyToMany(mappedBy = "roles")
    private List<Producto> producto;

    @OneToOne (mappedBy = "roles")
    private Compra compra;

}
