package com.codeper.ControlClientes.entity;

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
@Table (name ="compra")
public class Compra {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column (nullable = false)
    private LocalDateTime fechaHora;

    @Column (nullable = false)
    private Double total;

     @Column (nullable = false)
    private Double saldo;
    //RELACIONES

    //TIPO DE COMPRA
    @ManyToOne 
    @JoinColumn (name = "tipo_compra_id", nullable = false)
    private TipoCompra tipoCompra;
    //CLIENTE
    @ManyToOne 
    @JoinColumn (name = "cliente_id", nullable = false)
    private Cliente cliente;
    //USUARIO
    @ManyToOne 
    @JoinColumn (name = "usuario_id", nullable = false)
    private Usuario usuario;




}
