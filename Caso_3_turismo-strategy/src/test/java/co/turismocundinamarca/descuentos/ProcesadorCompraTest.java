package co.turismocundinamarca.descuentos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ProcesadorCompraTest {

    private final CatalogoPoliticas catalogo = ConfiguracionDescuentos.crearCatalogo();
    private final ProcesadorCompra proceso = new ProcesadorCompra(new CalculadorDescuento(catalogo));

    @Test
    void procesar_calculaDescuentoYTotal() {
        ResumenCompra r = proceso.procesar("CONVENIO", 200_000.0);

        assertEquals(200_000.0, r.valorBruto(), 1e-9);
        assertEquals(40_000.0, r.descuento(), 1e-9);
        assertEquals(160_000.0, r.total(), 1e-9);
        assertEquals("CONVENIO", r.tipoDescuento());
    }

    @Test
    void procesar_sinPoliticaAplicable_cobraElValorCompleto() {
        ResumenCompra r = proceso.procesar("NO_EXISTE", 90_000.0);

        assertEquals(0.0, r.descuento(), 1e-9);
        assertEquals(90_000.0, r.total(), 1e-9);
    }

    @Test
    void procesoNoCambia_cuandoSeReemplazaUnaPolitica() {
        catalogo.registrar(new DescuentoPorcentual("FRECUENTE", 0.5));

        assertEquals(50_000.0, proceso.procesar("FRECUENTE", 100_000.0).descuento(), 1e-9);
    }

    @Test
    void rechazaCalculadorNulo() {
        assertThrows(NullPointerException.class, () -> new ProcesadorCompra(null));
    }
}
