package mercadoregional;

/** Único lugar donde se decide qué formatos están disponibles. */
public final class ConfiguracionComprobantes {

    private ConfiguracionComprobantes() {
    }

    public static CatalogoFabricas crearCatalogo() {
        CatalogoFabricas catalogo = new CatalogoFabricas();
        catalogo.registrar(new FabricaPdf());
        catalogo.registrar(new FabricaHtml());
        catalogo.registrar(new FabricaXml());
        return catalogo;
    }
}
