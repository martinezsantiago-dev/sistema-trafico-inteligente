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
    // Emergencias que ya despacharon un vehículo pero el operador todavía no confirmó que se resolvieron.
    private ListaEnlazada<Emergencia> emergenciasEnCurso;

    // Seguimiento de posición del ciudadano
    private String interseccionActualCiudadano;
    private Vehiculo vehiculoCiudadano;

    public SistemaTrafico() {
        this.dispositivos = new DiccionarioDispositivos(31);
        this.grafoVial = new GrafoVial();
        this.arbolTerritorial = new ArbolTerritorial();
        this.colaEmergencias = new ColaPrioridadEmergencias();
        this.historialCambios = new PilaHistorial<>();
        this.emergenciasEnCurso = new ListaEnlazada<>();
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
        if (resultado) historialCambios.apilar(new Cambio("Dispositivo", d.getId(), "activo", anterior, "true"));
        return resultado;
    }

    public boolean desactivarDispositivo(String id) {
        Dispositivo d = dispositivos.obtener(id);
        if (d == null) return false;
        String anterior = String.valueOf(d.isActivo());
        boolean resultado = dispositivos.modificarActivo(id, false);
        if (resultado) historialCambios.apilar(new Cambio("Dispositivo", d.getId(), "activo", anterior, "false"));
        return resultado;
    }

    public boolean cambiarEstadoSemaforo(String id, Semaforo.Estado nuevoEstado) {
        Dispositivo d = dispositivos.obtener(id);
        if (!(d instanceof Semaforo)) return false;
        Semaforo semaforo = (Semaforo) d;
        String anterior = semaforo.getEstado().toString();
        semaforo.setEstado(nuevoEstado);
        historialCambios.apilar(new Cambio("Semaforo", d.getId(), "estado", anterior, nuevoEstado.toString()));
        return true;
    }

    public Dispositivo obtenerDispositivo(String id) {
        return dispositivos.obtener(id);
    }

    public void mostrarDispositivos() {
        ListaEnlazada<Dispositivo> todos = dispositivos.obtenerTodos();
        if (todos.estaVacia()) {
            System.out.println("No hay dispositivos registrados.");
            return;
        }

        System.out.println("Dispositivos registrados (" + todos.tamanio() + "):");
        System.out.println("─────────────────────────────────────────");
        Nodo<Dispositivo> aux = todos.getCabeza();
        while (aux != null) {
            Dispositivo d = aux.dato;
            System.out.println(d.toString(getNombreInterseccion(d.getInterseccion())));
            aux = aux.siguiente;
        }
        System.out.println("─────────────────────────────────────────");
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

    public ListaEnlazada<String> obtenerNombresCalles() {
        return grafoVial.obtenerNombresCalles();
    }

    public String estadoCalle(String nombreCalle) {
        return grafoVial.estadoCalle(nombreCalle);
    }

    public ListaEnlazada<Calle> obtenerTramosDeCalle(String nombreCalle) {
        return grafoVial.obtenerTramosDeCalle(nombreCalle);
    }

    public ListaEnlazada<Calle> getCallesAdyacentes(String interseccionId) {
        return grafoVial.getCallesAdyacentes(interseccionId);
    }

    public boolean esTramoDobleMano(String origenId, String destinoId) {
        return grafoVial.existeTramoInverso(origenId, destinoId);
    }

    public boolean isTramoBloqueado(String origenId, String destinoId) {
        return grafoVial.isTramoBloqueado(origenId, destinoId);
    }

    public boolean bloquearTramo(String origenId, String destinoId, boolean ambosSentidos) {
        boolean resultado = grafoVial.bloquearTramo(origenId, destinoId);
        if (resultado) {
            historialCambios.apilar(new Cambio("Calle", origenId + "-" + destinoId, "bloqueadaTramo", "false", "true"));
        }
        if (ambosSentidos && grafoVial.existeTramoInverso(origenId, destinoId)) {
            boolean resultadoInverso = grafoVial.bloquearTramo(destinoId, origenId);
            if (resultadoInverso) {
                historialCambios.apilar(new Cambio("Calle", destinoId + "-" + origenId, "bloqueadaTramo", "false", "true"));
            }
        }
        return resultado;
    }

    public boolean desbloquearTramo(String origenId, String destinoId, boolean ambosSentidos) {
        boolean resultado = grafoVial.desbloquearTramo(origenId, destinoId);
        if (resultado) {
            historialCambios.apilar(new Cambio("Calle", origenId + "-" + destinoId, "bloqueadaTramo", "true", "false"));
        }
        if (ambosSentidos && grafoVial.existeTramoInverso(origenId, destinoId)) {
            boolean resultadoInverso = grafoVial.desbloquearTramo(destinoId, origenId);
            if (resultadoInverso) {
                historialCambios.apilar(new Cambio("Calle", destinoId + "-" + origenId, "bloqueadaTramo", "true", "false"));
            }
        }
        return resultado;
    }

    // ===== SEGUIMIENTO DE VEHÍCULO =====

    public void registrarVehiculoCiudadano(String patente, String tipo) {
        vehiculoCiudadano = new Vehiculo(patente, tipo, null);
        System.out.println("Vehículo registrado: " + patente + " (" + tipo + ")");
    }

    public boolean registrarVehiculoEnInterseccion(String interseccionId, Vehiculo vehiculo) {
        return grafoVial.registrarVehiculoEnInterseccion(interseccionId, vehiculo);
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


    // ===== EMERGENCIAS =====

    public void reportarEmergencia(Emergencia emergencia) {
        colaEmergencias.insertar(emergencia);
        if (emergencia.getZona() != null) {
            arbolTerritorial.incrementarIncidentes(emergencia.getZona());
        }
        System.out.println("Emergencia registrada: " + emergencia);
    }

    // Devuelve el tipo de vehículo que corresponde según el incidente, o null si no requiere despacho
    public String determinarTipoVehiculo(Emergencia emergencia) {
        String tipoIncidente = emergencia.getTipoIncidente();
        if (tipoIncidente == null) return null;

        switch (tipoIncidente) {
            case "CHOQUE": return emergencia.isHayHeridos() ? "ambulancia" : "patrulla";
            case "INCENDIO": return "bomberos";
            case "SEMAFORO_ROTO": return "tecnico";
            case "CAMARA_ROTA": return "tecnico";
            case "CALLE_BLOQUEADA": return "patrulla";
            case "OTRO": return emergencia.getTipoVehiculoRequerido();
            default: return null;
        }
    }

    // Solo hace el despacho en sí (asume que ya se confirmó que hay un vehículo disponible).
    private void despacharVehiculoSegunEmergencia(Emergencia emergencia, String tipoVehiculo) {
        String interseccionEmergenciaId = emergencia.getInterseccionId();
        String interseccionVehiculoId = grafoVial.buscarVehiculoDisponiblePorTipo(interseccionEmergenciaId, tipoVehiculo);
        Vehiculo vehiculo = grafoVial.liberarVehiculoDeTipoEnInterseccion(interseccionVehiculoId, tipoVehiculo);

        System.out.println("Despachando " + tipoVehiculo + " (" + vehiculo.getPatente() + ") desde "
                + interseccionVehiculoId + " (" + getNombreInterseccion(interseccionVehiculoId) + ")"
                + " hacia la emergencia en "
                + interseccionEmergenciaId + " (" + getNombreInterseccion(interseccionEmergenciaId) + ")");

        calcularRutaMasRapida(interseccionVehiculoId, interseccionEmergenciaId);

        // El vehículo queda físicamente en la intersección de la emergencia, marcado ocupado
        // (no vuelve a aparecer como disponible hasta que se resuelva esa emergencia).
        vehiculo.setInterseccionActual(interseccionEmergenciaId);
        vehiculo.setOcupado(true);
        grafoVial.registrarVehiculoEnInterseccion(interseccionEmergenciaId, vehiculo);
        emergencia.registrarVehiculoDespachado(tipoVehiculo, vehiculo.getPatente());
        emergenciasEnCurso.insertarFinal(emergencia);
    }

    public boolean hayEmergencias() {
        return !colaEmergencias.estaVacia();
    }

    // Saca la emergencia más urgente de la cola de prioridad y despacha el vehículo correspondiente,
    // pero solo si hay uno disponible: si no lo hay, la emergencia NO se saca de la cola (sigue pendiente),
    // para no perderla silenciosamente. El llamador puede optar por forzarAtencionSinVehiculo() en ese caso.
    public Emergencia atenderEmergencia() {
        Emergencia e = colaEmergencias.frente();
        if (e == null) {
            System.out.println("No hay emergencias pendientes.");
            return null;
        }

        String tipoVehiculo = determinarTipoVehiculo(e);
        if (tipoVehiculo != null && grafoVial.buscarVehiculoDisponiblePorTipo(e.getInterseccionId(), tipoVehiculo) == null) {
            System.out.println("No hay vehículos de tipo '" + tipoVehiculo + "' disponibles en este momento. "
                    + "La emergencia sigue pendiente en la cola.");
            return null;
        }

        colaEmergencias.atender();
        System.out.println("Atendiendo: " + e);
        if (tipoVehiculo != null) {
            despacharVehiculoSegunEmergencia(e, tipoVehiculo);
        }
        return e;
    }

    // Saca la emergencia de la cola sin despachar ningún vehículo (para cuando el operador decide
    // cerrarla igual aunque no haya vehículos disponibles, ej. se resolvió sola o por otro medio).
    public Emergencia forzarAtencionSinVehiculo() {
        Emergencia e = colaEmergencias.atender();
        if (e == null) return null;
        System.out.println("Atendiendo (sin vehículo despachado): " + e);
        return e;
    }

    public boolean hayEmergenciasEnCurso() {
        return !emergenciasEnCurso.estaVacia();
    }

    public void mostrarEmergenciasEnCurso() {
        if (emergenciasEnCurso.estaVacia()) {
            System.out.println("No hay emergencias en curso.");
            return;
        }
        Nodo<Emergencia> aux = emergenciasEnCurso.getCabeza();
        while (aux != null) {
            System.out.println(aux.dato);
            aux = aux.siguiente;
        }
    }

    public ListaEnlazada<Emergencia> getEmergenciasEnCurso() {
        return emergenciasEnCurso;
    }

    // Libera al vehículo que atendió esta emergencia: queda disponible para el próximo despacho,
    // y la emergencia deja de figurar como "en curso".
    public boolean resolverEmergencia(Emergencia emergencia) {
        emergenciasEnCurso.eliminar(emergencia);

        String patente = emergencia.getPatenteVehiculoDespachado();
        if (patente == null) return true;

        boolean encontrado = grafoVial.marcarVehiculoDisponible(emergencia.getInterseccionId(), patente);
        if (encontrado) {
            System.out.println("Vehículo " + emergencia.getTipoVehiculoDespachado() + " (" + patente
                    + ") liberado: terminó su tarea en la emergencia y quedó disponible de nuevo.");
        } else {
            System.out.println("No se encontró el vehículo " + patente + " en "
                    + emergencia.getInterseccionId() + " para liberarlo.");
        }
        return encontrado;
    }

    public String getZonaDeInterseccion(String id) {
        Interseccion i = grafoVial.buscarInterseccion(id);
        if (i == null) return "Sin zona";
        return i.getZona();
    }

    public void mostrarEmergencias() {
        colaEmergencias.mostrar();
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
        return revertirCambio(cambio);
    }

    // Devuelve si el revertido realmente tuvo efecto (ej. puede fallar si, entre el cambio original
    // y el deshacer, el tramo se bloqueó y ya no admite la demora anterior).
    private boolean revertirCambio(Cambio cambio) {
        if (cambio.getAtributo().equals("activo")) {
            return dispositivos.modificarActivo(cambio.getIdEntidad(),
                    Boolean.parseBoolean(cambio.getValorAnterior()));

        } else if (cambio.getAtributo().equals("estado")) {
            Dispositivo d = dispositivos.obtener(cambio.getIdEntidad());
            if (!(d instanceof Semaforo)) return false;  // instanceof sirve para comprobar si un objeto es una instancia de una clase específica o de una de sus subclases
            ((Semaforo) d).setEstado(
                    Semaforo.Estado.valueOf(cambio.getValorAnterior()));
            return true;

        } else if (cambio.getAtributo().equals("bloqueadaNombre")) {
            boolean valorAnterior = Boolean.parseBoolean(cambio.getValorAnterior());
            return valorAnterior
                    ? grafoVial.bloquearCallePorNombre(cambio.getIdEntidad())
                    : grafoVial.desbloquearCallePorNombre(cambio.getIdEntidad());

        } else if (cambio.getAtributo().equals("bloqueadaTramo")) {
            String[] partes = cambio.getIdEntidad().split("-");
            if (partes.length != 2) return false;
            boolean valorAnterior = Boolean.parseBoolean(cambio.getValorAnterior());
            return valorAnterior
                    ? grafoVial.bloquearTramo(partes[0], partes[1])
                    : grafoVial.desbloquearTramo(partes[0], partes[1]);

        } else if (cambio.getAtributo().equals("demora")) {
            String[] partes = cambio.getIdEntidad().split("-");
            if (partes.length != 2) return false;
            return grafoVial.registrarDemoraEnCalle(partes[0], partes[1],
                    Integer.parseInt(cambio.getValorAnterior()));
        }
        return false;
    }

    public int getDemoraEnCalle(String origenId, String destinoId) {
        return grafoVial.getDemoraEnCalle(origenId, destinoId);
    }

    public void mostrarCallesConDemora() {
        grafoVial.mostrarCallesConDemora();
    }

    public void mostrarHistorial() {
        historialCambios.mostrar();
    }

    public Nodo<Cambio> obtenerHistorialDesdeElTope() {
        return historialCambios.getTope();
    }


    // ===== TERRITORIO =====

    public void crearCiudad(String nombre) { arbolTerritorial.crearCiudad(nombre); }
    public boolean agregarZona(String nombreZona) { return arbolTerritorial.agregarZona(nombreZona); }
    public boolean agregarBarrio(String z, String b) { return arbolTerritorial.agregarBarrio(z, b); }
    public boolean agregarManzana(String b, String m) { return arbolTerritorial.agregarManzana(b, m); }
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