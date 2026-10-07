package co.turismocundinamarca.descuentos;

import java.util.Objects;

/**
 * Contexto (rol <b>Context</b> del patrón Strategy).
 *
 * <p>Selecciona la política indicada para la operación y le delega el cálculo. No conoce los
 * porcentajes ni las reglas de ninguna política: agregar, reemplazar o retirar una política
 * NO exige modificar esta clase (principio Abierto/Cerrado).
 */
public class CalculadorDescuento {

    private final CatalogoPoliticas catalogo;

    public CalculadorDescuento(CatalogoPoliticas catalogo) {
        this.catalogo = Objects.requireNonNull(catalogo, "catalogo");
    }

    /**
     * @return descuento de la política con ese código; 0 si no existe (comportamiento original)
     * @throws IllegalArgumentException si el tipo es nulo o el valor es negativo o no numérico
     */
    public double calcular(String tipo, double valorCompra) {
        validar(tipo, valorCompra);
        return catalogo.buscar(tipo)
                .map(politica -> politica.calcular(valorCompra))
                .orElse(0.0);
    }

    private static void validar(String tipo, double valorCompra) {
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de descuento es obligatorio");
        }
        if (Double.isNaN(valorCompra) || valorCompra < 0) {
            throw new IllegalArgumentException(
                    "El valor de la compra debe ser un número no negativo: " + valorCompra);
        }
    }
}
