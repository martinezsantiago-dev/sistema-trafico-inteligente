package modelo;

public class Vehiculo {

    private String patente;
    private String tipo;
    private String destino;
    private long timestampLlegada;
    private double velocidad;
    private String interseccionActual;

    public Vehiculo(String patente, String tipo, String destino) {
        this.patente = patente;
        this.tipo = tipo;
        this.destino = destino;
        this.timestampLlegada = System.currentTimeMillis();
        this.velocidad = 0.0;
        this.interseccionActual = null;
    }


    public void setInterseccionActual(String interseccionActual) {
        this.interseccionActual = interseccionActual;
    }

    @Override
    public String toString() {
        return "Vehiculo" +
                " | Patente: " + patente +
                " | Tipo: " + tipo +
                " | Destino: " + destino +
                " | Velocidad: " + velocidad + " km/h" +
                " | Interseccion actual: " + interseccionActual;
    }
}