package mercadoregional;

/** Resultado de emitir un comprobante: nombre del archivo, tipo MIME y contenido. */
public record ComprobanteEmitido(String nombreArchivo, String tipoMime, String contenido) {
}
