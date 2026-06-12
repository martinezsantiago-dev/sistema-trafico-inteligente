package tda.interfaces;

public interface IColaFIFO<T> {

    void encolar(T dato);

    T desencolar();

    T frente();

    boolean estaVacia();

    int tamanio();

    void mostrar();
}