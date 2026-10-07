package co.turismocundinamarca.descuentos;

/**
 * Vista mínima del cálculo de descuentos que usan las pruebas de comportamiento.
 * Permite ejecutar las MISMAS aserciones antes y después del refactor: solo cambia
 * cómo se construye el calculador (ver CalculadorDescuentoTest#crearCalculador).
 */
@FunctionalInterface
interface Descontador {
    double calcular(String tipo, double valorCompra);
}
