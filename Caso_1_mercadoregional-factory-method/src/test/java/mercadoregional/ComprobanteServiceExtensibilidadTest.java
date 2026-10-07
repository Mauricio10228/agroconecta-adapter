package mercadoregional;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/** Demuestran que el servicio acepta formatos nuevos sin modificarse. */
class ComprobanteServiceExtensibilidadTest {

    private static final Compra COMPRA = new Compra("C-7", "Marta", 50);

    private static FabricaComprobante formato(String codigo, String marca) {
        return new FabricaComprobante() {
            public String codigo() { return codigo; }

            protected Comprobante crearComprobante() {
                return new Comprobante() {
                    public String generar(Compra c) { return marca + c.id(); }
                    public String extension() { return "dat"; }
                    public String tipoMime() { return "application/x-" + codigo.toLowerCase(); }
                };
            }
        };
    }

    @Test
    void unFormatoRegistradoDespuesSePuedeEmitirSinCambiarElServicio() {
        CatalogoFabricas catalogo = ConfiguracionComprobantes.crearCatalogo();
        ComprobanteService servicio = new ComprobanteService(catalogo);
        assertThrows(IllegalArgumentException.class, () -> servicio.emitir("ALIADO_A", COMPRA));

        catalogo.registrar(formato("ALIADO_A", "A|"));

        assertEquals("A|C-7", servicio.emitir("ALIADO_A", COMPRA).contenido());
    }

    @Test
    void unFormatoSePuedeReemplazarEnEjecucion() {
        CatalogoFabricas catalogo = ConfiguracionComprobantes.crearCatalogo();
        ComprobanteService servicio = new ComprobanteService(catalogo);
        catalogo.registrar(formato("PDF", "NUEVO|"));
        assertEquals("NUEVO|C-7", servicio.emitir("PDF", COMPRA).contenido());
    }

    @Test
    void unFormatoRetiradoDejaDeEstarDisponible() {
        CatalogoFabricas catalogo = ConfiguracionComprobantes.crearCatalogo();
        ComprobanteService servicio = new ComprobanteService(catalogo);
        catalogo.retirar("HTML");
        assertThrows(IllegalArgumentException.class, () -> servicio.emitir("HTML", COMPRA));
    }

    @Test
    void elServicioExigeUnCatalogo() {
        assertThrows(NullPointerException.class, () -> new ComprobanteService(null));
    }

    @Test
    void laConfiguracionPorDefectoOfrecePdfHtmlYXml() {
        assertEquals(java.util.Set.of("HTML", "PDF", "XML"), ConfiguracionComprobantes.crearCatalogo().codigos());
    }
}
