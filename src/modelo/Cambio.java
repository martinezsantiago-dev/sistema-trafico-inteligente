package modelo;

public class Cambio {
    private String entidad;     // "Semaforo", "Camara"
    private String idEntidad;   // "S1", "C3"
    private String atributo;    // "estado", "ubicacion", "activo"
    private String valorAnterior;
    private String valorNuevo;

    public Cambio(String entidad, String idEntidad, String atributo,
                  String valorAnterior, String valorNuevo) {
        this.entidad = entidad;
        this.idEntidad = idEntidad;
        this.atributo = atributo;
        this.valorAnterior = valorAnterior;
        this.valorNuevo = valorNuevo;
    }

    @Override
    public String toString() {
        return entidad + " " + idEntidad +
                " | " + atributo + ": " + valorAnterior + " -> " + valorNuevo;
    }
}