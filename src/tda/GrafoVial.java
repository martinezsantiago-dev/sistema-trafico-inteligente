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

    // Busca intersecciones cuyo NOMBRE contenga el texto ingresado
    public Interseccion buscarInterseccionPorNombre(String nombre) {
        if (nombre == null) return null;
        Nodo<Interseccion> aux = intersecciones.getCabeza();
        while (aux != null) {
            if (aux.dato.getNombre().toLowerCase().contains(nombre.toLowerCase())) {
                return aux.dato;
            }
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
    public boolean bloquearCalle(String origenId, String destinoId) {
        Calle calle = buscarCalle(origenId, destinoId);
        if (calle == null) return false;
        calle.setBloqueada(true);
        return true;
    }

    @Override
    public boolean desbloquearCalle(String origenId, String destinoId) {
        Calle calle = buscarCalle(origenId, destinoId);
        if (calle == null) return false;
        calle.setBloqueada(false);
        return true;
    }

    @Override
    public boolean registrarDemoraEnCalle(String origenId, String destinoId, int demoraExtraMinutos) {
        Calle calle = buscarCalle(origenId, destinoId);
        if (calle == null || demoraExtraMinutos < 0) return false;
        calle.setDemoraExtraMinutos(demoraExtraMinutos);
        return true;
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
        ListaEnlazada<String> nombresVistos = new ListaEnlazada<>();
        Nodo<Interseccion> auxI = intersecciones.getCabeza();
        while (auxI != null) {
            Nodo<Calle> auxC = auxI.dato.getCallesAdyacentes().getCabeza();
            while (auxC != null) {
                String nombre = auxC.dato.getNombre();
                if (!nombresVistos.buscar(nombre)) {
                    System.out.println("  - " + nombre);
                    nombresVistos.insertarFinal(nombre);
                }
                auxC = auxC.siguiente;
            }
            auxI = auxI.siguiente;
        }
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

    // Resuelve nombre de calle + altura a ID de intersección
    @Override
    public String obtenerInterseccionCercana(String calle, int altura) {
        if (calle == null) return null;
        Nodo<Interseccion> auxI = intersecciones.getCabeza();
        while (auxI != null) {
            Nodo<Calle> auxC = auxI.dato.getCallesAdyacentes().getCabeza();
            while (auxC != null) {
                if (auxC.dato.contieneAltura(calle, altura)) {
                    return auxC.dato.obtenerInterseccionMasCercana(altura);
                }
                auxC = auxC.siguiente;
            }
            auxI = auxI.siguiente;
        }
        return null;
    }

    // Resuelve solo por nombre de calle, sin altura — devuelve la primera intersección que la contiene
    public String obtenerInterseccionPorNombreCalle(String nombreCalle) {
        if (nombreCalle == null) return null;
        Nodo<Interseccion> auxI = intersecciones.getCabeza();
        while (auxI != null) {
            Nodo<Calle> auxC = auxI.dato.getCallesAdyacentes().getCabeza();
            while (auxC != null) {
                if (auxC.dato.getNombre().equalsIgnoreCase(nombreCalle)) {
                    return auxI.dato.getId();
                }
                auxC = auxC.siguiente;
            }
            auxI = auxI.siguiente;
        }
        return null;
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
        if (cantidad == 0) { System.out.println("El grafo está vacío"); return false; }

        String[] ids = cargarIds();
        int indiceOrigen = obtenerIndice(ids, origenId);
        int indiceDestino = obtenerIndice(ids, destinoId);

        if (indiceOrigen == -1 || indiceDestino == -1) {
            System.out.println("Origen o destino inexistente");
            return false;
        }

        boolean[] visitados = new boolean[cantidad];
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
        }
        return true;
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

    public String getNombreRealCalle(String nombreCalle) {
        Nodo<Interseccion> auxI = intersecciones.getCabeza();
        while (auxI != null) {
            Nodo<Calle> auxC = auxI.dato.getCallesAdyacentes().getCabeza();
            while (auxC != null) {
                if (auxC.dato.getNombre().equalsIgnoreCase(nombreCalle))
                    return auxC.dato.getNombre();
                auxC = auxC.siguiente;
            }
            auxI = auxI.siguiente;
        }
        return nombreCalle;
    }

    public boolean bloquearCallePorNombre(String nombreCalle) {
        if (nombreCalle == null) return false;
        boolean encontro = false;
        Nodo<Interseccion> auxI = intersecciones.getCabeza();
        while (auxI != null) {
            Nodo<Calle> auxC = auxI.dato.getCallesAdyacentes().getCabeza();
            while (auxC != null) {
                if (auxC.dato.getNombre().equalsIgnoreCase(nombreCalle)) {
                    if (auxC.dato.isBloqueada()) {          // ← agregás esto
                        System.out.println("La calle '" + nombreCalle + "' ya estaba bloqueada.");
                        return true;
                    }
                    auxC.dato.setBloqueada(true);
                    System.out.println("Calle '" + auxC.dato.getNombre() + "' bloqueada.");
                    encontro = true;
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
                if (auxC.dato.getNombre().equalsIgnoreCase(nombreCalle)) {
                    encontro = true;
                    if (!auxC.dato.isBloqueada()) {
                        System.out.println("La calle '" + nombreCalle + "' ya estaba desbloqueada.");
                        return true; // ← true, se encontró, solo que ya estaba libre
                    }
                    auxC.dato.setBloqueada(false);
                    System.out.println("Calle '" + auxC.dato.getNombre() + "' desbloqueada.");
                    encontro = true;
                }
                auxC = auxC.siguiente;
            }
            auxI = auxI.siguiente;
        }
        return encontro;
    }

    public String normalizarId(String id) {
        if (id == null) return null;
        Nodo<Interseccion> aux = intersecciones.getCabeza();
        while (aux != null) {
            if (aux.dato.getId().equalsIgnoreCase(id)) {
                return aux.dato.getId(); // devuelve "I1" aunque entró "i1"
            }
            aux = aux.siguiente;
        }
        return null;
    } // para que el usuario pueda agregar ids en minuscula


    public ListaEnlazada<String> buscarInterseccionesPorCalle(String nombreCalle) {
        ListaEnlazada<String> resultado = new ListaEnlazada<>();
        if (nombreCalle == null) return resultado;

        Nodo<Interseccion> auxI = intersecciones.getCabeza();
        while (auxI != null) {
            // Busca en el NOMBRE de la intersección
            if (auxI.dato.getNombre().toLowerCase().contains(nombreCalle.toLowerCase())) {
                resultado.insertarFinal(auxI.dato.getId());
            }
            auxI = auxI.siguiente;
        }
        return resultado;
    }
    public String resolverEntrada(String entrada) {
        if (entrada == null) return null;

        // 1. Por nombre de calle exacto — si hay varias intersecciones lo maneja pedirInterseccion
        Nodo<Interseccion> auxI = intersecciones.getCabeza();
        while (auxI != null) {
            Nodo<Calle> auxC = auxI.dato.getCallesAdyacentes().getCabeza();
            while (auxC != null) {
                if (auxC.dato.getNombre().equalsIgnoreCase(entrada)) {
                    return auxI.dato.getId();
                }
                auxC = auxC.siguiente;
            }
            auxI = auxI.siguiente;
        }

        // 2. Por nombre de intersección exacto
        auxI = intersecciones.getCabeza();
        while (auxI != null) {
            if (auxI.dato.getNombre().equalsIgnoreCase(entrada)) {
                return auxI.dato.getId();
            }
            auxI = auxI.siguiente;
        }

        // 3. Por ID normalizado
        return normalizarId(entrada);
    }

}