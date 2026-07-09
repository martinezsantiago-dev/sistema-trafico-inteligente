package modelo;

public class Camara extends Dispositivo {

    private String tipo;

    public Camara(String id, Direccion ubicacion, String interseccion, String tipo) {
        super(id, ubicacion, interseccion);
        this.tipo = tipo;
    }


    @Override
    public String toString() {
        return "[Cámara " + getId() + "]" + detalle();
    }

    @Override
    public String toString(String nombreInterseccion) {
        return "[Cámara " + getId() + "]" +
                " Intersección: " + getInterseccion() + " (" + nombreInterseccion + ") |" +
                detalle();
    }

    private String detalle() {
        return " Ubicación: " + getUbicacion() +
                " | Tipo: " + tipo +
                " | " + (isActivo() ? "ACTIVA" : "INACTIVA");
    }
}
