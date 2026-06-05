package tda.interfaces;

public interface IListaEnlazada<T> {
    void insertarInicio(T dato);
    void insertarFinal(T dato);
    boolean buscar(T dato);
    void eliminar(T dato);
    boolean estaVacia();
}
