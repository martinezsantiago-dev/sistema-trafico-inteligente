package modelo;

import tda.ColaFIFO;
import tda.ListaEnlazada;
import tda.Nodo;

public class Interseccion {

    private String id;
    private String nombre;
    private ListaEnlazada<Calle> callesAdyacentes;
    private ColaFIFO<Vehiculo> colaVehiculos;
    private String zona;


    public Interseccion(String id, String nombre, String zona) {
        this.id = id;
        this.nombre = nombre;
        this.callesAdyacentes = new ListaEnlazada<>();
        this.colaVehiculos = new ColaFIFO<>();
        this.zona = zona;
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

    // Chequeo de solo lectura: reconstruye la cola en el mismo orden, sin alterarla.
    // Solo cuenta vehículos NO ocupados (los que están atendiendo otra emergencia no sirven).
    public boolean tieneVehiculoDisponibleDeTipo(String tipo) {
        if (tipo == null) return false;
        ColaFIFO<Vehiculo> temporal = new ColaFIFO<>();
        boolean encontrado = false;
        while (!colaVehiculos.estaVacia()) {
            Vehiculo v = colaVehiculos.desencolar();
            if (!v.isOcupado() && v.getTipo().equalsIgnoreCase(tipo)) encontrado = true;
            temporal.encolar(v);
        }
        colaVehiculos = temporal;
        return encontrado;
    }

    // Extrae el primer vehículo NO ocupado de ese tipo (el que llegó antes), sin alterar el orden de los demás
    public Vehiculo liberarVehiculoDeTipo(String tipo) {
        if (tipo == null) return null;
        ColaFIFO<Vehiculo> temporal = new ColaFIFO<>();
        Vehiculo encontrado = null;
        while (!colaVehiculos.estaVacia()) {
            Vehiculo v = colaVehiculos.desencolar();
            if (encontrado == null && !v.isOcupado() && v.getTipo().equalsIgnoreCase(tipo)) {
                encontrado = v;
            } else {
                temporal.encolar(v);
            }
        }
        colaVehiculos = temporal;
        return encontrado;
    }

    // Marca disponible de nuevo a un vehículo por patente, sin sacarlo de la cola (sigue "parado" ahí)
    public boolean marcarVehiculoDisponible(String patente) {
        if (patente == null) return false;
        boolean encontrado = false;
        Nodo<Vehiculo> aux = colaVehiculos.getFrente();
        while (aux != null) {
            if (aux.dato.getPatente().equalsIgnoreCase(patente)) {
                aux.dato.setOcupado(false);
                encontrado = true;
                break;
            }
            aux = aux.siguiente;
        }
        return encontrado;
    }

    public String getZona() {
        return zona;
    }


    @Override
    public String toString() {
        return "Interseccion " + id + " | Nombre: " + nombre;
    }
}