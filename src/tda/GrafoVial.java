package tda;

import modelo.Calle;
import modelo.Direccion;
import modelo.Interseccion;
import modelo.Vehiculo;

public class GrafoVial {

    private ListaEnlazada<Interseccion> intersecciones;

    private static final int SIN_RUTA = 999999999;

    public GrafoVial() {
        this.intersecciones = new ListaEnlazada<>();
    }

    public boolean estaVacio() {
        return intersecciones.estaVacia();
    }

    public int cantidadIntersecciones() {
        return intersecciones.tamanio();
    }

    public boolean agregarInterseccion(Interseccion interseccion) {
        if (interseccion == null) {
            return false;
        }

        if (interseccion.getId() == null) {
            return false;
        }

        if (buscarInterseccion(interseccion.getId()) != null) {
            return false;
        }

        intersecciones.insertarFinal(interseccion);
        return true;
    }

    public Interseccion buscarInterseccion(String id) {
        if (id == null) {
            return null;
        }

        Nodo<Interseccion> aux = intersecciones.getCabeza();

        while (aux != null) {
            if (aux.dato.getId().equalsIgnoreCase(id)) {
                return aux.dato;
            }

            aux = aux.siguiente;
        }

        return null;
    }

    public boolean agregarCalle(String origenId, String destinoId, String nombreCalle,
                                int alturaOrigen, int alturaDestino,
                                int distanciaMetros, int tiempoMinutos,
                                boolean dobleMano) {

        Interseccion origen = buscarInterseccion(origenId);
        Interseccion destino = buscarInterseccion(destinoId);

        if (origen == null || destino == null) {
            return false;
        }

        if (nombreCalle == null || distanciaMetros <= 0 || tiempoMinutos <= 0) {
            return false;
        }

        if (buscarCalle(origenId, destinoId) != null) {
            return false;
        }

        Calle ida = new Calle(
                origenId,
                destinoId,
                nombreCalle,
                alturaOrigen,
                alturaDestino,
                distanciaMetros,
                tiempoMinutos
        );

        origen.agregarCalle(ida);

        if (dobleMano && buscarCalle(destinoId, origenId) == null) {
            Calle vuelta = new Calle(
                    destinoId,
                    origenId,
                    nombreCalle,
                    alturaDestino,
                    alturaOrigen,
                    distanciaMetros,
                    tiempoMinutos
            );

            destino.agregarCalle(vuelta);
        }

        return true;
    }

    public boolean eliminarInterseccion(String id) {
        Interseccion interseccion = buscarInterseccion(id);
        if (interseccion == null) return false;

        intersecciones.eliminar(interseccion);

        Nodo<Interseccion> aux = intersecciones.getCabeza();
        while (aux != null) {
            Calle calleApuntando = buscarCalle(aux.dato.getId(), id);
            if (calleApuntando != null) {
                aux.dato.getCallesAdyacentes().eliminar(calleApuntando);
            }
            aux = aux.siguiente;
        }

        return true;
    }

    public boolean eliminarCalle(String origenId, String destinoId) {
        Calle calle = buscarCalle(origenId, destinoId);
        if (calle == null) return false;

        Interseccion origen = buscarInterseccion(origenId);
        origen.getCallesAdyacentes().eliminar(calle);
        return true;
    }

    public boolean eliminarCalleDobleMano(String origenId, String destinoId) {
        boolean ida = eliminarCalle(origenId, destinoId);
        boolean vuelta = eliminarCalle(destinoId, origenId);
        return ida || vuelta;
    }

    public void mostrarIntersecciones() {
        if (intersecciones.estaVacia()) {
            System.out.println("No hay intersecciones cargadas");
            return;
        }

        System.out.println("Intersecciones disponibles:");

        Nodo<Interseccion> aux = intersecciones.getCabeza();

        while (aux != null) {
            System.out.println(aux.dato);
            aux = aux.siguiente;
        }
    }

    public void mostrarGrafo() {
        if (intersecciones.estaVacia()) {
            System.out.println("El grafo vial está vacío");
            return;
        }

        System.out.println("Grafo vial - Lista de adyacencia:");

        Nodo<Interseccion> auxInterseccion = intersecciones.getCabeza();

        while (auxInterseccion != null) {
            Interseccion interseccion = auxInterseccion.dato;

            System.out.print(interseccion.getId() + " -> ");

            Nodo<Calle> auxCalle = interseccion.getCallesAdyacentes().getCabeza();

            if (auxCalle == null) {
                System.out.print("sin calles salientes");
            }

            while (auxCalle != null) {
                System.out.print(auxCalle.dato.getDestinoId());

                if (auxCalle.siguiente != null) {
                    System.out.print(" -> ");
                }

                auxCalle = auxCalle.siguiente;
            }

            System.out.println();
            auxInterseccion = auxInterseccion.siguiente;
        }
    }

    public void mostrarCallesDetalladas() {
        if (intersecciones.estaVacia()) {
            System.out.println("El grafo vial está vacío");
            return;
        }

        System.out.println("Calles cargadas:");

        Nodo<Interseccion> auxInterseccion = intersecciones.getCabeza();

        while (auxInterseccion != null) {
            Interseccion interseccion = auxInterseccion.dato;
            Nodo<Calle> auxCalle = interseccion.getCallesAdyacentes().getCabeza();

            while (auxCalle != null) {
                System.out.println(auxCalle.dato);
                auxCalle = auxCalle.siguiente;
            }

            auxInterseccion = auxInterseccion.siguiente;
        }
    }

    public String obtenerInterseccionCercana(Direccion direccion) {
        if (direccion == null) {
            return null;
        }

        return obtenerInterseccionCercana(direccion.getCalle(), direccion.getAltura());
    }

    public String obtenerInterseccionCercana(String calle, int altura) {
        if (calle == null) {
            return null;
        }

        Nodo<Interseccion> auxInterseccion = intersecciones.getCabeza();

        while (auxInterseccion != null) {
            Interseccion interseccion = auxInterseccion.dato;

            Nodo<Calle> auxCalle = interseccion.getCallesAdyacentes().getCabeza();

            while (auxCalle != null) {
                Calle tramo = auxCalle.dato;

                if (tramo.contieneAltura(calle, altura)) {
                    return tramo.obtenerInterseccionMasCercana(altura);
                }

                auxCalle = auxCalle.siguiente;
            }

            auxInterseccion = auxInterseccion.siguiente;
        }

        return null;
    }

    public boolean existeRutaPorIntersecciones(String origenId, String destinoId) {
        return calcularRutaBFS(origenId, destinoId, false);
    }

    public boolean mostrarRutaMenosTramosPorIntersecciones(String origenId, String destinoId) {
        return calcularRutaBFS(origenId, destinoId, true);
    }

    public boolean mostrarRutaMasRapidaPorIntersecciones(String origenId, String destinoId) {
        return calcularRutaMenorCosto(origenId, destinoId, "TIEMPO", true);
    }

    public boolean mostrarRutaMasCortaPorIntersecciones(String origenId, String destinoId) {
        return calcularRutaMenorCosto(origenId, destinoId, "DISTANCIA", true);
    }

    public boolean mostrarRutaMenosTramosPorDirecciones(Direccion origen, Direccion destino) {
        String origenId = obtenerInterseccionCercana(origen);
        String destinoId = obtenerInterseccionCercana(destino);

        if (!validarInterseccionesAproximadas(origen, destino, origenId, destinoId)) {
            return false;
        }

        return mostrarRutaMenosTramosPorIntersecciones(origenId, destinoId);
    }

    public boolean mostrarRutaMasRapidaPorDirecciones(Direccion origen, Direccion destino) {
        String origenId = obtenerInterseccionCercana(origen);
        String destinoId = obtenerInterseccionCercana(destino);

        if (!validarInterseccionesAproximadas(origen, destino, origenId, destinoId)) {
            return false;
        }

        return mostrarRutaMasRapidaPorIntersecciones(origenId, destinoId);
    }

    public boolean mostrarRutaMasCortaPorDirecciones(Direccion origen, Direccion destino) {
        String origenId = obtenerInterseccionCercana(origen);
        String destinoId = obtenerInterseccionCercana(destino);

        if (!validarInterseccionesAproximadas(origen, destino, origenId, destinoId)) {
            return false;
        }

        return mostrarRutaMasCortaPorIntersecciones(origenId, destinoId);
    }

    private boolean validarInterseccionesAproximadas(Direccion origen, Direccion destino,
                                                     String origenId, String destinoId) {
        if (origenId == null) {
            System.out.println("No se encontró una intersección cercana para el origen: " + origen);
            return false;
        }

        if (destinoId == null) {
            System.out.println("No se encontró una intersección cercana para el destino: " + destino);
            return false;
        }

        System.out.println("Origen aproximado: " + origen + " -> " + origenId);
        System.out.println("Destino aproximado: " + destino + " -> " + destinoId);

        return true;
    }

    public boolean registrarDemoraEnCalle(String origenId, String destinoId, int demoraExtraMinutos) {
        Calle calle = buscarCalle(origenId, destinoId);

        if (calle == null) {
            return false;
        }

        if (demoraExtraMinutos < 0) {
            return false;
        }

        calle.setDemoraExtraMinutos(demoraExtraMinutos);
        return true;
    }

    public boolean registrarDemoraEnCalleDobleMano(String origenId, String destinoId, int demoraExtraMinutos) {
        boolean ida = registrarDemoraEnCalle(origenId, destinoId, demoraExtraMinutos);
        boolean vuelta = registrarDemoraEnCalle(destinoId, origenId, demoraExtraMinutos);

        return ida || vuelta;
    }

    public boolean bloquearCalle(String origenId, String destinoId) {
        Calle calle = buscarCalle(origenId, destinoId);

        if (calle == null) {
            return false;
        }

        calle.setBloqueada(true);
        return true;
    }

    public boolean bloquearCalleDobleMano(String origenId, String destinoId) {
        boolean ida = bloquearCalle(origenId, destinoId);
        boolean vuelta = bloquearCalle(destinoId, origenId);

        return ida || vuelta;
    }

    public boolean desbloquearCalle(String origenId, String destinoId) {
        Calle calle = buscarCalle(origenId, destinoId);

        if (calle == null) {
            return false;
        }

        calle.setBloqueada(false);
        return true;
    }

    public boolean desbloquearCalleDobleMano(String origenId, String destinoId) {
        boolean ida = desbloquearCalle(origenId, destinoId);
        boolean vuelta = desbloquearCalle(destinoId, origenId);

        return ida || vuelta;
    }

    public boolean registrarVehiculoEnInterseccion(String interseccionId, Vehiculo vehiculo) {
        Interseccion interseccion = buscarInterseccion(interseccionId);

        if (interseccion == null || vehiculo == null) {
            return false;
        }

        interseccion.registrarVehiculo(vehiculo);
        return true;
    }

    public Vehiculo liberarVehiculoEnInterseccion(String interseccionId) {
        Interseccion interseccion = buscarInterseccion(interseccionId);

        if (interseccion == null) {
            return null;
        }

        return interseccion.liberarVehiculo();
    }

    public void mostrarVehiculosEnInterseccion(String interseccionId) {
        Interseccion interseccion = buscarInterseccion(interseccionId);

        if (interseccion == null) {
            System.out.println("No existe la intersección indicada");
            return;
        }

        interseccion.mostrarVehiculos();
    }

    public int obtenerDemoraEnCalle(String origenId, String destinoId) {
        Calle calle = buscarCalle(origenId, destinoId);
        return calle == null ? 0 : calle.getDemoraExtraMinutos();
    }

    private Calle buscarCalle(String origenId, String destinoId) {
        Interseccion origen = buscarInterseccion(origenId);

        if (origen == null || destinoId == null) {
            return null;
        }

        Nodo<Calle> aux = origen.getCallesAdyacentes().getCabeza();

        while (aux != null) {
            if (aux.dato.getDestinoId().equalsIgnoreCase(destinoId)) {
                return aux.dato;
            }

            aux = aux.siguiente;
        }

        return null;
    }

    private boolean calcularRutaBFS(String origenId, String destinoId, boolean mostrar) {
        int cantidad = intersecciones.tamanio();

        if (cantidad == 0) {
            if (mostrar) {
                System.out.println("El grafo está vacío");
            }
            return false;
        }

        String[] ids = cargarIds();

        int indiceOrigen = obtenerIndice(ids, origenId);
        int indiceDestino = obtenerIndice(ids, destinoId);

        if (indiceOrigen == -1 || indiceDestino == -1) {
            if (mostrar) {
                System.out.println("Origen o destino inexistente");
            }
            return false;
        }

        boolean[] visitados = new boolean[cantidad];
        String[] anteriores = new String[cantidad];

        for (int i = 0; i < cantidad; i++) {
            visitados[i] = false;
            anteriores[i] = null;
        }

        ColaFIFO<String> cola = new ColaFIFO<>();

        visitados[indiceOrigen] = true;
        cola.encolar(origenId);

        while (!cola.estaVacia()) {
            String actualId = cola.desencolar();

            if (actualId.equalsIgnoreCase(destinoId)) {
                break;
            }

            Interseccion actual = buscarInterseccion(actualId);

            if (actual != null) {
                Nodo<Calle> auxCalle = actual.getCallesAdyacentes().getCabeza();

                while (auxCalle != null) {
                    Calle calle = auxCalle.dato;

                    if (!calle.isBloqueada()) {
                        int indiceVecino = obtenerIndice(ids, calle.getDestinoId());

                        if (indiceVecino != -1 && !visitados[indiceVecino]) {
                            visitados[indiceVecino] = true;
                            anteriores[indiceVecino] = actualId;
                            cola.encolar(calle.getDestinoId());
                        }
                    }

                    auxCalle = auxCalle.siguiente;
                }
            }
        }

        if (!visitados[indiceDestino]) {
            if (mostrar) {
                System.out.println("No existe ruta disponible entre " + origenId + " y " + destinoId);
            }
            return false;
        }

        if (mostrar) {
            System.out.println("Ruta encontrada por menor cantidad de tramos:");
            imprimirCamino(anteriores, ids, origenId, destinoId);
            System.out.println();

            int tramos = contarTramos(anteriores, ids, origenId, destinoId);
            System.out.println("Cantidad de tramos: " + tramos);
        }

        return true;
    }

    private boolean calcularRutaMenorCosto(String origenId, String destinoId,
                                           String criterio, boolean mostrar) {
        int cantidad = intersecciones.tamanio();

        if (cantidad == 0) {
            if (mostrar) {
                System.out.println("El grafo está vacío");
            }
            return false;
        }

        String[] ids = cargarIds();

        int indiceOrigen = obtenerIndice(ids, origenId);
        int indiceDestino = obtenerIndice(ids, destinoId);

        if (indiceOrigen == -1 || indiceDestino == -1) {
            if (mostrar) {
                System.out.println("Origen o destino inexistente");
            }
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
            int actualIndice = obtenerIndiceMenorCostoNoVisitado(costos, visitados);

            if (actualIndice == -1) {
                break;
            }

            visitados[actualIndice] = true;

            String actualId = ids[actualIndice];
            Interseccion actual = buscarInterseccion(actualId);

            if (actual != null) {
                Nodo<Calle> auxCalle = actual.getCallesAdyacentes().getCabeza();

                while (auxCalle != null) {
                    Calle calle = auxCalle.dato;

                    if (!calle.isBloqueada()) {
                        int indiceVecino = obtenerIndice(ids, calle.getDestinoId());

                        if (indiceVecino != -1 && !visitados[indiceVecino]) {
                            int peso = obtenerPeso(calle, criterio);

                            if (costos[actualIndice] != SIN_RUTA &&
                                    costos[actualIndice] + peso < costos[indiceVecino]) {

                                costos[indiceVecino] = costos[actualIndice] + peso;
                                anteriores[indiceVecino] = actualId;
                            }
                        }
                    }

                    auxCalle = auxCalle.siguiente;
                }
            }
        }

        if (costos[indiceDestino] == SIN_RUTA) {
            if (mostrar) {
                System.out.println("No existe ruta disponible entre " + origenId + " y " + destinoId);
            }
            return false;
        }

        if (mostrar) {
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
                System.out.println("Tiempo estimado total: " + costos[indiceDestino] + " minutos");
            }
        }

        return true;
    }

    private int obtenerPeso(Calle calle, String criterio) {
        if (criterio.equals("DISTANCIA")) {
            return calle.getDistanciaMetros();
        }

        return calle.getTiempoTotalMinutos();
    }

    private String[] cargarIds() {
        int cantidad = intersecciones.tamanio();
        String[] ids = new String[cantidad];

        Nodo<Interseccion> aux = intersecciones.getCabeza();

        int i = 0;

        while (aux != null) {
            ids[i] = aux.dato.getId();
            i++;
            aux = aux.siguiente;
        }

        return ids;
    }

    private int obtenerIndice(String[] ids, String idBuscado) {
        if (idBuscado == null) {
            return -1;
        }

        for (int i = 0; i < ids.length; i++) {
            if (ids[i].equalsIgnoreCase(idBuscado)) {
                return i;
            }
        }

        return -1;
    }

    private int obtenerIndiceMenorCostoNoVisitado(int[] costos, boolean[] visitados) {
        int menorCosto = SIN_RUTA;
        int indiceMenor = -1;

        for (int i = 0; i < costos.length; i++) {
            if (!visitados[i] && costos[i] < menorCosto) {
                menorCosto = costos[i];
                indiceMenor = i;
            }
        }

        return indiceMenor;
    }

    private void imprimirCamino(String[] anteriores, String[] ids,
                                String origenId, String actualId) {

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
        Interseccion interseccion = buscarInterseccion(id);

        if (interseccion == null) {
            System.out.print(id);
        } else {
            System.out.print(interseccion.getId() + " (" + interseccion.getNombre() + ")");
        }
    }

    private int contarTramos(String[] anteriores, String[] ids,
                             String origenId, String destinoId) {

        int tramos = 0;
        String actual = destinoId;

        while (!actual.equalsIgnoreCase(origenId)) {
            int indiceActual = obtenerIndice(ids, actual);

            if (indiceActual == -1 || anteriores[indiceActual] == null) {
                return -1;
            }

            actual = anteriores[indiceActual];
            tramos++;
        }

        return tramos;
    }
}
