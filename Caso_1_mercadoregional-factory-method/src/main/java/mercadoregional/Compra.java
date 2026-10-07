package mercadoregional;

/** Datos mínimos de una compra (reconstruido: el zip del caso no estuvo disponible). */
public record Compra(String id, String cliente, double total) {
}
