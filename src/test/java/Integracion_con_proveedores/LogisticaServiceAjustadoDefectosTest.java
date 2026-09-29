package Integracion_con_proveedores;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Documenta los defectos de LogisticaServiceAjustado (solo línea base).
 * Estas pruebas se retiran en el refactor porque el diseño nuevo elimina las causas.
 */
class LogisticaServiceAjustadoDefectosTest {

    @Test
    void sinInyeccion_local_lanzaNullPointerException() {
        LogisticaServiceAjustado servicio = new LogisticaServiceAjustado();

        assertThrows(NullPointerException.class,
                () -> servicio.cotizar("LOCAL", "A", "B", 1.0));
    }

    @Test
    void sinInyeccion_rapid_lanzaNullPointerException() {
        LogisticaServiceAjustado servicio = new LogisticaServiceAjustado();

        assertThrows(NullPointerException.class,
                () -> servicio.cotizar("RAPID", "A", "B", 1.0));
    }

    @Test
    void proveedorNulo_lanzaNullPointerException() {
        LogisticaServiceAjustado servicio = new LogisticaServiceAjustado();

        assertThrows(NullPointerException.class,
                () -> servicio.cotizar(null, "A", "B", 1.0));
    }

    @Test
    void proveedorDesconocido_lanzaExcepcionSinMensaje() {
        LogisticaServiceAjustado servicio = new LogisticaServiceAjustado();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> servicio.cotizar("DHL", "A", "B", 1.0));
        assertNull(ex.getMessage());
    }
}
