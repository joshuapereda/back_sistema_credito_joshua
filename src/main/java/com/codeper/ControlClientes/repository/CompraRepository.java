package com.codeper.ControlClientes.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.codeper.ControlClientes.entity.Compra;
import com.codeper.ControlClientes.entity.EstadoCompra;


public interface CompraRepository extends JpaRepository<Compra ,Integer>,JpaSpecificationExecutor<Compra> {
     boolean existsByTipoCompraId(Integer tipoCompraId);
     boolean existsByClienteId(Integer clienteId);

     // DASHBOARD
     // Cuenta las compras de un estado (Spring arma la consulta por el nombre)
     long countByEstado(EstadoCompra estado);

     // Lo que deben todos los clientes (COALESCE: 0 si no hay compras)
     @Query("SELECT COALESCE(SUM(c.saldo), 0) FROM Compra c")
     BigDecimal sumarSaldoPorCobrar();

     // Total vendido entre dos fechas (hasta no se incluye)
     @Query("SELECT COALESCE(SUM(c.total), 0) FROM Compra c WHERE c.fechaHora >= :desde AND c.fechaHora < :hasta")
     BigDecimal sumarVentasEntre(@Param("desde") LocalDateTime desde, @Param("hasta") LocalDateTime hasta);
}
