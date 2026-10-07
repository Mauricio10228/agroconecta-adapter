package co.turismocundinamarca.descuentos;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Estrategia concreta: campaña temporal con vigencia [desde, hasta] (ambos inclusive).
 *
 * <p>Fuera de la vigencia no otorga descuento. El {@link Clock} se inyecta para que la
 * vigencia sea verificable sin depender de la fecha real del sistema.
 */
public class DescuentoAniversario implements PoliticaDescuento {

    public static final String CODIGO = "ANIVERSARIO";

    private final double porcentaje;
    private final LocalDate desde;
    private final LocalDate hasta;
    private final Clock reloj;

    public DescuentoAniversario(double porcentaje, LocalDate desde, LocalDate hasta, Clock reloj) {
        if (Double.isNaN(porcentaje) || porcentaje < 0.0 || porcentaje > 1.0) {
            throw new IllegalArgumentException("El porcentaje debe estar entre 0 y 1: " + porcentaje);
        }
        Objects.requireNonNull(desde, "desde");
        Objects.requireNonNull(hasta, "hasta");
        if (hasta.isBefore(desde)) {
            throw new IllegalArgumentException("La vigencia es inválida: hasta es anterior a desde");
        }
        this.porcentaje = porcentaje;
        this.desde = desde;
        this.hasta = hasta;
        this.reloj = Objects.requireNonNull(reloj, "reloj");
    }

    @Override
    public String codigo() {
        return CODIGO;
    }

    @Override
    public double calcular(double valorCompra) {
        LocalDate hoy = LocalDate.now(reloj);
        boolean vigente = !hoy.isBefore(desde) && !hoy.isAfter(hasta);
        return vigente ? valorCompra * porcentaje : 0.0;
    }
}
