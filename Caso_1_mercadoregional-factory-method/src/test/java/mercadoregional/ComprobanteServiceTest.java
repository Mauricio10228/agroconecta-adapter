package mercadoregional;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/** Pruebas de caracterización: describen lo que el servicio hace hoy. */
class ComprobanteServiceTest {

    /** Firma funcional usada por las pruebas; permite cambiar cómo se construye el servicio. */
    interface Emisor {
        ComprobanteEmitido emitir(String tipo, Compra compra);
    }

    private static final Compra COMPRA = new Compra("C-100", "Ana Gómez", 1234.5);

    private Emisor crearServicio() {
        return new ComprobanteService(ConfiguracionComprobantes.crearCatalogo())::emitir;
    }

    @Test
    void pdfGeneraContenidoConLosDatosDeLaCompra() {
        String contenido = crearServicio().emitir("PDF", COMPRA).contenido();
        assertTrue(contenido.startsWith("%PDF-1.4"));
        assertTrue(contenido.contains("C-100"));
        assertTrue(contenido.contains("Ana Gómez"));
        assertTrue(contenido.contains("Total: 1234.50"));
    }

    @Test
    void pdfUsaNombreYTipoMimeEsperados() {
        ComprobanteEmitido emitido = crearServicio().emitir("PDF", COMPRA);
        assertEquals("comprobante-C-100.pdf", emitido.nombreArchivo());
        assertEquals("application/pdf", emitido.tipoMime());
    }

    @Test
    void htmlGeneraContenidoConLosDatosDeLaCompra() {
        String contenido = crearServicio().emitir("HTML", COMPRA).contenido();
        assertTrue(contenido.startsWith("<html>"));
        assertTrue(contenido.contains("Comprobante C-100"));
        assertTrue(contenido.contains("Cliente: Ana Gómez"));
        assertTrue(contenido.contains("Total: 1234.50"));
    }

    @Test
    void htmlUsaNombreYTipoMimeEsperados() {
        ComprobanteEmitido emitido = crearServicio().emitir("HTML", COMPRA);
        assertEquals("comprobante-C-100.html", emitido.nombreArchivo());
        assertEquals("text/html", emitido.tipoMime());
    }

    @Test
    void tipoDesconocidoLanzaIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> crearServicio().emitir("DOCX", COMPRA));
    }

    @Test
    void tipoVacioLanzaIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> crearServicio().emitir("", COMPRA));
    }

    @Test
    void elTipoDistingueMayusculasDeMinusculas() {
        assertThrows(IllegalArgumentException.class, () -> crearServicio().emitir("pdf", COMPRA));
    }

    @Test
    void elTipoNoSeNormalizaConEspacios() {
        assertThrows(IllegalArgumentException.class, () -> crearServicio().emitir(" PDF ", COMPRA));
    }

    @Test
    void cadaLlamadaDevuelveElMismoContenidoParaLaMismaCompra() {
        Emisor servicio = crearServicio();
        assertEquals(servicio.emitir("HTML", COMPRA), servicio.emitir("HTML", COMPRA));
    }
}
