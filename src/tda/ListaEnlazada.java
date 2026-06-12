package tda;
import tda.interfaces.IListaEnlazada;

public class ListaEnlazada<T> implements IListaEnlazada<T>{
    private Nodo<T> cabeza;

    public ListaEnlazada() {
        cabeza = null;
    }

    private boolean sonIguales(T a, T b) { // lo ponemos en private ya que solo lo usara este metodo.
        if (a == null && b == null) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }           // para reemplazar a .equals q no trabaja con posibles valores null.
        return a.equals(b);
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
            if (sonIguales(aux.dato, dato)) return true;
            aux = aux.siguiente;
        }

        return false;
    }

    public void eliminar(T dato) {
        if (cabeza == null) return;

        if (sonIguales(cabeza.dato, dato)) {
            cabeza = cabeza.siguiente;
            return;
        }
        Nodo<T> actual = cabeza;

        while (actual.siguiente != null) {
            if (sonIguales(actual.siguiente.dato, dato)) {
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

    public int tamanio() {
        int contador = 0;
        Nodo<T> aux = cabeza;
        while (aux != null) {
            contador++;
            aux = aux.siguiente;
        }
        return contador;
    }

    public void mostrar() {
        Nodo<T> aux = cabeza;
        while (aux != null) {
            System.out.println(aux.dato);
            aux = aux.siguiente;
        }
    }
}