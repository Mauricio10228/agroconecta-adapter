# MercadoRegional: comprobantes multicanal con Factory Method

Refactorización del módulo de comprobantes (PDF, HTML y XML) con el patrón Factory Method,
precedida de diagnóstico, comparación de alternativas y pruebas de caracterización.

> **Alcance.** El zip del caso no estuvo disponible: `ComprobanteService`, `Comprobante*` y `Compra`
> se reconstruyeron a partir del enunciado. El PDF se simula como texto.

## Requisitos
- JDK 17 o superior.
- Para ejecutar las pruebas: Maven (`mvn test`) **o** `scripts/run-tests.sh` (usa JUnit Platform Console;
  con `--cobertura` mide líneas y ramas con JaCoCo). Los números del informe se obtuvieron con el script.

## Ramas
- `main`: historia del refactor, un commit por paso (ver tabla de trazabilidad del informe).
- `experimento/xml-sin-patron`: el mismo requisito (XML) sobre el diseño original, solo para medir.

## Diseño
`ComprobanteService` -> `CatalogoFabricas` -> `FabricaComprobante` (Creador) -> `Comprobante` (Producto).
Agregar un formato = una clase `Comprobante` + una `FabricaComprobante` + una línea en `ConfiguracionComprobantes`.
