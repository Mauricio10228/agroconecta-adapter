package Integracion_con_proveedores;

/** Doble de prueba del proveedor local: registra los argumentos recibidos. */
class ProveedorLocalFalso implements ServicioEnvio {

    final double costoFijo;
    int invocaciones = 0;
    String origenRecibido;
    String destinoRecibido;
    double pesoRecibido;

    ProveedorLocalFalso(double costoFijo) {
        this.costoFijo = costoFijo;
    }

    @Override
    public double calcularCosto(String origen, String destino, double peso) {
        invocaciones++;
        origenRecibido = origen;
        destinoRecibido = destino;
        pesoRecibido = peso;
        return costoFijo;
    }
}
