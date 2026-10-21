package com.codeper.ControlClientes.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.codeper.ControlClientes.dto.CompraDto;
import com.codeper.ControlClientes.dto.CreateCompraDto;
import com.codeper.ControlClientes.entity.Cliente;
import com.codeper.ControlClientes.entity.Compra;
import com.codeper.ControlClientes.entity.DetalleCompra;
import com.codeper.ControlClientes.entity.EstadoCompra;
import com.codeper.ControlClientes.entity.Producto;
import com.codeper.ControlClientes.entity.TipoCompra;
import com.codeper.ControlClientes.entity.Usuario;
import com.codeper.ControlClientes.repository.ClienteRepository;
import com.codeper.ControlClientes.repository.CompraRepository;
import com.codeper.ControlClientes.repository.ProductoRepository;
import com.codeper.ControlClientes.repository.TipoCompraRepository;
import com.codeper.ControlClientes.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public List<CompraDto> listarCompra() {
       return compraRepository.findAll().stream()
            .map(CompraDto::fromEntityCompra)   // los detalles se arman dentro del DTO
            .toList();
    }

    @Override
    public CompraDto listarCompraPorId(Integer id) {
        return compraRepository.findById(id)
            .map(CompraDto::fromEntityCompra)  // los detalles se arman dentro del DTO
            .orElseThrow(() -> new EntityNotFoundException("Compra no encontrada con id: " + id));
    }

    @Override
    @Transactional
    public CompraDto crearCompra(CreateCompraDto dto) {

        // 1. Validar que existan
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
            .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id: " + dto.getUsuarioId()));
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
            .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con id: " + dto.getClienteId()));
        TipoCompra tCompra = tipoCompraRepository.findById(dto.getTipoCompraId())
            .orElseThrow(() -> new EntityNotFoundException("Tipo de compra no encontrado con id: " + dto.getTipoCompraId()));

        // 2. Cabecera
        Compra compra = new Compra();
        compra.setCliente(cliente);
        compra.setUsuario(usuario);
        compra.setTipoCompra(tCompra);
        compra.setFechaHora(LocalDateTime.now());

        //ESTE PROCESO  ESTA IMPLMENTANDO DENTRO DE LA FUNCION cargarDetalles() PARA NO REPETIR CODIGO
        /*  3. Una línea por cada producto
        BigDecimal total = BigDecimal.ZERO;
        for (CreateCompraDto.DetalleItemDto item : dto.getDetalles()) {
            Producto producto = productoRepository.findById(item.getProductoId())
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con id: " + item.getProductoId()));

            // subtotal = precio de venta × cantidad (lo calcula el backend)
            BigDecimal subtotal = producto.getPrecioVenta()
                .multiply(BigDecimal.valueOf(item.getCantidad()));
            total = total.add(subtotal);

            DetalleCompra detalle = new DetalleCompra();
            detalle.setCantidad(item.getCantidad());
            detalle.setSubtotal(subtotal);
            detalle.setProducto(producto);
            detalle.setCompra(compra);
            compra.getDetalles().add(detalle);   // ← sin esto no se guarda
        }*/
        BigDecimal total = cargarDetalles(compra, dto);
        // 4. Total y saldo
        compra.setTotal(total);
       if ("Credito".equalsIgnoreCase(tCompra.getNombre())) {
            compra.setSaldo(total);
            compra.setEstado(EstadoCompra.PENDIENTE);
        } else { // contado
            compra.setSaldo(BigDecimal.ZERO);
            compra.setEstado(EstadoCompra.PAGADO);
        }
        // 5. Guarda compra + detalles (cascade)
        return CompraDto.fromEntityCompra(compraRepository.save(compra));
    }

    @Override
    @Transactional
    public CompraDto actualizarCompra(Integer id, CreateCompraDto compraActualizada) {

        // 1. La compra debe existir
        Compra compra = compraRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Compra no encontrada con id: " + id));

        // 2. Validar que existan y actualizar la cabecera (la fecha original NO se toca)
        Usuario usuario = usuarioRepository.findById(compraActualizada.getUsuarioId())
            .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id: " + compraActualizada.getUsuarioId()));
        Cliente cliente = clienteRepository.findById(compraActualizada.getClienteId())
            .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con id: " + compraActualizada.getClienteId()));
        TipoCompra tCompra = tipoCompraRepository.findById(compraActualizada.getTipoCompraId())
            .orElseThrow(() -> new EntityNotFoundException("Tipo de compra no encontrado con id: " + compraActualizada.getTipoCompraId()));

        compra.setUsuario(usuario);
        compra.setCliente(cliente);
        compra.setTipoCompra(tCompra);

        // 3. Borrar las líneas anteriores y crear las nuevas
        compra.getDetalles().clear();   // orphanRemoval las borra de la BD
        BigDecimal total = cargarDetalles(compra, compraActualizada);

        // 4. Recalcular total y saldo
        compra.setTotal(total);
        compra.setSaldo(calcularSaldo(tCompra, total));

        return CompraDto.fromEntityCompra(compraRepository.save(compra));
    }
    private BigDecimal cargarDetalles(Compra compra, CreateCompraDto dto) {
    BigDecimal total = BigDecimal.ZERO;
    for (CreateCompraDto.DetalleItemDto item : dto.getDetalles()) {
        Producto producto = productoRepository.findById(item.getProductoId())
            .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con id: " + item.getProductoId()));

        BigDecimal subtotal = producto.getPrecioVenta()
            .multiply(BigDecimal.valueOf(item.getCantidad()));
        total = total.add(subtotal);

        DetalleCompra detalle = new DetalleCompra();
        detalle.setCantidad(item.getCantidad());
        detalle.setPrecioUnitario(producto.getPrecioVenta());
        detalle.setSubtotal(subtotal);
        detalle.setProducto(producto);
        detalle.setCompra(compra);
        compra.getDetalles().add(detalle);
    }
        return total;
    }

    private BigDecimal calcularSaldo(TipoCompra tipo, BigDecimal total) {
        return "CONTADO".equalsIgnoreCase(tipo.getNombre()) ? BigDecimal.ZERO : total;
    }

    @Override
    public void eliminarCompra(Integer id) {
         if (!compraRepository.existsById(id)) {
        throw new EntityNotFoundException("Compra no encontrada con id: " + id);
        }
        compraRepository.deleteById(id);
    }

/* 
    @Override
    public List<CompraDto> listarCompra() {
       
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
    }*/

}
