package sistema;

import modelo.*;
import java.util.Scanner;

public class Main {

    private static SistemaTrafico sistema = new SistemaTrafico();
    private static Scanner scanner = new Scanner(System.in);
    private static int contadorEmergencias = 1;
    private static int contadorSemaforos = 4;
    private static int contadorCamaras = 3;

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
            if (sistema.tieneVehiculo() && sistema.getInterseccionActualCiudadano() != null) {
                System.out.println("[Posición actual registrada]");
            }
            System.out.println("1. Reportar emergencia");
            System.out.println("2. Calcular ruta (menos tramos)");
            System.out.println("3. Calcular ruta más rápida");
            System.out.println("4. Calcular ruta más corta por distancia");
            System.out.println("5. Ver zonas territoriales");
            System.out.println("6. Informar llegada a destino");
            System.out.println("0. Volver");
            System.out.print("Seleccione: ");
            opcion = leerEntero();

            switch (opcion) {
                case 1: reportarEmergencia(); break;
                case 2: calcularRuta("TRAMOS"); break;
                case 3: calcularRuta("RAPIDA"); break;
                case 4: calcularRuta("CORTA"); break;
                case 5: sistema.mostrarTerritorio(); break;
                case 6: informarLlegada(); break;
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

        int gravedad = leerGravedad();
        String descripcion = leerTexto("Descripción del incidente: ");
        String id = "E" + contadorEmergencias++;

        sistema.reportarEmergencia(new Emergencia(id, gravedad, interseccionId, descripcion));
    }

    private static void calcularRuta(String tipo) {
        System.out.println("\nCalles disponibles:");
        sistema.mostrarCallesDisponibles();
        System.out.println("\nIntersecciones disponibles:");
        sistema.mostrarIntersecciones();

        String origenId;

        // Si tiene posición registrada, ofrece usarla como origen
        if (sistema.getInterseccionActualCiudadano() != null) {
            System.out.println("\nTiene una posición registrada. ¿Desea usarla como origen? (s/n)");
            System.out.print("> ");
            String resp = scanner.nextLine().trim();
            if (resp.equalsIgnoreCase("s")) {
                origenId = sistema.getInterseccionActualCiudadano();
            } else {
                origenId = pedirInterseccion("origen");
                if (origenId == null) return;
            }
        } else {
            origenId = pedirInterseccion("origen");
            if (origenId == null) return;
        }

        String destinoId = pedirInterseccion("destino");
        if (destinoId == null) return;

        boolean exito;
        switch (tipo) {
            case "TRAMOS": exito = sistema.calcularRutaMenosTramos(origenId, destinoId); break;
            case "RAPIDA": exito = sistema.calcularRutaMasRapida(origenId, destinoId); break;
            default:       exito = sistema.calcularRutaMasCorta(origenId, destinoId); break;
        }

        // Si calculó la ruta, ofrece guardar el destino como posición futura
        if (exito) {
            System.out.println("\n¿Desea registrar el destino como su posición al llegar? (s/n)");
            System.out.print("> ");
            String resp = scanner.nextLine().trim();
            if (resp.equalsIgnoreCase("s")) {
                sistema.informarLlegadaADestino(destinoId);
            }
        }
    }

    private static void informarLlegada() {
        System.out.println("\nIntersecciones disponibles:");
        sistema.mostrarIntersecciones();
        String interseccionId = pedirInterseccion("intersección a la que llegó");
        if (interseccionId != null) {
            sistema.informarLlegadaADestino(interseccionId);
        }
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
        String id = "S" + contadorSemaforos++;
        String calle = leerTexto("Calle donde está el semáforo: ");
        int altura = leerEnteroPositivo("Altura en esa calle: ");
        String interseccionId = pedirInterseccion("intersección asociada");
        if (interseccionId == null) return;
        int tiempoVerde    = leerEnteroPositivo("Tiempo verde (seg): ");
        int tiempoAmarillo = leerEnteroPositivo("Tiempo amarillo (seg): ");
        int tiempoRojo     = leerEnteroPositivo("Tiempo rojo (seg): ");

        boolean resultado = sistema.registrarSemaforo(
                new Semaforo(id, new Direccion(calle, altura), interseccionId,
                        tiempoVerde, tiempoAmarillo, tiempoRojo));
        System.out.println(resultado
                ? "Semáforo registrado con ID: " + id
                : "Error: ya existe un dispositivo con ese ID.");
    }

    private static void registrarCamara() {
        sistema.mostrarIntersecciones();
        String id = "C" + contadorCamaras++;
        String calle = leerTexto("Calle donde está la cámara: ");
        int altura = leerEnteroPositivo("Altura en esa calle: ");
        String interseccionId = pedirInterseccion("intersección asociada");
        if (interseccionId == null) return;

        // Tipo con opciones fijas
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

        boolean resultado = sistema.registrarCamara(
                new Camara(id, new Direccion(calle, altura), interseccionId, tipo));
        System.out.println(resultado
                ? "Cámara registrada con ID: " + id
                : "Error: ya existe un dispositivo con ese ID.");
    }

    private static void gestionarBloqueo(boolean bloquear) {
        System.out.println("\nCalles disponibles:");
        sistema.mostrarCallesDisponibles();
        System.out.println("\nIntersecciones disponibles:");
        sistema.mostrarIntersecciones();

        System.out.println("\nPuede ingresar el nombre de la calle para bloquearla en ambas");
        System.out.println("direcciones, o ingresar origen y destino para una dirección específica.");
        System.out.println("¿Bloquear por nombre de calle o por dirección específica? (nombre/dir)");
        System.out.print("> ");
        String modo = scanner.nextLine().trim().toLowerCase();

        if (modo.equals("nombre")) {
            String nombreCalle = leerTexto("Nombre de la calle: ");
            // bloquea en todas las direcciones que tenga esa calle
            boolean resultado = bloquear
                    ? sistema.bloquearCallePorNombre(nombreCalle)
                    : sistema.desbloquearCallePorNombre(nombreCalle);
            System.out.println(resultado
                    ? (bloquear ? "Calle bloqueada en todas sus direcciones."
                    : "Calle desbloqueada en todas sus direcciones.")
                    : "No se encontró esa calle.");

        } else {
            String origenId  = pedirInterseccion("intersección origen");
            if (origenId == null) return;
            String destinoId = pedirInterseccion("intersección destino");
            if (destinoId == null) return;

            boolean resultado = bloquear
                    ? sistema.bloquearCalle(origenId, destinoId)
                    : sistema.desbloquearCalle(origenId, destinoId);
            System.out.println(resultado
                    ? (bloquear ? "Dirección bloqueada." : "Dirección desbloqueada.")
                    : "No se encontró esa calle en esa dirección.");
        }
    }

    private static void registrarDemora() {
        System.out.println("\nIntersecciones disponibles:");
        sistema.mostrarIntersecciones();
        String origenId  = pedirInterseccion("intersección origen de la calle");
        if (origenId == null) return;
        String destinoId = pedirInterseccion("intersección destino de la calle");
        if (destinoId == null) return;
        int minutos = leerEnteroPositivo("Minutos de demora extra: ");

        boolean resultado = sistema.registrarDemoraEnCalle(origenId, destinoId, minutos);
        System.out.println(resultado ? "Demora registrada." : "No se encontró esa calle.");
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
        System.out.println("Ingrese el nombre de calle o ID de la " + contexto + ":");
        System.out.print("> ");
        String entrada = scanner.nextLine().trim();

        if (entrada.isEmpty()) {
            System.out.println("Entrada vacía, operación cancelada.");
            return null;
        }

        // Primero intenta por nombre de calle
        String id = sistema.resolverInterseccionPorCalle(entrada);

        // Si no, intenta como ID directo y normaliza la capitalización
        if (id == null) {
            id = sistema.normalizarIdInterseccion(entrada);
            if (id == null) {
                System.out.println("No se encontró ninguna intersección para: " + entrada);
                return null;
            }
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
        sistema.agregarInterseccion(new Interseccion("I1", "Rivadavia y Belgrano"));
        sistema.agregarInterseccion(new Interseccion("I2", "Rivadavia y San Martín"));
        sistema.agregarInterseccion(new Interseccion("I3", "Belgrano y San Martín"));
        sistema.agregarInterseccion(new Interseccion("I4", "Rivadavia y Lavalle"));
        sistema.agregarInterseccion(new Interseccion("I5", "Corrientes y San Martín"));
        sistema.agregarInterseccion(new Interseccion("I6", "Corrientes y Lavalle"));

        // ===== CALLES =====
        // agregarCalle(origen, destino, nombre, altOrigen, altDestino, metros, minutos, dobleMano)

        // Rivadavia: solo ida oeste→este (I1 → I2), NO vuelta
        sistema.agregarCalle("I1", "I2", "Rivadavia", 1200, 1800, 600, 6, false);

        // Belgrano: doble mano entre I1 e I4
        sistema.agregarCalle("I1", "I4", "Belgrano",  2400, 2000, 400, 4, true);

        // Belgrano norte: solo ida I1 → I3 (contramano respecto a I4→I1)
        sistema.agregarCalle("I1", "I3", "Belgrano",  2400, 2800, 500, 5, false);

        // San Martín: doble mano entre I2 e I5
        sistema.agregarCalle("I2", "I5", "San Martín", 1800, 1400, 300, 3, true);

        // San Martín norte: solo ida I2 → I3
        sistema.agregarCalle("I2", "I3", "San Martín", 1800, 2200, 400, 4, false);

        // Lavalle: solo ida este→oeste (I6 → I4), NO vuelta — contramano de Rivadavia
        sistema.agregarCalle("I6", "I4", "Lavalle",   1000, 1400, 700, 8, false);

        // Corrientes: solo ida oeste→este (I6 → I5)
        sistema.agregarCalle("I6", "I5", "Corrientes", 800, 1200, 450, 4, false);

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
        System.out.println();
        System.out.println("Calles: Rivadavia | Belgrano | San Martín | Lavalle | Corrientes");
    }

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