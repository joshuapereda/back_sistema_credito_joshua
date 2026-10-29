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
import com.codeper.ControlClientes.exception.ResourNotFoundException;
import com.codeper.ControlClientes.repository.ClienteRepository;
import com.codeper.ControlClientes.repository.CompraRepository;
import com.codeper.ControlClientes.repository.PagoRepository;
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

    @Autowired
    private PagoRepository pagoRepository;

    @Override
    public List<CompraDto> listarCompra() {
       return compraRepository.findAll().stream()
            .map(CompraDto::fromEntityCompra)   // los detalles se arman dentro del DTO
            .toList();
    }

    @Override
    public CompraDto listarCompraPorId(Integer id) {
        try {
            return compraRepository.findById(id)
            .map(CompraDto::fromEntityCompra)  // los detalles se arman dentro del DTO
            .orElseThrow(() -> new ResourNotFoundException("Compra " , id));
            
        } catch (ResourNotFoundException e) {
           throw e;
        }
    }

    @Override
    @Transactional
    public CompraDto crearCompra(CreateCompraDto dto) {

        // 1. Validar que existan
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
            .orElseThrow(() -> new ResourNotFoundException("Usuario" , dto.getUsuarioId()));
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
            .orElseThrow(() -> new ResourNotFoundException("Cliente " ,dto.getClienteId()));
        TipoCompra tCompra = tipoCompraRepository.findById(dto.getTipoCompraId())
            .orElseThrow(() -> new ResourNotFoundException("Tipo de compra" , dto.getTipoCompraId()));

        // 2. Cabecera
        Compra compra = new Compra();
        compra.setCliente(cliente);
        compra.setUsuario(usuario);
        compra.setTipoCompra(tCompra);
        compra.setFechaHora(LocalDateTime.now());

        /* 3. Cargar detalles: por cada producto del DTO crea un DetalleCompra
        (producto, cantidad, precio unitario y subtotal = precio x cantidad),
        lo agrega a la compra y devuelve el total (suma de todos los subtotales). */
        BigDecimal total = cargarDetalles(compra, dto);

        // 4. Guarda en la cabecera el total calculado por el backend
        compra.setTotal(total);

        // 5. Define saldo y estado según el tipo de compra (crédito o contado)
        aplicarSaldoYEstado(compra, BigDecimal.ZERO);

        // 6. Guarda compra + detalles (cascade)
        return CompraDto.fromEntityCompra(compraRepository.save(compra));
    }

    @Override
    @Transactional
    public CompraDto actualizarCompra(Integer id, CreateCompraDto compraActualizada) {

        // 1. La compra debe existir
        Compra compra = compraRepository.findById(id)
            .orElseThrow(() -> new ResourNotFoundException("Compra " , id));

        // 2. Validar que existan y actualizar la cabecera (la fecha original NO se toca)
        Usuario usuario = usuarioRepository.findById(compraActualizada.getUsuarioId())
            .orElseThrow(() -> new ResourNotFoundException("Usuario " , compraActualizada.getUsuarioId()));
        Cliente cliente = clienteRepository.findById(compraActualizada.getClienteId())
            .orElseThrow(() -> new ResourNotFoundException("Cliente " , compraActualizada.getClienteId()));
        TipoCompra tCompra = tipoCompraRepository.findById(compraActualizada.getTipoCompraId())
            .orElseThrow(() -> new ResourNotFoundException("Tipo de compra " , compraActualizada.getTipoCompraId()));

        compra.setUsuario(usuario);
        compra.setCliente(cliente);
        compra.setTipoCompra(tCompra);

        // 3. Borrar las líneas anteriores y crear las nuevas
        compra.getDetalles().clear();   // orphanRemoval las borra de la BD
        BigDecimal total = cargarDetalles(compra, compraActualizada);

        // 4. Recalcular total, saldo y estado descontando lo que ya se pagó
        compra.setTotal(total);
        aplicarSaldoYEstado(compra, pagoRepository.sumarPagosPorCompra(id));

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

    // Única regla de saldo y estado, usada al crear y al editar.
    // Contado: no queda deuda. Crédito: saldo = total - lo que ya pagó.
    private void aplicarSaldoYEstado(Compra compra, BigDecimal totalPagado) {
        TipoCompra tipo = compra.getTipoCompra();
        if (tipo.getEsCredito() == null) {
            throw new RuntimeException("El tipo de compra '" + tipo.getNombre()
                + "' no indica si es a crédito o al contado (campo esCredito).");
        }

        if (!tipo.getEsCredito()) {
            compra.setSaldo(BigDecimal.ZERO);
            compra.setEstado(EstadoCompra.PAGADO);
            return;
        }

        BigDecimal saldo = compra.getTotal().subtract(totalPagado);
        if (saldo.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("El nuevo total (" + compra.getTotal()
                + ") es menor a lo que el cliente ya pagó (" + totalPagado + ").");
        }

        compra.setSaldo(saldo);
        compra.setEstado(saldo.compareTo(BigDecimal.ZERO) == 0 ? EstadoCompra.PAGADO : EstadoCompra.PENDIENTE);
    }

    @Override
    public void eliminarCompra(Integer id) {
         if (!compraRepository.existsById(id)) {
        throw new ResourNotFoundException("Compra" , id);
        }
        compraRepository.deleteById(id);
    }

}
