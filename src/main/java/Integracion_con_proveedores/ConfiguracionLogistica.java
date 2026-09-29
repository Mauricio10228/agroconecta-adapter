package Integracion_con_proveedores;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Raíz de composición: único lugar que conoce qué proveedores existen y cómo se ensamblan.
 * Registrar un operador nuevo = crear su adaptador + una línea aquí.
 */
public final class ConfiguracionLogistica {

    public static final String LOCAL = "LOCAL";
    public static final String RAPID = "RAPID";
    public static final String ANDINA = "ANDINA";

    private ConfiguracionLogistica() {
    }

    /**
     * @param proveedorLocal implementación del proveedor logístico original (ya cumple ServicioEnvio)
     */
    public static LogisticaService crear(ServicioEnvio proveedorLocal) {
        Map<String, ServicioEnvio> proveedores = new LinkedHashMap<>();
        proveedores.put(LOCAL, proveedorLocal);
        proveedores.put(RAPID, new RapidExpressAdapter(new RapidExpressAPI()));
        proveedores.put(ANDINA, new AndinaCargoAdapter(new AndinaCargoAPI()));
        return new LogisticaService(proveedores);
    }
}
