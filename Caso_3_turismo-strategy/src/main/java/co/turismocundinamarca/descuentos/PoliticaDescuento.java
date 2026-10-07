package co.turismocundinamarca.descuentos;

/**
 * Strategy (GoF, comportamiento): contrato común de todas las políticas de descuento.
 *
 * <p>Cada implementación encapsula UN algoritmo de descuento y se identifica con un código
 * único que el proceso de compra usa para seleccionarla en cada operación.
 */
public interface PoliticaDescuento {

    /** Código con el que la política se registra y se selecciona (p. ej. "FRECUENTE"). */
    String codigo();

    /**
     * @param valorCompra valor de la compra antes de descuentos
     * @return monto del descuento (no el total a pagar)
     */
    double calcular(double valorCompra);
}
