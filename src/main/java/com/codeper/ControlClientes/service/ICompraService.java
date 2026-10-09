package com.codeper.ControlClientes.service;

import java.util.List;
import com.codeper.ControlClientes.dto.CompraDto;
import com.codeper.ControlClientes.dto.CreateCompraDto;

public interface ICompraService {
    List<CompraDto> listarCompra();
    CompraDto listarCompraPorId(Integer id);
    CompraDto crearCompra(CreateCompraDto crearCompra);
    CompraDto actualizarCompra(Integer id, CreateCompraDto compraActualizada);
    void eliminarCompra(Integer id);


}
