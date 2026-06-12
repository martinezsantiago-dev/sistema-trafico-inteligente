package tda.interfaces;

import modelo.NodoTerritorial;

public interface IArbolTerritorial {
    boolean estaVacio();
    void crearCiudad(String nombre);
    boolean agregarZona(String nombreZona);
    boolean agregarBarrio(String nombreZona, String nombreBarrio);
    boolean agregarManzana(String nombreBarrio, String nombreManzana);
    boolean buscar(String nombre);
    NodoTerritorial buscarPorNombre(String nombre);
    void incrementarIncidentes(String nombre);
    int tamanio();
    int altura();
    void mostrar();
}