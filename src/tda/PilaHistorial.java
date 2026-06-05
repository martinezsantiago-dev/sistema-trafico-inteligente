package tda;

import tda.interfaces.IPilaHistorial;

public class PilaHistorial<T> implements IPilaHistorial<T> {

    private int max;
    private int tope;
    private T[] datos;

    @SuppressWarnings("unchecked")
    public PilaHistorial(int max) {
        this.max = max;
        this.tope = -1;
        this.datos = (T[]) new Object[max];
    }

    @Override
    public void apilar(T dato) {
        if (!estaLlena()) {
            datos[++tope] = dato;
        } else {
            System.out.println("La pila está llena");
        }
    }

    @Override
    public T desapilar() {
        if (!estaVacia()) {
            T eliminado = datos[tope];
            datos[tope--] = null;
            return eliminado;
        }
        return null;
    }

    @Override
    public T tope() {
        if (!estaVacia()) {
            return datos[tope];
        }
        return null;
    }

    @Override
    public boolean estaLlena() {
        return tope == max - 1;
    }

    @Override
    public boolean estaVacia() {
        return tope == -1;
    }

    @Override
    public int tamanio() {
        return tope + 1;
    }

    @Override
    public void mostrar() {
        for (int i = tope; i >= 0; i--) {
            System.out.println(datos[i]);
        }
    }
}