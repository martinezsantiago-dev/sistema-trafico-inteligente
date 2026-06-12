package modelo;

import tda.ColaFIFO;
import tda.ListaEnlazada;

public class Interseccion {

    private String id;
    private String nombre;
    private ListaEnlazada<Calle> callesAdyacentes;
    private ColaFIFO<Vehiculo> colaVehiculos;

    public Interseccion(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.callesAdyacentes = new ListaEnlazada<>();
        this.colaVehiculos = new ColaFIFO<>();
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public ListaEnlazada<Calle> getCallesAdyacentes() {
        return callesAdyacentes;
    }

    public void agregarCalle(Calle calle) {
        callesAdyacentes.insertarFinal(calle);
    }

    public void registrarVehiculo(Vehiculo vehiculo) {
        colaVehiculos.encolar(vehiculo);
    }

    public Vehiculo liberarVehiculo() {
        return colaVehiculos.desencolar();
    }

    public void mostrarVehiculos() {
        colaVehiculos.mostrar();
    }

    @Override
    public String toString() {
        return "Interseccion " + id + " | Nombre: " + nombre;
    }
}