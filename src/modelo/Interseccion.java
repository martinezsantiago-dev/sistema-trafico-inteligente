package modelo;

import tda.ListaEnlazada;

public class Interseccion {

    private String id;
    private String nombre;
    private ListaEnlazada<Semaforo> semaforos;
    private ListaEnlazada<Camara> camaras;
    private ListaEnlazada<Vehiculo> vehiculosEsperando;

    public Interseccion(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.semaforos = new ListaEnlazada<>();
        this.camaras = new ListaEnlazada<>();
        this.vehiculosEsperando = new ListaEnlazada<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ListaEnlazada<Semaforo> getSemaforos() {
        return semaforos;
    }

    public void setSemaforos(ListaEnlazada<Semaforo> semaforos) {
        this.semaforos = semaforos;
    }

    public ListaEnlazada<Camara> getCamaras() {
        return camaras;
    }

    public void setCamaras(ListaEnlazada<Camara> camaras) {
        this.camaras = camaras;
    }

    public ListaEnlazada<Vehiculo> getVehiculosEsperando() {
        return vehiculosEsperando;
    }

    public void setVehiculosEsperando(ListaEnlazada<Vehiculo> vehiculosEsperando) {
        this.vehiculosEsperando = vehiculosEsperando;
    }

    @Override
    public String toString() {
        return "Interseccion " + id + " | Nombre: " + nombre;
    }
}
