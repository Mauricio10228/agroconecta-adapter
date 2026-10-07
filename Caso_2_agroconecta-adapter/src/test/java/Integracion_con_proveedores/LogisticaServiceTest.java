package Integracion_con_proveedores;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

/** Pruebas del diseño nuevo: sin reflexión y sin depender de proveedores concretos. */
class LogisticaServiceTest {

    @Test
    void usaCualquierServicioEnvioRegistrado_sinConocerSuImplementacion() {
        ServicioEnvio nuevoOperador = (origen, destino, peso) -> peso * 100;
        LogisticaService servicio = new LogisticaService(Map.of("NUEVO", nuevoOperador));

        assertEquals(300.0, servicio.cotizar("NUEVO", "A", "B", 3.0), 1e-9);
    }

    @Test
    void proveedorNulo_lanzaIllegalArgumentExceptionConMensaje() {
        LogisticaService servicio = new LogisticaService(Map.of());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> servicio.cotizar(null, "A", "B", 1.0));
        assertTrue(ex.getMessage().contains("no soportado"));
    }

    @Test
    void proveedorDesconocido_incluyeElNombreEnElMensaje() {
        LogisticaService servicio = new LogisticaService(Map.of());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> servicio.cotizar("DHL", "A", "B", 1.0));
        assertTrue(ex.getMessage().contains("DHL"));
    }

    @Test
    void copiaDefensiva_cambiosPosterioresAlMapaOriginalNoAfectanAlServicio() {
        Map<String, ServicioEnvio> catalogo = new HashMap<>();
        catalogo.put("LOCAL", new ProveedorLocalFalso(1.0));
        LogisticaService servicio = new LogisticaService(catalogo);

        catalogo.put("INTRUSO", new ProveedorLocalFalso(2.0));

        assertThrows(IllegalArgumentException.class,
                () -> servicio.cotizar("INTRUSO", "A", "B", 1.0));
    }

    @Test
    void rechazaCatalogoNulo() {
        assertThrows(NullPointerException.class, () -> new LogisticaService(null));
    }
}
