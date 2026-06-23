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
| Belgrano    | I1 ↔ I4          | Doble mano | 300 m     | 9 min       |
| San Martín  | I2 ↔ I5          | Doble mano | 400 m     | 4 min       |
| San Martín  | I2 → I3          | Mano única | 350 m     | 3 min       |
| Lavalle     | I6 → I4          | Mano única | 3000 m    | 2 min       |
| Corrientes  | I6 → I5          | Mano única | 500 m     | 5 min       |
| Autopista   | I1 → I5          | Mano única | 8000 m    | 6 min       |
| Mitre       | I3 → I7          | Mano única | 100 m     | 8 min       |
| Mitre       | I7 → I5          | Mano única | 150 m     | 9 min       |

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
| I1     | I5      | I1→I5 (Autopista, 1 tramo) | I1→I2→I5 (6 min, empate con Autopista) | I1→I2→I5 (2400 m) |
| I1     | I4      | I1→I4 (Belgrano, 1 tramo) | I1→I4 (Belgrano, 9 min) | I1→I4 (Belgrano, 300 m) |
| I6     | I4      | I6→I4 (Lavalle, 1 tramo) | I6→I4 (Lavalle, 2 min) | I6→I5→I2→I5... ver nota |
| I3     | I5      | I3→I7→I5 (Mitre, 2 tramos) | I3→I7→I5 (17 min) | I3→I7→I5 (250 m) |
| I1     | I6      | **Sin camino posible** (I6 no tiene aristas de entrada desde I1) | — | — |

> I6 solo tiene salidas (I6→I4 y I6→I5), no tiene calles que lleguen a ella desde el resto del mapa. Usarlo para probar el caso de ruta imposible.

---

## Notas para los casos de prueba

- Para **bloquear una calle**: ingresar el nombre tal como aparece en la tabla (ej: `San Martín`, `Autopista`).
- Para **registrar demora**: ingresar los IDs de origen y destino (ej: `I1` y `I2`).
- Para **reportar emergencia**: se puede indicar zona como `Zona Norte`, `Zona Sur` o `Zona Centro`.
- Para **calcular ruta**: ingresar los IDs de intersección (ej: `I1`, `I5`) o el nombre del cruce.
- Los dispositivos se identifican por su ID (ej: `S1`, `C2`).