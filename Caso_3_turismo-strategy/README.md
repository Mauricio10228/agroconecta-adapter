# TurismoCundinamarca — Motor flexible de asignación de descuentos

**Caso 3 · Patrón GoF de comportamiento: Strategy** · R1-A2-S7 Patrones de arquitectura

Refactorización del cálculo de descuentos para que las políticas puedan **incorporarse,
reemplazarse o retirarse sin modificar el proceso principal de compra**.

## Requisitos
- JDK 17 o superior
- Maven 3.8+ (o un IDE con soporte Maven)

## Ejecutar las pruebas
```bash
mvn test
```
Resultado esperado: **39 pruebas, 0 fallas** (JUnit 5).

## Estructura
```
src/main/java/co/turismocundinamarca/descuentos/
  PoliticaDescuento.java         Strategy: contrato común (codigo + calcular)
  DescuentoPorcentual.java       ConcreteStrategy parametrizada: FRECUENTE, TEMPORADA_BAJA, CONVENIO
  DescuentoAniversario.java      ConcreteStrategy del nuevo requisito (vigencia con Clock inyectado)
  CatalogoPoliticas.java         Registro: registrar (alta/reemplazo), retirar, buscar
  CalculadorDescuento.java       Context: selecciona la política por código y delega
  ProcesadorCompra.java          Proceso principal (versión mínima creada para el ejercicio)
  ResumenCompra.java             Resultado de la compra
  ConfiguracionDescuentos.java   Raíz de composición: registra las políticas
src/test/java/...                Pruebas JUnit 5
docs/img/                        Diagramas (diseño original, UML Strategy, arquitectura)
docs/evidencias/                 Salidas de pruebas, complejidad ciclomática y cobertura
```
> El enunciado no entrega el código del proceso de compra ni el `.zip` fue recibido: la clase base
> `CalculadorDescuento` se reconstruyó del código visible en el PDF y `ProcesadorCompra` es una
> versión mínima creada para el ejercicio.

## Cómo agregar una política nueva
1. Si su algoritmo es solo un porcentaje: no se crea clase; se registra `new DescuentoPorcentual("CODIGO", 0.12)`.
2. Si su regla es distinta (vigencia, tope, municipio…): crear `NuevaPolitica implements PoliticaDescuento`.
3. Registrarla en `ConfiguracionDescuentos` (o en tiempo de ejecución con `catalogo.registrar(...)`).
4. `CalculadorDescuento` y `ProcesadorCompra` **no se modifican**.

## Trazabilidad (historial Git)
| Tipo | Propósito |
|---|---|
| `chore` | Importa el código base (reconstruido del enunciado) y estructura Maven |
| `test` | Línea base de caracterización (11 pruebas) **antes** de refactorizar |
| `refactor` | Agrega `PoliticaDescuento` y `DescuentoPorcentual` |
| `refactor` | `CalculadorDescuento` delega en `CatalogoPoliticas`; elimina el `if/else` |
| `feat` | `ProcesadorCompra` (proceso principal) |
| `feat` | Nuevo requisito: campaña de aniversario con vigencia |
| `fix` | Valida tipo nulo y valor negativo/NaN (único cambio de comportamiento intencional) |
| `test` | Completa cobertura de ramas de `DescuentoAniversario` |

La rama `experimento/aniversario-sin-patron` aplica el mismo requisito sobre el diseño original
(`if/else`) y sirve de comparación para las métricas del informe. No se fusiona.

## Publicar en GitHub
```bash
git remote add origin https://github.com/<usuario>/<repositorio>.git
git push -u origin main
git push origin experimento/aniversario-sin-patron
```
