package modelo;

public class Dispositivo {

    private String id;
    private Direccion ubicacion;
    private boolean activo;
    private String interseccion;

    public Dispositivo(String id, Direccion ubicacion, String interseccion) {
        this.id = id;
        this.ubicacion = ubicacion;
        this.activo = true;
        this.interseccion = interseccion;
    }

    public String getId() {
        return id;
    }

    public String getInterseccion() {
        return interseccion;
    }

    public Direccion getUbicacion() {
        return ubicacion;
    }


    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return "ID: " + id + " | Activo: " + activo;
    }

    // Variante para listados: arma la línea con el nombre de la intersección ya resuelto
    // (Dispositivo no conoce el grafo vial, por eso el nombre se lo pasan armado).
    public String toString(String nombreInterseccion) {
        return toString();
    }
}
