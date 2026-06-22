package tda;

import modelo.Emergencia;
import tda.interfaces.IColaPrioridadEmergencias;

public class ColaPrioridadEmergencias implements IColaPrioridadEmergencias {
    // optamos por poner la variable prioridad en el objeto emergencias. De igual manera,
    // se podrá obtener la prioridad con un get.

    private Nodo<Emergencia> cabeza;
    private int tamanio;

    public ColaPrioridadEmergencias() {
        this.cabeza = null;
        this.tamanio = 0;
    }

    @Override
    public void insertar(Emergencia emergencia) {
        Nodo<Emergencia> nuevo = new Nodo<>(emergencia);

        if (cabeza == null || tieneMayorPrioridad(emergencia, cabeza.dato)) {
            nuevo.siguiente = cabeza;
            cabeza = nuevo;
            tamanio++;
            return;
        }

        Nodo<Emergencia> actual = cabeza;

        while (actual.siguiente != null &&
                !tieneMayorPrioridad(emergencia, actual.siguiente.dato)) {
            actual = actual.siguiente;
        }

        nuevo.siguiente = actual.siguiente;
        actual.siguiente = nuevo;
        tamanio++;
    }

    private boolean tieneMayorPrioridad(Emergencia nueva, Emergencia actual) {
        if (nueva.getGravedad() > actual.getGravedad()) {
            return true;
        }

        if (nueva.getGravedad() == actual.getGravedad()
                && nueva.getTimestamp() < actual.getTimestamp()) {  // si tienen la misma gravedad, primero la mas antigua.
            return true;
        }

        return false;
    }

    @Override
    public Emergencia atender() {
        if (estaVacia()) {
            return null;
        }

        Emergencia emergenciaAtendida = cabeza.dato;
        cabeza = cabeza.siguiente;
        tamanio--;

        return emergenciaAtendida;
    }

    @Override
    public Emergencia frente() {
        if (estaVacia()) {
            return null;
        }

        return cabeza.dato;
    }

    public void contarPorZona(ListaEnlazada<String> zonas, ListaEnlazada<int[]> conteos) {
        Nodo<Emergencia> aux = cabeza;
        while (aux != null) {
            String zona = aux.dato.getZona();
            if (zona == null) zona = "Sin zona";

            // Buscar si ya existe
            Nodo<String> nZ = zonas.getCabeza();
            Nodo<int[]> nC = conteos.getCabeza();
            boolean encontrada = false;
            while (nZ != null) {
                if (nZ.dato.equals(zona)) {
                    nC.dato[0]++;
                    encontrada = true;
                    break;
                }
                nZ = nZ.siguiente;
                nC = nC.siguiente;
            }
            if (!encontrada) {
                zonas.insertarFinal(zona);
                conteos.insertarFinal(new int[]{1});
            }
            aux = aux.siguiente;
        }
    }

    @Override
    public boolean estaVacia() {
        return cabeza == null;
    }

    @Override
    public int tamanio() {
        return tamanio;
    }

    @Override
    public void mostrar() {
        if (estaVacia()) {
            System.out.println("No hay emergencias pendientes");
            return;
        }

        Nodo<Emergencia> aux = cabeza;

        while (aux != null) {
            System.out.println(aux.dato);
            aux = aux.siguiente;
        }
    }
}