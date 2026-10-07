package mercadoregional;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import javax.xml.parsers.DocumentBuilderFactory;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

/** Requisito nuevo: el formato XML entra solo con dos clases y una línea de registro. */
class ComprobanteXmlTest {

    private final ComprobanteService servicio =
            new ComprobanteService(ConfiguracionComprobantes.crearCatalogo());

    private static Document parsear(String xml) throws Exception {
        return DocumentBuilderFactory.newInstance().newDocumentBuilder()
                .parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    void emiteUnXmlBienFormadoConLosDatosDeLaCompra() throws Exception {
        ComprobanteEmitido emitido = servicio.emitir("XML", new Compra("C-100", "Ana Gómez", 1234.5));
        Document doc = parsear(emitido.contenido());
        assertEquals("C-100", doc.getElementsByTagName("id").item(0).getTextContent());
        assertEquals("Ana Gómez", doc.getElementsByTagName("cliente").item(0).getTextContent());
        assertEquals("1234.50", doc.getElementsByTagName("total").item(0).getTextContent());
    }

    @Test
    void usaNombreYTipoMimeEsperados() {
        ComprobanteEmitido emitido = servicio.emitir("XML", new Compra("C-100", "Ana", 1));
        assertEquals("comprobante-C-100.xml", emitido.nombreArchivo());
        assertEquals("application/xml", emitido.tipoMime());
    }

    @Test
    void escapaLosCaracteresEspecialesParaMantenerElXmlValido() throws Exception {
        ComprobanteEmitido emitido = servicio.emitir("XML", new Compra("C-1", "Ana & <Cía> \"S.A.\"", 5));
        Document doc = parsear(emitido.contenido());
        assertEquals("Ana & <Cía> \"S.A.\"", doc.getElementsByTagName("cliente").item(0).getTextContent());
    }
}
