# Sistema Inteligente de Gestión de Tráfico Urbano

## Integrantes del grupo

* Martinez Santiago Nicolas
* Mastieri Ramiro

## Alternativa elegida

**Alternativa B: Sistema Inteligente de Tráfico y Emergencias**

El proyecto consiste en el desarrollo de un sistema de gestión de tráfico urbano implementado en Java, utilizando estructuras de datos propias sin recurrir a las colecciones de la biblioteca estándar de Java. El sistema modela una ciudad mediante intersecciones y calles, gestiona dispositivos viales como semáforos y cámaras, reporta y atiende emergencias según su prioridad, calcula rutas entre intersecciones y organiza territorialmente la ciudad en zonas, barrios y manzanas.

## Cómo compilar y ejecutar

### Requisitos

* Java Development Kit (JDK) 8 o superior instalado.
* Tener `javac` y `java` disponibles en el PATH del sistema.

### Compilación

Desde la raíz del proyecto, ejecutar:

**Windows (PowerShell o CMD):**
```
javac -d bin -sourcepath src src\sistema\Main.java
```

**Linux / macOS:**
```
javac -d bin -sourcepath src src/sistema/Main.java
```

Esto compila todos los archivos fuente y genera los `.class` en la carpeta `bin/`.

### Ejecución

```
java -cp bin sistema.Main
```

El sistema arranca por consola y solicita al usuario que seleccione un perfil: **Ciudadano** u **Operador**.

## Organización de paquetes

```
src/
├── modelo/          # Entidades del dominio
│   ├── Calle.java
│   ├── Camara.java
│   ├── Cambio.java
│   ├── Direccion.java
│   ├── Dispositivo.java
│   ├── Emergencia.java
│   ├── Interseccion.java
│   ├── NodoTerritorial.java
│   ├── Semaforo.java
│   └── Vehiculo.java
├── tda/             # Estructuras de datos propias
│   ├── interfaces/  # Contratos de cada TDA
│   ├── ArbolTerritorial.java
│   ├── ColaFIFO.java
│   ├── ColaPrioridadEmergencias.java
│   ├── DiccionarioDispositivos.java
│   ├── EntradaDiccionario.java
│   ├── GrafoVial.java
│   ├── ListaEnlazada.java
│   ├── Nodo.java
│   └── PilaHistorial.java
└── sistema/         # Lógica central e interfaz por consola
    ├── Main.java
    └── SistemaTrafico.java
```

## Estructuras de datos utilizadas

### Lista enlazada genérica (`ListaEnlazada<T>`)
Estructura base del sistema. Se usa para guardar intersecciones, calles adyacentes de cada intersección, hijos de cada nodo territorial y listas auxiliares en distintos algoritmos.

### Cola FIFO (`ColaFIFO<T>`)
Modela el flujo vehicular en una intersección. Los vehículos se registran en orden de llegada y se atienden en ese mismo orden (primero en entrar, primero en salir).

### Cola con prioridad (`ColaPrioridadEmergencias`)
Implementada como lista enlazada ordenada. Las emergencias se insertan ordenadas por gravedad descendente; ante igual gravedad, tiene precedencia la más antigua por timestamp. Se usa para atender primero las emergencias más críticas.

### Pila de historial (`PilaHistorial<T>`)
Almacena los cambios realizados sobre dispositivos y calles (bloqueos, demoras, activaciones, estados de semáforo). Permite deshacer el último cambio aplicado (LIFO).

### Diccionario de dispositivos (`DiccionarioDispositivos`)
Tabla de hash con encadenamiento (capacidad 31). Registra semáforos y cámaras con su ID como clave. Permite buscar, insertar, modificar estado y eliminar dispositivos en tiempo promedio O(1).

### Grafo vial (`GrafoVial`)
Representa la red vial como lista de adyacencia. Cada vértice es una intersección y cada arista es una calle (con nombre, distancia, tiempo base, demora extra y estado de bloqueo). Implementa BFS para rutas de menos tramos y Dijkstra para rutas más rápidas o más cortas.

### Árbol n-ario territorial (`ArbolTerritorial`)
Modela la jerarquía territorial: Ciudad → Zona → Barrio → Manzana. Cada nodo puede tener múltiples hijos. Registra la cantidad de incidentes por zona a medida que se reportan emergencias.

## Funcionalidades implementadas

### Perfil Ciudadano
1. Calcular ruta entre intersecciones (por menos tramos, menor tiempo o menor distancia)
2. Ver estado de los semáforos
3. Reportar emergencia
4. Seguimiento de posición (origen se actualiza al llegar al destino)

### Perfil Operador
1. Ver dispositivos registrados
2. Activar / desactivar dispositivo
3. Cambiar estado de semáforo
4. Registrar nuevo semáforo
5. Registrar nueva cámara
6. Bloquear / desbloquear calle por nombre
7. Deshacer último cambio
8. Registrar demora en una calle
9. Cancelar demora en una calle
10. Ver calles con demora activa
11. Atender próxima emergencia (por prioridad)
12. Ver cola de emergencias pendientes
13. Ver red vial
14. Ver organización territorial con incidentes por zona
15. Demostración de cola FIFO (flujo vehicular en intersección)
16. Reporte de emergencias por zona

## Aclaraciones técnicas

* El proyecto no utiliza ninguna colección de `java.util` (`ArrayList`, `HashMap`, `LinkedList`, `Stack`, `Queue`, `PriorityQueue`, etc.). Todas las estructuras fueron implementadas desde cero.
* Las calles de doble mano se representan como dos aristas independientes en el grafo.
* El algoritmo BFS calcula la ruta con menos intersecciones intermedias. Dijkstra calcula la ruta de menor tiempo o menor distancia según pesos de cada calle.
* El sistema carga automáticamente datos iniciales al iniciar: 7 intersecciones, 9 calles, 6 semáforos y 3 cámaras, junto con la organización territorial de la ciudad.

## Link del repositorio

https://github.com/martinezsantiago-dev/sistema-trafico-inteligente.git

## Actividades realizadas por cada integrante

### Mastieri Ramiro

* Implementación de las clases del modelo: `Dispositivo`, `Semaforo`, `Camara`, `Cambio`.
* Desarrollo y revisión del menú Operador en `Main.java`.
* Corrección de bugs en la gestión de bloqueos, demoras e historial.
* Documentación de casos de prueba.

### Martinez Santiago Nicolas

* Implementación de todas las estructuras de datos propias: `ListaEnlazada`, `ColaFIFO`, `PilaHistorial`, `ColaPrioridadEmergencias`, `DiccionarioDispositivos`, `GrafoVial`, `ArbolTerritorial`.
* Diseño e implementación de la clase `SistemaTrafico` como fachada principal.
* Implementación de los algoritmos de cálculo de rutas (BFS y Dijkstra).
* Desarrollo del menú Ciudadano y flujo general por consola.
* Integración general del sistema.