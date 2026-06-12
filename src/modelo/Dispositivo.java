package modelo;

public class Dispositivo {

    private String id;
    private Direccion ubicacion;
    private boolean activo;
    private String interseccion;

    public Dispositivo(String id, Direccion ubicacion) {
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


    public void setId(String id) {
        this.id = id;
    }

    public Direccion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Direccion ubicacion) {
        this.ubicacion = ubicacion;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public void setInterseccion() {
        this.interseccion = interseccion;
    }

    @Override
    public String toString() {
        return "ID: " + id + " | Activo: " + activo;
    }
}
