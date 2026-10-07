package mercadoregional;

/** Creador concreto de comprobantes XML. */
public class FabricaXml extends FabricaComprobante {

    @Override
    public String codigo() {
        return "XML";
    }

    @Override
    protected Comprobante crearComprobante() {
        return new ComprobanteXml();
    }
}
