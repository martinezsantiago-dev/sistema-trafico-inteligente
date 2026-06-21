package sistema;

import modelo.*;
import java.util.Scanner;

public class Main {

    private static SistemaTrafico sistema = new SistemaTrafico();
    private static Scanner scanner = new Scanner(System.in);
    private static int contadorEmergencias = 1;

    public static void main(String[] args) {
        cargarDatosIniciales();

        int opcion;
        do {
            System.out.println("\n========================================");
            System.out.println("  SISTEMA DE GESTION DE TRAFICO URBANO");
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
                default: System.out.println("Opcion invalida.");
            }
        } while (opcion != 0);
    }

    // ===== MENU CIUDADANO =====

    private static void menuCiudadano() {
        int opcion;
        do {
            System.out.println("\n--- MENU CIUDADANO ---");
            System.out.println("1. Reportar emergencia");
            System.out.println("2. Calcular ruta (menos tramos)");
            System.out.println("3. Calcular ruta mas rapida");
            System.out.println("4. Calcular ruta mas corta por distancia");
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
                default: System.out.println("Opcion invalida.");
            }
        } while (opcion != 0);
    }

    // ===== MENU OPERADOR =====

    private static void menuOperador() {
        int opcion;
        do {
            System.out.println("\n--- MENU OPERADOR ---");
            System.out.println("[Dispositivos]");
            System.out.println(" 1. Ver dispositivos registrados");
            System.out.println(" 2. Registrar semaforo");
            System.out.println(" 3. Registrar camara");
            System.out.println(" 4. Activar dispositivo");
            System.out.println(" 5. Desactivar dispositivo");
            System.out.println(" 6. Cambiar estado de semaforo");
            System.out.println("[Calles]");
            System.out.println(" 7. Ver calles detalladas");
            System.out.println(" 8. Bloquear calle");
            System.out.println(" 9. Desbloquear calle");
            System.out.println("10. Registrar demora en calle");
            System.out.println("[Emergencias]");
            System.out.println("11. Ver emergencias pendientes");
            System.out.println("12. Atender proxima emergencia");
            System.out.println("[Historial]");
            System.out.println("13. Deshacer ultimo cambio");
            System.out.println("14. Ver historial de cambios");
            System.out.println("[Red Vial]");
            System.out.println("15. Ver red vial (grafo)");
            System.out.println("16. Agregar interseccion");
            System.out.println("17. Agregar calle");
            System.out.println(" 0. Volver");
            System.out.print("Seleccione: ");
            opcion = leerEntero();

            switch (opcion) {
                case 1:  sistema.mostrarDispositivos(); break;
                case 2:  registrarSemaforo(); break;
                case 3:  registrarCamara(); break;
                case 4:  gestionarDispositivo(true); break;
                case 5:  gestionarDispositivo(false); break;
                case 6:  cambiarEstadoSemaforo(); break;
                case 7:  sistema.mostrarCallesDetalladas(); break;
                case 8:  gestionarBloqueo(true); break;
                case 9:  gestionarBloqueo(false); break;
                case 10: registrarDemoraEnCalle(); break;
                case 11: sistema.mostrarEmergencias(); break;
                case 12: atenderEmergencia(); break;
                case 13: sistema.deshacerUltimoCambio(); break;
                case 14: sistema.mostrarHistorial(); break;
                case 15: sistema.mostrarRedVial(); break;
                case 16: agregarInterseccion(); break;
                case 17: agregarCalle(); break;
                case 0:  break;
                default: System.out.println("Opcion invalida.");
            }
        } while (opcion != 0);
    }

    // ===== ACCIONES CIUDADANO =====

    private static void reportarEmergencia() {
        System.out.println("\nIntersecciones disponibles:");
        sistema.mostrarIntersecciones();

        int gravedad = leerGravedad();

        String interseccionId = null;
        do {
            interseccionId = leerTexto("ID de interseccion afectada: ");
            if (!sistema.existeInterseccion(interseccionId)) {
                System.out.println("La interseccion '" + interseccionId + "' no existe. Intente de nuevo.");
                interseccionId = null;
            }
        } while (interseccionId == null);

        String descripcion = leerTexto("Descripcion: ");

        String id = "E" + contadorEmergencias++;
        sistema.reportarEmergencia(new Emergencia(id, gravedad, interseccionId, descripcion));

        System.out.println("\nZonas territoriales disponibles:");
        sistema.mostrarTerritorio();
        String zona = leerTextoOpcional("Zona afectada (Enter para omitir): ");
        if (!zona.isEmpty()) {
            sistema.incrementarIncidentesEn(zona);
            System.out.println("Incidente registrado en zona '" + zona + "'.");
        }
    }

    private static void calcularRuta(String tipo) {
        System.out.println("\nCalles disponibles en el sistema:");
        sistema.mostrarCallesDetalladas();

        System.out.println("\nIngrese la direccion de ORIGEN (calle y altura):");
        Direccion origen = leerDireccion();

        System.out.println("Ingrese la direccion de DESTINO (calle y altura):");
        Direccion destino = leerDireccion();

        switch (tipo) {
            case "TRAMOS": sistema.calcularRutaMenosTramosPorDirecciones(origen, destino); break;
            case "RAPIDA": sistema.calcularRutaMasRapidaPorDirecciones(origen, destino); break;
            case "CORTA":  sistema.calcularRutaMasCortaPorDirecciones(origen, destino); break;
        }
    }

    // ===== ACCIONES OPERADOR — DISPOSITIVOS =====

    private static void registrarSemaforo() {
        String id = leerTexto("ID del nuevo semaforo (ej: S4): ");
        if (sistema.existeDispositivo(id)) {
            System.out.println("Ya existe un dispositivo con ID '" + id + "'.");
            return;
        }

        System.out.println("\nIntersecciones disponibles:");
        sistema.mostrarIntersecciones();

        String interseccionId = null;
        do {
            interseccionId = leerTexto("ID de interseccion donde instalar: ");
            if (!sistema.existeInterseccion(interseccionId)) {
                System.out.println("La interseccion '" + interseccionId + "' no existe. Intente de nuevo.");
                interseccionId = null;
            }
        } while (interseccionId == null);

        System.out.println("Direccion de instalacion:");
        Direccion ubicacion = leerDireccion();

        int verde    = leerEnteroPositivo("Tiempo verde (seg): ");
        int amarillo = leerEnteroPositivo("Tiempo amarillo (seg): ");
        int rojo     = leerEnteroPositivo("Tiempo rojo (seg): ");

        boolean ok = sistema.registrarSemaforo(new Semaforo(id, ubicacion, interseccionId, verde, amarillo, rojo));
        System.out.println(ok
                ? "Semaforo '" + id + "' registrado correctamente."
                : "Error: no se pudo registrar el semaforo.");
    }

    private static void registrarCamara() {
        String id = leerTexto("ID de la nueva camara (ej: C3): ");
        if (sistema.existeDispositivo(id)) {
            System.out.println("Ya existe un dispositivo con ID '" + id + "'.");
            return;
        }

        System.out.println("\nIntersecciones disponibles:");
        sistema.mostrarIntersecciones();

        String interseccionId = null;
        do {
            interseccionId = leerTexto("ID de interseccion donde instalar: ");
            if (!sistema.existeInterseccion(interseccionId)) {
                System.out.println("La interseccion '" + interseccionId + "' no existe. Intente de nuevo.");
                interseccionId = null;
            }
        } while (interseccionId == null);

        System.out.println("Direccion de instalacion:");
        Direccion ubicacion = leerDireccion();

        System.out.println("Tipo de camara:");
        System.out.println("  1. velocidad");
        System.out.println("  2. flujo");
        System.out.println("  3. infraroja");
        System.out.print("Seleccione tipo (1-3): ");
        int tipoOp = leerEntero();
        String tipo;
        switch (tipoOp) {
            case 1:  tipo = "velocidad"; break;
            case 2:  tipo = "flujo"; break;
            case 3:  tipo = "infraroja"; break;
            default: tipo = "flujo"; System.out.println("Opcion no reconocida, se usa 'flujo'."); break;
        }

        boolean ok = sistema.registrarCamara(new Camara(id, ubicacion, interseccionId, tipo));
        System.out.println(ok
                ? "Camara '" + id + "' registrada correctamente."
                : "Error: no se pudo registrar la camara.");
    }

    private static void gestionarDispositivo(boolean activar) {
        System.out.println("\nDispositivos registrados:");
        sistema.mostrarDispositivos();
        String id = leerTexto("ID del dispositivo a " + (activar ? "activar" : "desactivar") + ": ");
        boolean resultado = activar ? sistema.activarDispositivo(id) : sistema.desactivarDispositivo(id);
        if (resultado) {
            System.out.println("Dispositivo '" + id + "' " + (activar ? "activado" : "desactivado") + " correctamente.");
        } else {
            System.out.println("No se encontro el dispositivo '" + id + "'.");
        }
    }

    private static void cambiarEstadoSemaforo() {
        System.out.println("\nDispositivos registrados:");
        sistema.mostrarDispositivos();
        String id = leerTexto("ID del semaforo: ");

        System.out.println("Nuevo estado:");
        System.out.println("  1. ROJO");
        System.out.println("  2. AMARILLO");
        System.out.println("  3. VERDE");

        int opcion;
        do {
            System.out.print("Seleccione (1-3): ");
            opcion = leerEntero();
            if (opcion < 1 || opcion > 3) System.out.println("Opcion invalida.");
        } while (opcion < 1 || opcion > 3);

        Semaforo.Estado estado;
        switch (opcion) {
            case 1:  estado = Semaforo.Estado.ROJO; break;
            case 2:  estado = Semaforo.Estado.AMARILLO; break;
            default: estado = Semaforo.Estado.VERDE; break;
        }

        boolean resultado = sistema.cambiarEstadoSemaforo(id, estado);
        if (resultado) {
            System.out.println("Semaforo '" + id + "' cambiado a " + estado + ".");
        } else {
            System.out.println("No se encontro un semaforo con ID '" + id + "'. Verifique que sea un semaforo y no una camara.");
        }
    }

    // ===== ACCIONES OPERADOR — CALLES =====

    private static void gestionarBloqueo(boolean bloquear) {
        System.out.println("\nCalles disponibles:");
        sistema.mostrarCallesDetalladas();

        String origenId  = leerTexto("ID interseccion origen: ");
        String destinoId = leerTexto("ID interseccion destino: ");

        boolean resultado = bloquear
                ? sistema.bloquearCalle(origenId, destinoId)
                : sistema.desbloquearCalle(origenId, destinoId);

        if (resultado) {
            System.out.println("Calle " + origenId + " -> " + destinoId + (bloquear ? " bloqueada." : " desbloqueada."));
        } else {
            System.out.println("No se encontro la calle entre '" + origenId + "' y '" + destinoId + "'.");
        }
    }

    private static void registrarDemoraEnCalle() {
        System.out.println("\nCalles disponibles:");
        sistema.mostrarCallesDetalladas();

        String origenId  = leerTexto("ID interseccion origen: ");
        String destinoId = leerTexto("ID interseccion destino: ");
        int demora = leerEnteroPositivo("Demora extra (minutos): ");

        boolean resultado = sistema.registrarDemoraEnCalle(origenId, destinoId, demora);
        if (resultado) {
            System.out.println("Demora de " + demora + " min registrada en calle " + origenId + " -> " + destinoId + ".");
        } else {
            System.out.println("No se encontro la calle entre '" + origenId + "' y '" + destinoId + "'.");
        }
    }

    // ===== ACCIONES OPERADOR — EMERGENCIAS =====

    private static void atenderEmergencia() {
        if (!sistema.hayEmergencias()) {
            System.out.println("No hay emergencias pendientes.");
            return;
        }

        System.out.println("\nEmergencia mas prioritaria en cola:");
        sistema.mostrarProximaEmergencia();

        String confirmar = leerTexto("¿Atender esta emergencia? (s/n): ");
        if (confirmar.equalsIgnoreCase("s")) {
            sistema.atenderEmergencia();
        } else {
            System.out.println("Operacion cancelada.");
        }
    }

    // ===== ACCIONES OPERADOR — RED VIAL =====

    private static void agregarInterseccion() {
        String id = leerTexto("ID de la nueva interseccion (ej: I5): ");
        if (sistema.existeInterseccion(id)) {
            System.out.println("Ya existe una interseccion con ID '" + id + "'.");
            return;
        }
        String nombre = leerTexto("Nombre descriptivo (ej: Corrientes y Callao): ");
        boolean ok = sistema.agregarInterseccion(new Interseccion(id, nombre));
        System.out.println(ok ? "Interseccion '" + id + "' agregada." : "Error al agregar la interseccion.");
    }

    private static void agregarCalle() {
        System.out.println("\nIntersecciones disponibles:");
        sistema.mostrarIntersecciones();

        String origenId = null;
        do {
            origenId = leerTexto("ID interseccion origen: ");
            if (!sistema.existeInterseccion(origenId)) {
                System.out.println("No existe la interseccion '" + origenId + "'.");
                origenId = null;
            }
        } while (origenId == null);

        String destinoId = null;
        do {
            destinoId = leerTexto("ID interseccion destino: ");
            if (!sistema.existeInterseccion(destinoId)) {
                System.out.println("No existe la interseccion '" + destinoId + "'.");
                destinoId = null;
            }
        } while (destinoId == null);

        String nombre      = leerTexto("Nombre de la calle: ");
        int alturaOrigen   = leerEnteroPositivo("Altura en interseccion origen: ");
        int alturaDestino  = leerEnteroPositivo("Altura en interseccion destino: ");
        int distancia      = leerEnteroPositivo("Distancia en metros: ");
        int tiempo         = leerEnteroPositivo("Tiempo base en minutos: ");
        String dmStr       = leerTexto("¿Es doble mano? (s/n): ");
        boolean dobleMano  = dmStr.equalsIgnoreCase("s");

        boolean ok = sistema.agregarCalle(origenId, destinoId, nombre,
                alturaOrigen, alturaDestino, distancia, tiempo, dobleMano);
        System.out.println(ok ? "Calle '" + nombre + "' agregada correctamente." : "Error: ya existe esa calle o datos invalidos.");
    }

    // ===== UTILIDADES DE LECTURA =====

    private static Direccion leerDireccion() {
        String calle = leerTexto("  Nombre de la calle: ");
        int altura = leerEnteroPositivo("  Altura (numero): ");
        return new Direccion(calle, altura);
    }

    private static int leerEntero() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Ingrese un numero valido: ");
            }
        }
    }

    private static int leerEnteroPositivo(String prompt) {
        int valor;
        do {
            System.out.print(prompt);
            try {
                valor = Integer.parseInt(scanner.nextLine().trim());
                if (valor <= 0) System.out.println("Debe ser un numero mayor a 0.");
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un numero valido.");
                valor = 0;
            }
        } while (valor <= 0);
        return valor;
    }

    private static String leerTexto(String mensaje) {
        String texto = "";
        while (texto.isEmpty()) {
            System.out.print(mensaje);
            texto = scanner.nextLine().trim();
            if (texto.isEmpty()) System.out.println("El campo no puede estar vacio.");
        }
        return texto;
    }

    private static String leerTextoOpcional(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine().trim();
    }

    private static int leerGravedad() {
        int gravedad = 0;
        while (gravedad < 1 || gravedad > 5) {
            System.out.print("Gravedad (1-5): ");
            gravedad = leerEntero();
            if (gravedad < 1 || gravedad > 5) System.out.println("La gravedad debe estar entre 1 y 5.");
        }
        return gravedad;
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
        sistema.agregarCalle("I1", "I2", "Santa Fe",         2800, 3200, 400,  3, true);
        sistema.agregarCalle("I2", "I4", "Scalabrini Ortiz", 1500, 1200, 350,  4, true);
        sistema.agregarCalle("I1", "I4", "Coronel Diaz",     1500, 1200, 600,  7, true);
        sistema.agregarCalle("I3", "I2", "Juramento",        2000, 2400, 500,  5, false);

        // Semaforos
        sistema.registrarSemaforo(new Semaforo("S1", new Direccion("Santa Fe",         2800), "I1", 30, 5, 25));
        sistema.registrarSemaforo(new Semaforo("S2", new Direccion("Scalabrini Ortiz", 1500), "I2", 25, 5, 30));
        sistema.registrarSemaforo(new Semaforo("S3", new Direccion("Cabildo",          2000), "I3", 20, 5, 35));

        // Camaras
        sistema.registrarCamara(new Camara("C1", new Direccion("Santa Fe",  3000), "I2", "velocidad"));
        sistema.registrarCamara(new Camara("C2", new Direccion("Juramento", 2200), "I3", "flujo"));

        System.out.println("Sistema iniciado con datos de prueba (Buenos Aires).");
    }
}