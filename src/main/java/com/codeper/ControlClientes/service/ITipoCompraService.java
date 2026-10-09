package com.codeper.ControlClientes.service;

import java.util.List;
import com.codeper.ControlClientes.dto.CreateTipoCompraDto;
import com.codeper.ControlClientes.dto.TipoCompraDto;

public interface ITipoCompraService {
    List<TipoCompraDto> listarTipoCompra();
    TipoCompraDto listarTipoCompraPorId(Integer id);
    TipoCompraDto crearTipoCompra(CreateTipoCompraDto createTipoCompraDto);
    TipoCompraDto editarTipoCompra(Integer id, CreateTipoCompraDto actualizarTipoCompra);
    void eliminarTipoCompra(Integer id);

}
