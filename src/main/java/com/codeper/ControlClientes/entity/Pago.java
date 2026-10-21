package com.codeper.ControlClientes.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
@Builder 
@Entity 
@Table (name ="pago")
public class Pago {

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column (nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @Column (nullable = false, precision = 10, scale = 2)
    private BigDecimal saldoRestante;

    @Column (nullable = false)
    private LocalDateTime fechaHora;
    
    @ManyToOne 
    @JoinColumn (name = "compra_id", nullable = false)
    private Compra compra;

}
