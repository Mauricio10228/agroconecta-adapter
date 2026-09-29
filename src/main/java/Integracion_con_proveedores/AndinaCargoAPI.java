package Integracion_con_proveedores;

/** Librería externa del tercer operador (no modificable). */
public class AndinaCargoAPI {

    public double quote(String originCity, String destinationCity, double weightInPounds) {
        // Simulación de API externa: tarifa base + costo por libra
        return 4.0 + weightInPounds * 0.8;
    }
}
