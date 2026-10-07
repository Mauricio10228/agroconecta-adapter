package mercadoregional;

import java.util.Objects;

/** Servicio que coordina la generación de comprobantes; ya no conoce ningún formato concreto. */
public class ComprobanteService {

    private final CatalogoFabricas catalogo;

    public ComprobanteService(CatalogoFabricas catalogo) {
        this.catalogo = Objects.requireNonNull(catalogo, "catalogo");
    }

    public ComprobanteEmitido emitir(String tipo, Compra compra) {
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de comprobante es obligatorio");
        }
        FabricaComprobante fabrica = catalogo.buscar(tipo)
                .orElseThrow(() -> new IllegalArgumentException("Tipo de comprobante no soportado: " + tipo));
        Objects.requireNonNull(compra, "compra");
        return fabrica.emitir(compra);
    }
}
