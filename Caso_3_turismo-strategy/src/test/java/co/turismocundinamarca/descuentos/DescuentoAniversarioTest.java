package co.turismocundinamarca.descuentos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;

class DescuentoAniversarioTest {

    private static final LocalDate DESDE = LocalDate.of(2026, 11, 1);
    private static final LocalDate HASTA = LocalDate.of(2026, 11, 30);

    private static Clock relojEn(LocalDate fecha) {
        return Clock.fixed(fecha.atStartOfDay().toInstant(ZoneOffset.UTC), ZoneOffset.UTC);
    }

    private static DescuentoAniversario campana(LocalDate hoy) {
        return new DescuentoAniversario(0.25, DESDE, HASTA, relojEn(hoy));
    }

    @Test
    void dentroDeLaVigencia_aplicaElPorcentaje() {
        assertEquals(25_000.0, campana(LocalDate.of(2026, 11, 15)).calcular(100_000.0), 1e-9);
    }

    @Test
    void laVigenciaIncluyeSusDosExtremos() {
        assertEquals(25_000.0, campana(DESDE).calcular(100_000.0), 1e-9);
        assertEquals(25_000.0, campana(HASTA).calcular(100_000.0), 1e-9);
    }

    @Test
    void fueraDeLaVigencia_noOtorgaDescuento() {
        assertEquals(0.0, campana(DESDE.minusDays(1)).calcular(100_000.0), 1e-9);
        assertEquals(0.0, campana(HASTA.plusDays(1)).calcular(100_000.0), 1e-9);
    }

    @Test
    void estaDisponibleEnElCatalogoSinTocarElCalculador() {
        CatalogoPoliticas catalogo = ConfiguracionDescuentos.crearCatalogo(relojEn(LocalDate.of(2026, 11, 10)));
        CalculadorDescuento calculador = new CalculadorDescuento(catalogo);

        assertEquals(25_000.0, calculador.calcular("ANIVERSARIO", 100_000.0), 1e-9);
    }

    @Test
    void validaSusParametros() {
        assertThrows(IllegalArgumentException.class,
                () -> new DescuentoAniversario(1.5, DESDE, HASTA, relojEn(DESDE)));
        assertThrows(IllegalArgumentException.class,
                () -> new DescuentoAniversario(0.2, HASTA, DESDE, relojEn(DESDE)));
        assertThrows(NullPointerException.class,
                () -> new DescuentoAniversario(0.2, DESDE, HASTA, null));
    }

    @Test
    void rechazaPorcentajeNegativoONoNumerico() {
        assertThrows(IllegalArgumentException.class,
                () -> new DescuentoAniversario(-0.1, DESDE, HASTA, relojEn(DESDE)));
        assertThrows(IllegalArgumentException.class,
                () -> new DescuentoAniversario(Double.NaN, DESDE, HASTA, relojEn(DESDE)));
    }

    @Test
    void exponeSuCodigo() {
        assertEquals("ANIVERSARIO", campana(DESDE).codigo());
    }
}
