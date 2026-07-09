// tda/GrafoVial.java
package tda;

import modelo.Calle;
import modelo.Interseccion;
import modelo.Vehiculo;
import tda.interfaces.IGrafoVial;

public class GrafoVial implements IGrafoVial {

    private ListaEnlazada<Interseccion> intersecciones;
    private static final int SIN_RUTA = 999999999;

    public GrafoVial() {
        this.intersecciones = new ListaEnlazada<>();
    }

    public ListaEnlazada<Interseccion> getIntersecciones() {
        return intersecciones;
    }

    @Override
    public boolean estaVacio() {
        return intersecciones.estaVacia();
    }

    @Override
    public int cantidadIntersecciones() {
        return intersecciones.tamanio();
    }

    @Override
    public boolean agregarInterseccion(Interseccion interseccion) {
        if (interseccion == null || interseccion.getId() == null) return false;
        if (buscarInterseccion(interseccion.getId()) != null) return false;
        intersecciones.insertarFinal(interseccion);
        return true;
    }

    @Override
    public Interseccion buscarInterseccion(String id) {
        if (id == null) return null;
        Nodo<Interseccion> aux = intersecciones.getCabeza();
        while (aux != null) {
            if (aux.dato.getId().equalsIgnoreCase(id)) return aux.dato;
            aux = aux.siguiente;
        }
        return null;
    }

    @Override
    public boolean agregarCalle(String origenId, String destinoId, String nombreCalle,
                                int alturaOrigen, int alturaDestino,
                                int distanciaMetros, int tiempoMinutos, boolean dobleMano) {
        Interseccion origen = buscarInterseccion(origenId);
        Interseccion destino = buscarInterseccion(destinoId);
        if (origen == null || destino == null) return false;
        if (nombreCalle == null || distanciaMetros <= 0 || tiempoMinutos <= 0) return false;
        if (buscarCalle(origenId, destinoId) != null) return false;

        origen.agregarCalle(new Calle(origenId, destinoId, nombreCalle,
                alturaOrigen, alturaDestino, distanciaMetros, tiempoMinutos));

        if (dobleMano && buscarCalle(destinoId, origenId) == null) {
            destino.agregarCalle(new Calle(destinoId, origenId, nombreCalle,
                    alturaDestino, alturaOrigen, distanciaMetros, tiempoMinutos));
        }
        return true;
    }

    @Override
    public boolean eliminarInterseccion(String id) {
        Interseccion interseccion = buscarInterseccion(id);
        if (interseccion == null) return false;
        intersecciones.eliminar(interseccion);
        Nodo<Interseccion> aux = intersecciones.getCabeza();
        while (aux != null) {
            Calle c = buscarCalle(aux.dato.getId(), id);
            if (c != null) aux.dato.getCallesAdyacentes().eliminar(c);
            aux = aux.siguiente;
        }
        return true;
    }

    @Override
    public boolean eliminarCalle(String origenId, String destinoId) {
        Calle calle = buscarCalle(origenId, destinoId);
        if (calle == null) return false;
        buscarInterseccion(origenId).getCallesAdyacentes().eliminar(calle);
        return true;
    }

    @Override
    public boolean registrarDemoraEnCalle(String origenId, String destinoId, int demoraExtraMinutos) {
        Calle calle = buscarCalle(origenId, destinoId);
        if (calle == null || demoraExtraMinutos < 0) return false;
        if (calle.isBloqueada() && demoraExtraMinutos > 0) return false;
        if (demoraExtraMinutos == 0 && calle.getDemoraExtraMinutos() == 0) {
            return false;
        }
        calle.setDemoraExtraMinutos(demoraExtraMinutos);
        return true;
    }

    public int getDemoraEnCalle(String origenId, String destinoId) {
        Calle calle = buscarCalle(origenId, destinoId);
        if (calle == null) return -1;
        return calle.getDemoraExtraMinutos();
    }

    private String tiempoTranscurridoDesde(long timestamp) {
        long segundos = (System.currentTimeMillis() - timestamp) / 1000;
        if (segundos < 60) return segundos + " seg";
        long minutos = segundos / 60;
        if (minutos < 60) return minutos + " min";
        long horas = minutos / 60;
        if (horas < 24) return horas + " h";
        long dias = horas / 24;
        return dias + " d";
    }

    public void mostrarCallesConDemora() {
        boolean hayDemoras = false;
        Nodo<Interseccion> auxI = intersecciones.getCabeza();
        while (auxI != null) {
            Nodo<Calle> auxC = auxI.dato.getCallesAdyacentes().getCabeza();
            while (auxC != null) {
                if (auxC.dato.getDemoraExtraMinutos() > 0) {
                    System.out.println("  " + auxC.dato.getNombre() +
                            " (" + auxC.dato.getOrigenId() + "→" + auxC.dato.getDestinoId() + ")" +
                            " | Demora: +" + auxC.dato.getDemoraExtraMinutos() + " min" +
                            " | Reportada hace " + tiempoTranscurridoDesde(auxC.dato.getTimestampDemora()));
                    hayDemoras = true;
                }
                auxC = auxC.siguiente;
            }
            auxI = auxI.siguiente;
        }
        if (!hayDemoras) System.out.println("  No hay demoras registradas.");
    }

    @Override
    public boolean registrarVehiculoEnInterseccion(String interseccionId, Vehiculo vehiculo) {
        Interseccion interseccion = buscarInterseccion(interseccionId);
        if (interseccion == null || vehiculo == null) return false;
        interseccion.registrarVehiculo(vehiculo);
        return true;
    }

    @Override
    public Vehiculo liberarVehiculoEnInterseccion(String interseccionId) {
        Interseccion interseccion = buscarInterseccion(interseccionId);
        if (interseccion == null) return null;
        return interseccion.liberarVehiculo();
    }

    public Vehiculo liberarVehiculoDeTipoEnInterseccion(String interseccionId, String tipo) {
        Interseccion interseccion = buscarInterseccion(interseccionId);
        if (interseccion == null) return null;
        return interseccion.liberarVehiculoDeTipo(tipo);
    }

    public boolean marcarVehiculoDisponible(String interseccionId, String patente) {
        Interseccion interseccion = buscarInterseccion(interseccionId);
        if (interseccion == null) return false;
        return interseccion.marcarVehiculoDisponible(patente);
    }

    // Dijkstra en sentido INVERSO: para cada intersección asentada, el costo representa
    // "tiempo desde esa intersección hasta interseccionEmergenciaId" (no al revés), porque
    // relajamos mirando quién tiene una calle que LLEGA al nodo recién asentado, no que sale de él.
    // Corta apenas asienta la primera intersección con un vehículo disponible del tipo pedido.
    public String buscarVehiculoDisponiblePorTipo(String interseccionEmergenciaId, String tipo) {
        int cantidad = intersecciones.tamanio();
        if (cantidad == 0) return null;

        String[] ids = cargarIds();
        int indiceEmergencia = obtenerIndice(ids, interseccionEmergenciaId);
        if (indiceEmergencia == -1) return null;

        int[] costos = new int[cantidad];
        boolean[] visitados = new boolean[cantidad];
        for (int i = 0; i < cantidad; i++) {
            costos[i] = SIN_RUTA;
            visitados[i] = false;
        }
        costos[indiceEmergencia] = 0;

        for (int i = 0; i < cantidad; i++) {
            int actualIndice = indiceMenorCosto(costos, visitados);
            if (actualIndice == -1) break;

            visitados[actualIndice] = true;
            Interseccion actual = buscarInterseccion(ids[actualIndice]);
            if (actual != null && actual.tieneVehiculoDisponibleDeTipo(tipo)) {
                return ids[actualIndice];
            }

            // Relajación inversa: busco, entre todas las intersecciones no asentadas,
            // cuáles tienen una calle que llega directamente a "actual".
            Nodo<Interseccion> auxCand = intersecciones.getCabeza();
            while (auxCand != null) {
                Interseccion candidata = auxCand.dato;
                int indiceCandidata = obtenerIndice(ids, candidata.getId());
                if (indiceCandidata != -1 && !visitados[indiceCandidata]) {
                    Calle calle = buscarCalle(candidata.getId(), ids[actualIndice]);
                    if (calle != null && !calle.isBloqueada()) {
                        int peso = calle.getTiempoTotalMinutos();
                        if (costos[actualIndice] + peso < costos[indiceCandidata]) {
                            costos[indiceCandidata] = costos[actualIndice] + peso;
                        }
                    }
                }
                auxCand = auxCand.siguiente;
            }
        }

        return null;
    }

    @Override
    public void mostrarIntersecciones() {
        if (intersecciones.estaVacia()) {
            System.out.println("No hay intersecciones cargadas");
            return;
        }
        System.out.println("Intersecciones disponibles:");
        Nodo<Interseccion> aux = intersecciones.getCabeza();
        while (aux != null) {
            System.out.println("  " + aux.dato.getId() + " | " + aux.dato.getNombre());
            aux = aux.siguiente;
        }
    }

    // Muestra las calles con sus nombres para que el usuario sepa qué ingresar
    @Override
    public void mostrarCallesDisponibles() {
        if (intersecciones.estaVacia()) {
            System.out.println("No hay calles cargadas");
            return;
        }
        System.out.println("Calles disponibles:");
        Nodo<String> aux = obtenerNombresCalles().getCabeza();
        while (aux != null) {
            String estado = estadoCalle(aux.dato);
            String tag = estado.equals("LIBRE") ? "" : " [" + estado + "]";
            System.out.println("  - " + aux.dato + tag);
            aux = aux.siguiente;
        }
    }

    // Nombres de calle únicos, sin repetir por cada tramo/interseccion
    public ListaEnlazada<String> obtenerNombresCalles() {
        ListaEnlazada<String> nombresVistos = new ListaEnlazada<>();
        Nodo<Interseccion> auxI = intersecciones.getCabeza();
        while (auxI != null) {
            Nodo<Calle> auxC = auxI.dato.getCallesAdyacentes().getCabeza();
            while (auxC != null) {
                String nombre = auxC.dato.getNombre();
                if (!nombresVistos.buscar(nombre)) {
                    nombresVistos.insertarFinal(nombre);
                }
                auxC = auxC.siguiente;
            }
            auxI = auxI.siguiente;
        }
        return nombresVistos;
    }
    // Nuevo
    // Un tramo por cada objeto Calle físico: si es doble mano, I1->I2 e I2->I1
    // son el mismo tramo y se devuelve una sola vez (la primera arista encontrada).
    public ListaEnlazada<Calle> getCallesAdyacentes(String interseccionId) {
        Interseccion interseccion = buscarInterseccion(interseccionId);
        return interseccion != null ? interseccion.getCallesAdyacentes() : new ListaEnlazada<>();
    }

    public ListaEnlazada<Calle> obtenerTramosDeCalle(String nombreCalle) {
        ListaEnlazada<Calle> tramos = new ListaEnlazada<>();
        Nodo<Interseccion> auxI = intersecciones.getCabeza();
        while (auxI != null) {
            Nodo<Calle> auxC = auxI.dato.getCallesAdyacentes().getCabeza();
            while (auxC != null) {
                Calle calle = auxC.dato;
                if (Calle.normalizar(calle.getNombre()).equals(Calle.normalizar(nombreCalle))
                        && !esTramoYaListado(tramos, calle)) {
                    tramos.insertarFinal(calle);
                }
                auxC = auxC.siguiente;
            }
            auxI = auxI.siguiente;
        }
        return tramos;
    }

    private boolean esTramoYaListado(ListaEnlazada<Calle> tramos, Calle calle) {
        Nodo<Calle> aux = tramos.getCabeza();
        while (aux != null) {
            Calle c = aux.dato;
            boolean mismoSentido = c.getOrigenId().equalsIgnoreCase(calle.getOrigenId())
                    && c.getDestinoId().equalsIgnoreCase(calle.getDestinoId());
            boolean sentidoInverso = c.getOrigenId().equalsIgnoreCase(calle.getDestinoId())
                    && c.getDestinoId().equalsIgnoreCase(calle.getOrigenId());
            if (mismoSentido || sentidoInverso) return true;
            aux = aux.siguiente;
        }
        return false;
    }

    public boolean existeTramoInverso(String origenId, String destinoId) {
        return buscarCalle(destinoId, origenId) != null;
    }
    // Nuevo
    public boolean isTramoBloqueado(String origenId, String destinoId) {
        Calle calle = buscarCalle(origenId, destinoId);
        return calle != null && calle.isBloqueada();
    }

    public boolean bloquearTramo(String origenId, String destinoId) {
        Calle calle = buscarCalle(origenId, destinoId);
        if (calle == null) return false;
        calle.setBloqueada(true);
        return true;
    }

    public boolean desbloquearTramo(String origenId, String destinoId) {
        Calle calle = buscarCalle(origenId, destinoId);
        if (calle == null) return false;
        calle.setBloqueada(false);
        return true;
    }

    // "LIBRE": ningún tramo bloqueado | "PARCIAL": algunos tramos sí y otros no | "BLOQUEADA": todos los tramos
    public String estadoCalle(String nombreCalle) {
        boolean algunaBloqueada = false;
        boolean algunaLibre = false;
        Nodo<Interseccion> auxI = intersecciones.getCabeza();
        while (auxI != null) {
            Nodo<Calle> auxC = auxI.dato.getCallesAdyacentes().getCabeza();
            while (auxC != null) {
                if (Calle.normalizar(auxC.dato.getNombre()).equals(Calle.normalizar(nombreCalle))) {
                    if (auxC.dato.isBloqueada()) algunaBloqueada = true;
                    else algunaLibre = true;
                }
                auxC = auxC.siguiente;
            }
            auxI = auxI.siguiente;
        }
        if (algunaBloqueada && algunaLibre) return "PARCIAL";
        if (algunaBloqueada) return "BLOQUEADA";
        return "LIBRE";
    }

    @Override
    public void mostrarGrafo() {
        if (intersecciones.estaVacia()) {
            System.out.println("El grafo vial está vacío");
            return;
        }
        System.out.println("Grafo vial - Lista de adyacencia:");
        Nodo<Interseccion> auxI = intersecciones.getCabeza();
        while (auxI != null) {
            Interseccion interseccion = auxI.dato;
            System.out.print("  " + interseccion.getId() + " (" + interseccion.getNombre() + ") -> ");
            Nodo<Calle> auxC = interseccion.getCallesAdyacentes().getCabeza();
            if (auxC == null) {
                System.out.print("sin calles salientes");
            }
            while (auxC != null) {
                System.out.print(auxC.dato.getDestinoId());
                if (auxC.siguiente != null) System.out.print(", ");
                auxC = auxC.siguiente;
            }
            System.out.println();
            auxI = auxI.siguiente;
        }
    }

    @Override
    public boolean mostrarRutaMenosTramos(String origenId, String destinoId) {
        return calcularRutaBFS(origenId, destinoId);
    }

    @Override
    public boolean mostrarRutaMasRapida(String origenId, String destinoId) {
        return calcularRutaMenorCosto(origenId, destinoId, "TIEMPO");
    }


    @Override
    public boolean mostrarRutaMasCorta(String origenId, String destinoId) {
        return calcularRutaMenorCosto(origenId, destinoId, "DISTANCIA");
    }

    // ===== PRIVADOS =====

    private Calle buscarCalle(String origenId, String destinoId) {
        Interseccion origen = buscarInterseccion(origenId);
        if (origen == null || destinoId == null) return null;
        Nodo<Calle> aux = origen.getCallesAdyacentes().getCabeza();
        while (aux != null) {
            if (aux.dato.getDestinoId().equalsIgnoreCase(destinoId)) return aux.dato;
            aux = aux.siguiente;
        }
        return null;
    }

    private String[] cargarIds() {
        int cantidad = intersecciones.tamanio();
        String[] ids = new String[cantidad];
        Nodo<Interseccion> aux = intersecciones.getCabeza();
        int i = 0;
        while (aux != null) {
            ids[i++] = aux.dato.getId();
            aux = aux.siguiente;
        }
        return ids;
    }

    private int obtenerIndice(String[] ids, String id) {
        if (id == null) return -1;
        for (int i = 0; i < ids.length; i++) {
            if (ids[i].equalsIgnoreCase(id)) return i;
        }
        return -1;
    }

    private boolean calcularRutaBFS(String origenId, String destinoId) {
        int cantidad = intersecciones.tamanio();
        if (cantidad == 0) {
            System.out.println("El grafo está vacío");
            return false;
        }

        String[] ids = cargarIds();
        int indiceOrigen = obtenerIndice(ids, origenId);
        int indiceDestino = obtenerIndice(ids, destinoId);

        if (indiceOrigen == -1 || indiceDestino == -1) {
            System.out.println("Origen o destino inexistente");
            return false;
        }

        boolean[] visitados = new boolean[cantidad]; // decidir vecino
        String[] anteriores = new String[cantidad];
        ColaFIFO<String> cola = new ColaFIFO<>();

        visitados[indiceOrigen] = true;
        cola.encolar(origenId);

        while (!cola.estaVacia()) {
            String actualId = cola.desencolar();
            if (actualId.equalsIgnoreCase(destinoId)) break;

            Interseccion actual = buscarInterseccion(actualId);
            if (actual != null) {
                Nodo<Calle> auxC = actual.getCallesAdyacentes().getCabeza();
                while (auxC != null) {
                    Calle calle = auxC.dato;
                    if (!calle.isBloqueada()) {
                        int indiceVecino = obtenerIndice(ids, calle.getDestinoId());
                        if (indiceVecino != -1 && !visitados[indiceVecino]) {
                            visitados[indiceVecino] = true;
                            anteriores[indiceVecino] = actualId;
                            cola.encolar(calle.getDestinoId());
                        }
                    }
                    auxC = auxC.siguiente;
                }
            }
        }

        if (!visitados[indiceDestino]) {
            System.out.println("No existe ruta disponible entre " + origenId + " y " + destinoId);
            return false;
        }

        System.out.println("Ruta por menor cantidad de tramos:");
        imprimirCamino(anteriores, ids, origenId, destinoId);
        System.out.println();
        System.out.println("Tramos: " + contarTramos(anteriores, ids, origenId, destinoId));
        return true;
    }

    private boolean calcularRutaMenorCosto(String origenId, String destinoId, String criterio) {
        int cantidad = intersecciones.tamanio();
        if (cantidad == 0) { System.out.println("El grafo está vacío"); return false; }

        String[] ids = cargarIds();
        int indiceOrigen = obtenerIndice(ids, origenId);
        int indiceDestino = obtenerIndice(ids, destinoId);

        if (indiceOrigen == -1 || indiceDestino == -1) {
            System.out.println("Origen o destino inexistente");
            return false;
        }

        int[] costos = new int[cantidad];
        boolean[] visitados = new boolean[cantidad];
        String[] anteriores = new String[cantidad];

        for (int i = 0; i < cantidad; i++) {
            costos[i] = SIN_RUTA;
            visitados[i] = false;
            anteriores[i] = null;
        }
        costos[indiceOrigen] = 0;

        for (int i = 0; i < cantidad; i++) {
            int actualIndice = indiceMenorCosto(costos, visitados);
            if (actualIndice == -1) break;

            visitados[actualIndice] = true;
            Interseccion actual = buscarInterseccion(ids[actualIndice]);

            if (actual != null) {
                Nodo<Calle> auxC = actual.getCallesAdyacentes().getCabeza();
                while (auxC != null) {
                    Calle calle = auxC.dato;
                    if (!calle.isBloqueada()) {
                        int indiceVecino = obtenerIndice(ids, calle.getDestinoId());
                        if (indiceVecino != -1 && !visitados[indiceVecino]) {
                            int peso = criterio.equals("DISTANCIA")
                                    ? calle.getDistanciaMetros()
                                    : calle.getTiempoTotalMinutos();
                            if (costos[actualIndice] + peso < costos[indiceVecino]) {
                                costos[indiceVecino] = costos[actualIndice] + peso;
                                anteriores[indiceVecino] = ids[actualIndice];
                            }
                        }
                    }
                    auxC = auxC.siguiente;
                }
            }
        }

        if (costos[indiceDestino] == SIN_RUTA) {
            System.out.println("No existe ruta disponible entre " + origenId + " y " + destinoId);
            return false;
        }

        if (criterio.equals("DISTANCIA")) {
            System.out.println("Ruta más corta por distancia:");
        } else {
            System.out.println("Ruta más rápida por tiempo:");
        }

        imprimirCamino(anteriores, ids, origenId, destinoId);
        System.out.println();

        if (criterio.equals("DISTANCIA")) {
            System.out.println("Distancia total: " + costos[indiceDestino] + " metros");
        } else {
            System.out.println("Tiempo estimado: " + costos[indiceDestino] + " minutos");
            mostrarDemorasEnRuta(anteriores, ids, origenId, destinoId);
        }
        return true;
    }

    // Recorre el camino calculado y avisa si alguno de sus tramos tiene una demora activa que sumó tiempo
    private void mostrarDemorasEnRuta(String[] anteriores, String[] ids, String origenId, String destinoId) {
        ListaEnlazada<Calle> tramosConDemora = new ListaEnlazada<>();
        int totalDemora = 0;

        String actual = destinoId;
        while (!actual.equalsIgnoreCase(origenId)) {
            int idx = obtenerIndice(ids, actual);
            if (idx == -1 || anteriores[idx] == null) return;
            String anteriorId = anteriores[idx];
            Calle calle = buscarCalle(anteriorId, actual);
            if (calle != null && calle.getDemoraExtraMinutos() > 0) {
                tramosConDemora.insertarFinal(calle);
                totalDemora += calle.getDemoraExtraMinutos();
            }
            actual = anteriorId;
        }

        if (tramosConDemora.estaVacia()) return;

        System.out.println("Esta ruta se vio afectada por demoras activas:");
        Nodo<Calle> aux = tramosConDemora.getCabeza();
        while (aux != null) {
            Calle c = aux.dato;
            System.out.println("  - " + c.getNombre() + " (" + c.getOrigenId() + "→" + c.getDestinoId() + "): +" + c.getDemoraExtraMinutos() + " min");
            aux = aux.siguiente;
        }
        System.out.println("Total agregado por demoras: " + totalDemora + " min.");
    }

    private int indiceMenorCosto(int[] costos, boolean[] visitados) {
        int menor = SIN_RUTA;
        int indice = -1;
        for (int i = 0; i < costos.length; i++) {
            if (!visitados[i] && costos[i] < menor) {
                menor = costos[i];
                indice = i;
            }
        }
        return indice;
    }

    private void imprimirCamino(String[] anteriores, String[] ids, String origenId, String actualId) {
        if (actualId.equalsIgnoreCase(origenId)) {
            imprimirInterseccion(actualId);
            return;
        }
        int indiceActual = obtenerIndice(ids, actualId);
        if (indiceActual == -1 || anteriores[indiceActual] == null) {
            imprimirInterseccion(actualId);
            return;
        }
        imprimirCamino(anteriores, ids, origenId, anteriores[indiceActual]);
        System.out.print(" -> ");
        imprimirInterseccion(actualId);
    }

    private void imprimirInterseccion(String id) {
        Interseccion i = buscarInterseccion(id);
        if (i == null) System.out.print(id);
        else System.out.print(i.getId() + " (" + i.getNombre() + ")");
    }

    private int contarTramos(String[] anteriores, String[] ids, String origenId, String destinoId) {
        int tramos = 0;
        String actual = destinoId;
        while (!actual.equalsIgnoreCase(origenId)) {
            int idx = obtenerIndice(ids, actual);
            if (idx == -1 || anteriores[idx] == null) return -1;
            actual = anteriores[idx];
            tramos++;
        }
        return tramos;
    }

    public boolean bloquearCallePorNombre(String nombreCalle) {
        if (nombreCalle == null) return false;
        boolean encontro = false;
        Nodo<Interseccion> auxI = intersecciones.getCabeza();
        while (auxI != null) {
            Nodo<Calle> auxC = auxI.dato.getCallesAdyacentes().getCabeza();
            while (auxC != null) {
                if (Calle.normalizar(auxC.dato.getNombre()).equals(Calle.normalizar(nombreCalle))) {
                    encontro = true;
                    if (!auxC.dato.isBloqueada()) {
                        auxC.dato.setBloqueada(true);
                    }
                }
                auxC = auxC.siguiente;
            }
            auxI = auxI.siguiente;
        }
        return encontro;
    }

    public boolean desbloquearCallePorNombre(String nombreCalle) {
        if (nombreCalle == null) return false;
        boolean encontro = false;
        Nodo<Interseccion> auxI = intersecciones.getCabeza();
        while (auxI != null) {
            Nodo<Calle> auxC = auxI.dato.getCallesAdyacentes().getCabeza();
            while (auxC != null) {
                if (Calle.normalizar(auxC.dato.getNombre()).equals(Calle.normalizar(nombreCalle))) {
                    encontro = true;
                    if (auxC.dato.isBloqueada()) {
                        auxC.dato.setBloqueada(false);
                    }
                }
                auxC = auxC.siguiente;
            }
            auxI = auxI.siguiente;
        }
        return encontro;
    }

}