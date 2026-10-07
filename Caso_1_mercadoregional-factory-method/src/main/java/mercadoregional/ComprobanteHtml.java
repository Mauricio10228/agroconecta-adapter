package mercadoregional;

import java.util.Locale;

/** Comprobante en HTML. */
public class ComprobanteHtml implements Comprobante {

    @Override
    public String generar(Compra compra) {
        return "<html><body>"
                + "<h1>Comprobante " + compra.id() + "</h1>"
                + "<p>Cliente: " + compra.cliente() + "</p>"
                + "<p>Total: " + String.format(Locale.US, "%.2f", compra.total()) + "</p>"
                + "</body></html>";
    }

    @Override
    public String extension() {
        return "html";
    }

    @Override
    public String tipoMime() {
        return "text/html";
    }
}
