package Integracion_con_proveedores;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class AndinaCargoAdapterTest {

    /** Espía: registra lo recibido y delega en la implementación real. */
    private static class AndinaEspia extends AndinaCargoAPI {
        String origen;
        String destino;
        double libras;

        @Override
        public double quote(String originCity, String destinationCity, double weightInPounds) {
            origen = originCity;
            destino = destinationCity;
            libras = weightInPounds;
            return super.quote(originCity, destinationCity, weightInPounds);
        }
    }

    private final AndinaEspia api = new AndinaEspia();
    private final AndinaCargoAdapter adapter = new AndinaCargoAdapter(api);

    @Test
    void cumpleElContratoServicioEnvio() {
        assertInstanceOf(ServicioEnvio.class, adapter);
    }

    @Test
    void convierteKgALibrasYPasaOrigenYDestinoPorSeparado() {
        adapter.calcularCosto("Cali", "Pasto", 10.0);

        assertEquals("Cali", api.origen);
        assertEquals("Pasto", api.destino);
        assertEquals(22.0462, api.libras, 1e-9);
    }

    @Test
    void devuelveElPrecioDeLaApiExterna() {
        // 4.0 + (10 kg * 2.20462 lb/kg) * 0.8 = 21.63696
        assertEquals(21.63696, adapter.calcularCosto("A", "B", 10.0), 1e-9);
    }

    @Test
    void rechazaApiNula() {
        assertThrows(NullPointerException.class, () -> new AndinaCargoAdapter(null));
    }
}
