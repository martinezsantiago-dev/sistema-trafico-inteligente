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
            System.out.println("6. Reportar demora en tránsito");
            System.out.println("7. Ver demoras activas");
            System.out.println("0. Volver");
            System.out.print("Seleccione: ");
            opcion = leerEntero();

            switch (opcion) {
                case 1: reportarEmergencia(); break;
                case 2: calcularRuta("TRAMOS"); break;
                case 3: calcularRuta("RAPIDA"); break;
                case 4: calcularRuta("CORTA"); break;
                case 5: sistema.mostrarTerritorio(); break;
                case 6: reportarDemoraCiudadano(); break;
                case 7: sistema.mostrarCallesConDemora(); break;
                case 0: break;
                default: System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }

    // Versión reducida de registrarDemora para el ciudadano: solo un tramo puntual, sin cancelar ni "toda la calle"
    private static void reportarDemoraCiudadano() {
        System.out.print("\nMinutos de demora que notaste: ");
        int minutos = leerEntero();
        if (minutos <= 0) {
            System.out.println("Ingrese un número mayor a 0.");
            return;
        }

        String nombreCalle = seleccionarCalle();
        if (nombreCalle == null) return;



        ListaEnlazada<Calle> tramos = sistema.obtenerTramosDeCalle(nombreCalle);
        if (tramos.estaVacia()) {
            System.out.println("No se encontraron tramos para esa calle.");
            return;
        }

        Calle tramo = tramos.tamanio() == 1 ? tramos.getCabeza().dato : seleccionarTramo(tramos);
        if (tramo == null) return;

        aplicarDemoraTramo(tramo, minutos, false);
    }

    // ===== MENÚ OPERADOR =====

    private static void menuOperador() {
        int opcion;
        do {
            System.out.println("\n--- MENÚ OPERADOR ---");
            System.out.println("1. Gestionar dispositivos");
            System.out.println("2. Gestionar calles");
            System.out.println("3. Gestionar emergencias");
            System.out.println("4. Ver y deshacer cambios");
            System.out.println("0. Volver");
            System.out.print("Seleccione: ");
            opcion = leerEntero();

            switch (opcion) {
                case 1: menuDispositivos(); break;
                case 2: menuCalles(); break;
                case 3: menuEmergencias(); break;
                case 4: menuCambios(); break;
                case 0: break;
                default: System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }

    private static void menuDispositivos() {
        int opcion;
        do {
            System.out.println("\n--- GESTIONAR DISPOSITIVOS ---");
            System.out.println("1. Ver dispositivos registrados");
            System.out.println("2. Activar dispositivo");
            System.out.println("3. Desactivar dispositivo");
            System.out.println("4. Cambiar estado de semáforo");
            System.out.println("5. Registrar semáforo");
            System.out.println("6. Registrar cámara");
            System.out.println("0. Volver");
            System.out.print("Seleccione: ");
            opcion = leerEntero();

            switch (opcion) {
                case 1: sistema.mostrarDispositivos(); break;
                case 2: gestionarDispositivo(true); break;
                case 3: gestionarDispositivo(false); break;
                case 4: cambiarEstadoSemaforo(); break;
                case 5: registrarSemaforo(); break;
                case 6: registrarCamara(); break;
                case 0: break;
                default: System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }

    private static void menuCalles() {
        int opcion;
        do {
            System.out.println("\n--- GESTIONAR CALLES ---");
            System.out.println("1. Ver red vial");
            System.out.println("2. Bloquear calle");
            System.out.println("3. Desbloquear calle");
            System.out.println("4. Gestionar demoras");
            System.out.println("0. Volver");
            System.out.print("Seleccione: ");
            opcion = leerEntero();

            switch (opcion) {
                case 1: sistema.mostrarRedVial(); break;
                case 2: gestionarBloqueo(true); break;
                case 3: gestionarBloqueo(false); break;
                case 4: registrarDemora(); break;
                case 0: break;
                default: System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }

    private static void menuEmergencias() {
        int opcion;
        do {
            System.out.println("\n--- GESTIONAR EMERGENCIAS ---");
            System.out.println("1. Ver emergencias pendientes");
            System.out.println("2. Atender próxima emergencia");
            System.out.println("3. Reportar emergencia");
            System.out.println("4. Reporte de emergencias por zona");
            System.out.println("5. Ver/resolver emergencias en curso");
            System.out.println("0. Volver");
            System.out.print("Seleccione: ");
            opcion = leerEntero();

            switch (opcion) {
                case 1: sistema.mostrarEmergencias(); break;
                case 2: atenderEmergencia(); break;
                case 3: reportarEmergencia(); break;
                case 4: sistema.reporteEmergenciasPorZona(); break;
                case 5: resolverEmergenciaEnCurso(); break;
                case 0: break;
                default: System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }

    private static void menuCambios() {
        int opcion;
        do {
            System.out.println("\n--- VER Y DESHACER CAMBIOS ---");
            System.out.println("1. Ver historial de cambios");
            System.out.println("2. Deshacer último cambio");
            System.out.println("0. Volver");
            System.out.print("Seleccione: ");
            opcion = leerEntero();

            switch (opcion) {
                case 1: mostrarHistorialLegible(); break;
                case 2: deshacerCambio(); break;
                case 0: break;
                default: System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }

    // ===== ACCIONES CIUDADANO =====

    private static void reportarEmergencia() {
        System.out.println("\n¿En qué intersección ocurrió el incidente?");
        String interseccionId = seleccionarInterseccion();
        if (interseccionId == null) return;

        // Obtener zona desde la intersección
        String zona = sistema.getZonaDeInterseccion(interseccionId);

        String tipoIncidente = seleccionarTipoIncidente();
        if (tipoIncidente == null) return;

        boolean hayHeridos = false;
        String tipoVehiculoRequerido = null;

        if (tipoIncidente.equals("CHOQUE")) {
            hayHeridos = leerTexto("¿Hay heridos? (s/n): ").equalsIgnoreCase("s");
        } else if (tipoIncidente.equals("OTRO")) {
            if (leerTexto("¿Requiere despacho de vehículo? (s/n): ").equalsIgnoreCase("s")) {
                tipoVehiculoRequerido = seleccionarTipoVehiculo();
                if (tipoVehiculoRequerido == null) return;
            }
        }

        int gravedad = leerGravedad();
        if (gravedad == 0) { System.out.println("Operación cancelada."); return; }
        String descripcion = leerTexto("Descripción del incidente: ");
        String id = "E" + contadorEmergencias++;

        Emergencia emergencia = new Emergencia(id, gravedad, interseccionId, descripcion, zona,
                tipoIncidente, hayHeridos, tipoVehiculoRequerido);
        sistema.reportarEmergencia(emergencia);
    }

    // Devuelve la constante de tipo de incidente elegida, o null si la opción fue inválida (cancela el reporte)
    private static String seleccionarTipoIncidente() {
        System.out.println("\nTipo de incidente:");
        System.out.println("1. Choque");
        System.out.println("2. Incendio");
        System.out.println("3. Semáforo fuera de servicio");
        System.out.println("4. Cámara fuera de servicio");
        System.out.println("5. Calle bloqueada");
        System.out.println("6. Otro");
        System.out.print("Seleccione: ");
        int opcion = leerEntero();

        switch (opcion) {
            case 1: return "CHOQUE";
            case 2: return "INCENDIO";
            case 3: return "SEMAFORO_ROTO";
            case 4: return "CAMARA_ROTA";
            case 5: return "CALLE_BLOQUEADA";
            case 6: return "OTRO";
            default:
                System.out.println("Opción inválida.");
                return null;
        }
    }

    // Devuelve el tipo de vehículo elegido (para el caso "Otro"), o null si la opción fue inválida
    private static String seleccionarTipoVehiculo() {
        System.out.println("\n¿Qué tipo de vehículo se necesita?");
        System.out.println("1. Ambulancia");
        System.out.println("2. Patrulla");
        System.out.println("3. Bomberos");
        System.out.println("4. Técnico");
        System.out.print("Seleccione: ");
        int opcion = leerEntero();

        switch (opcion) {
            case 1: return "ambulancia";
            case 2: return "patrulla";
            case 3: return "bomberos";
            case 4: return "tecnico";
            default:
                System.out.println("Opción inválida.");
                return null;
        }
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

        Dispositivo dispositivo = sistema.obtenerDispositivo(id);
        if (!(dispositivo instanceof Semaforo)) {
            System.out.println("No existe un semáforo con ID '" + id + "'.");
            return;
        }

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
        System.out.println(resultado ? "Estado actualizado." : "No se pudo actualizar el estado.");
    }

    private static void registrarSemaforo() {
        System.out.println("\n¿En qué intersección instalar el semáforo?");
        String interseccionId = seleccionarInterseccion();
        if (interseccionId == null) return;

        Direccion ubicacion = seleccionarUbicacionEnInterseccion(interseccionId);

        int tiempoVerde = leerEnteroPositivo("Tiempo verde (seg)");
        if (tiempoVerde == 0) { System.out.println("Operación cancelada."); return; }
        int tiempoAmarillo = leerEnteroPositivo("Tiempo amarillo (seg)");
        if (tiempoAmarillo == 0) { System.out.println("Operación cancelada."); return; }
        int tiempoRojo = leerEnteroPositivo("Tiempo rojo (seg)");
        if (tiempoRojo == 0) { System.out.println("Operación cancelada."); return; }

        String id = "S" + (sistema.cantidadSemaforos() + 1);
        boolean resultado = sistema.registrarSemaforo(
                new Semaforo(id, ubicacion, interseccionId,
                        tiempoVerde, tiempoAmarillo, tiempoRojo));
        System.out.println(resultado
                ? "Semáforo " + id + " registrado correctamente."
                : "Error al registrar el semáforo.");
    }

    private static void registrarCamara() {
        System.out.println("\n¿En qué intersección instalar la cámara?");
        String interseccionId = seleccionarInterseccion();
        if (interseccionId == null) return;

        Direccion ubicacion = seleccionarUbicacionEnInterseccion(interseccionId);

        String tipo = "";
        while (tipo.isEmpty()) {
            System.out.println("Tipo de cámara:");
            System.out.println("  1. Velocidad");
            System.out.println("  2. Flujo");
            System.out.println("  3. Seguridad");
            System.out.println("  0. Cancelar");
            System.out.print("Seleccione (0-3): ");
            int opTipo = leerEntero();
            switch (opTipo) {
                case 1: tipo = "velocidad"; break;
                case 2: tipo = "flujo"; break;
                case 3: tipo = "seguridad"; break;
                case 0: return;
                default: System.out.println("Opción inválida.");
            }
        }

        String id = "C" + (sistema.cantidadCamaras() + 1);
        boolean resultado = sistema.registrarCamara(
                new Camara(id, ubicacion, interseccionId, tipo));
        System.out.println(resultado
                ? "Cámara " + id + " registrada correctamente."
                : "Error al registrar la cámara.");
    }

    // La altura no se le pide al operador: se toma la altura de origen de la calle elegida,
    // que ya representa la altura real de esa calle justo en esta intersección.
    private static Direccion seleccionarUbicacionEnInterseccion(String interseccionId) {
        ListaEnlazada<Calle> calles = sistema.getCallesAdyacentes(interseccionId);
        if (calles.estaVacia()) {
            return new Direccion(sistema.getNombreInterseccion(interseccionId), null); // es un caso borde. solo pondra altura null si no tengo cargada ninguna calle en una interseccion, cosa q no pasa con nuestro ejemplo.
        }

        System.out.println("\n¿Sobre qué calle está el dispositivo?");
        int numero = 1;
        Nodo<Calle> aux = calles.getCabeza();
        while (aux != null) {
            System.out.println("  " + numero + ". " + aux.dato.getNombre() + " (altura " + aux.dato.getAlturaOrigen() + ")");
            numero++;
            aux = aux.siguiente;
        }

        int eleccion = -1;
        while (eleccion < 1 || eleccion > calles.tamanio()) {
            System.out.print("Seleccione (1-" + calles.tamanio() + "): ");
            eleccion = leerEntero();
            if (eleccion < 1 || eleccion > calles.tamanio())
                System.out.println("Opción inválida.");
        }

        Nodo<Calle> nodo = calles.getCabeza();
        for (int i = 1; i < eleccion; i++) nodo = nodo.siguiente;
        return new Direccion(nodo.dato.getNombre(), nodo.dato.getAlturaOrigen());
    }

    private static void gestionarBloqueo(boolean bloquear) {
        String nombreCalle = seleccionarCalle();
        if (nombreCalle == null) return;

        String estado = sistema.estadoCalle(nombreCalle);
        if (bloquear && estado.equals("BLOQUEADA")) {
            System.out.println("La calle '" + nombreCalle + "' ya está bloqueada por completo. No se hizo ningún cambio.");
            return;
        }
        if (!bloquear && estado.equals("LIBRE")) {
            System.out.println("La calle '" + nombreCalle + "' ya está libre. No se hizo ningún cambio.");
            return;
        }

        ListaEnlazada<Calle> tramos = sistema.obtenerTramosDeCalle(nombreCalle);
        if (tramos.estaVacia()) {
            System.out.println("No se encontraron tramos para esa calle.");
            return;
        }

        if (tramos.tamanio() == 1) {
            aplicarBloqueoTramo(tramos.getCabeza().dato, bloquear);
            return;
        }

        System.out.println("\nLa calle '" + nombreCalle + "' tiene " + tramos.tamanio() + " tramos.");
        System.out.println("1. " + (bloquear ? "Bloquear" : "Desbloquear") + " toda la calle");
        System.out.println("2. " + (bloquear ? "Bloquear" : "Desbloquear") + " un tramo específico");
        System.out.println("0. Cancelar");
        System.out.print("Seleccione: ");
        int opcion = leerEntero();

        if (opcion == 1) {
            boolean resultado = bloquear
                    ? sistema.bloquearCallePorNombre(nombreCalle)
                    : sistema.desbloquearCallePorNombre(nombreCalle);
            System.out.println(resultado
                    ? "Calle '" + nombreCalle + "' " + (bloquear ? "bloqueada" : "desbloqueada") + " completa."
                    : "No se pudo " + (bloquear ? "bloquear" : "desbloquear") + " la calle '" + nombreCalle + "'.");
        } else if (opcion == 2) {
            Calle tramo = seleccionarTramo(tramos);
            if (tramo == null) return;
            aplicarBloqueoTramo(tramo, bloquear);
        } else if (opcion != 0) {
            System.out.println("Opción inválida.");
        }
    }

    // Muestra los tramos de una calle numerados (con intersecciones, alturas y estado) y devuelve el elegido, o null si se cancela
    private static Calle seleccionarTramo(ListaEnlazada<Calle> tramos) {
        System.out.println("\nTramos disponibles:");
        int numero = 1;
        Nodo<Calle> aux = tramos.getCabeza();
        while (aux != null) {
            Calle c = aux.dato;
            String estado = sistema.isTramoBloqueado(c.getOrigenId(), c.getDestinoId()) ? "BLOQUEADO" : "libre";
            System.out.println("  " + numero + ". " + sistema.getNombreInterseccion(c.getOrigenId())
                    + " <-> " + sistema.getNombreInterseccion(c.getDestinoId())
                    + " (alturas " + c.getAlturaOrigen() + "-" + c.getAlturaDestino() + ") [" + estado + "]");
            numero++;
            aux = aux.siguiente;
        }
        System.out.println("  0. Cancelar");

        int eleccion = -1;
        while (eleccion < 0 || eleccion > tramos.tamanio()) {
            System.out.print("Seleccione (0-" + tramos.tamanio() + "): ");
            eleccion = leerEntero();
            if (eleccion < 0 || eleccion > tramos.tamanio())
                System.out.println("Opción inválida.");
        }
        if (eleccion == 0) return null;

        Nodo<Calle> nodo = tramos.getCabeza();
        for (int i = 1; i < eleccion; i++) nodo = nodo.siguiente;
        return nodo.dato;
    }

    private static void aplicarBloqueoTramo(Calle tramo, boolean bloquear) {
        String origenId = tramo.getOrigenId();
        String destinoId = tramo.getDestinoId();
        boolean ambosSentidos = true;

        if (sistema.esTramoDobleMano(origenId, destinoId)) {
            System.out.println("\nEste tramo es doble mano. ¿Qué sentido " + (bloquear ? "bloqueás" : "desbloqueás") + "?");
            System.out.println("1. Ambos sentidos");
            System.out.println("2. Solo " + sistema.getNombreInterseccion(origenId) + " -> " + sistema.getNombreInterseccion(destinoId));
            System.out.println("3. Solo " + sistema.getNombreInterseccion(destinoId) + " -> " + sistema.getNombreInterseccion(origenId));
            System.out.print("Seleccione: ");
            int sentido = leerEntero();
            if (sentido == 2) {
                ambosSentidos = false;
            } else if (sentido == 3) {
                ambosSentidos = false;
                String temp = origenId;
                origenId = destinoId;
                destinoId = temp;
            } else if (sentido != 1) {
                System.out.println("Opción inválida. Se canceló la operación.");
                return;
            }
        }

        if (sistema.isTramoBloqueado(origenId, destinoId) == bloquear) {
            System.out.println("Ese tramo ya estaba " + (bloquear ? "bloqueado" : "libre") + ". No se hizo ningún cambio.");
            return;
        }

        boolean resultado = bloquear
                ? sistema.bloquearTramo(origenId, destinoId, ambosSentidos)
                : sistema.desbloquearTramo(origenId, destinoId, ambosSentidos);

        boolean mostrarAmbosSentidos = ambosSentidos && sistema.esTramoDobleMano(origenId, destinoId);
        System.out.println(resultado
                ? "Tramo " + sistema.getNombreInterseccion(origenId)
                    + (mostrarAmbosSentidos ? " <-> " : " -> ") + sistema.getNombreInterseccion(destinoId)
                    + " " + (bloquear ? "bloqueado" : "desbloqueado") + " correctamente."
                : "No se pudo procesar el tramo.");
    }

    // Muestra las calles numeradas (con su estado) y devuelve el nombre elegido, o null si se cancela
    private static String seleccionarCalle() {
        ListaEnlazada<String> nombres = sistema.obtenerNombresCalles();
        if (nombres.estaVacia()) {
            System.out.println("No hay calles cargadas.");
            return null;
        }

        System.out.println("\nCalles disponibles:");
        int numero = 1;
        Nodo<String> aux = nombres.getCabeza();
        while (aux != null) {
            String estado = sistema.estadoCalle(aux.dato);
            System.out.println("  " + numero + ". " + aux.dato + " (" + estado + ")");
            numero++;
            aux = aux.siguiente;
        }
        System.out.println("  0. Cancelar");

        int eleccion = -1;
        while (eleccion < 0 || eleccion > nombres.tamanio()) {
            System.out.print("Seleccione (0-" + nombres.tamanio() + "): ");
            eleccion = leerEntero();
            if (eleccion < 0 || eleccion > nombres.tamanio())
                System.out.println("Opción inválida.");
        }
        if (eleccion == 0) return null;

        Nodo<String> nodo = nombres.getCabeza();
        for (int i = 1; i < eleccion; i++) nodo = nodo.siguiente;
        return nodo.dato;
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
        boolean cancelar = (opcion == 2);

        int minutos = 0;
        if (!cancelar) {
            System.out.print("Minutos de demora extra: ");
            minutos = leerEntero();
            if (minutos <= 0) {
                System.out.println("Ingrese un número mayor a 0.");
                return;
            }
        }

        String nombreCalle = seleccionarCalle();
        if (nombreCalle == null) return;

        ListaEnlazada<Calle> tramos = sistema.obtenerTramosDeCalle(nombreCalle);
        if (tramos.estaVacia()) {
            System.out.println("No se encontraron tramos para esa calle.");
            return;
        }

        if (tramos.tamanio() == 1) {
            aplicarDemoraTramo(tramos.getCabeza().dato, minutos, cancelar);
            return;
        }

        System.out.println("\nLa calle '" + nombreCalle + "' tiene " + tramos.tamanio() + " tramos.");
        System.out.println("1. " + (cancelar ? "Cancelar demora en" : "Registrar demora en") + " toda la calle");
        System.out.println("2. " + (cancelar ? "Cancelar demora en" : "Registrar demora en") + " un tramo específico");
        System.out.println("0. Cancelar");
        System.out.print("Seleccione: ");
        int subOpcion = leerEntero();

        if (subOpcion == 1) {
            Nodo<Calle> aux = tramos.getCabeza();
            while (aux != null) {
                aplicarDemoraDireccion(aux.dato.getOrigenId(), aux.dato.getDestinoId(), minutos, cancelar);
                if (sistema.esTramoDobleMano(aux.dato.getOrigenId(), aux.dato.getDestinoId())) {
                    aplicarDemoraDireccion(aux.dato.getDestinoId(), aux.dato.getOrigenId(), minutos, cancelar);
                }
                aux = aux.siguiente;
            }
        } else if (subOpcion == 2) {
            Calle tramo = seleccionarTramo(tramos);
            if (tramo == null) return;
            aplicarDemoraTramo(tramo, minutos, cancelar);
        } else if (subOpcion != 0) {
            System.out.println("Opción inválida.");
        }
    }

    private static void aplicarDemoraTramo(Calle tramo, int minutos, boolean cancelar) {
        String origenId = tramo.getOrigenId();
        String destinoId = tramo.getDestinoId();
        boolean ambosSentidos = true;

        if (sistema.esTramoDobleMano(origenId, destinoId)) {
            System.out.println("\nEste tramo es doble mano. ¿A qué sentido aplicás " + (cancelar ? "la cancelación" : "la demora") + "?");
            System.out.println("1. Ambos sentidos");
            System.out.println("2. Solo " + sistema.getNombreInterseccion(origenId) + " -> " + sistema.getNombreInterseccion(destinoId));
            System.out.println("3. Solo " + sistema.getNombreInterseccion(destinoId) + " -> " + sistema.getNombreInterseccion(origenId));
            System.out.print("Seleccione: ");
            int sentido = leerEntero();
            if (sentido == 2) {
                ambosSentidos = false;
            } else if (sentido == 3) {
                ambosSentidos = false;
                String temp = origenId;
                origenId = destinoId;
                destinoId = temp;
            } else if (sentido != 1) {
                System.out.println("Opción inválida. Se canceló la operación.");
                return;
            }
        }

        aplicarDemoraDireccion(origenId, destinoId, minutos, cancelar);
        if (ambosSentidos && sistema.esTramoDobleMano(origenId, destinoId)) {
            aplicarDemoraDireccion(destinoId, origenId, minutos, cancelar);
        }
    }

    private static void aplicarDemoraDireccion(String origenId, String destinoId, int minutos, boolean cancelar) {
        String etiqueta = sistema.getNombreInterseccion(origenId) + " -> " + sistema.getNombreInterseccion(destinoId);
        if (!cancelar && sistema.isTramoBloqueado(origenId, destinoId)) {
            System.out.println("El tramo " + etiqueta + " está bloqueado, no tiene sentido registrar una demora ahí.");
            return;
        }
        if (cancelar) {
            int demoraActual = sistema.getDemoraEnCalle(origenId, destinoId);
            if (demoraActual <= 0) {
                System.out.println("El tramo " + etiqueta + " no tiene demora activa.");
                return;
            }
            boolean resultado = sistema.registrarDemoraEnCalle(origenId, destinoId, 0);
            System.out.println(resultado
                    ? "Demora cancelada en " + etiqueta + "."
                    : "No se pudo cancelar la demora en " + etiqueta + ".");
        } else {
            boolean resultado = sistema.registrarDemoraEnCalle(origenId, destinoId, minutos);
            System.out.println(resultado
                    ? "Demora de " + minutos + " min registrada en " + etiqueta + "."
                    : "No se pudo registrar la demora en " + etiqueta + ".");
        }
    }

    private static void atenderEmergencia() {
        System.out.println("\nEmergencias pendientes:");
        sistema.mostrarEmergencias();

        if (!sistema.hayEmergencias()) {
            return; // no pregunta nada si no hay emergencias
        }

        String confirmar = leerTexto("¿Atender la más prioritaria? (s/n): ");
        if (!confirmar.equalsIgnoreCase("s")) {
            System.out.println("Operación cancelada.");
            return;
        }

        // Puede devolver null si no hay vehículos disponibles del tipo que corresponde;
        // en ese caso la emergencia queda pendiente en la cola, no se pierde.
        Emergencia atendida = sistema.atenderEmergencia();

        if (atendida == null) {
            if (sistema.hayEmergencias()) {
                String forzar = leerTexto("¿Desea marcarla como atendida igual, sin despachar vehículo? (s/n): ");
                if (forzar.equalsIgnoreCase("s")) {
                    atendida = sistema.forzarAtencionSinVehiculo();
                }
            }
            if (atendida == null) return;
        }

        if (atendida.getPatenteVehiculoDespachado() == null) {
            System.out.println("Emergencia " + atendida.getId() + " marcada como atendida sin despacho de vehículo.");
            return;
        }

        String resuelto = leerTexto("¿Se resolvió la emergencia en el lugar? (s/n): ");
        if (resuelto.equalsIgnoreCase("s")) {
            sistema.resolverEmergencia(atendida);
        } else {
            System.out.println("El vehículo " + atendida.getTipoVehiculoDespachado()
                    + " (" + atendida.getPatenteVehiculoDespachado() + ") sigue ocupado en el lugar."
                    + " Podés resolverla más tarde desde 'Ver/resolver emergencias en curso'.");
        }
    }

    // Muestra las emergencias que ya despacharon un vehículo pero siguen sin resolverse,
    // y permite cerrar una de ellas (libera el vehículo asociado).
    private static void resolverEmergenciaEnCurso() {
        if (!sistema.hayEmergenciasEnCurso()) {
            System.out.println("\nNo hay emergencias en curso.");
            return;
        }

        System.out.println("\nEmergencias en curso:");
        ListaEnlazada<Emergencia> enCurso = sistema.getEmergenciasEnCurso();
        int numero = 1;
        Nodo<Emergencia> aux = enCurso.getCabeza();
        while (aux != null) {
            System.out.println("  " + numero + ". " + aux.dato);
            numero++;
            aux = aux.siguiente;
        }
        System.out.println("  0. Cancelar");

        int eleccion = -1;
        while (eleccion < 0 || eleccion > enCurso.tamanio()) {
            System.out.print("¿Cuál desea marcar como resuelta? (0-" + enCurso.tamanio() + "): ");
            eleccion = leerEntero();
            if (eleccion < 0 || eleccion > enCurso.tamanio())
                System.out.println("Opción inválida.");
        }
        if (eleccion == 0) return;

        Nodo<Emergencia> nodo = enCurso.getCabeza();
        for (int i = 1; i < eleccion; i++) nodo = nodo.siguiente;
        sistema.resolverEmergencia(nodo.dato);
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
// Rivadavia: avenida rápida, 60 km/h
        sistema.agregarCalle("I1", "I2", "Rivadavia",   1200, 1800, 2000, 2, false);

// Belgrano: calle lenta y corta, ~9 km/h
        sistema.agregarCalle("I1", "I4", "Belgrano",    2400, 2000,  300, 2, true);

// San Martín: doble mano, calle céntrica con tránsito, ~8 km/h
        sistema.agregarCalle("I2", "I5", "San Martín",  1800, 1400,  400, 3, true);

// San Martín norte, ~10 km/h
        sistema.agregarCalle("I2", "I3", "San Martín",  1800, 2200,  350, 2, false);

// Lavalle: avenida rápida, 60 km/h
        sistema.agregarCalle("I6", "I4", "Lavalle",     1000, 1400, 3000, 3, false);

// Corrientes: calle urbana, ~15 km/h
        sistema.agregarCalle("I6", "I5", "Corrientes",   800, 1200,  500, 2, false);

// Atajo directo I1->I5: 1 solo tramo, autopista a 80 km/h, distancia enorme
        sistema.agregarCalle("I1", "I5", "Autopista",    100,  200, 8000, 6, false);

// Mitre: conecta I3 con I7, tramos cortos, ~6-9 km/h
        sistema.agregarCalle("I3", "I7", "Mitre",       2800, 3200,  100, 1, false);
        sistema.agregarCalle("I7", "I5", "Mitre",       3200, 1400,  150, 1, false);

        // ===== SEMÁFOROS =====
        sistema.registrarSemaforo(new Semaforo("S1",
                new Direccion("Rivadavia", 1200), "I1", 30, 5, 25));
        sistema.registrarSemaforo(new Semaforo("S2",
                new Direccion("Rivadavia", 1800), "I2", 25, 5, 30));
        sistema.registrarSemaforo(new Semaforo("S3",
                new Direccion("Belgrano",  2100), "I3", 20, 5, 35));
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

        sistema.registrarVehiculoCiudadano("ABC123", "Auto");

        // ===== VEHÍCULOS DE EMERGENCIA =====
        sistema.registrarVehiculoEnInterseccion("I1", new Vehiculo("AMB-01", "ambulancia", null));
        sistema.registrarVehiculoEnInterseccion("I4", new Vehiculo("PAT-01", "patrulla", null));
        sistema.registrarVehiculoEnInterseccion("I4", new Vehiculo("PAT-02", "patrulla", null)); // dos patrullas en I4, para demostrar el orden FIFO
        sistema.registrarVehiculoEnInterseccion("I6", new Vehiculo("BOM-01", "bomberos", null));
        sistema.registrarVehiculoEnInterseccion("I3", new Vehiculo("TEC-01", "tecnico", null));

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

    // Devuelve 0 si el usuario quiere cancelar la operación (0 nunca es un valor válido para lo que se usa esto).
    private static int leerEnteroPositivo(String mensaje) {
        int valor = -1;
        while (valor < 0) {
            System.out.print(mensaje + " (0 para cancelar): ");
            valor = leerEntero();
            if (valor < 0) System.out.println("Ingrese un número mayor a 0, o 0 para cancelar.");
        }
        return valor;
    }

    // Devuelve 0 si el usuario quiere cancelar la operación (0 está fuera del rango válido 1-5).
    private static int leerGravedad() {
        int gravedad = -1;
        while (gravedad < 0 || gravedad > 5) {
            System.out.print("Gravedad (1-5, 0 para cancelar): ");
            gravedad = leerEntero();
            if (gravedad < 0 || gravedad > 5) System.out.println("La gravedad debe ser entre 1 y 5, o 0 para cancelar.");
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
        System.out.println("  " + describirCambio(ultimo));
        System.out.print("¿Está seguro de deshacer este cambio? (s/n): ");
        String confirmar = scanner.nextLine().trim();

        if (!confirmar.equalsIgnoreCase("s")) {
            System.out.println("Operación cancelada.");
            return;
        }

        boolean resultado = sistema.deshacerUltimoCambio();
        if (resultado) {
            System.out.println("Cambio deshecho: " + describirCambio(ultimo));
        } else {
            System.out.println("No se pudo deshacer el cambio.");
        }
    }

    private static void mostrarHistorialLegible() {
        Nodo<Cambio> aux = sistema.obtenerHistorialDesdeElTope();
        if (aux == null) {
            System.out.println("No hay cambios registrados.");
            return;
        }

        System.out.println("\n=== HISTORIAL DE CAMBIOS (más reciente primero) ===");
        int numero = 1;
        while (aux != null) {
            System.out.println("  " + numero + ". " + describirCambio(aux.dato));
            numero++;
            aux = aux.siguiente;
        }
    }

    // Traduce un Cambio (entidad/atributo/valores crudos) a una oración legible, resolviendo IDs a nombres cuando aplica
    private static String describirCambio(Cambio c) {
        String id = c.getIdEntidad();
        String anterior = c.getValorAnterior();
        String nuevo = c.getValorNuevo();

        switch (c.getAtributo()) {
            case "activo":
                return "Dispositivo " + id + " fue " + (nuevo.equals("true") ? "activado" : "desactivado");
            case "estado":
                return "Semáforo " + id + " cambió de " + anterior + " a " + nuevo;
            case "bloqueadaNombre":
                return "Calle '" + id + "' fue " + (nuevo.equals("true") ? "bloqueada por completo" : "desbloqueada por completo");
            case "bloqueadaTramo": {
                String[] tramo = nombresDeTramo(id);
                return "Tramo " + tramo[0] + " -> " + tramo[1] + " fue " + (nuevo.equals("true") ? "bloqueado" : "desbloqueado");
            }
            case "demora": {
                String[] tramo = nombresDeTramo(id);
                return nuevo.equals("0")
                        ? "Se canceló la demora en el tramo " + tramo[0] + " -> " + tramo[1]
                        : "Se registró una demora de " + nuevo + " min en el tramo " + tramo[0] + " -> " + tramo[1];
            }
            default:
                return c.toString();
        }
    }

    // A partir de un idEntidad "origenId-destinoId" devuelve los nombres de ambas intersecciones
    private static String[] nombresDeTramo(String idEntidad) {
        String[] partes = idEntidad.split("-");
        if (partes.length != 2) return new String[] { idEntidad, "" };
        return new String[] { sistema.getNombreInterseccion(partes[0]), sistema.getNombreInterseccion(partes[1]) };
    }


}