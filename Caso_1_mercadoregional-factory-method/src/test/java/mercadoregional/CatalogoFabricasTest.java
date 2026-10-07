package mercadoregional;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;
import org.junit.jupiter.api.Test;

class CatalogoFabricasTest {

    private static FabricaComprobante fabrica(String codigo, String contenido) {
        return new FabricaComprobante() {
            public String codigo() { return codigo; }

            protected Comprobante crearComprobante() {
                return new Comprobante() {
                    public String generar(Compra c) { return contenido; }
                    public String extension() { return "x"; }
                    public String tipoMime() { return "text/x"; }
                };
            }
        };
    }

    private static final Compra COMPRA = new Compra("C-1", "Luis", 10);

    @Test
    void registrarYBuscarDevuelveLaFabrica() {
        CatalogoFabricas catalogo = new CatalogoFabricas();
        FabricaComprobante pdf = new FabricaPdf();
        catalogo.registrar(pdf);
        assertSame(pdf, catalogo.buscar("PDF").orElseThrow());
    }

    @Test
    void buscarUnCodigoInexistenteDevuelveVacio() {
        assertTrue(new CatalogoFabricas().buscar("PDF").isEmpty());
    }

    @Test
    void registrarConElMismoCodigoReemplazaLaFabrica() {
        CatalogoFabricas catalogo = new CatalogoFabricas();
        catalogo.registrar(fabrica("PDF", "v1"));
        catalogo.registrar(fabrica("PDF", "v2"));
        assertEquals("v2", catalogo.buscar("PDF").orElseThrow().emitir(COMPRA).contenido());
        assertEquals(1, catalogo.codigos().size());
    }

    @Test
    void retirarQuitaElFormato() {
        CatalogoFabricas catalogo = new CatalogoFabricas();
        catalogo.registrar(new FabricaPdf());
        assertTrue(catalogo.retirar("PDF"));
        assertTrue(catalogo.buscar("PDF").isEmpty());
    }

    @Test
    void retirarUnFormatoInexistenteDevuelveFalse() {
        assertFalse(new CatalogoFabricas().retirar("PDF"));
    }

    @Test
    void registrarUnaFabricaNulaLanzaNullPointerException() {
        assertThrows(NullPointerException.class, () -> new CatalogoFabricas().registrar(null));
    }

    @Test
    void registrarConCodigoNuloOEnBlancoLanzaIllegalArgumentException() {
        CatalogoFabricas catalogo = new CatalogoFabricas();
        assertThrows(IllegalArgumentException.class, () -> catalogo.registrar(fabrica(null, "x")));
        assertThrows(IllegalArgumentException.class, () -> catalogo.registrar(fabrica("  ", "x")));
    }

    @Test
    void codigosSeDevuelvenOrdenados() {
        CatalogoFabricas catalogo = new CatalogoFabricas();
        catalogo.registrar(new FabricaPdf());
        catalogo.registrar(new FabricaHtml());
        assertEquals(Set.of("HTML", "PDF"), catalogo.codigos());
        assertEquals("HTML", catalogo.codigos().iterator().next());
    }
}
