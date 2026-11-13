package com.codeper.ControlClientes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.codeper.ControlClientes.entity.Producto;
 //JpaSpecificationExecutor para trabajar con especificaciones en los filtros 
public interface ProductoRepository extends JpaRepository<Producto, Integer>,JpaSpecificationExecutor<Producto> {

}
