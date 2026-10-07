package mercadoregional;

/** Creador concreto de comprobantes PDF. */
public class FabricaPdf extends FabricaComprobante {

    @Override
    public String codigo() {
        return "PDF";
    }

    @Override
    protected Comprobante crearComprobante() {
        return new ComprobantePdf();
    }
}
