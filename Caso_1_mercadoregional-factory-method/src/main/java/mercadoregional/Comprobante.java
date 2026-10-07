package mercadoregional;

/** Producto: un formato concreto de comprobante. Cada formato conoce su extensión y su tipo MIME. */
public interface Comprobante {

    String generar(Compra compra);

    String extension();

    String tipoMime();
}
