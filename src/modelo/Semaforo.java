package modelo;

public class Semaforo extends Dispositivo {

    public enum Estado { ROJO, AMARILLO, VERDE }

    private Estado estado;
    private int tiempoVerde;
    private int tiempoAmarillo;
    private int tiempoRojo;
    private String interseccion;

    public Semaforo(String id, Direccion ubicacion, String interseccion,
                    int tiempoVerde, int tiempoAmarillo, int tiempoRojo) {
        super(id, ubicacion);
        this.interseccion = interseccion;
        this.tiempoVerde = tiempoVerde;
        this.tiempoAmarillo = tiempoAmarillo;
        this.tiempoRojo = tiempoRojo;
        this.estado = Estado.ROJO;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public int getTiempoVerde() {
        return tiempoVerde;
    }

    public void setTiempoVerde(int tiempoVerde) {
        this.tiempoVerde = tiempoVerde;
    }

    public int getTiempoAmarillo() {
        return tiempoAmarillo;
    }

    public void setTiempoAmarillo(int tiempoAmarillo) {
        this.tiempoAmarillo = tiempoAmarillo;
    }

    public int getTiempoRojo() {
        return tiempoRojo;
    }

    public void setTiempoRojo(int tiempoRojo) {
        this.tiempoRojo = tiempoRojo;
    }

    public String getInterseccion() {
        return interseccion;
    }

    public void setInterseccion(String interseccion) {
        this.interseccion = interseccion;
    }

    @Override
    public String toString() {
        return "Semaforo " + getId() +
                " | Interseccion: " + interseccion +
                " | Estado: " + estado +
                " | Verde: " + tiempoVerde + "s" +
                " | Amarillo: " + tiempoAmarillo + "s" +
                " | Rojo: " + tiempoRojo + "s" +
                " | Activo: " + isActivo();
    }
}
