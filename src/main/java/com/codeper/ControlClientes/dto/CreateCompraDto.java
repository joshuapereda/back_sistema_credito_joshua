package com.codeper.ControlClientes.dto;

import java.util.List;
import com.codeper.ControlClientes.entity.Cliente;
import com.codeper.ControlClientes.entity.Compra;
import com.codeper.ControlClientes.entity.DetalleCompra;
import com.codeper.ControlClientes.entity.Producto;
import com.codeper.ControlClientes.entity.TipoCompra;
import com.codeper.ControlClientes.entity.Usuario;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor 
@NoArgsConstructor 
@Builder 
public class CreateCompraDto {

    // ---- Compra ---- //
    /*Fecha Hora, Total, Saldo    ya no se envian por que eso validara el backend */

    @NotNull(message = "El cliente es obligatorio")
    private Integer clienteId;

    @NotNull(message = "El usuario es obligatorio")
    private Integer usuarioId;

    @NotNull(message = "El tipo de compra es obligatorio")
    private Integer tipoCompraId;

    // ---- Detalle: lista de productos ----//
    /* Las líneas van en una lista:
    ---------------------------------------------
    Zapatilla Nike Air Max       x1     180   ← línea 1
    Zapatilla Nike Revolution    x1     150   ← línea 2*/
    @NotEmpty(message = "Debes asignar al menos un producto a la compra")
    @Valid
    private List<DetalleItemDto> detalles;
    //---------DetalleCompra------------//
    @Data @AllArgsConstructor @NoArgsConstructor
    public static class DetalleItemDto {
        @NotNull(message = "El producto es obligatorio")
        private Integer productoId; //qué producto

        @NotNull(message = "La cantidad es obligatoria")
        @Positive(message = "La cantidad debe ser mayor a 0")
        private Integer cantidad; // cuántos
    }


    public static Compra toEntityCompra(CreateCompraDto createCompraDto, Cliente cliente,Usuario usuario, TipoCompra tipoCompra){
        return Compra.builder()
            .cliente(cliente)
            .usuario(usuario)
            .tipoCompra(tipoCompra)
            .build();
    }

    /*ANTES: recibía el DTO completo y sacaba cantidad y subtotal de ahí
    toEntityDetalleCompra(CreateCompraDto createCompraDto, Compra compra, Producto producto)

    AHORA: recibe solo UNA línea
    toEntityDetalleCompra(DetalleItemDto item, Compra compra, Producto producto) */
    
    public static DetalleCompra toEntityDetalleCompra(DetalleItemDto  item, Compra compra, Producto productoId){
        return DetalleCompra.builder()
            .cantidad(item.getCantidad())
            .compra(compra)
            .producto(productoId)
            .build();
    }
}
