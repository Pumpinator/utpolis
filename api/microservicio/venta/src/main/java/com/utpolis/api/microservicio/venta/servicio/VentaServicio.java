package com.utpolis.api.microservicio.venta.servicio;

import com.utpolis.api.microservicio.venta.repositorio.*;
import com.utpolis.modelo.dto.DetalleVentaDto;
import com.utpolis.modelo.dto.VentaDto;
import com.utpolis.modelo.entidad.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
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
    private final InventarioRepositorio inventarioRepositorio;

    public VentaDto obtener(Long id) {
        Venta venta = ventaRepositorio.findById(id).orElseThrow(() -> new RuntimeException(String.format("Venta con id '%s' no encontrada", id)));
        List<DetalleVenta> detalles = (List<DetalleVenta>) detalleVentaRepositorio.findAllByVentaId(venta.getId());
        return construirDto(venta, detalles);
    }

    public Iterable<VentaDto> listar() {
        return ((List<Venta>) ventaRepositorio.findAll()).stream().map(venta -> {
            List<DetalleVenta> detalles = (List<DetalleVenta>) detalleVentaRepositorio.findAllByVentaId(venta.getId());
            return construirDto(venta, detalles);
        }).toList();
    }

    public Page<VentaDto> paginar(Pageable pageable) {
        return ventaRepositorio.findAll(pageable).map(venta -> {
            List<DetalleVenta> detalles = (List<DetalleVenta>) detalleVentaRepositorio.findAllByVentaId(venta.getId());
            return construirDto(venta, detalles);
        });
    }



    @Transactional
    public VentaDto generar(VentaDto venta) {
        validar(venta);

        Usuario cliente = usuarioRepositorio.findById(venta.getClienteId()).orElseThrow(() -> new RuntimeException(String.format("Cliente con id '%s' no encontrado", venta.getClienteId())));
        if (!cliente.getRol().getNombre().equals(Roles.CLIENTE.name()))
            throw new RuntimeException("El usuario no es un cliente");
        Usuario empleado = usuarioRepositorio.findById(venta.getEmpleadoId()).orElseThrow(() -> new RuntimeException(String.format("Empleado con id '%s' no encontrado", venta.getEmpleadoId())));
        if (!empleado.getRol().getNombre().equals(Roles.EMPLEADO.name()))
            throw new RuntimeException("El usuario no es un empleado");
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
                if (detalleVenta.getCantidad() != detalleVenta.getAsientosId().size())
                    throw new RuntimeException("La cantidad de asientos no coincide con la cantidad de detalles");

                Funcion funcion = funcionRepositorio.findById(detalleVenta.getFuncionId()).orElseThrow(() -> new RuntimeException(String.format("Funcion con id '%s' no encontrada", detalleVenta.getFuncionId())));
                if (funcion.getFecha().isBefore(LocalDate.now()) ||
                        (funcion.getFecha().isEqual(LocalDate.now()) && funcion.getHorario().getHora().isBefore(LocalTime.now()))) {
                    throw new RuntimeException(String.format("La función con id '%s' ya ha pasado en la fecha '%s' y hora '%s'", funcion.getId(), funcion.getFecha(), funcion.getHorario().getHora()));
                }

                detalleVenta.getAsientosId().forEach(asientoId -> {
                    Asiento asiento = asientoRepositorio.findById(asientoId).orElseThrow(() -> new RuntimeException(String.format("Asiento con id '%s' no encontrado", asientoId)));
                    if (!asiento.getSala().getId().equals(funcion.getSala().getId()))
                        throw new RuntimeException(String.format("El asiento '%s' no pertenece a la sala '%s' de la función '%s'", asiento.getId(), funcion.getSala().getId(), funcion.getId()));
                    if (asiento.isOcupado())
                        throw new RuntimeException(String.format("El asiento '%s' ya está ocupado", asiento.getId()));
                    asiento.setOcupado(true);
                    asientoRepositorio.save(asiento);
                    boletoRepositorio.save(Boleto.builder()
                            .funcion(funcion)
                            .asiento(asiento)
                            .precio(funcion.getPrecio())
                            .build());
                });
                detalleVenta.setVentaId(nueva.getId());
                detalleVenta.setFuncionId(funcion.getId());
                detalleVenta.setSubtotalDetalle(funcion.getPrecio() * detalleVenta.getCantidad());
            } else {
                Producto producto = productoRepositorio.findById(detalleVenta.getProductoId()).orElseThrow(() -> new RuntimeException(String.format("Producto con id '%s' no encontrado", detalleVenta.getProductoId())));
                Inventario inventario = inventarioRepositorio.findByProductoId(detalleVenta.getProductoId()).orElseThrow(() -> new RuntimeException(String.format("Producto con id '%s' no encontrado en el inventario", detalleVenta.getProductoId())));
                if (inventario.getCantidad() < detalleVenta.getCantidad())
                    throw new RuntimeException(String.format("No hay suficientes productos '%s' en el inventario", producto.getNombreProducto()));
                inventario.setCantidad(inventario.getCantidad() - detalleVenta.getCantidad());
                inventarioRepositorio.save(inventario);
                detalleVenta.setProductoId(producto.getId());
                detalleVenta.setSubtotalDetalle(producto.getPrecio() * detalleVenta.getCantidad());
                detalleVenta.setVentaId(nueva.getId());
                detalleVenta.setProductoId(producto.getId());
                detalleVenta.setAsientosId(null);
            }
            return construir(detalleVenta);
        }).toList();
        detalles = (List<DetalleVenta>) detalleVentaRepositorio.saveAll(detalles);

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
            throw new RuntimeException("El id del cliente es requerido");

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
                .detalleVenta(detalles.stream().map((detalleVenta) -> DetalleVentaDto.builder()
                        .id(detalleVenta.getId())
                        .ventaId(detalleVenta.getVenta().getId())
                        .productoId(detalleVenta.getProducto() != null ? detalleVenta.getProducto().getId() : null)
                        .funcionId(detalleVenta.getFuncion() != null ? detalleVenta.getFuncion().getId() : null)
                        .asientosId(detalleVenta.getAsientos() != null ? detalleVenta.getAsientos().stream().map(Asiento::getId).toList() : null)
                        .cantidad(detalleVenta.getCantidad())
                        .subtotalDetalle(detalleVenta.getSubtotalDetalle())
                        .build()).toList())
                .total(total)
                .build();
    }


    public DetalleVenta construir(DetalleVentaDto detalleVenta) {
        return DetalleVenta.builder()
                .id(detalleVenta.getId())
                .venta(ventaRepositorio.findById(detalleVenta.getVentaId()).orElseThrow(() -> new RuntimeException(String.format("Venta con id '%s' no encontrada", detalleVenta.getVentaId()))))
                .producto(detalleVenta.getProductoId() != null ? productoRepositorio.findById(detalleVenta.getProductoId()).orElseThrow(() -> new RuntimeException(String.format("Producto con id '%s' no encontrado", detalleVenta.getProductoId()))) : null)
                .funcion(detalleVenta.getFuncionId() != null ? funcionRepositorio.findById(detalleVenta.getFuncionId()).orElseThrow(() -> new RuntimeException(String.format("Función con id '%s' no encontrada", detalleVenta.getFuncionId()))) : null)
                .asientos(detalleVenta.getAsientosId() != null ? detalleVenta.getAsientosId().stream().map(asientoId -> asientoRepositorio.findById(asientoId).orElseThrow(() -> new RuntimeException(String.format("Asiento con id '%s' no encontrado", asientoId)))).toList() : null)
                .cantidad(detalleVenta.getCantidad())
                .subtotalDetalle(detalleVenta.getSubtotalDetalle())
                .build();
    }
}
