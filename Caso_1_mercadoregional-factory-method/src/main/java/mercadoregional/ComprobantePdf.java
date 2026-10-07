package mercadoregional;

import java.util.Locale;

/** Comprobante en PDF (simulado como texto, igual que la librería simulada del caso). */
public class ComprobantePdf implements Comprobante {

    @Override
    public String generar(Compra compra) {
        return "%PDF-1.4\n"
                + "Comprobante de compra " + compra.id() + "\n"
                + "Cliente: " + compra.cliente() + "\n"
                + "Total: " + String.format(Locale.US, "%.2f", compra.total()) + "\n"
                + "%%EOF";
    }

    @Override
    public String extension() {
        return "pdf";
    }

    @Override
    public String tipoMime() {
        return "application/pdf";
    }
}
