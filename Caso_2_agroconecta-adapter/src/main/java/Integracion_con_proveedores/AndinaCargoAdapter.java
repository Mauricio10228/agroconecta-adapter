package Integracion_con_proveedores;

import java.util.Objects;

/**
 * Adapter de {@link AndinaCargoAPI}: recibe origen/destino por separado y el peso en libras.
 * Traduce desde el contrato {@link ServicioEnvio} (peso en kg).
 */
public class AndinaCargoAdapter implements ServicioEnvio {

    private static final double LIBRAS_POR_KG = 2.20462;

    private final AndinaCargoAPI api;

    public AndinaCargoAdapter(AndinaCargoAPI api) {
        this.api = Objects.requireNonNull(api, "api");
    }

    @Override
    public double calcularCosto(String origen, String destino, double peso) {
        return api.quote(origen, destino, peso * LIBRAS_POR_KG);
    }
}
