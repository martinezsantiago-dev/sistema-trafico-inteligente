// sistema/SistemaTrafico.java
package sistema;

import modelo.*;
import tda.*;

public class SistemaTrafico {

    private DiccionarioDispositivos dispositivos;
    private GrafoVial grafoVial;
    private ArbolTerritorial arbolTerritorial;
    private ColaPrioridadEmergencias colaEmergencias;
    private PilaHistorial<Cambio> historialCambios;

    // Seguimiento de posición del ciudadano
    private String interseccionActualCiudadano;
    private Vehiculo vehiculoCiudadano;

    public SistemaTrafico() {
        this.dispositivos = new DiccionarioDispositivos(31);
        this.grafoVial = new GrafoVial();
        this.arbolTerritorial = new ArbolTerritorial();
        this.colaEmergencias = new ColaPrioridadEmergencias();
        this.historialCambios = new PilaHistorial<>();
        this.interseccionActualCiudadano = null;
        this.vehiculoCiudadano = null;
    }

    // ===== DISPOSITIVOS =====

    public boolean registrarSemaforo(Semaforo semaforo) {
        return dispositivos.insertar(semaforo.getId(), semaforo);
    }

    public boolean registrarCamara(Camara camara) {
        return dispositivos.insertar(camara.getId(), camara);
    }

    public boolean activarDispositivo(String id) {
        Dispositivo d = dispositivos.obtener(id);
        if (d == null) return false;
        String anterior = String.valueOf(d.isActivo());
        boolean resultado = dispositivos.modificarActivo(id, true);
        if (resultado) historialCambios.apilar(new Cambio("Dispositivo", id, "activo", anterior, "true"));
        return resultado;
    }

    public boolean desactivarDispositivo(String id) {
        Dispositivo d = dispositivos.obtener(id);
        if (d == null) return false;
        String anterior = String.valueOf(d.isActivo());
        boolean resultado = dispositivos.modificarActivo(id, false);
        if (resultado) historialCambios.apilar(new Cambio("Dispositivo", id, "activo", anterior, "false"));
        return resultado;
    }

    public boolean cambiarEstadoSemaforo(String id, Semaforo.Estado nuevoEstado) {
        Dispositivo d = dispositivos.obtener(id);
        if (!(d instanceof Semaforo)) return false;
        Semaforo semaforo = (Semaforo) d;
        String anterior = semaforo.getEstado().toString();
        semaforo.setEstado(nuevoEstado);
        historialCambios.apilar(new Cambio("Semaforo", id, "estado", anterior, nuevoEstado.toString()));
        return true;
    }

    public Dispositivo obtenerDispositivo(String id) {
        return dispositivos.obtener(id);
    }

    public void mostrarDispositivos() {
        dispositivos.mostrar();
    }

    // ===== RED VIAL =====

    public boolean agregarInterseccion(Interseccion interseccion) {
        return grafoVial.agregarInterseccion(interseccion);
    }

    public boolean agregarCalle(String origenId, String destinoId, String nombre,
                                int alturaOrigen, int alturaDestino,
                                int distanciaMetros, int tiempoMinutos, boolean dobleMano) {
        return grafoVial.agregarCalle(origenId, destinoId, nombre,
                alturaOrigen, alturaDestino, distanciaMetros, tiempoMinutos, dobleMano);
    }

    // Resuelve nombre de calle a ID de intersección
    public String resolverInterseccion(String nombreCalle, int altura) {
        return grafoVial.obtenerInterseccionCercana(nombreCalle, altura);
    }

    // Para cuando el usuario informa nombre de calle sin altura
    public String resolverInterseccionPorCalle(String nombreCalle) {
        return grafoVial.obtenerInterseccionPorNombreCalle(nombreCalle);
    }

    public boolean bloquearCallePorNombre(String nombreCalle) {
        boolean resultado = grafoVial.bloquearCallePorNombre(nombreCalle);
        if (resultado) {
            historialCambios.apilar(new Cambio("Calle", nombreCalle, "bloqueadaNombre", "false", "true"));
        }
        return resultado;
    }

    public boolean desbloquearCallePorNombre(String nombreCalle) {
        boolean resultado = grafoVial.desbloquearCallePorNombre(nombreCalle);
        if (resultado) {
            historialCambios.apilar(new Cambio("Calle", nombreCalle, "bloqueadaNombre", "true", "false"));
        }
        return resultado;
    }

    public boolean existeInterseccion(String id) {
        return grafoVial.buscarInterseccion(id) != null;
    }

    public boolean calcularRutaMenosTramos(String origenId, String destinoId) {
        return grafoVial.mostrarRutaMenosTramos(origenId, destinoId);
    }

    public boolean calcularRutaMasRapida(String origenId, String destinoId) {
        return grafoVial.mostrarRutaMasRapida(origenId, destinoId);
    }

    public boolean calcularRutaMasCorta(String origenId, String destinoId) {
        return grafoVial.mostrarRutaMasCorta(origenId, destinoId);
    }

    public boolean bloquearCalle(String origenId, String destinoId) {
        boolean resultado = grafoVial.bloquearCalle(origenId, destinoId);
        if (resultado) {
            historialCambios.apilar(new Cambio("Calle",
                    origenId + "-" + destinoId, "bloqueada", "false", "true"));
        }
        return resultado;
    }

    public boolean desbloquearCalle(String origenId, String destinoId) {
        boolean resultado = grafoVial.desbloquearCalle(origenId, destinoId);
        if (resultado) {
            historialCambios.apilar(new Cambio("Calle",
                    origenId + "-" + destinoId, "bloqueada", "true", "false"));
        }
        return resultado;
    }

    public boolean registrarDemoraEnCalle(String origenId, String destinoId, int minutos) {
        int demoraAnterior = grafoVial.getDemoraEnCalle(origenId, destinoId);
        boolean resultado = grafoVial.registrarDemoraEnCalle(origenId, destinoId, minutos);
        if (resultado) {
            historialCambios.apilar(new Cambio("Calle",
                    origenId + "-" + destinoId, "demora",
                    String.valueOf(Math.max(demoraAnterior, 0)), String.valueOf(minutos)));
        }
        return resultado;
    }

    public void mostrarRedVial() {
        grafoVial.mostrarGrafo();
    }

    public void mostrarIntersecciones() {
        grafoVial.mostrarIntersecciones();
    }

    public void mostrarCallesDisponibles() {
        grafoVial.mostrarCallesDisponibles();
    }

    // ===== SEGUIMIENTO DE VEHÍCULO =====

    public void registrarVehiculoCiudadano(String patente, String tipo) {
        vehiculoCiudadano = new Vehiculo(patente, tipo, null);
        System.out.println("Vehículo registrado: " + patente + " (" + tipo + ")");
    }

    public boolean tieneVehiculo() {
        return vehiculoCiudadano != null;
    }

    public String getInterseccionActualCiudadano() {
        return interseccionActualCiudadano;
    }

    // El ciudadano informa que llegó a destino — ese destino pasa a ser el origen siguiente
    public void informarLlegadaADestino(String interseccionDestinoId) {
        interseccionActualCiudadano = interseccionDestinoId;
        if (vehiculoCiudadano != null) {
            vehiculoCiudadano.setInterseccionActual(interseccionDestinoId);
            grafoVial.registrarVehiculoEnInterseccion(interseccionDestinoId, vehiculoCiudadano);
        }
        Interseccion i = grafoVial.buscarInterseccion(interseccionDestinoId);
        String nombre = (i != null) ? i.getNombre() : interseccionDestinoId;
        System.out.println("Posición actualizada: ahora está en " + nombre);
        System.out.println("Esta intersección será usada como origen en la próxima consulta.");
    }

    public void limpiarPosicionActual() {
        interseccionActualCiudadano = null;
    }

    // ===== EMERGENCIAS =====

    public void reportarEmergencia(Emergencia emergencia) {
        colaEmergencias.insertar(emergencia);
        if (emergencia.getZona() != null) {
            arbolTerritorial.incrementarIncidentes(emergencia.getZona());
        }
        System.out.println("Emergencia registrada: " + emergencia);
    }

    public boolean hayEmergencias() {
        return !colaEmergencias.estaVacia();
    }


    public Emergencia atenderEmergencia() {
        Emergencia e = colaEmergencias.atender();
        if (e == null) System.out.println("No hay emergencias pendientes.");
        else System.out.println("Atendiendo: " + e);
        return e;
    }

    public String getZonaDeInterseccion(String id) {
        Interseccion i = grafoVial.buscarInterseccion(id);
        if (i == null) return "Sin zona";
        return i.getZona();
    }

    public void mostrarEmergencias() {
        colaEmergencias.mostrar();
    }

    public String getNombreCalle(String nombreCalle) {
        return grafoVial.getNombreRealCalle(nombreCalle);
    }

    public ListaEnlazada<Interseccion> getIntersecciones() {
        return grafoVial.getIntersecciones();
    }

    public int cantidadSemaforos() {
        return dispositivos.contarPorTipo("Semaforo");
    }

    public int cantidadCamaras() {
        return dispositivos.contarPorTipo("Camara");
    }

    public ListaEnlazada<String> buscarInterseccionesPorCalle(String nombreCalle) {
        return grafoVial.buscarInterseccionesPorCalle(nombreCalle);
    }

    public String resolverEntradaInterseccion(String entrada) {
        return grafoVial.resolverEntrada(entrada);
    }

    public String getNombreInterseccion(String id) {
        Interseccion i = grafoVial.buscarInterseccion(id);
        if (i == null) return "desconocida";
        return i.getNombre();
    }

    // ===== HISTORIAL =====

    public Cambio obtenerUltimoCambio() {
        return historialCambios.tope();
    }

    public boolean deshacerUltimoCambio() {
        Cambio cambio = historialCambios.desapilar();
        if (cambio == null) return false;
        revertirCambio(cambio);
        return true;
    }

    private void revertirCambio(Cambio cambio) {
        if (cambio.getAtributo().equals("activo")) {
            dispositivos.modificarActivo(cambio.getIdEntidad(),
                    Boolean.parseBoolean(cambio.getValorAnterior()));

        } else if (cambio.getAtributo().equals("estado")) {
            Dispositivo d = dispositivos.obtener(cambio.getIdEntidad());
            if (d instanceof Semaforo) {  // instanceof sirve para comprobar si un objeto es una instancia de una clase específica o de una de sus subclases
                ((Semaforo) d).setEstado(
                        Semaforo.Estado.valueOf(cambio.getValorAnterior()));
            }

        } else if (cambio.getAtributo().equals("bloqueada")) {
            String[] partes = cambio.getIdEntidad().split("-");
            if (partes.length == 2) {
                boolean valorAnterior = Boolean.parseBoolean(cambio.getValorAnterior());
                if (valorAnterior) {
                    grafoVial.bloquearCalle(partes[0], partes[1]);
                } else {
                    grafoVial.desbloquearCalle(partes[0], partes[1]);
                }
            }

        } else if (cambio.getAtributo().equals("bloqueadaNombre")) {
            boolean valorAnterior = Boolean.parseBoolean(cambio.getValorAnterior());
            if (valorAnterior) {
                grafoVial.bloquearCallePorNombre(cambio.getIdEntidad());
            } else {
                grafoVial.desbloquearCallePorNombre(cambio.getIdEntidad());
            }

        } else if (cambio.getAtributo().equals("demora")) {
            String[] partes = cambio.getIdEntidad().split("-");
            if (partes.length == 2) {
                grafoVial.registrarDemoraEnCalle(partes[0], partes[1],
                        Integer.parseInt(cambio.getValorAnterior()));
            }
        }
    }

    public void mostrarCallesConDemora() {
        grafoVial.mostrarCallesConDemora();
    }

    public void mostrarHistorial() {
        historialCambios.mostrar();
    }

    public String normalizarIdInterseccion(String id) {
        return grafoVial.normalizarId(id);
    }


    // ===== TERRITORIO =====

    public void crearCiudad(String nombre) { arbolTerritorial.crearCiudad(nombre); }
    public boolean agregarZona(String nombreZona) { return arbolTerritorial.agregarZona(nombreZona); }
    public boolean agregarBarrio(String z, String b) { return arbolTerritorial.agregarBarrio(z, b); }
    public boolean agregarManzana(String b, String m) { return arbolTerritorial.agregarManzana(b, m); }
    public void incrementarIncidentesEn(String nombre) { arbolTerritorial.incrementarIncidentes(nombre); }
    public void mostrarTerritorio() {
        arbolTerritorial.mostrar();
        System.out.println();
        reporteEmergenciasPorZona();
    }
    public void reporteEmergenciasPorZona() {
        System.out.println("=== EMERGENCIAS POR ZONA ===");
        ListaEnlazada<String> zonas = new ListaEnlazada<>();
        ListaEnlazada<int[]> conteos = new ListaEnlazada<>();
        colaEmergencias.contarPorZona(zonas, conteos);

        if (zonas.estaVacia()) {
            System.out.println("  No hay emergencias pendientes.");
        } else {
            Nodo<String> nZ = zonas.getCabeza();
            Nodo<int[]> nC = conteos.getCabeza();
            while (nZ != null) {
                System.out.println("  " + nZ.dato + ": " + nC.dato[0] + " emergencia(s)");
                nZ = nZ.siguiente;
                nC = nC.siguiente;
            }
        }
        System.out.println("============================");
    }

}