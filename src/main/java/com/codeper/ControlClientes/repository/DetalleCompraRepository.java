package com.codeper.ControlClientes.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codeper.ControlClientes.entity.DetalleCompra;

public interface DetalleCompraRepository extends JpaRepository<DetalleCompra, Integer> {

    boolean existsByProductoId(Integer productoId);
}
