# Manual de datos cargados — Sistema de Tráfico

Este documento describe todos los datos que el sistema carga automáticamente al iniciar. Sirve de referencia para probar las funcionalidades sin necesidad de leer el código.

---

## Vehículo del ciudadano

| Patente | Tipo |
|---------|------|
| ABC123  | Auto |

---

## Intersecciones

| ID | Nombre del cruce          | Zona         |
|----|---------------------------|--------------|
| I1 | Rivadavia y Belgrano      | Zona Centro  |
| I2 | Rivadavia y San Martín    | Zona Centro  |
| I3 | Belgrano y San Martín     | Zona Norte   |
| I4 | Rivadavia y Lavalle       | Zona Sur     |
| I5 | Corrientes y San Martín   | Zona Norte   |
| I6 | Corrientes y Lavalle      | Zona Sur     |
| I7 | Mitre y San Martín        | Zona Norte   |

---

## Calles

| Nombre      | Origen → Destino | Sentido    | Distancia | Tiempo base |
|-------------|------------------|------------|-----------|-------------|
| Rivadavia   | I1 → I2          | Mano única | 2000 m    | 2 min       |
| Belgrano    | I1 ↔ I4          | Doble mano | 300 m     | 2 min       |
| San Martín  | I2 ↔ I5          | Doble mano | 400 m     | 3 min       |
| San Martín  | I2 → I3          | Mano única | 350 m     | 2 min       |
| Lavalle     | I6 → I4          | Mano única | 3000 m    | 3 min       |
| Corrientes  | I6 → I5          | Mano única | 500 m     | 2 min       |
| Autopista   | I1 → I5          | Mano única | 8000 m    | 6 min       |
| Mitre       | I3 → I7          | Mano única | 100 m     | 1 min       |
| Mitre       | I7 → I5          | Mano única | 150 m     | 1 min       |

> Las calles de doble mano generan dos aristas independientes en el grafo (una por sentido).

---

## Semáforos

| ID | Intersección | Ubicación               | Verde | Amarillo | Rojo |
|----|--------------|-------------------------|-------|----------|------|
| S1 | I1           | Rivadavia altura 1200   | 30 s  | 5 s      | 25 s |
| S2 | I2           | Rivadavia altura 1800   | 25 s  | 5 s      | 30 s |
| S3 | I3           | Belgrano altura 2800    | 20 s  | 5 s      | 35 s |
| S4 | I4           | Belgrano altura 2000    | 30 s  | 5 s      | 25 s |
| S5 | I5           | San Martín altura 1400  | 25 s  | 5 s      | 30 s |
| S6 | I6           | Lavalle altura 1000     | 20 s  | 5 s      | 35 s |

Estado inicial de todos los semáforos: **ROJO**

---

## Cámaras

| ID | Intersección | Ubicación               | Tipo       |
|----|--------------|-------------------------|------------|
| C1 | I1           | Rivadavia altura 1500   | velocidad  |
| C2 | I2           | San Martín altura 1600  | flujo      |
| C3 | I6           | Corrientes altura 1000  | seguridad  |

Estado inicial de todos los dispositivos: **activos**

---

## Organización territorial

```
Ciudad: San Martín
├── Zona Norte
│   ├── Barrio Belgrano
│   │   ├── Manzana 1
│   │   └── Manzana 2
│   └── Barrio San Martín
│       └── Manzana 3
├── Zona Sur
│   └── Barrio Lavalle
│       └── Manzana 4
└── Zona Centro
    ├── Barrio Corrientes
    │   └── Manzana 5
    └── Barrio Rivadavia
        └── Manzana 6
```

---

## Ejemplos de rutas útiles para pruebas

| Origen | Destino | Menos tramos | Más rápida | Más corta |
|--------|---------|--------------|------------|-----------|
| I1     | I5      | I1→I5 (Autopista, 1 tramo, 8000 m, 6 min) | I1→I2→I5 (2 tramos, 5 min) | I1→I2→I5 (2 tramos, 2400 m) |
| I1     | I4      | I1→I4 (Belgrano, único camino, 1 tramo, 300 m, 2 min) | ídem | ídem |
| I6     | I4      | I6→I4 (Lavalle, único camino, 1 tramo, 3000 m, 3 min) | ídem | ídem |
| I3     | I5      | I3→I7→I5 (Mitre, único camino, 2 tramos, 250 m, 2 min) | ídem | ídem |
| I1     | I6      | **Sin camino posible** (I6 no tiene aristas de entrada desde I1) | — | — |

> **I1 → I5 es el caso que demuestra la diferencia real entre BFS y Dijkstra**: BFS (menos tramos) toma el atajo directo por la Autopista (1 tramo, pero 8000 m); Dijkstra por distancia y por tiempo coinciden en tomar el camino más largo en tramos (I1→I2→I5, 2 tramos) porque en total recorre menos metros (2400 m) y tarda menos (5 min) que el atajo. Antes tiempo y distancia arrastraban valores irreales (ej. 9 km/h en Belgrano, <1 km/h en Mitre) que generaban un empate de tiempo entre ambas rutas; se ajustaron los tiempos base a velocidades realistas y ahora las tres rutas (menos tramos, más rápida, más corta) dan resultados distintos para el mismo par de nodos.
>
> Los demás pares (I1-I4, I6-I4, I3-I5) tienen un único camino posible en el grafo, así que los tres algoritmos coinciden trivialmente — no sirven para mostrar la diferencia, solo para probar que el cálculo de ruta funciona.
>
> I6 solo tiene salidas (I6→I4 y I6→I5), no tiene calles que lleguen a ella desde el resto del mapa. Usarlo para probar el caso de ruta imposible.

---

## Notas para los casos de prueba

- Para **bloquear una calle**: ingresar el nombre tal como aparece en la tabla (ej: `San Martín`, `Autopista`).
- Para **registrar demora**: ingresar los IDs de origen y destino (ej: `I1` y `I2`).
- Para **reportar emergencia**: se puede indicar zona como `Zona Norte`, `Zona Sur` o `Zona Centro`.
- Para **calcular ruta**: ingresar los IDs de intersección (ej: `I1`, `I5`) o el nombre del cruce.
- Los dispositivos se identifican por su ID (ej: `S1`, `C2`).