package co.turismocundinamarca.descuentos;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Catálogo de políticas de descuento vigentes, indexadas por código.
 *
 * <p>Permite incorporar, reemplazar o retirar políticas en tiempo de ejecución sin tocar el
 * proceso de compra. Es seguro para uso concurrente.
 */
public class CatalogoPoliticas {

    private final Map<String, PoliticaDescuento> politicas = new ConcurrentHashMap<>();

    /** Incorpora la política o reemplaza la que ya tenía el mismo código. */
    public void registrar(PoliticaDescuento politica) {
        Objects.requireNonNull(politica, "politica");
        politicas.put(politica.codigo(), politica);
    }

    /** Retira (desactiva) la política con ese código. */
    public boolean retirar(String codigo) {
        return politicas.remove(codigo) != null;
    }

    public Optional<PoliticaDescuento> buscar(String codigo) {
        return Optional.ofNullable(politicas.get(codigo));
    }

    public Set<String> codigos() {
        return Set.copyOf(politicas.keySet());
    }
}
