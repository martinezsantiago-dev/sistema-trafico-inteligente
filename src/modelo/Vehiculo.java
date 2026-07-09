package modelo;

public class Vehiculo {

    private String patente;
    private String tipo;
    private String destino;
    private String interseccionActual;
    private boolean ocupado; // true mientras está despachado atendiendo una emergencia

    public Vehiculo(String patente, String tipo, String destino) {
        this.patente = patente;
        this.tipo = tipo;
        this.destino = destino;
        this.interseccionActual = null;
        this.ocupado = false;
    }


    public void setInterseccionActual(String interseccionActual) {
        this.interseccionActual = interseccionActual;
    }

    public String getPatente() {
        return patente;
    }

    public String getTipo() {
        return tipo;
    }

    public boolean isOcupado() {
        return ocupado;
    }

    public void setOcupado(boolean ocupado) {
        this.ocupado = ocupado;
    }

    @Override
    public String toString() {
        return "Vehiculo" +
                " | Patente: " + patente +
                " | Tipo: " + tipo +
                " | Destino: " + destino +
                " | Interseccion actual: " + interseccionActual +
                " | " + (ocupado ? "OCUPADO" : "DISPONIBLE");
    }
}