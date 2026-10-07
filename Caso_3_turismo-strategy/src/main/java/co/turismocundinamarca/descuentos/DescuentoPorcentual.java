package co.turismocundinamarca.descuentos;

import java.util.Objects;

/**
 * Estrategia concreta: descuento de un porcentaje fijo sobre el valor de la compra.
 *
 * <p>Las tres modalidades originales (cliente frecuente, temporada baja y convenio empresarial)
 * solo se diferencian por el código y el porcentaje, de modo que se modelan como DATOS de una
 * misma clase y no como tres clases casi idénticas.
 */
public class DescuentoPorcentual implements PoliticaDescuento {

    private final String codigo;
    private final double porcentaje;

    /**
     * @param codigo     identificador no vacío de la política
     * @param porcentaje fracción entre 0 y 1 (0.10 = 10 %)
     */
    public DescuentoPorcentual(String codigo, double porcentaje) {
        Objects.requireNonNull(codigo, "codigo");
        if (codigo.isBlank()) {
            throw new IllegalArgumentException("El código de la política no puede estar vacío");
        }
        if (Double.isNaN(porcentaje) || porcentaje < 0.0 || porcentaje > 1.0) {
            throw new IllegalArgumentException(
                    "El porcentaje debe estar entre 0 y 1: " + porcentaje);
        }
        this.codigo = codigo;
        this.porcentaje = porcentaje;
    }

    @Override
    public String codigo() {
        return codigo;
    }

    @Override
    public double calcular(double valorCompra) {
        return valorCompra * porcentaje;
    }
}
