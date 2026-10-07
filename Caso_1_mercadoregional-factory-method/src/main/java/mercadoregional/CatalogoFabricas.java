package mercadoregional;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

/** Registro de fábricas por código de formato: permite alta, reemplazo y retiro en ejecución. */
public class CatalogoFabricas {

    private final Map<String, FabricaComprobante> fabricas = new ConcurrentHashMap<>();

    /** Da de alta la fábrica o reemplaza la que ya existía con el mismo código. */
    public void registrar(FabricaComprobante fabrica) {
        Objects.requireNonNull(fabrica, "fabrica");
        String codigo = fabrica.codigo();
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("El código de la fábrica es obligatorio");
        }
        fabricas.put(codigo, fabrica);
    }

    /** Retira un formato; devuelve false si no estaba registrado. */
    public boolean retirar(String codigo) {
        return fabricas.remove(codigo) != null;
    }

    public Optional<FabricaComprobante> buscar(String codigo) {
        return Optional.ofNullable(fabricas.get(codigo));
    }

    public Set<String> codigos() {
        return new TreeSet<>(fabricas.keySet());
    }
}
