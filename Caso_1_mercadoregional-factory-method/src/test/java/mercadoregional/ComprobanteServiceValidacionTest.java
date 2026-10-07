package mercadoregional;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/** Validaciones corregidas: antes eran defectos documentados (NPE crudo y excepciones sin mensaje). */
class ComprobanteServiceValidacionTest {

    private static final Compra COMPRA = new Compra("C-100", "Ana Gómez", 1234.5);

    private ComprobanteServiceTest.Emisor crearServicio() {
        return new ComprobanteService(ConfiguracionComprobantes.crearCatalogo())::emitir;
    }

    @Test
    void tipoNuloLanzaIllegalArgumentExceptionConMensaje() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> crearServicio().emitir(null, COMPRA));
        assertEquals("El tipo de comprobante es obligatorio", e.getMessage());
    }

    @Test
    void laExcepcionDeTipoDesconocidoIndicaElTipoRecibido() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> crearServicio().emitir("DOCX", COMPRA));
        assertEquals("Tipo de comprobante no soportado: DOCX", e.getMessage());
    }

    @Test
    void compraNulaLanzaNullPointerExceptionConMensaje() {
        NullPointerException e = assertThrows(NullPointerException.class,
                () -> crearServicio().emitir("PDF", null));
        assertEquals("compra", e.getMessage());
    }
}
