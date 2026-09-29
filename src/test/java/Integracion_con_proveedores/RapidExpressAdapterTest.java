package Integracion_con_proveedores;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class RapidExpressAdapterTest {

    private final RapidExpressEspia api = new RapidExpressEspia();
    private final RapidExpressAdapter adapter = new RapidExpressAdapter(api);

    @Test
    void cumpleElContratoServicioEnvio() {
        assertInstanceOf(ServicioEnvio.class, adapter);
    }

    @Test
    void traduceOrigenYDestinoARutaYKgAGramos() {
        adapter.calcularCosto("Bogotá", "Medellín", 2.5);

        assertEquals("Bogotá-Medellín", api.rutaRecibida);
        assertEquals(2500, api.gramosRecibidos);
    }

    @Test
    void devuelveElPrecioDeLaApiExterna() {
        assertEquals(5.0, adapter.calcularCosto("A", "B", 2.0), 1e-9);
    }

    @Test
    void rechazaApiNula() {
        assertThrows(NullPointerException.class, () -> new RapidExpressAdapter(null));
    }
}
