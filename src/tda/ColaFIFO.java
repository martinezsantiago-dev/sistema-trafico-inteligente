package tda;

import tda.interfaces.IColaFIFO;

public class ColaFIFO<T> implements IColaFIFO<T> {

    private Nodo<T> frente;
    private Nodo<T> fin;
    private int tamanio;

    public ColaFIFO() {
        this.frente = null;
        this.fin = null;
        this.tamanio = 0;
    }

    @Override
    public void encolar(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);

        if (estaVacia()) {
            frente = nuevo;
            fin = nuevo;
        } else {
            fin.siguiente = nuevo;
            fin = nuevo;
        }

        tamanio++;
    }

    @Override
    public T desencolar() {
        if (estaVacia()) {
            return null;
        }

        T datoEliminado = frente.dato;
        frente = frente.siguiente;

        if (frente == null) {
            fin = null;
        }

        tamanio--;
        return datoEliminado;
    }

    // Acceso de solo lectura al nodo frente, para recorrer la cola sin desencolar
    public Nodo<T> getFrente() {
        return frente;
    }

    @Override
    public T frente() {
        if (estaVacia()) {
            return null;
        }

        return frente.dato;
    }

    @Override
    public boolean estaVacia() {
        return frente == null;
    }

    @Override
    public int tamanio() {
        return tamanio;
    }

    @Override
    public void mostrar() {
        if (estaVacia()) {
            System.out.println("La cola está vacía");
            return;
        }

        Nodo<T> aux = frente;

        System.out.print("Frente -> ");

        while (aux != null) {
            System.out.print("[" + aux.dato + "]");

            if (aux.siguiente != null) {
                System.out.print(" -> ");
            }

            aux = aux.siguiente;
        }

        System.out.println(" <- Fin");
    }
}
