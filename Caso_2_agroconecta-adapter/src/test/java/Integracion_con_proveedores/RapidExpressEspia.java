package Integracion_con_proveedores;

/** Espía de la librería externa: registra lo recibido y delega en la implementación real. */
class RapidExpressEspia extends RapidExpressAPI {

    int invocaciones = 0;
    String rutaRecibida;
    int gramosRecibidos;

    @Override
    public double getShippingPrice(String route, int weightInGrams) {
        invocaciones++;
        rutaRecibida = route;
        gramosRecibidos = weightInGrams;
        return super.getShippingPrice(route, weightInGrams);
    }
}
