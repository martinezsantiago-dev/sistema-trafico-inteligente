package sistema;

import modelo.*;
import java.util.Scanner;
import tda.ListaEnlazada;
import tda.Nodo;
import modelo.Interseccion;

public class Main {

    private static SistemaTrafico sistema = new SistemaTrafico();
    private static Scanner scanner = new Scanner(System.in);
    private static int contadorEmergencias = 1;


    public static void main(String[] args) {
        cargarDatosIniciales();

        int opcion;
        do {
            System.out.println("\n========================================");
            System.out.println("  SISTEMA DE GESTIÓN DE TRÁFICO URBANO");
            System.out.println("========================================");
            System.out.println("1. Perfil Ciudadano");
            System.out.println("2. Perfil Operador");
            System.out.println("0. Salir");
            System.out.print("Seleccione perfil: ");
            opcion = leerEntero();

            switch (opcion) {
                case 1: menuCiudadano(); break;
                case 2: menuOperador(); break;
                case 0: System.out.println("Saliendo del sistema..."); break;
                default: System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }

    // ===== MENÚ CIUDADANO =====

    private static void menuCiudadano() {
        int opcion;
        do {
            System.out.println("\n--- MENÚ CIUDADANO ---");
            if (sistema.getInterseccionActualCiudadano() != null) {
                System.out.println("[Posición actual: " +
                        sistema.getNombreInterseccion(sistema.getInterseccionActualCiudadano()) + "]");
            }
            System.out.println("1. Reportar emergencia");
            System.out.println("2. Calcular ruta (menos tramos)");
            System.out.println("3. Calcular ruta más rápida");
            System.out.println("4. Calcular ruta más corta por distancia");
            System.out.println("5. Ver zonas territoriales");
            System.out.println("0. Volver");
            System.out.print("Seleccione: ");
            opcion = leerEntero();

            switch (opcion) {
                case 1: reportarEmergencia(); break;
                case 2: calcularRuta("TRAMOS"); break;
                case 3: calcularRuta("RAPIDA"); break;
                case 4: calcularRuta("CORTA"); break;
                case 5: sistema.mostrarTerritorio(); break;
                case 0: break;
                default: System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }

    // ===== MENÚ OPERADOR =====

    private static void menuOperador() {
        int opcion;
        do {
            System.out.println("\n--- MENÚ OPERADOR ---");
            System.out.println("1. Ver dispositivos registrados");
            System.out.println("2. Activar dispositivo");
            System.out.println("3. Desactivar dispositivo");
            System.out.println("4. Cambiar estado de semáforo");
            System.out.println("5. Atender próxima emergencia");
            System.out.println("6. Ver emergencias pendientes");
            System.out.println("7. Deshacer último cambio");
            System.out.println("8. Ver historial de cambios");
            System.out.println("9. Ver red vial");
            System.out.println("10. Registrar semáforo");
            System.out.println("11. Registrar cámara");
            System.out.println("12. Bloquear calle");
            System.out.println("13. Desbloquear calle");
            System.out.println("14. Registrar demora en calle");
            System.out.println("15. Registrar emergencia");
            System.out.println("16. Reporte de emergencias por zona");
            System.out.println("0. Volver");
            System.out.print("Seleccione: ");
            opcion = leerEntero();

            switch (opcion) {
                case 1:  sistema.mostrarDispositivos(); break;
                case 2:  gestionarDispositivo(true); break;
                case 3:  gestionarDispositivo(false); break;
                case 4:  cambiarEstadoSemaforo(); break;
                case 5:  atenderEmergencia(); break;
                case 6:  sistema.mostrarEmergencias(); break;
                case 7: deshacerCambio(); break;
                case 8:  sistema.mostrarHistorial(); break;
                case 9:  sistema.mostrarRedVial(); break;
                case 10: registrarSemaforo(); break;
                case 11: registrarCamara(); break;
                case 12: gestionarBloqueo(true); break;
                case 13: gestionarBloqueo(false); break;
                case 14: registrarDemora(); break;
                case 15: reportarEmergencia(); break;
                case 16: sistema.reporteEmergenciasPorZona(); break;
                case 0:  break;
                default: System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }

    // ===== ACCIONES CIUDADANO =====

    private static void reportarEmergencia() {
        sistema.mostrarIntersecciones();

        String interseccionId = pedirInterseccion("intersección afectada");
        if (interseccionId == null) return;

        // Obtener zona desde la intersección
        String zona = sistema.getZonaDeInterseccion(interseccionId);

        int gravedad = leerGravedad();
        String descripcion = leerTexto("Descripción del incidente: ");
        String id = "E" + contadorEmergencias++;

        sistema.reportarEmergencia(new Emergencia(id, gravedad, interseccionId, descripcion, zona));
    }

    private static void calcularRuta(String tipo) {
        String origenId;

        if (sistema.getInterseccionActualCiudadano() != null) {
            System.out.println("\nÚltima posición: " +
                    sistema.getNombreInterseccion(sistema.getInterseccionActualCiudadano()));
            System.out.println("¿Usarla como origen? (s/n)");
            System.out.print("> ");
            if (scanner.nextLine().trim().equalsIgnoreCase("s")) {
                origenId = sistema.getInterseccionActualCiudadano();
            } else {
                System.out.println("\n¿Desde dónde salís?");
                origenId = seleccionarInterseccion();
                if (origenId == null) return;
            }
        } else {
            System.out.println("\n¿Desde dónde salís?");
            origenId = seleccionarInterseccion();
            if (origenId == null) return;
        }

        System.out.println("\n¿A dónde vas?");
        String destinoId = seleccionarInterseccion();
        if (destinoId == null) return;

        if (origenId.equals(destinoId)) {
            System.out.println("Origen y destino son iguales.");
            return;
        }

        boolean exito;
        switch (tipo) {
            case "TRAMOS": exito = sistema.calcularRutaMenosTramos(origenId, destinoId); break;
            case "RAPIDA": exito = sistema.calcularRutaMasRapida(origenId, destinoId); break;
            default:       exito = sistema.calcularRutaMasCorta(origenId, destinoId); break;
        }

        if (exito) {
            System.out.println("\n¿Registrar destino como tu posición al llegar? (s/n)");
            System.out.print("> ");
            if (scanner.nextLine().trim().equalsIgnoreCase("s")) {
                sistema.informarLlegadaADestino(destinoId);
            }
        }
    }

    private static String seleccionarInterseccion() {
        String posActual = sistema.getInterseccionActualCiudadano();

        ListaEnlazada<Interseccion> lista = sistema.getIntersecciones();
        ListaEnlazada<String> ids = new ListaEnlazada<>();

        int numero = 1;
        Nodo<Interseccion> aux = lista.getCabeza();
        while (aux != null) {
            String marcador = aux.dato.getId().equals(posActual) ? " ← posición actual" : "";
            System.out.println("  " + numero + ". " + aux.dato.getNombre() + marcador);
            ids.insertarFinal(aux.dato.getId());
            numero++;
            aux = aux.siguiente;
        }

        int eleccion = 0;
        while (eleccion < 1 || eleccion > ids.tamanio()) {
            System.out.print("Seleccione (1-" + ids.tamanio() + "): ");
            eleccion = leerEntero();
            if (eleccion < 1 || eleccion > ids.tamanio())
                System.out.println("Opción inválida.");
        }

        Nodo<String> nodo = ids.getCabeza();
        for (int i = 1; i < eleccion; i++) nodo = nodo.siguiente;
        return nodo.dato;
    }

    // ===== ACCIONES OPERADOR =====

    private static void gestionarDispositivo(boolean activar) {
        sistema.mostrarDispositivos();
        String id = leerTexto("ID del dispositivo: ");
        boolean resultado = activar
                ? sistema.activarDispositivo(id)
                : sistema.desactivarDispositivo(id);
        System.out.println(resultado ? "Operación exitosa." : "No se encontró el dispositivo.");
    }

    private static void cambiarEstadoSemaforo() {
        sistema.mostrarDispositivos();
        String id = leerTexto("ID del semáforo: ");
        System.out.println("1. ROJO   2. AMARILLO   3. VERDE");

        int opcion;
        do {
            System.out.print("Seleccione estado (1-3): ");
            opcion = leerEntero();
            if (opcion < 1 || opcion > 3) System.out.println("Opción inválida.");
        } while (opcion < 1 || opcion > 3);

        Semaforo.Estado estado;
        switch (opcion) {
            case 1:  estado = Semaforo.Estado.ROJO; break;
            case 2:  estado = Semaforo.Estado.AMARILLO; break;
            default: estado = Semaforo.Estado.VERDE; break;
        }

        boolean resultado = sistema.cambiarEstadoSemaforo(id, estado);
        System.out.println(resultado ? "Estado actualizado." : "No se encontró el semáforo.");
    }

    private static void registrarSemaforo() {
        sistema.mostrarIntersecciones();
        String interseccionId = pedirInterseccion("intersección donde instalar el semáforo");
        if (interseccionId == null) return;

        int tiempoVerde    = leerEnteroPositivo("Tiempo verde (seg): ");
        int tiempoAmarillo = leerEnteroPositivo("Tiempo amarillo (seg): ");
        int tiempoRojo     = leerEnteroPositivo("Tiempo rojo (seg): ");

        String id = "S" + (sistema.cantidadSemaforos() + 1);        // La dirección se genera desde la intersección, sin pedirla al usuario
        boolean resultado = sistema.registrarSemaforo(
                new Semaforo(id, new Direccion(interseccionId, 0), interseccionId,
                        tiempoVerde, tiempoAmarillo, tiempoRojo));
        System.out.println(resultado
                ? "Semáforo " + id + " registrado correctamente."
                : "Error al registrar el semáforo.");
    }

    private static void registrarCamara() {
        sistema.mostrarIntersecciones();
        String interseccionId = pedirInterseccion("intersección donde instalar la cámara");
        if (interseccionId == null) return;

        String tipo = "";
        while (tipo.isEmpty()) {
            System.out.println("Tipo de cámara:");
            System.out.println("  1. Velocidad");
            System.out.println("  2. Flujo");
            System.out.println("  3. Seguridad");
            System.out.print("Seleccione (1-3): ");
            int opTipo = leerEntero();
            switch (opTipo) {
                case 1: tipo = "velocidad"; break;
                case 2: tipo = "flujo"; break;
                case 3: tipo = "seguridad"; break;
                default: System.out.println("Opción inválida.");
            }
        }

        String id = "C" + (sistema.cantidadCamaras() + 1);
        boolean resultado = sistema.registrarCamara(
                new Camara(id, new Direccion(interseccionId, 0), interseccionId, tipo));
        System.out.println(resultado
                ? "Cámara " + id + " registrada correctamente."
                : "Error al registrar la cámara.");
    }

    private static void gestionarBloqueo(boolean bloquear) {
        sistema.mostrarCallesDisponibles();

        String nombreCalle = leerTexto(bloquear
                ? "Nombre de la calle a bloquear: "
                : "Nombre de la calle a desbloquear: ");

        String nombreReal = sistema.getNombreCalle(nombreCalle);

        boolean resultado = bloquear
                ? sistema.bloquearCallePorNombre(nombreCalle)
                : sistema.desbloquearCallePorNombre(nombreCalle);

        if (!resultado) {
            System.out.println("No se encontró la calle '" + nombreReal + "'.");
        }
    }

    private static void registrarDemora() {
        System.out.println("\nDemoras activas:");
        sistema.mostrarCallesConDemora();

        System.out.println("\n1. Registrar demora");
        System.out.println("2. Cancelar demora");
        System.out.println("0. Volver");
        System.out.print("Seleccione: ");
        int opcion = leerEntero();
        if (opcion == 0) return;
        if (opcion != 1 && opcion != 2) {
            System.out.println("Opción inválida.");
            return;
        }

        System.out.println("\n¿Desde qué intersección sale la calle?");
        String origenId = seleccionarInterseccion();
        if (origenId == null) return;

        System.out.println("\n¿A qué intersección llega la calle?");
        String destinoId = seleccionarInterseccion();
        if (destinoId == null) return;

        if (origenId.equals(destinoId)) {
            System.out.println("Origen y destino no pueden ser iguales.");
            return;
        }

        if (opcion == 2) {
            boolean resultado = sistema.registrarDemoraEnCalle(origenId, destinoId, 0);
            System.out.println(resultado
                    ? "Demora cancelada. Calle vuelve a tiempo normal."
                    : "No había demora registrada en esa calle.");
            return;
        }

        System.out.print("Minutos de demora extra: ");
        int minutos = leerEntero();
        if (minutos <= 0) {
            System.out.println("Ingrese un número mayor a 0.");
            return;
        }

        boolean resultado = sistema.registrarDemoraEnCalle(origenId, destinoId, minutos);
        System.out.println(resultado
                ? "Demora de " + minutos + " min registrada. Afecta cálculo de ruta más rápida."
                : "No existe calle entre esas intersecciones.");
    }

    private static void atenderEmergencia() {
        System.out.println("\nEmergencias pendientes:");
        sistema.mostrarEmergencias();

        if (!sistema.hayEmergencias()) {
            return; // no pregunta nada si no hay emergencias
        }

        String confirmar = leerTexto("¿Atender la más prioritaria? (s/n): ");
        if (confirmar.equalsIgnoreCase("s")) sistema.atenderEmergencia();
        else System.out.println("Operación cancelada.");
    }

    /*
     * El usuario puede ingresar:
     *   - Nombre de calle (ej: "santa fe")  → se resuelve a ID
     *   - ID directamente (ej: "I1")        → se usa directo
     * En ambos casos se acepta cualquier capitalización.
     */
    private static String pedirInterseccion(String contexto) {
        System.out.println("Ingrese nombre de calle, nombre del cruce o ID de la " + contexto + ":");
        System.out.print("> ");
        String entrada = scanner.nextLine().trim();

        if (entrada.isEmpty()) {
            System.out.println("Entrada vacía, operación cancelada.");
            return null;
        }

        // Primero verifica si es un nombre de calle con múltiples intersecciones
        ListaEnlazada<String> coincidencias = sistema.buscarInterseccionesPorCalle(entrada);

        if (!coincidencias.estaVacia() && coincidencias.tamanio() > 1) {
            // Hay ambigüedad — pregunta cuál
            System.out.println("La calle '" + entrada + "' pasa por varias intersecciones:");
            int numero = 1;
            Nodo<String> aux = coincidencias.getCabeza();
            while (aux != null) {
                String nombre = sistema.getNombreInterseccion(aux.dato);
                System.out.println("  " + numero + ". " + aux.dato + " (" + nombre + ")");
                numero++;
                aux = aux.siguiente;
            }

            int eleccion = 0;
            while (eleccion < 1 || eleccion > coincidencias.tamanio()) {
                System.out.print("Seleccione (1-" + coincidencias.tamanio() + "): ");
                eleccion = leerEntero();
                if (eleccion < 1 || eleccion > coincidencias.tamanio())
                    System.out.println("Opción inválida.");
            }

            // Obtiene el ID elegido
            aux = coincidencias.getCabeza();
            for (int i = 1; i < eleccion; i++) aux = aux.siguiente;
            String id = aux.dato;
            System.out.println("  → " + id);
            return id;
        }

        if (!coincidencias.estaVacia()) {
            // Solo una intersección con ese nombre de calle
            String id = coincidencias.getCabeza().dato;
            System.out.println("  → " + id);
            return id;
        }

        // Si no encontró por calle, intenta por nombre de intersección o ID
        String id = sistema.resolverEntradaInterseccion(entrada);
        if (id == null) {
            System.out.println("No se encontró ninguna intersección para: " + entrada);
            return null;
        }

        System.out.println("  → " + id);
        return id;
    }

    // ===== DATOS INICIALES =====

    private static void cargarDatosIniciales() {

        // ===== TERRITORIO =====
        sistema.crearCiudad("San Martín");

        sistema.agregarZona("Zona Norte");
        sistema.agregarZona("Zona Sur");
        sistema.agregarZona("Zona Centro");

        sistema.agregarBarrio("Zona Norte", "Barrio Belgrano");
        sistema.agregarBarrio("Zona Norte", "Barrio San Martín");
        sistema.agregarBarrio("Zona Sur",   "Barrio Lavalle");
        sistema.agregarBarrio("Zona Centro","Barrio Corrientes");
        sistema.agregarBarrio("Zona Centro","Barrio Rivadavia");

        sistema.agregarManzana("Barrio Belgrano",   "Manzana 1");
        sistema.agregarManzana("Barrio Belgrano",   "Manzana 2");
        sistema.agregarManzana("Barrio San Martín", "Manzana 3");
        sistema.agregarManzana("Barrio Lavalle",    "Manzana 4");
        sistema.agregarManzana("Barrio Corrientes", "Manzana 5");
        sistema.agregarManzana("Barrio Rivadavia",  "Manzana 6");

        // ===== INTERSECCIONES =====
        //         ID    Nombre del cruce
// ===== INTERSECCIONES =====
        sistema.agregarInterseccion(new Interseccion("I1", "Rivadavia y Belgrano", "Zona Centro"));
        sistema.agregarInterseccion(new Interseccion("I2", "Rivadavia y San Martín", "Zona Centro"));
        sistema.agregarInterseccion(new Interseccion("I3", "Belgrano y San Martín", "Zona Norte"));
        sistema.agregarInterseccion(new Interseccion("I4", "Rivadavia y Lavalle", "Zona Sur"));
        sistema.agregarInterseccion(new Interseccion("I5", "Corrientes y San Martín", "Zona Norte"));
        sistema.agregarInterseccion(new Interseccion("I6", "Corrientes y Lavalle", "Zona Sur"));
        sistema.agregarInterseccion(new Interseccion("I7", "Mitre y San Martín", "Zona Norte"));

// ===== CALLES =====
// Rivadavia: rápida pero larga en metros
        sistema.agregarCalle("I1", "I2", "Rivadavia",   1200, 1800, 2000, 2, false);

// Belgrano: lenta y corta
        sistema.agregarCalle("I1", "I4", "Belgrano",    2400, 2000,  300, 9, true);

// San Martín: doble mano
        sistema.agregarCalle("I2", "I5", "San Martín",  1800, 1400,  400, 4, true);

// San Martín norte
        sistema.agregarCalle("I2", "I3", "San Martín",  1800, 2200,  350, 3, false);

// Lavalle: muchos metros, rápida
        sistema.agregarCalle("I6", "I4", "Lavalle",     1000, 1400, 3000, 2, false);

// Corrientes
        sistema.agregarCalle("I6", "I5", "Corrientes",   800, 1200,  500, 5, false);

// Atajo directo I1->I5: 1 solo tramo, tiempo medio, distancia enorme
        sistema.agregarCalle("I1", "I5", "Autopista",    100,  200, 8000, 6, false);

// Mitre: conecta I3 con I7, pocos metros
        sistema.agregarCalle("I3", "I7", "Mitre",       2800, 3200,  100, 8, false);
        sistema.agregarCalle("I7", "I5", "Mitre",       3200, 1400,  150, 9, false);

        // ===== SEMÁFOROS =====
        sistema.registrarSemaforo(new Semaforo("S1",
                new Direccion("Rivadavia", 1200), "I1", 30, 5, 25));
        sistema.registrarSemaforo(new Semaforo("S2",
                new Direccion("Rivadavia", 1800), "I2", 25, 5, 30));
        sistema.registrarSemaforo(new Semaforo("S3",
                new Direccion("Belgrano",  2800), "I3", 20, 5, 35));
        sistema.registrarSemaforo(new Semaforo("S4",
                new Direccion("Belgrano",  2000), "I4", 30, 5, 25));
        sistema.registrarSemaforo(new Semaforo("S5",
                new Direccion("San Martín",1400), "I5", 25, 5, 30));
        sistema.registrarSemaforo(new Semaforo("S6",
                new Direccion("Lavalle",   1000), "I6", 20, 5, 35));

        // ===== CÁMARAS =====
        sistema.registrarCamara(new Camara("C1",
                new Direccion("Rivadavia", 1500), "I1", "velocidad"));
        sistema.registrarCamara(new Camara("C2",
                new Direccion("San Martín",1600), "I2", "flujo"));
        sistema.registrarCamara(new Camara("C3",
                new Direccion("Corrientes",1000), "I6", "seguridad"));

        System.out.println("Sistema iniciado.");
        System.out.println();
        System.out.println("Mapa cargado:");
        System.out.println("  I1: Rivadavia y Belgrano");
        System.out.println("  I2: Rivadavia y San Martín");
        System.out.println("  I3: Belgrano y San Martín");
        System.out.println("  I4: Rivadavia y Lavalle");
        System.out.println("  I5: Corrientes y San Martín");
        System.out.println("  I6: Corrientes y Lavalle");
        System.out.println("  I7: Mitre y San Martín");
        System.out.println();
        System.out.println("Calles: Rivadavia | Belgrano | San Martín | Lavalle | Corrientes | Autopista | Mitre");    }

    // ===== UTILIDADES =====

    private static int leerEntero() {
        while (true) {
            try { return Integer.parseInt(scanner.nextLine().trim()); }
            catch (NumberFormatException e) { System.out.print("Ingrese un número válido: "); }
        }
    }

    private static String leerTexto(String mensaje) {
        String texto = "";
        while (texto.isEmpty()) {
            System.out.print(mensaje);
            texto = scanner.nextLine().trim();
            if (texto.isEmpty()) System.out.println("El campo no puede estar vacío.");
        }
        return texto;
    }

    private static int leerEnteroPositivo(String mensaje) {
        int valor = 0;
        while (valor <= 0) {
            System.out.print(mensaje);
            valor = leerEntero();
            if (valor <= 0) System.out.println("Ingrese un número mayor a 0.");
        }
        return valor;
    }

    private static int leerGravedad() {
        int gravedad = 0;
        while (gravedad < 1 || gravedad > 5) {
            System.out.print("Gravedad (1-5): ");
            gravedad = leerEntero();
            if (gravedad < 1 || gravedad > 5) System.out.println("La gravedad debe ser entre 1 y 5.");
        }
        return gravedad;
    }

    private static void deshacerCambio() {
        Cambio ultimo = sistema.obtenerUltimoCambio();

        if (ultimo == null) {
            System.out.println("No hay cambios para deshacer.");
            return;
        }

        System.out.println("\nÚltimo cambio registrado:");
        System.out.println("  " + ultimo);
        System.out.print("¿Está seguro de deshacer este cambio? (s/n): ");
        String confirmar = scanner.nextLine().trim();

        if (!confirmar.equalsIgnoreCase("s")) {
            System.out.println("Operación cancelada.");
            return;
        }

        boolean resultado = sistema.deshacerUltimoCambio();
        if (resultado) {
            System.out.println("Cambio deshecho: " + ultimo);
        } else {
            System.out.println("No se pudo deshacer el cambio.");
        }
    }

}