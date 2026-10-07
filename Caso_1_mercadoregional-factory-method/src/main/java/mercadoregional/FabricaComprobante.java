package mercadoregional;

/**
 * Creador (Factory Method). Las subclases deciden qué {@link Comprobante} concreto crear;
 * la operación {@link #emitir(Compra)} usa ese producto sin conocer su clase.
 */
public abstract class FabricaComprobante {

    /** Código con el que el formato se identifica en el catálogo (por ejemplo "PDF"). */
    public abstract String codigo();

    /** Método de fábrica: cada subclase devuelve su formato. */
    protected abstract Comprobante crearComprobante();

    public final ComprobanteEmitido emitir(Compra compra) {
        Comprobante comprobante = crearComprobante();
        String contenido = comprobante.generar(compra);
        String nombre = "comprobante-" + compra.id() + "." + comprobante.extension();
        return new ComprobanteEmitido(nombre, comprobante.tipoMime(), contenido);
    }
}
