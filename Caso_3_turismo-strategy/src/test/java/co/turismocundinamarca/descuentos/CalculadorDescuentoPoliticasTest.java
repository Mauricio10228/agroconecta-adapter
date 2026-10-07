package co.turismocundinamarca.descuentos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/** Pruebas del diseño nuevo: el calculador funciona con CUALQUIER política, sin conocerla. */
class CalculadorDescuentoPoliticasTest {

    @Test
    void usaCualquierPoliticaRegistrada_sinConocerSuImplementacion() {
        CatalogoPoliticas catalogo = new CatalogoPoliticas();
        catalogo.registrar(new PoliticaDescuento() {
            @Override public String codigo() { return "FIJO_5000"; }
            @Override public double calcular(double valorCompra) { return 5_000.0; }
        });

        assertEquals(5_000.0, new CalculadorDescuento(catalogo).calcular("FIJO_5000", 80_000.0), 1e-9);
    }

    @Test
    void reemplazarUnaPolitica_cambiaElResultadoSinTocarElCalculador() {
        CatalogoPoliticas catalogo = ConfiguracionDescuentos.crearCatalogo();
        CalculadorDescuento calculador = new CalculadorDescuento(catalogo);
        assertEquals(10_000.0, calculador.calcular("FRECUENTE", 100_000.0), 1e-9);

        catalogo.registrar(new DescuentoPorcentual("FRECUENTE", 0.12));

        assertEquals(12_000.0, calculador.calcular("FRECUENTE", 100_000.0), 1e-9);
    }

    @Test
    void retirarUnaPolitica_laDejaSinEfecto() {
        CatalogoPoliticas catalogo = ConfiguracionDescuentos.crearCatalogo();
        CalculadorDescuento calculador = new CalculadorDescuento(catalogo);

        assertEquals(true, catalogo.retirar("CONVENIO"));

        assertEquals(0.0, calculador.calcular("CONVENIO", 100_000.0), 1e-9);
    }

    @Test
    void rechazaCatalogoNulo() {
        assertThrows(NullPointerException.class, () -> new CalculadorDescuento(null));
    }
}
