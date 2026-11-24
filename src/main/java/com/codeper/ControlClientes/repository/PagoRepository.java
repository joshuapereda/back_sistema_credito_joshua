package com.codeper.ControlClientes.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.codeper.ControlClientes.entity.Pago;

public interface PagoRepository extends JpaRepository<Pago, Integer>,JpaSpecificationExecutor<Pago> {
    // Suma de todos los pagos de una compra (0 si no tiene pagos)
    @Query("SELECT COALESCE(SUM(p.monto), 0) FROM Pago p WHERE p.compra.id = :compraId")
    BigDecimal sumarPagosPorCompra(@Param("compraId") Integer compraId);

    List<Pago> findByCompraIdOrderByIdAsc(Integer compraId);

    // DASHBOARD: pagos recibidos entre dos fechas (hasta no se incluye)
    @Query("SELECT COALESCE(SUM(p.monto), 0) FROM Pago p WHERE p.fechaHora >= :desde AND p.fechaHora < :hasta")
    BigDecimal sumarPagosEntre(@Param("desde") LocalDateTime desde, @Param("hasta") LocalDateTime hasta);
}
