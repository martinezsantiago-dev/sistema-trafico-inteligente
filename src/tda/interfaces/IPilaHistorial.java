package tda.interfaces;

public interface IPilaHistorial<T> {
    void apilar(T dato);
    T desapilar();
    T tope();

    boolean estaLlena();
    boolean estaVacia();

    int tamanio();
    void mostrar();
}