package mercadoregional;

import java.util.Locale;

/** Comprobante en XML. */
public class ComprobanteXml implements Comprobante {

    @Override
    public String generar(Compra compra) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<comprobante>\n"
                + "  <id>" + escapar(compra.id()) + "</id>\n"
                + "  <cliente>" + escapar(compra.cliente()) + "</cliente>\n"
                + "  <total>" + String.format(Locale.US, "%.2f", compra.total()) + "</total>\n"
                + "</comprobante>";
    }

    @Override
    public String extension() {
        return "xml";
    }

    @Override
    public String tipoMime() {
        return "application/xml";
    }

    private static String escapar(String texto) {
        return String.valueOf(texto)
                .replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
                .replace("\"", "&quot;").replace("'", "&apos;");
    }
}
