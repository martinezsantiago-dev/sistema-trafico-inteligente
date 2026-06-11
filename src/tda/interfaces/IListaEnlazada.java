package tda.interfaces;
import tda.Nodo;
public interface IListaEnlazada<T> {
    void insertarInicio(T dato);
    void insertarFinal(T dato);
    boolean buscar(T dato);
    void eliminar(T dato);
    boolean estaVacia();
    Nodo<T> getCabeza();
    int tamanio();
    void mostrar();
}
