package tda.interfaces;

public interface IPilaHistorial<T> {
    void apilar(T dato);
    T desapilar();
    T tope();

    boolean estaVacia();

    int tamanio();
}