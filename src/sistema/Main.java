package sistema;

import modelo.*;
import java.util.Scanner;

public class Main {

    private static SistemaTrafico sistema = new SistemaTrafico();
    private static Scanner scanner = new Scanner(System.in);

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
            System.out.println("0. Volver");
            System.out.print("Seleccione: ");
            opcion = leerEntero();

            switch (opcion) {
                case 1: sistema.mostrarDispositivos(); break;
                case 2: gestionarDispositivo(true); break;
                case 3: gestionarDispositivo(false); break;
                case 4: cambiarEstadoSemaforo(); break;
                case 5: atenderEmergencia();break;
                case 6: sistema.mostrarEmergencias(); break;
                case 7: sistema.deshacerUltimoCambio(); break;
                case 8: sistema.mostrarHistorial(); break;
                case 9: sistema.mostrarRedVial(); break;
                case 0: break;
                default: System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }

    // ===== ACCIONES =====

    private static void reportarEmergencia() {

        sistema.mostrarIntersecciones();

        String id = leerTexto("ID de la emergencia: ");
        int gravedad = leerGravedad();
        String interseccionId = leerTexto("ID de intersección afectada: ");
        String descripcion = leerTexto("Descripción: ");

        sistema.reportarEmergencia(new Emergencia(id, gravedad, interseccionId, descripcion));
    }

    private static void calcularRuta(String tipo) {
        System.out.println("\nIntersecciones disponibles:");
        sistema.mostrarIntersecciones();


        String origen = leerTexto("ID intersección origen: ");
        String destino = leerTexto("ID intersección destino: ");

        switch (tipo) {
            case "TRAMOS": sistema.calcularRutaMenosTramos(origen, destino); break;
            case "RAPIDA": sistema.calcularRutaMasRapida(origen, destino); break;
            case "CORTA":  sistema.calcularRutaMasCorta(origen, destino); break;
        }
    }

    private static void gestionarDispositivo(boolean activar) {
        String id = leerTexto("ID del dispositivo: ");
        boolean resultado = activar ? sistema.activarDispositivo(id) : sistema.desactivarDispositivo(id);
        System.out.println(resultado ? "Operación exitosa." : "No se encontró el dispositivo.");
    }

    private static void cambiarEstadoSemaforo() {
        String id = leerTexto("ID del semáforo: ");
        System.out.println("1. ROJO   2. AMARILLO   3. VERDE");

        int opcion;
        do {
            System.out.print("Seleccione estado (1-3): ");
            opcion = leerEntero();
            if (opcion < 1 || opcion > 3) System.out.println("Opción inválida, ingrese 1, 2 o 3.");
        } while (opcion < 1 || opcion > 3);

        Semaforo.Estado estado;
        switch (opcion) {
            case 1: estado = Semaforo.Estado.ROJO; break;
            case 2: estado = Semaforo.Estado.AMARILLO; break;
            default: estado = Semaforo.Estado.VERDE; break;
        }

        boolean resultado = sistema.cambiarEstadoSemaforo(id, estado);
        System.out.println(resultado ? "Estado actualizado." : "No se encontró el semáforo.");
    }

    // ===== DATOS INICIALES =====

    private static void cargarDatosIniciales() {
        // Territorio
        sistema.crearCiudad("Buenos Aires");
        sistema.agregarZona("Palermo");
        sistema.agregarZona("Belgrano");
        sistema.agregarBarrio("Palermo", "Palermo Soho");
        sistema.agregarBarrio("Palermo", "Palermo Hollywood");
        sistema.agregarBarrio("Belgrano", "Belgrano C");
        sistema.agregarManzana("Palermo Soho", "Manzana 1");
        sistema.agregarManzana("Palermo Soho", "Manzana 2");
        sistema.agregarManzana("Belgrano C", "Manzana 3");

        // Intersecciones
        sistema.agregarInterseccion(new Interseccion("I1", "Santa Fe y Coronel Diaz"));
        sistema.agregarInterseccion(new Interseccion("I2", "Santa Fe y Scalabrini Ortiz"));
        sistema.agregarInterseccion(new Interseccion("I3", "Cabildo y Juramento"));
        sistema.agregarInterseccion(new Interseccion("I4", "Scalabrini Ortiz y Cordoba"));

        // Calles (origenId, destinoId, nombre, altOrigen, altDestino, metros, minutos, dobleMano)
        sistema.agregarCalle("I1", "I2", "Santa Fe",         2800, 3200, 400, 3, true);
        sistema.agregarCalle("I2", "I4", "Scalabrini Ortiz", 1500, 1200, 350, 4, true);
        sistema.agregarCalle("I1", "I4", "Coronel Diaz",     1500, 1200, 600, 7, true);
        sistema.agregarCalle("I3", "I2", "Juramento",        2000, 2400, 500, 5, false);

        // Semáforos
        sistema.registrarSemaforo(new Semaforo("S1", new Direccion("Santa Fe", 2800),         "I1", 30, 5, 25));
        sistema.registrarSemaforo(new Semaforo("S2", new Direccion("Scalabrini Ortiz", 1500), "I2", 25, 5, 30));
        sistema.registrarSemaforo(new Semaforo("S3", new Direccion("Cabildo", 2000),          "I3", 20, 5, 35));

        // Cámaras
        sistema.registrarCamara(new Camara("C1", new Direccion("Santa Fe", 3000),   "I2", "velocidad"));
        sistema.registrarCamara(new Camara("C2", new Direccion("Juramento", 2200),  "I3", "flujo"));

        System.out.println("Sistema iniciado con datos de prueba.");
    }

    // ===== UTILIDAD =====

    private static int leerEntero() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Ingrese un número válido: ");
            }
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

    private static int leerGravedad() {
        int gravedad = 0;
        while (gravedad < 1 || gravedad > 5) {
            System.out.print("Gravedad (1-5): ");
            gravedad = leerEntero();
            if (gravedad < 1 || gravedad > 5) System.out.println("La gravedad debe ser entre 1 y 5.");
        }
        return gravedad;
    }

    private static void atenderEmergencia() {
        System.out.println("\nEmergencias pendientes:");
        sistema.mostrarEmergencias();

        String confirmar = leerTexto("¿Atender la emergencia más prioritaria? (s/n): ");

        if (confirmar.equalsIgnoreCase("s")) {
            sistema.atenderEmergencia();
        } else {
            System.out.println("Operación cancelada.");
        }
    }

}