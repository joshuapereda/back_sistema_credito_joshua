package com.codeper.ControlClientes.service;

import java.util.List;

import com.codeper.ControlClientes.common.dto.PageableRequest;
import com.codeper.ControlClientes.common.dto.PageableResponse;
import com.codeper.ControlClientes.dto.CompraDto;
import com.codeper.ControlClientes.dto.CompraFilterCriteria;
import com.codeper.ControlClientes.dto.CreateCompraDto;

public interface ICompraService {
    List<CompraDto> listarCompra();
    CompraDto listarCompraPorId(Integer id);
    CompraDto crearCompra(CreateCompraDto dto);
    CompraDto actualizarCompra(Integer id, CreateCompraDto compraActualizada);
    void eliminarCompra(Integer id);
    //paginacion
    PageableResponse<CompraDto>getCompraPagination(PageableRequest<CompraFilterCriteria>pageableRequest);

}
