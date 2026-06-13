# Sistema Inteligente de Gestión de Tráfico Urbano

## Integrantes del grupo

* Mastieri Ramiro
* Martinez Santiago Nicolas

## Alternativa elegida

**Alternativa B: Sistema Inteligente de Tráfico y Emergencias**

El proyecto consiste en el desarrollo de un sistema de gestión de tráfico urbano implementado en Java, utilizando estructuras de datos propias. El sistema busca modelar una ciudad mediante intersecciones y calles, gestionar dispositivos viales como semáforos y cámaras, reportar y atender emergencias según su prioridad, consultar rutas y organizar territorialmente la ciudad en zonas, barrios y manzanas.

Esta versión corresponde a una **pre-entrega**, por lo que algunas funcionalidades se encuentran implementadas de forma parcial o en proceso de integración.

## Estructuras de datos utilizadas

Durante esta segunda etapa se implementaron y utilizaron las siguientes estructuras de datos propias:

### Lista enlazada

Se utiliza como estructura base para almacenar conjuntos dinámicos de elementos. Fue aplicada en distintas partes del sistema, por ejemplo para guardar intersecciones, calles adyacentes y nodos hijos dentro del árbol territorial.

### Cola FIFO

Se utiliza para representar el flujo vehicular en una intersección. Los vehículos que llegan a una intersección se registran en orden de llegada y se liberan respetando el criterio FIFO: el primero en llegar es el primero en salir.

### Cola con prioridad

Se utiliza para administrar las emergencias reportadas. Cada emergencia tiene una gravedad asociada, y la intención es que el sistema atienda primero aquellas con mayor prioridad. En esta pre-entrega, la implementación de emergencias se encuentra en proceso de ajuste e integración con el resto del sistema.

### Pila

Se utiliza para guardar el historial de cambios realizados sobre dispositivos. Permite deshacer el último cambio aplicado, por ejemplo al activar o desactivar un dispositivo, o al modificar el estado de un semáforo.

### Diccionario de dispositivos

Se utiliza para registrar, buscar, modificar y eliminar dispositivos mediante un identificador único. Permite almacenar semáforos y cámaras en una misma estructura, utilizando como clave el ID del dispositivo.

### Grafo vial

Se utiliza para representar la red vial de la ciudad. Cada intersección representa un vértice y cada calle representa una arista. El grafo permite consultar rutas entre intersecciones, calcular rutas por menor cantidad de tramos, por menor tiempo o por menor distancia, y contemplar demoras o bloqueos en calles.

### Árbol n-ario territorial

Se utiliza para representar la organización territorial de la ciudad. La estructura permite modelar una jerarquía del tipo:

Ciudad → Zona → Barrio → Manzana

Cada nodo puede tener varios hijos, por lo que se adapta a la representación de zonas con múltiples barrios y barrios con múltiples manzanas.

## Funcionalidades implementadas en esta segunda etapa

En esta etapa se avanzó en la integración de las estructuras de datos propias con el dominio del sistema. Las principales funcionalidades implementadas o iniciadas son:

* Carga inicial de datos de prueba del sistema.
* Gestión de perfiles de usuario: ciudadano y operador.
* Registro de dispositivos viales como semáforos y cámaras.
* Búsqueda de dispositivos mediante un diccionario propio.
* Activación y desactivación de dispositivos.
* Modificación del estado de semáforos.
* Registro de cambios en una pila de historial.
* Deshacer el último cambio realizado sobre un dispositivo.
* Representación de la red vial mediante un grafo.
* Carga de intersecciones y calles.
* Representación de calles de doble mano o mano única.
* Consulta de rutas entre intersecciones.
* Cálculo de rutas según menor cantidad de tramos.
* Cálculo de rutas más rápidas según tiempo estimado.
* Cálculo de rutas más cortas según distancia.
* Posibilidad de asociar demoras o bloqueos a calles.
* Representación de la organización territorial mediante un árbol n-ario.
* Visualización de zonas, barrios y manzanas.
* Uso de una cola FIFO para modelar vehículos esperando en una intersección.
* Implementación inicial del módulo de emergencias con cola de prioridad.

## Funcionalidades pendientes o en desarrollo

Al tratarse de una pre-entrega, algunas funcionalidades todavía no se encuentran completamente finalizadas o requieren ajustes:

* La gestión de emergencias se encuentra parcialmente implementada. La estructura de cola con prioridad está planteada, pero falta terminar de integrar correctamente el registro, visualización y atención de emergencias desde el menú principal.
* Algunas opciones del menú pueden requerir mejoras en la validación de datos ingresados por el usuario.
* La consulta de rutas por dirección y altura está planteada, pero puede requerir ajustes según los datos cargados en el mapa.
* La integración completa entre emergencias, intersecciones afectadas y modificación del estado vial todavía se encuentra en desarrollo.
* Falta realizar una mayor cantidad de pruebas manuales para verificar todos los casos posibles.
* El sistema todavía puede requerir mejoras en la presentación de la información por consola.

## Link del repositorio

https://github.com/martinezsantiago-dev/sistema-trafico-inteligente.git


## Actividades realizadas por cada integrante

### Mastieri Ramiro

* Desarrollo de clases del modelo relacionadas con dispositivos viales.
* Implementación de las clases `Dispositivo`, `Semaforo` y `Camara`.
* Colaboración en la definición de atributos y comportamiento de los dispositivos.
* Apoyo en la integración de semáforos y cámaras con el diccionario de dispositivos.
* Participación en la carga inicial de datos de prueba.
* Revisión de funcionalidades del perfil operador.

### Martinez Santiago Nicolas

* Implementación de estructuras de datos propias.
* Desarrollo de lista enlazada, cola FIFO, pila de historial, cola con prioridad y diccionario de dispositivos.
* Diseño e implementación del grafo vial con intersecciones y calles.
* Desarrollo de la lógica para cálculo de rutas.
* Implementación del árbol n-ario territorial.
* Desarrollo de la clase `SistemaTrafico` como fachada principal del sistema.
* Desarrollo del menú principal en consola con perfiles ciudadano y operador.
* Integración general de las funcionalidades del sistema.

## Aclaraciones técnicas

El proyecto fue desarrollado sin utilizar colecciones propias de Java como `ArrayList`, `HashMap`, `LinkedList`, `Stack`, `Queue` o `PriorityQueue`. En su lugar, se implementaron estructuras de datos propias de acuerdo con los contenidos vistos en la materia.

El sistema utiliza clases del paquete `modelo` para representar las entidades principales del dominio, clases del paquete `tda` para las estructuras de datos, y clases del paquete `sistema` para la lógica central y la interacción por consola.

## Estado actual del proyecto

El sistema cuenta con una versión preliminar funcional por consola, orientada a demostrar la integración de las estructuras de datos requeridas. Actualmente se encuentran más avanzadas las funcionalidades relacionadas con dispositivos, grafo vial, rutas, historial y territorio. La parte de emergencias está iniciada, pero todavía requiere ajustes para funcionar de manera completa y estable dentro del flujo general del sistema.
