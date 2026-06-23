package modelo;

public class Camara extends Dispositivo {

    private String tipo;
   //  private String interseccion;

    public Camara(String id, Direccion ubicacion, String interseccion, String tipo) {
        super(id, ubicacion, interseccion);
        // this.interseccion = interseccion;
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "[Cámara " + getId() + "]" +
                " Tipo: " + tipo +
                " | " + (isActivo() ? "ACTIVA" : "INACTIVA");
    }
}
