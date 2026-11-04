package com.codeper.ControlClientes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.codeper.ControlClientes.entity.Compra;


public interface CompraRepository extends JpaRepository<Compra ,Integer>,JpaSpecificationExecutor<Compra> {

}
