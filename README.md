# CompostaNet – Entregable N° 2 (modo consola)

Programación II – NRC 2327 – Universidad de las Américas

Equipo: Michelle Altamirano (líder), María Sol Buitrón, Carlos Lima y Miguel Centeno.

## Estructura de paquetes

```
src/
├── modelo/     Clases de datos (Fecha, Persona, Participante, Residuo, PilaCompost, Medicion, ...)
├── negocio/    Lógica de negocio (GestorPilas, GestorMediciones, GestorParticipantes,
│               GestorImpacto, GestorRecompensas, Validador y excepciones)
└── interfaz/   Menús de consola (Main, MenuPrincipal, un menú por módulo, Consola, DatosEjemplo)
```

## Cómo ejecutarlo

1. Abrir la carpeta `CompostaNet` como proyecto en Eclipse (File > Import > Existing Projects into Workspace), o crear un proyecto Java en NetBeans / IntelliJ usando la carpeta `src`.
2. Ejecutar la clase `interfaz.Main` (Run As > Java Application).
3. Al iniciar, el programa pide la **fecha de hoy** (formato dd/mm/aaaa). Se usa para validar que las fechas no sean futuras.

Desde la terminal:

```
javac -encoding UTF-8 -d out src/modelo/*.java src/negocio/*.java src/interfaz/*.java
java -cp out interfaz.Main
```

## Diagramas UML

Carpeta `docs/`: imágenes PNG de los diagramas y `CompostaNet_UML.drawio` (editable en app.diagrams.net / Draw.io, una página por figura).

## Datos de ejemplo

En el menú principal, la opción **6** carga datos de prueba (2 pilas, 4 mediciones, 3 participantes y 4 aportes).

Cédulas válidas de ejemplo: `1710034065` (Ana Torres), `1712345675` (Luis Mena), `1701234567` (Rosa Vega).

## Tildes en la consola

El proyecto usa codificación UTF-8. Si en la consola de Windows se ven símbolos raros en lugar de tildes, en Eclipse use Run > Run Configurations > Common > Encoding: UTF-8.

## Módulos

| N° | Módulo | Clase de negocio | Menú |
|----|--------|------------------|------|
| 1 | Pilas de compostaje (RF-01 a RF-03) | GestorPilas | MenuPilas |
| 2 | Mediciones (RF-04 a RF-06) | GestorMediciones | MenuMediciones |
| 3 | Participantes y aportes (RF-07 a RF-09) | GestorParticipantes | MenuParticipantes |
| 4 | Impacto ambiental (RF-10 a RF-12) | GestorImpacto | MenuImpacto |
| 5 | Recompensas (RF-13 a RF-15) | GestorRecompensas | MenuRecompensas |

## Conceptos de POO aplicados

- Encapsulamiento: atributos privados con getters y setters.
- Herencia: Persona > Participante; Residuo > ResiduoVerde / ResiduoSeco / ResiduoNoCompostable; ValidacionException > SaldoInsuficienteException.
- Polimorfismo: arreglo `Residuo[]` (esCompostable, puntosPorKg) y `Persona.mostrarResumen()`.
- Arreglos de tamaño fijo con contador, eliminación desplazando elementos y ordenamiento burbuja.
- Manejo de excepciones con try-catch y excepciones propias.
