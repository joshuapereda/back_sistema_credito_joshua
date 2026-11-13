package com.codeper.ControlClientes.service;

import java.util.List;

import com.codeper.ControlClientes.common.dto.PageableRequest;
import com.codeper.ControlClientes.common.dto.PageableResponse;
import com.codeper.ControlClientes.dto.CreateTipoCompraDto;
import com.codeper.ControlClientes.dto.TipoCompraDto;
import com.codeper.ControlClientes.dto.TipoCompraFilterCriteria;

public interface ITipoCompraService {
    List<TipoCompraDto> listarTipoCompra();
    TipoCompraDto listarTipoCompraPorId(Integer id);
    TipoCompraDto crearTipoCompra(CreateTipoCompraDto createTipoCompraDto);
    TipoCompraDto editarTipoCompra(Integer id, CreateTipoCompraDto actualizarTipoCompra);
    void eliminarTipoCompra(Integer id);
     //paginacion
    PageableResponse<TipoCompraDto>getTipoCompraPagination(PageableRequest<TipoCompraFilterCriteria>pageableRequest);

}
