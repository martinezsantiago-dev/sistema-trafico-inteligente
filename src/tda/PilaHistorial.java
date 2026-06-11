package tda;

import tda.interfaces.IPilaHistorial;

public class PilaHistorial<T> implements IPilaHistorial<T> {

    private Nodo<T> tope;
    private int tamanio;

    public PilaHistorial() {
        this.tope = null;
        this.tamanio = 0;
    }

    @Override
    public void apilar(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);

        nuevo.siguiente = tope;
        tope = nuevo;

        tamanio++;
    }

    @Override
    public T desapilar() {
        if (estaVacia()) {
            return null;
        }

        T datoEliminado = tope.dato;
        tope = tope.siguiente;

        tamanio--;

        return datoEliminado;
    }

    @Override
    public T tope() {
        if (estaVacia()) {
            return null;
        }

        return tope.dato;
    }

    @Override
    public boolean estaVacia() {
        return tope == null;
    }

    @Override
    public int tamanio() {
        return tamanio;
    }

    @Override
    public void mostrar() {
        if (estaVacia()) {
            System.out.println("La pila está vacía");
            return;
        }

        Nodo<T> aux = tope;

        while (aux != null) {
            System.out.println(aux.dato);
            aux = aux.siguiente;
        }
    }


}