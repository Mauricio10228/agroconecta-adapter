package Integracion_con_proveedores;

/**
 * Vista mínima del servicio de logística que usan las pruebas de comportamiento.
 * Permite ejecutar EXACTAMENTE las mismas aserciones antes y después de refactorizar:
 * solo cambia la forma de construir el servicio (ver CotizacionLogisticaTest#crearServicio).
 */
@FunctionalInterface
interface Cotizador {
    double cotizar(String proveedor, String origen, String destino, double pesoKg);
}
