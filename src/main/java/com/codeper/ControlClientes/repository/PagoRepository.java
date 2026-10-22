package com.codeper.ControlClientes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codeper.ControlClientes.entity.Pago;

public interface PagoRepository extends JpaRepository<Pago, Integer> {
   List<Pago> findByCompraIdOrderByIdAsc(Integer compraId);
}
