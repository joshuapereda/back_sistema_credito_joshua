package com.codeper.ControlClientes.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codeper.ControlClientes.entity.Pago;

public interface PagoRepository extends JpaRepository<Pago, Integer> {

}
