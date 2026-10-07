package mercadoregional;

/** Creador concreto de comprobantes HTML. */
public class FabricaHtml extends FabricaComprobante {

    @Override
    public String codigo() {
        return "HTML";
    }

    @Override
    protected Comprobante crearComprobante() {
        return new ComprobanteHtml();
    }
}
