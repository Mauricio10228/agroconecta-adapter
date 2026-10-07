package mercadoregional;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class FabricaComprobanteTest {

    private static final Compra COMPRA = new Compra("C-100", "Ana Gómez", 1234.5);

    private static ComprobanteService servicio() {
        return new ComprobanteService(ConfiguracionComprobantes.crearCatalogo());
    }

    @Test
    void fabricaPdfEmiteElMismoResultadoQueElServicioOriginal() {
        assertEquals(servicio().emitir("PDF", COMPRA), new FabricaPdf().emitir(COMPRA));
    }

    @Test
    void fabricaHtmlEmiteElMismoResultadoQueElServicioOriginal() {
        assertEquals(servicio().emitir("HTML", COMPRA), new FabricaHtml().emitir(COMPRA));
    }

    @Test
    void cadaFabricaDeclaraSuCodigo() {
        assertEquals("PDF", new FabricaPdf().codigo());
        assertEquals("HTML", new FabricaHtml().codigo());
    }

    @Test
    void unaFabricaNuevaSeDefineSinTocarLasExistentes() {
        FabricaComprobante texto = new FabricaComprobante() {
            @Override
            public String codigo() {
                return "TXT";
            }

            @Override
            protected Comprobante crearComprobante() {
                return new Comprobante() {
                    public String generar(Compra c) { return c.id(); }
                    public String extension() { return "txt"; }
                    public String tipoMime() { return "text/plain"; }
                };
            }
        };
        ComprobanteEmitido emitido = texto.emitir(COMPRA);
        assertEquals("comprobante-C-100.txt", emitido.nombreArchivo());
        assertEquals("text/plain", emitido.tipoMime());
        assertEquals("C-100", emitido.contenido());
    }
}
