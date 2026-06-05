package modelo;

public class Emergencia {
    public int id;
    public int gravedad;
    public String ubicacion;
    public long timestamp;

    public Emergencia(String id, int gravedad, String ubicacion) {
        this.id = id;
        this.gravedad = gravedad;
        this.ubicacion = ubicacion;
        this.timestamp = System.currentTimeMillis();
    }
}
