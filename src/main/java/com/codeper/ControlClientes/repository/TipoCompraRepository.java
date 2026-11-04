package com.codeper.ControlClientes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.codeper.ControlClientes.entity.TipoCompra;

public interface TipoCompraRepository extends JpaRepository<TipoCompra,Integer>,JpaSpecificationExecutor<TipoCompra> {

}
