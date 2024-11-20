package com.utpolis.api.microservicio.boleto.servicio;

import com.utpolis.api.microservicio.boleto.repositorio.*;
import com.utpolis.modelo.dto.DetalleVentaDto;
import com.utpolis.modelo.dto.VentaDto;
import com.utpolis.modelo.entidad.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;


@Service
@RequiredArgsConstructor
public class VentaServicio {

    private final UsuarioRepositorio usuarioRepositorio;
    private final ProductoRepositorio productoRepositorio;
    private final FuncionRepositorio funcionRepositorio;
    private final AsientoRepositorio asientoRepositorio;
    private final VentaRepositorio ventaRepositorio;
    private final DetalleVentaRepositorio detalleVentaRepositorio;
    private final BoletoRepositorio boletoRepositorio;


    @Transactional
    public VentaDto generar(VentaDto venta) {
        validar(venta);

        Usuario cliente = usuarioRepositorio.findById(venta.getClienteId()).orElseThrow(() -> new RuntimeException(String.format("Cliente con id '%s' no encontrado", venta.getClienteId())));
        if (!cliente.getRol().equals(Roles.CLIENTE.name())) throw new RuntimeException("El usuario no es un cliente");
        Usuario empleado = usuarioRepositorio.findById(venta.getEmpleadoId()).orElseThrow(() -> new RuntimeException(String.format("Empleado con id '%s' no encontrado", venta.getEmpleadoId())));
        if (!empleado.getRol().equals(Roles.EMPLEADO.name())) throw new RuntimeException("El usuario no es un empleado");
        Instant fecha = Instant.now();

        Venta nueva = ventaRepositorio.save(Venta.builder()
                .empleado(empleado)
                .cliente(cliente)
                .fecha(fecha)
                .build());

        List<DetalleVentaDto> detallesDto = venta.getDetalleVenta();
        List<DetalleVenta> detalles = detallesDto.stream().map(detalleVenta -> {
            validar(detalleVenta);
            if (detalleVenta.getFuncionId() != null) {
                if (venta.getDetalleVenta().size() != detalleVenta.getAsientosId().size())
                    throw new RuntimeException("La cantidad de asientos no coincide con la cantidad de detalles");
                Funcion funcion = funcionRepositorio.findById(detalleVenta.getFuncionId()).orElseThrow(() -> new RuntimeException(String.format("Funcion con id '%s' no encontrada", detalleVenta.getFuncionId())));
                if (funcion.getFecha().isBefore(LocalDate.now())) throw new RuntimeException("La función ya ha pasado");
                detalleVenta.getAsientosId().forEach(asientoId -> {
                    Asiento asiento = asientoRepositorio.findById(asientoId).orElseThrow(() -> new RuntimeException(String.format("Asiento con id '%s' no encontrado", asientoId)));
                    if (asiento.isOcupado())
                        throw new RuntimeException(String.format("El asiento '%s' ya está ocupado", asiento.getId()));
                    asiento.setOcupado(true);
                    asientoRepositorio.save(asiento);

                    Boleto boleto = boletoRepositorio.save(Boleto.builder()
                            .funcion(funcion)
                            .asiento(asiento)
                            .precio(funcion.getPrecio())
                            .build());
                });
                return DetalleVenta.builder()
                        .venta(nueva)
                        .funcion(funcion)
                        .cantidad(detalleVenta.getCantidad())
                        .subtotalDetalle(funcion.getPrecio() * detalleVenta.getCantidad())
                        .build();
            } else {
                Producto producto = productoRepositorio.findById(detalleVenta.getProductoId()).orElseThrow(() -> new RuntimeException(String.format("Producto con id '%s' no encontrado", detalleVenta.getProductoId())));
                return DetalleVenta.builder()
                        .venta(nueva)
                        .producto(producto)
                        .cantidad(detalleVenta.getCantidad())
                        .subtotalDetalle(producto.getPrecio() * detalleVenta.getCantidad())
                        .build();
            }
        }).toList();
        detalleVentaRepositorio.saveAll(detalles);

        return construirDto(nueva, detalles);
    }

    private void validar(DetalleVentaDto detalleVenta) {
        if (detalleVenta.getCantidad() <= 0)
            throw new RuntimeException(String.format("La cantidad de productos '%s' debe ser mayor a 0", detalleVenta.getCantidad()));

        if (detalleVenta.getVentaId() != null)
            throw new RuntimeException("No se puede asignar un id de venta al crear una venta");

        if (detalleVenta.getSubtotalDetalle() != 0)
            throw new RuntimeException(String.format("El subtotal del detalle $ '%s' no puede ser asignado en su creación", detalleVenta.getSubtotalDetalle()));

        if (detalleVenta.getProductoId() == null && detalleVenta.getFuncionId() == null)
            throw new RuntimeException("El detalle de la venta debe tener un producto o una función");
    }

    private void validar(VentaDto venta) {
        if (venta.getClienteId() == null)
            throw new RuntimeException("El id del usuario es requerido");

        if (venta.getEmpleadoId() == null)
            throw new RuntimeException("El id del empleado es requerido");

        if (venta.getDetalleVenta() == null || venta.getDetalleVenta().isEmpty())
            throw new RuntimeException("La venta debe tener al menos un detalle");

        if (venta.getFecha() != null)
            throw new RuntimeException("La fecha de la venta no puede ser asignada en su creación");

    }

    public VentaDto construirDto(Venta venta, List<DetalleVenta> detalles) {
        double total = detalles.stream().mapToDouble(DetalleVenta::getSubtotalDetalle).sum();
        return VentaDto.builder()
                .id(venta.getId())
                .clienteId(venta.getCliente().getId())
                .empleadoId(venta.getEmpleado().getId())
                .fecha(venta.getFecha().toString())
                .estatus(venta.isEstatus())
                .detalleVenta(detalles.stream().map(this::construirDto).toList())
                .total(total)
                .build();
    }

    public DetalleVentaDto construirDto(DetalleVenta detalle) {
        return DetalleVentaDto.builder()
                .ventaId(detalle.getVenta().getId())
                .productoId(detalle.getProducto() != null ? detalle.getProducto().getId() : null)
                .funcionId(detalle.getFuncion() != null ? detalle.getFuncion().getId() : null)
                .cantidad(detalle.getCantidad())
                .subtotalDetalle(detalle.getSubtotalDetalle())
                .build();
    }
}
