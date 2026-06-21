package sistema;

import modelo.*;
import tda.*;

public class SistemaTrafico {

    private DiccionarioDispositivos dispositivos;
    private GrafoVial grafoVial;
    private ArbolTerritorial arbolTerritorial;
    private ColaPrioridadEmergencias colaEmergencias;
    private PilaHistorial<Cambio> historialCambios;

    public SistemaTrafico() {
        this.dispositivos = new DiccionarioDispositivos(31);
        this.grafoVial = new GrafoVial();
        this.arbolTerritorial = new ArbolTerritorial();
        this.colaEmergencias = new ColaPrioridadEmergencias();
        this.historialCambios = new PilaHistorial<>();
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
        if (resultado) {
            historialCambios.apilar(new Cambio("Dispositivo", id, "activo", anterior, "true"));
        }
        return resultado;
    }

    public boolean desactivarDispositivo(String id) {
        Dispositivo d = dispositivos.obtener(id);
        if (d == null) return false;
        String anterior = String.valueOf(d.isActivo());
        boolean resultado = dispositivos.modificarActivo(id, false);
        if (resultado) {
            historialCambios.apilar(new Cambio("Dispositivo", id, "activo", anterior, "false"));
        }
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

    public boolean calcularRutaMenosTramos(String origenId, String destinoId) {
        return grafoVial.mostrarRutaMenosTramosPorIntersecciones(origenId, destinoId);
    }

    public boolean calcularRutaMasRapida(String origenId, String destinoId) {
        return grafoVial.mostrarRutaMasRapidaPorIntersecciones(origenId, destinoId);
    }

    public boolean calcularRutaMasCorta(String origenId, String destinoId) {
        return grafoVial.mostrarRutaMasCortaPorIntersecciones(origenId, destinoId);
    }

    public boolean bloquearCalle(String origenId, String destinoId) {
        boolean resultado = grafoVial.bloquearCalleDobleMano(origenId, destinoId);
        if (resultado) {
            historialCambios.apilar(new Cambio("Calle", origenId + "->" + destinoId, "bloqueada", "false", "true"));
        }
        return resultado;
    }

    public boolean desbloquearCalle(String origenId, String destinoId) {
        boolean resultado = grafoVial.desbloquearCalleDobleMano(origenId, destinoId);
        if (resultado) {
            historialCambios.apilar(new Cambio("Calle", origenId + "->" + destinoId, "bloqueada", "true", "false"));
        }
        return resultado;
    }

    public void mostrarRedVial() {
        grafoVial.mostrarGrafo();
    }

    public boolean registrarVehiculo(String interseccionId, Vehiculo vehiculo) {
        return grafoVial.registrarVehiculoEnInterseccion(interseccionId, vehiculo);
    }

    public Vehiculo liberarVehiculo(String interseccionId) {
        return grafoVial.liberarVehiculoEnInterseccion(interseccionId);
    }

    // ===== EMERGENCIAS =====

    public void reportarEmergencia(Emergencia emergencia) {
        colaEmergencias.insertar(emergencia);
        System.out.println("Emergencia registrada: " + emergencia);
    }

    public Emergencia atenderEmergencia() {
        Emergencia e = colaEmergencias.atender();
        if (e == null) {
            System.out.println("No hay emergencias pendientes.");
        } else {
            System.out.println("Atendiendo: " + e);
        }
        return e;
    }


    public void mostrarEmergencias() {
        colaEmergencias.mostrar();
    }

    // ===== HISTORIAL =====

    public void deshacerUltimoCambio() {
        Cambio cambio = historialCambios.desapilar();
        if (cambio == null) {
            System.out.println("No hay cambios para deshacer.");
            return;
        }
        System.out.println("Deshaciendo: " + cambio);
        revertirCambio(cambio);
    }

    private void revertirCambio(Cambio cambio) {
        if (cambio.getAtributo().equals("activo")) {
            boolean valorAnterior = Boolean.parseBoolean(cambio.getValorAnterior());
            dispositivos.modificarActivo(cambio.getIdEntidad(), valorAnterior);
        } else if (cambio.getAtributo().equals("estado")) {
            Dispositivo d = dispositivos.obtener(cambio.getIdEntidad());
            if (d instanceof Semaforo) {
                ((Semaforo) d).setEstado(Semaforo.Estado.valueOf(cambio.getValorAnterior()));
            }
        } else if (cambio.getAtributo().equals("bloqueada")) {
            String[] partes = cambio.getIdEntidad().split("->");
            if (partes.length == 2) {
                boolean estabaBloqueda = Boolean.parseBoolean(cambio.getValorAnterior());
                if (estabaBloqueda) {
                    grafoVial.bloquearCalleDobleMano(partes[0], partes[1]);
                } else {
                    grafoVial.desbloquearCalleDobleMano(partes[0], partes[1]);
                }
            }
        } else if (cambio.getAtributo().equals("demora")) {
            String[] partes = cambio.getIdEntidad().split("->");
            if (partes.length == 2) {
                int demoraAnterior = Integer.parseInt(cambio.getValorAnterior());
                grafoVial.registrarDemoraEnCalleDobleMano(partes[0], partes[1], demoraAnterior);
            }
        }
    }

    public void mostrarHistorial() {
        historialCambios.mostrar();
    }

    // ===== TERRITORIO =====

    public void crearCiudad(String nombre) {
        arbolTerritorial.crearCiudad(nombre);
    }

    public boolean agregarZona(String nombreZona) {
        return arbolTerritorial.agregarZona(nombreZona);
    }

    public boolean agregarBarrio(String nombreZona, String nombreBarrio) {
        return arbolTerritorial.agregarBarrio(nombreZona, nombreBarrio);
    }

    public boolean agregarManzana(String nombreBarrio, String nombreManzana) {
        return arbolTerritorial.agregarManzana(nombreBarrio, nombreManzana);
    }

    public void incrementarIncidentesEn(String nombre) {
        arbolTerritorial.incrementarIncidentes(nombre);
    }

    public void mostrarTerritorio() {
        arbolTerritorial.mostrar();
    }

    public void mostrarIntersecciones() {
        grafoVial.mostrarIntersecciones();
    }

    // ===== CONSULTAS =====

    public boolean existeInterseccion(String id) {
        return grafoVial.buscarInterseccion(id) != null;
    }

    public boolean existeDispositivo(String id) {
        return dispositivos.contieneClave(id);
    }

    public boolean hayEmergencias() {
        return !colaEmergencias.estaVacia();
    }

    public void mostrarProximaEmergencia() {
        Emergencia e = colaEmergencias.frente();
        if (e == null) {
            System.out.println("No hay emergencias pendientes.");
        } else {
            System.out.println(e);
        }
    }

    // ===== RUTAS POR DIRECCION =====

    public boolean calcularRutaMenosTramosPorDirecciones(Direccion origen, Direccion destino) {
        return grafoVial.mostrarRutaMenosTramosPorDirecciones(origen, destino);
    }

    public boolean calcularRutaMasRapidaPorDirecciones(Direccion origen, Direccion destino) {
        return grafoVial.mostrarRutaMasRapidaPorDirecciones(origen, destino);
    }

    public boolean calcularRutaMasCortaPorDirecciones(Direccion origen, Direccion destino) {
        return grafoVial.mostrarRutaMasCortaPorDirecciones(origen, destino);
    }

    // ===== GESTION DE CALLES =====

    public boolean registrarDemoraEnCalle(String origenId, String destinoId, int demoraMinutos) {
        int anterior = grafoVial.obtenerDemoraEnCalle(origenId, destinoId);
        boolean resultado = grafoVial.registrarDemoraEnCalleDobleMano(origenId, destinoId, demoraMinutos);
        if (resultado) {
            historialCambios.apilar(new Cambio("Calle", origenId + "->" + destinoId,
                    "demora", String.valueOf(anterior), String.valueOf(demoraMinutos)));
        }
        return resultado;
    }

    public void mostrarCallesDetalladas() {
        grafoVial.mostrarCallesDetalladas();
    }
}