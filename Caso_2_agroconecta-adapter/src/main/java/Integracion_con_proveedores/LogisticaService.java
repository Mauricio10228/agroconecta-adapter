package Integracion_con_proveedores;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Cliente (rol <b>Client</b> del patrón Adapter) de la lógica de logística.
 *
 * <p>Conoce únicamente la abstracción {@link ServicioEnvio}. No sabe cuántos proveedores existen,
 * ni cómo se llaman sus APIs, ni en qué unidades trabajan: esos detalles viven en los adaptadores.
 * Agregar un operador nuevo NO exige modificar esta clase (principio Abierto/Cerrado).
 */
public class LogisticaService {

    private final Map<String, ServicioEnvio> proveedores;

    /**
     * @param proveedores catálogo código-de-proveedor -&gt; servicio de envío (se copia defensivamente)
     */
    public LogisticaService(Map<String, ServicioEnvio> proveedores) {
        Objects.requireNonNull(proveedores, "proveedores");
        this.proveedores = Collections.unmodifiableMap(new HashMap<>(proveedores));
    }

    public double cotizar(String proveedor, String origen, String destino, double pesoKg) {
        ServicioEnvio servicio = proveedores.get(proveedor);
        if (servicio == null) {
            throw new IllegalArgumentException("Proveedor logístico no soportado: " + proveedor);
        }
        return servicio.calcularCosto(origen, destino, pesoKg);
    }
}
