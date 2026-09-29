package Integracion_con_proveedores;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/** Prueba de integración: ensamblado real con la librería RapidExpress sin dobles. */
class ConfiguracionLogisticaTest {

    private final LogisticaService servicio =
            ConfiguracionLogistica.crear(new ProveedorLocalFalso(12500.0));

    @Test
    void local_seCotizaConElProveedorOriginal() {
        assertEquals(12500.0,
                servicio.cotizar(ConfiguracionLogistica.LOCAL, "A", "B", 1.0), 1e-9);
    }

    @Test
    void rapid_seCotizaConLaLibreriaExternaRealATravesDelAdaptador() {
        assertEquals(5.0,
                servicio.cotizar(ConfiguracionLogistica.RAPID, "A", "B", 2.0), 1e-9);
    }
}
