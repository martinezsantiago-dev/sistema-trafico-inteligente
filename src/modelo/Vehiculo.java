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

    public String getPatente() {
        return patente;
    }

    public void setPatente(String patente) {
        this.patente = patente;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public long getTimestampLlegada() {
        return timestampLlegada;
    }

    public void setTimestampLlegada(long timestampLlegada) {
        this.timestampLlegada = timestampLlegada;
    }

    public double getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(double velocidad) {
        this.velocidad = velocidad;
    }

    public String getInterseccionActual() {
        return interseccionActual;
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