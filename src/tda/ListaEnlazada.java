package tda;
import tda.interfaces.IListaEnlazada;

public class ListaEnlazada<T> implements IListaEnlazada<T>{
    private Nodo<T> cabeza;

    public ListaEnlazada() {
        cabeza = null;
    }

    public void insertarInicio(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);
        nuevo.siguiente = cabeza;
        cabeza = nuevo;
    }

    public void insertarFinal(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);
        if (cabeza == null) {
            cabeza = nuevo;
        } else {
            Nodo<T> aux = cabeza;
            while (aux.siguiente != null) {
                aux = aux.siguiente;
            }
            aux.siguiente = nuevo;
        }
    }

    public boolean buscar(T dato) {
        Nodo<T> aux = cabeza;
        while (aux != null) {
            if (aux.dato.equals(dato)) return true;
            aux = aux.siguiente;
        }
        return false;
    }

    public void eliminar(T dato) {
        if (cabeza == null) return;
        if (cabeza.dato.equals(dato)) {
            cabeza = cabeza.siguiente;
            return;
        }
        Nodo<T> actual = cabeza;
        while (actual.siguiente != null) {
            if (actual.siguiente.dato.equals(dato)) {
                actual.siguiente = actual.siguiente.siguiente;
                return;
            }
            actual = actual.siguiente;
        }
    }

    public Nodo<T> getCabeza() {
        return cabeza;
    }

    public boolean estaVacia() {
        return cabeza == null;
    }
}