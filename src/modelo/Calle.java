package modelo;

public class Calle {

    private String id;
    private String nombre;
    private String interseccionOrigen;
    private String interseccionDestino;
    private boolean congestionada;

    public Calle(String id, String nombre, String interseccionOrigen, String interseccionDestino) {
        this.id = id;
        this.nombre = nombre;
        this.interseccionOrigen = interseccionOrigen;
        this.interseccionDestino = interseccionDestino;
        this.congestionada = false;
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

    public String getInterseccionOrigen() {
        return interseccionOrigen;
    }

    public void setInterseccionOrigen(String interseccionOrigen) {
        this.interseccionOrigen = interseccionOrigen;
    }

    public String getInterseccionDestino() {
        return interseccionDestino;
    }

    public void setInterseccionDestino(String interseccionDestino) {
        this.interseccionDestino = interseccionDestino;
    }

    public boolean isCongestionada() {
        return congestionada;
    }

    public void setCongestionada(boolean congestionada) {
        this.congestionada = congestionada;
    }

    @Override
    public String toString() {
        return "Calle " + id + " | " + nombre +
                " | De: " + interseccionOrigen + " a: " + interseccionDestino +
                " | Congestionada: " + congestionada;
    }
}
