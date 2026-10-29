package com.codeper.ControlClientes.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.codeper.ControlClientes.dto.CreateTipoCompraDto;
import com.codeper.ControlClientes.dto.TipoCompraDto;
import com.codeper.ControlClientes.entity.TipoCompra;
import com.codeper.ControlClientes.exception.ResourNotFoundException;
import com.codeper.ControlClientes.repository.TipoCompraRepository;

@Service 
public class TipoCompraService implements ITipoCompraService {

    @Autowired 
    private TipoCompraRepository tipoCompraRepository;
    @Override
    public List<TipoCompraDto> listarTipoCompra() {
        return tipoCompraRepository.findAll().stream()
            .map(TipoCompraDto::fromEntityTipoCompra)
            .toList();
    }

    @Override
    public TipoCompraDto listarTipoCompraPorId(Integer id) {
        try{
             return  tipoCompraRepository.findById(id)
                .map(TipoCompraDto:: fromEntityTipoCompra)
                .orElseThrow(() -> new ResourNotFoundException("Tipo de Compra ",id));
        }catch(ResourNotFoundException e){
            throw e;
        }
    }

    @Override
    public TipoCompraDto crearTipoCompra(CreateTipoCompraDto createTipoCompraDto) {
        TipoCompra tCompra=CreateTipoCompraDto.toEntityTipoCompra(createTipoCompraDto);
        tCompra=tipoCompraRepository.save(tCompra);
        return  TipoCompraDto.fromEntityTipoCompra(tCompra);
    }

    @Override
    public TipoCompraDto editarTipoCompra(Integer id, CreateTipoCompraDto actualizarTipoCompra) {
       //VALIDAR SI EXISTE LA PERSONA 
        TipoCompra tcompraRetrieved = tipoCompraRepository.findById(id)
                .orElseThrow(() -> new ResourNotFoundException("Tipo de compra" , id));
        
        tcompraRetrieved.setNombre(actualizarTipoCompra.getNombre());
        tcompraRetrieved.setDescripcion(actualizarTipoCompra.getDescripcion());
        tcompraRetrieved.setEsCredito(actualizarTipoCompra.getEsCredito());
        tcompraRetrieved=tipoCompraRepository.save(tcompraRetrieved);

        return TipoCompraDto.fromEntityTipoCompra(tcompraRetrieved);
    }

    @Override
    public void eliminarTipoCompra(Integer id) {
         if (!tipoCompraRepository.existsById(id)) {
        throw new ResourNotFoundException("Permiso" , id);
        }
        tipoCompraRepository.deleteById(id);
    }
}
