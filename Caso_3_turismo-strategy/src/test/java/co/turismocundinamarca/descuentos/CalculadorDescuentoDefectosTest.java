package co.turismocundinamarca.descuentos;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Defectos de entrada detectados en el diagnóstico, ya corregidos.
 * En la línea base: tipo nulo lanzaba NullPointerException y un valor negativo generaba un
 * descuento negativo (-10.0 para FRECUENTE con -100.0).
 */
class CalculadorDescuentoDefectosTest {

    private final CalculadorDescuento calculador =
            new CalculadorDescuento(ConfiguracionDescuentos.crearCatalogo());

    @Test
    void tipoNulo_lanzaIllegalArgumentExceptionConMensaje() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> calculador.calcular(null, 100.0));
        assertTrue(ex.getMessage().contains("obligatorio"));
    }

    @Test
    void valorNegativo_lanzaIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> calculador.calcular("FRECUENTE", -100.0));
    }

    @Test
    void valorNoNumerico_lanzaIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> calculador.calcular("FRECUENTE", Double.NaN));
    }
}
