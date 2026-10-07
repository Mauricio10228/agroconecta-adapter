# AgroConecta — Integración con proveedores externos de logística

**Caso 2 · Patrón GoF estructural: Adapter** · R1-A2-S7 Patrones de arquitectura

Refactorización de la cotización de envíos de AgroConecta para integrar proveedores con
interfaces incompatibles (`ServicioEnvio` y `RapidExpressAPI`) **sin contaminar la lógica
principal** con detalles de cada API externa.

## Requisitos
- JDK 17 o superior
- Maven 3.8+ (o cualquier IDE con soporte Maven: IntelliJ IDEA, Eclipse, VS Code)

## Ejecutar las pruebas
```bash
mvn test
```
Resultado esperado: **24 pruebas, 0 fallas** (JUnit 5).

## Estructura
```
src/main/java/Integracion_con_proveedores/
  ServicioEnvio.java            Target: contrato único que usa la aplicación (sin cambios)
  RapidExpressAPI.java          Adaptee: librería externa NO modificable (sin cambios)
  RapidExpressAdapter.java      Adapter: ruta "origen-destino" + kg -> gramos
  AndinaCargoAPI.java           Adaptee del nuevo requisito (tercer operador)
  AndinaCargoAdapter.java       Adapter: kg -> libras
  LogisticaService.java         Client: depende SOLO de ServicioEnvio (catálogo por código)
  ConfiguracionLogistica.java   Raíz de composición: registra LOCAL, RAPID y ANDINA
src/test/java/Integracion_con_proveedores/   Pruebas JUnit 5 (dobles: ProveedorLocalFalso, RapidExpressEspia)
docs/img/                       Diagramas (UML del Adapter, dependencias antes, arquitectura)
docs/evidencias/                Salidas de pruebas, complejidad ciclomática y cobertura
```
> El nombre de paquete `Integracion_con_proveedores` se conserva tal como fue entregado en el caso,
> para mantener la trazabilidad del diff.

## Cómo agregar un nuevo operador
1. Crear `NuevoOperadorAdapter implements ServicioEnvio` (traduce unidades/formato de su API).
2. Registrar una línea en `ConfiguracionLogistica`: `proveedores.put("NUEVO", new NuevoOperadorAdapter(...));`
3. `LogisticaService` **no se modifica**.

## Trazabilidad (historial Git)
| Commit | Propósito |
|---|---|
| `chore` | Importa el código base entregado y estructura Maven |
| `test` | Línea base de caracterización (14 pruebas) **antes** de refactorizar |
| `refactor` | Agrega `RapidExpressAdapter` (Adapter) |
| `refactor` | `LogisticaService` depende solo de `ServicioEnvio`; elimina `LogisticaServiceAjustado` |
| `feat` | `ConfiguracionLogistica` (raíz de composición) |
| `feat` | Nuevo requisito: tercer operador ANDINA (`LogisticaService` intacto) |
| `fix` | Redondeo kg→g (único cambio de comportamiento intencional) |

La rama `experimento/tercer-proveedor-sin-patron` aplica el mismo requisito sobre el diseño original
(con `if/else`) y sirve de punto de comparación para las métricas del informe. No se fusiona.

## Publicar en GitHub
```bash
git remote add origin https://github.com/<usuario>/<repositorio>.git
git push -u origin main
git push origin experimento/tercer-proveedor-sin-patron
```
