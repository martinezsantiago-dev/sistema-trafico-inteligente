package modelo;

public class Cambio {
    private String entidad;
    private String idEntidad;
    private String atributo;
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

    public String getEntidad() {
        return entidad;
    }

    public String getIdEntidad() {
        return idEntidad;
    }

    public String getAtributo() {
        return atributo;
    }

    public String getValorAnterior() {
        return valorAnterior;
    }

    public String getValorNuevo() {
        return valorNuevo;
    }

    @Override
    public String toString() {
        return entidad + " " + idEntidad +
                " | " + atributo + ": " + valorAnterior + " -> " + valorNuevo;
    }
    // qué entidad era       -> Semaforo / Camara
    // qué código tenía      -> S1 / C3
    // qué atributo cambió   -> estado
    // valor anterior        -> ACTIVO
    // valor nuevo           -> FUERA_DE_SERVICIO

}