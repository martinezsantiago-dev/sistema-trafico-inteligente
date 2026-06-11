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

    public ListaEnlazada<NodoTerritorial> getHijos() { // esto permite que cada nodo pueda tener muchos hijos
        return hijos;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return tipo + ": " + nombre + " | Incidentes: " + cantidadIncidentes;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof NodoTerritorial)) return false;

        NodoTerritorial otro = (NodoTerritorial) obj;

        if (nombre == null && otro.nombre == null) return true;
        if (nombre == null || otro.nombre == null) return false;

        return nombre.equals(otro.nombre);
    }
}
