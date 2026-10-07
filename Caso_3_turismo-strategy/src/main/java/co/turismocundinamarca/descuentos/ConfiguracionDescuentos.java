package co.turismocundinamarca.descuentos;

import java.time.Clock;
import java.time.LocalDate;

/**
 * Raíz de composición: único lugar que conoce qué políticas existen y con qué parámetros.
 * Incorporar una política nueva = crear su clase (si su algoritmo es distinto) y registrarla aquí.
 */
public final class ConfiguracionDescuentos {

    public static final String FRECUENTE = "FRECUENTE";
    public static final String TEMPORADA_BAJA = "TEMPORADA_BAJA";
    public static final String CONVENIO = "CONVENIO";

    private ConfiguracionDescuentos() {
    }

    public static CatalogoPoliticas crearCatalogo() {
        return crearCatalogo(Clock.systemDefaultZone());
    }

    public static CatalogoPoliticas crearCatalogo(Clock reloj) {
        CatalogoPoliticas catalogo = new CatalogoPoliticas();
        catalogo.registrar(new DescuentoPorcentual(FRECUENTE, 0.10));
        catalogo.registrar(new DescuentoPorcentual(TEMPORADA_BAJA, 0.15));
        catalogo.registrar(new DescuentoPorcentual(CONVENIO, 0.20));
        catalogo.registrar(new DescuentoAniversario(0.25,
                LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 30), reloj)); // requisito 2
        return catalogo;
    }
}
