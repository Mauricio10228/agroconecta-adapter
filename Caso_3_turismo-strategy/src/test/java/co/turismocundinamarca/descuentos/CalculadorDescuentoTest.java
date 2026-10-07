package co.turismocundinamarca.descuentos;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Pruebas de caracterización: documentan el comportamiento observable del cálculo de
 * descuentos. Se ejecutan sin cambios de aserciones antes y después del refactor.
 */
class CalculadorDescuentoTest {

    private static final double DELTA = 1e-9;

    // ---- ÚNICO punto que cambia entre la línea base y la versión refactorizada ----
    private Descontador crearCalculador() {
        return new CalculadorDescuento(ConfiguracionDescuentos.crearCatalogo())::calcular;
    }
    // -------------------------------------------------------------------------------

    @Test
    void frecuente_aplicaDiezPorCiento() {
        assertEquals(10_000.0, crearCalculador().calcular("FRECUENTE", 100_000.0), DELTA);
    }

    @Test
    void temporadaBaja_aplicaQuincePorCiento() {
        assertEquals(15_000.0, crearCalculador().calcular("TEMPORADA_BAJA", 100_000.0), DELTA);
    }

    @Test
    void convenio_aplicaVeintePorCiento() {
        assertEquals(20_000.0, crearCalculador().calcular("CONVENIO", 100_000.0), DELTA);
    }

    @Test
    void descuentoEsProporcionalAlValorDeLaCompra() {
        Descontador c = crearCalculador();

        assertEquals(c.calcular("CONVENIO", 100.0) * 2.5, c.calcular("CONVENIO", 250.0), DELTA);
    }

    @Test
    void valorCero_generaDescuentoCero() {
        assertEquals(0.0, crearCalculador().calcular("CONVENIO", 0.0), DELTA);
    }

    @Test
    void tipoDesconocido_devuelveCeroSinFallar() {
        assertEquals(0.0, crearCalculador().calcular("ANIVERSARIO", 100_000.0), DELTA);
    }

    @Test
    void tipoVacio_devuelveCero() {
        assertEquals(0.0, crearCalculador().calcular("", 100_000.0), DELTA);
    }

    @Test
    void tipo_distingueMayusculasDeMinusculas() {
        assertEquals(0.0, crearCalculador().calcular("frecuente", 100_000.0), DELTA);
    }

    @Test
    void tipoConEspacios_noSeNormaliza() {
        assertEquals(0.0, crearCalculador().calcular(" FRECUENTE ", 100_000.0), DELTA);
    }
}
