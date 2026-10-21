package com.codeper.ControlClientes.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

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

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal saldo;

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

    //es una relacionm con bidireccional, una compra tiene muchos detalles, y un detalle pertenece a una compra
    //aca no estamos indicando qeu cree un campo ams en la base de datos , si no con le mappedBy le decimos que 
    //el campo compra de la clase DetalleCompra es el que hace referencia a esta clase
    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<DetalleCompra> detalles = new ArrayList<>();

    //ESTADO DE LA COMPRA
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoCompra estado;




}
