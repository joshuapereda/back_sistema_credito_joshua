package com.codeper.ControlClientes.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.codeper.ControlClientes.dto.CompraDto;
import com.codeper.ControlClientes.dto.CreateCompraDto;
import com.codeper.ControlClientes.entity.Cliente;
import com.codeper.ControlClientes.entity.Compra;
import com.codeper.ControlClientes.entity.TipoCompra;
import com.codeper.ControlClientes.entity.Usuario;
import com.codeper.ControlClientes.repository.ClienteRepository;
import com.codeper.ControlClientes.repository.CompraRepository;
import com.codeper.ControlClientes.repository.TipoCompraRepository;
import com.codeper.ControlClientes.repository.UsuarioRepository;

@Service 
public class CompraService implements ICompraService {

    @Autowired 
    private CompraRepository compraRepository;

    @Autowired 
    private UsuarioRepository usuarioRepository;

    @Autowired 
    private ClienteRepository clienteRepository;

    @Autowired
    private TipoCompraRepository tipoCompraRepository;


    @Override
    public List<CompraDto> listarCompra() {
        return compraRepository.findAll().stream().map(compra->{
            return  CompraDto.fromEntityCompra(compra);
        }).toList();
    }

    @Override
    public CompraDto listarCompraPorId(Integer id) {
      return  compraRepository.findById(id).map(compra->{
        return  CompraDto.fromEntityCompra(compra);
      }).orElseThrow(()-> new RuntimeException("Cliente no encontrado con el ID: "+ id));
    }

    @Override
    public CompraDto crearCompra(CreateCompraDto crearCompra) {
       //VALIDAR SI EXISTE 
        Usuario usuario = usuarioRepository.findById(crearCompra.getUsuarioId())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrada"));

        Cliente cliente =clienteRepository.findById(crearCompra.getClienteId())
            .orElseThrow(() -> new RuntimeException("Cliente no encontrada"));

        TipoCompra tCompra = tipoCompraRepository.findById(crearCompra.getTipoCompraId())
            .orElseThrow(() -> new RuntimeException("Tipo de compra  no encontrada"));

        Compra compra= new Compra();
        compra.setCliente(cliente);
        compra.setUsuario(usuario);
        compra.setTipoCompra(tCompra);
        compra.setFechaHora(LocalDateTime.now());
        compra.setTotal(crearCompra.getTotal());
       
        if ("CONTADO".equalsIgnoreCase(tCompra.getNombre())) {
            compra.setSaldo(0.0);
        } else {
            compra.setSaldo(crearCompra.getTotal());
        }

        return CompraDto.fromEntityCompra(compraRepository.save(compra));
    
    }

    @Override
    public CompraDto actualizarCompra(Integer id, CreateCompraDto compraActualizada) {
         //VALIDAR SI EXISTE 
        Compra compraRetrieved=compraRepository.findById(id)
            .orElseThrow(()-> new  RuntimeException("Compra no encontrada con el id: "+id));
        
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrada con el id: "+id));

        Cliente cliente =clienteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cliente no encontrada con el id: "+id));

        TipoCompra tCompra = tipoCompraRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Tipo de compra no  encontrada con el id: "+id));

        compraRetrieved.setFechaHora(compraActualizada.getFechaHora());
        compraRetrieved.setTotal(compraActualizada.getTotal());
        compraRetrieved.setSaldo(compraActualizada.getSaldo());
        compraRetrieved.setCliente(cliente);
        compraRetrieved.setUsuario(usuario);
        compraRetrieved.setTipoCompra(tCompra);

        return CompraDto.fromEntityCompra(compraRepository.save(compraRetrieved));
    }

    @Override
    public void eliminarCompra(Integer id) {
        if (!compraRepository.existsById(id)) {
        throw new RuntimeException("Compra no encontrada con id: " + id);
        }
        compraRepository.deleteById(id);
    }

}
