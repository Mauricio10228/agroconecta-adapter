package Integracion_con_proveedores;

import java.util.Objects;

/**
 * Adapter (GoF, estructural) de la librería externa {@link RapidExpressAPI}.
 *
 * <p>Rol en el patrón: <b>Adapter</b>. Implementa el <b>Target</b> ({@link ServicioEnvio})
 * y traduce cada llamada a la interfaz incompatible del <b>Adaptee</b> ({@link RapidExpressAPI}):
 * <ul>
 *   <li>origen + destino  -&gt; ruta con formato "origen-destino"</li>
 *   <li>peso en kg (double) -&gt; peso en gramos (int)</li>
 * </ul>
 * Así, ningún otro componente conoce los detalles de la API de RapidExpress.
 */
public class RapidExpressAdapter implements ServicioEnvio {

    private static final int GRAMOS_POR_KG = 1000;
    private static final String SEPARADOR_RUTA = "-";

    private final RapidExpressAPI api;

    public RapidExpressAdapter(RapidExpressAPI api) {
        this.api = Objects.requireNonNull(api, "api");
    }

    @Override
    public double calcularCosto(String origen, String destino, double peso) {
        String ruta = origen + SEPARADOR_RUTA + destino;
        int gramos = (int) (peso * GRAMOS_POR_KG);
        return api.getShippingPrice(ruta, gramos);
    }
}
