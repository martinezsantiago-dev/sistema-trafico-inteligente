package modelo;

import tda.ListaEnlazada;

public class NodoTerritorial {

    private String nombre;
    private String tipo; // "Ciudad", "Zona", "Barrio", "Manzana"
    private int cantidadIncidentes;
    private ListaEnlazada<NodoTerritorial> hijos;

    public NodoTerritorial(String nombre, String tipo) {
        this.nombre = nombre;
        this.tipo = tipo; // si es ciudad, zona, barrio o manzana.
        this.cantidadIncidentes = 0;
        this.hijos = new ListaEnlazada<>();
    }

    public void agregarHijo(NodoTerritorial hijo) {
        hijos.insertarFinal(hijo);
    }

    public void incrementarIncidentes() {
        cantidadIncidentes++;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public int getCantidadIncidentes() {
        return cantidadIncidentes;
    }

    public ListaEnlazada<NodoTerritorial> getHijos() {
        return hijos;
    }

    @Override
    public String toString() {
        return tipo + ": " + nombre + " | Incidentes: " + cantidadIncidentes;
    }
}
