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
@Table (name ="tipo_compra")
public class TipoCompra {

    @Id 
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private Integer id;

    @Column (nullable = false, length = 50)
    private String nombre;

    @Column (length = 200)
    private String descripcion;

    // true = crédito (queda saldo pendiente), false = contado (se paga al momento)
    // Sin nullable = false para que Hibernate pueda agregar la columna a los registros existentes
    @Column
    private Boolean esCredito;

}
