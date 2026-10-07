package co.turismocundinamarca.descuentos;

import java.util.Objects;

/**
 * Proceso principal de compra (cliente del patrón).
 *
 * <p>NOTA DE ALCANCE: el enunciado del caso menciona "el proceso principal de compra" pero no
 * entrega su código; esta clase es una versión mínima creada para demostrar que dicho proceso
 * permanece intacto cuando las políticas cambian.
 */
public class ProcesadorCompra {

    private final CalculadorDescuento calculador;

    public ProcesadorCompra(CalculadorDescuento calculador) {
        this.calculador = Objects.requireNonNull(calculador, "calculador");
    }

    public ResumenCompra procesar(String tipoDescuento, double valorCompra) {
        double descuento = calculador.calcular(tipoDescuento, valorCompra);
        return new ResumenCompra(valorCompra, tipoDescuento, descuento, valorCompra - descuento);
    }
}
