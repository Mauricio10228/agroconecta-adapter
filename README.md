# Profundización 3 — Patrones GoF como soporte al diseño interno de componentes

R1-A2-S7 · Patrones de arquitectura. Tres casos de estudio resueltos con la ruta:
código problemático → *code smells* → causa del acoplamiento → principio de diseño →
alternativas → patrón → refactor → pruebas → impacto → arquitectura.

| Caso | Contexto | Patrón GoF | Categoría | Carpeta |
|---|---|---|---|---|
| 1 | MercadoRegional: comprobantes multicanal | Factory Method | Creacional | [`Caso_1_mercadoregional-factory-method`](Caso_1_mercadoregional-factory-method) |
| 2 | AgroConecta: proveedores externos de logística | Adapter | Estructural | [`Caso_2_agroconecta-adapter`](Caso_2_agroconecta-adapter) |
| 3 | TurismoCundinamarca: motor de descuentos | Strategy | Comportamiento | [`Caso_3_turismo-strategy`](Caso_3_turismo-strategy) |

Cada carpeta tiene su propio `README.md`, su `pom.xml`, el código Java y las pruebas JUnit 5.
Los casos 2 y 3 incluyen además los diagramas (`docs/img`) y las evidencias medidas (`docs/evidencias`).

## Requisitos
- JDK 17 o superior
- Maven 3.8 o superior

## Ejecutar las pruebas
```bash
mvn test                              # los tres casos, desde la raíz
cd Caso_2_agroconecta-adapter && mvn test      # Caso 2: 24 pruebas
cd Caso_3_turismo-strategy && mvn test          # Caso 3: 39 pruebas
```

## Historial, ramas y etiquetas
| Rama | Contenido |
|---|---|
| `main` | Solución final de los tres casos |
| `historial/caso3-strategy` | Historial completo de commits del Caso 3 (los citados en el informe) |
| `experimento/tercer-proveedor-sin-patron` | Caso 2: el tercer proveedor aplicado sobre el diseño con `if/else`, solo para medir |
| `experimento/aniversario-sin-patron` | Caso 3: la campaña de aniversario aplicada sobre el diseño con `if/else`, solo para medir |

El historial del Caso 2 está completo en `main`. Las ramas de experimento **no se fusionan**: contienen
el proyecto en la raíz de esa rama y sirven de comparación para las métricas (`git diff <línea-base> <experimento>`).

Las etiquetas (`git tag`) marcan los hitos de los casos 2 y 3: `casoN-1-linea-base`,
`casoN-2-refactor`, `casoN-3-nuevo-requisito` y `casoN-4-final`.
