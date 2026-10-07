package co.turismocundinamarca.descuentos;

/** Resultado de procesar una compra con una política de descuento. */
public record ResumenCompra(double valorBruto, String tipoDescuento, double descuento, double total) {
}
