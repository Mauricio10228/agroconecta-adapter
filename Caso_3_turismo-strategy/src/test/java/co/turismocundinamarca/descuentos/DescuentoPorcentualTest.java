package co.turismocundinamarca.descuentos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class DescuentoPorcentualTest {

    @Test
    void cumpleElContratoPoliticaDescuento() {
        assertInstanceOf(PoliticaDescuento.class, new DescuentoPorcentual("X", 0.1));
    }

    @Test
    void aplicaElPorcentajeSobreElValor() {
        assertEquals(15_000.0, new DescuentoPorcentual("TEMPORADA_BAJA", 0.15).calcular(100_000.0), 1e-9);
    }

    @Test
    void exponeSuCodigo() {
        assertEquals("CONVENIO", new DescuentoPorcentual("CONVENIO", 0.2).codigo());
    }

    @Test
    void aceptaLosLimitesDelPorcentaje() {
        assertEquals(0.0, new DescuentoPorcentual("CERO", 0.0).calcular(500.0), 1e-9);
        assertEquals(500.0, new DescuentoPorcentual("TOTAL", 1.0).calcular(500.0), 1e-9);
    }

    @Test
    void rechazaPorcentajeFueraDeRango() {
        assertThrows(IllegalArgumentException.class, () -> new DescuentoPorcentual("X", -0.01));
        assertThrows(IllegalArgumentException.class, () -> new DescuentoPorcentual("X", 1.01));
        assertThrows(IllegalArgumentException.class, () -> new DescuentoPorcentual("X", Double.NaN));
    }

    @Test
    void rechazaCodigoNuloOVacio() {
        assertThrows(NullPointerException.class, () -> new DescuentoPorcentual(null, 0.1));
        assertThrows(IllegalArgumentException.class, () -> new DescuentoPorcentual("  ", 0.1));
    }
}
