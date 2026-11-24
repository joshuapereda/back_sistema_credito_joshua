package com.codeper.ControlClientes.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.codeper.ControlClientes.dto.ProductoVendidoDto;
import com.codeper.ControlClientes.entity.DetalleCompra;

public interface DetalleCompraRepository extends JpaRepository<DetalleCompra, Integer> {

    boolean existsByProductoId(Integer productoId);

    // DASHBOARD: ranking por unidades vendidas (Pageable limita cuántos trae)
    @Query("""
        SELECT new com.codeper.ControlClientes.dto.ProductoVendidoDto(p.id, p.nombre, SUM(d.cantidad))
        FROM DetalleCompra d JOIN d.producto p
        GROUP BY p.id, p.nombre
        ORDER BY SUM(d.cantidad) DESC, p.id ASC
        """)
    List<ProductoVendidoDto> listarMasVendidos(Pageable pageable);
}
