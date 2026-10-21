package com.codeper.ControlClientes.service;

import java.util.List;
import com.codeper.ControlClientes.dto.CreatePagoDto;
import com.codeper.ControlClientes.dto.PagoDto;

public interface IPagoService {

    List<PagoDto> listarPago();
    PagoDto obtenerPagoPorId(Integer id);
    PagoDto crearPago(CreatePagoDto createPagoDto);
    PagoDto actualizarPago(Integer id, PagoDto actualizarPagoDto);
    void eliminarPago(Integer id);
    

}
