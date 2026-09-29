package Integracion_con_proveedores;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * Pruebas de caracterización: documentan el comportamiento observable de la cotización
 * multi-proveedor. Se ejecutan sin cambios de aserciones antes y después del refactor.
 */
class CotizacionLogisticaTest {

    private static final double DELTA = 1e-9;

    private final ProveedorLocalFalso local = new ProveedorLocalFalso(12500.0);
    private final RapidExpressEspia rapid = new RapidExpressEspia();

    // ---- ÚNICO punto que cambia entre la línea base y la versión refactorizada ----
    private Cotizador crearServicio() {
        LogisticaService servicio = new LogisticaService(Map.of(
                "LOCAL", local,
                "RAPID", new RapidExpressAdapter(rapid)));
        return servicio::cotizar;
    }
    // -------------------------------------------------------------------------------

    @Test
    void local_delegaEnProveedorLocalSinTransformarArgumentos() {
        double costo = crearServicio().cotizar("LOCAL", "Fusagasugá", "Bogotá", 3.5);

        assertEquals(12500.0, costo, DELTA);
        assertEquals(1, local.invocaciones);
        assertEquals("Fusagasugá", local.origenRecibido);
        assertEquals("Bogotá", local.destinoRecibido);
        assertEquals(3.5, local.pesoRecibido, DELTA);
    }

    @Test
    void local_noInvocaRapidExpress() {
        crearServicio().cotizar("LOCAL", "A", "B", 1.0);

        assertEquals(0, rapid.invocaciones);
    }

    @Test
    void rapid_calculaPrecioConLaTarifaDeLaApi() {
        double costo = crearServicio().cotizar("RAPID", "Fusagasugá", "Bogotá", 2.0);

        assertEquals(5.0, costo, DELTA); // 2000 g * 0.0025
    }

    @Test
    void rapid_armaLaRutaConGuionYConvierteKgAGramos() {
        crearServicio().cotizar("RAPID", "Bogotá", "Medellín", 2.5);

        assertEquals(1, rapid.invocaciones);
        assertEquals("Bogotá-Medellín", rapid.rutaRecibida);
        assertEquals(2500, rapid.gramosRecibidos);
    }

    @Test
    void rapid_noInvocaProveedorLocal() {
        crearServicio().cotizar("RAPID", "A", "B", 1.0);

        assertEquals(0, local.invocaciones);
    }

    /** Defecto latente detectado en el diagnóstico: (int) trunca; 2.01 kg * 1000 = 2009.99... */
    @Test
    void rapid_truncaLosGramosEnLugarDeRedondear() {
        crearServicio().cotizar("RAPID", "A", "B", 2.01);

        assertEquals(2009, rapid.gramosRecibidos);
    }

    @Test
    void proveedorDesconocido_lanzaIllegalArgumentException() {
        Cotizador servicio = crearServicio();

        assertThrows(IllegalArgumentException.class,
                () -> servicio.cotizar("DHL", "A", "B", 1.0));
    }

    @Test
    void proveedor_distingueMayusculasDeMinusculas() {
        Cotizador servicio = crearServicio();

        assertThrows(IllegalArgumentException.class,
                () -> servicio.cotizar("rapid", "A", "B", 1.0));
    }
}
