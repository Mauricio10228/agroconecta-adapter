package Integracion_con_proveedores;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/** Pruebas de la versión original (un solo proveedor). Solo línea base. */
class LogisticaServiceBaseTest {

    @Test
    void cotizar_delegaEnElServicioEnvioInyectado() {
        ProveedorLocalFalso local = new ProveedorLocalFalso(1234.5);
        LogisticaService servicio = new LogisticaService(local);

        double costo = servicio.cotizar("Fusagasugá", "Bogotá", 3.5);

        assertEquals(1234.5, costo, 1e-9);
        assertEquals("Fusagasugá", local.origenRecibido);
        assertEquals("Bogotá", local.destinoRecibido);
        assertEquals(3.5, local.pesoRecibido, 1e-9);
    }

    @Test
    void cotizar_propagaElCostoSinModificarlo() {
        LogisticaService servicio = new LogisticaService(new ProveedorLocalFalso(0.0));

        assertEquals(0.0, servicio.cotizar("A", "B", 10.0), 1e-9);
    }
}
