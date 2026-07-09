package modelo;

public class Direccion {
    private String calle;
    private Integer altura; // null si no hay altura real conocida (ej. intersección sin calles cargadas)

    public Direccion(String calle, Integer altura) {
        this.calle = calle;
        this.altura = altura;
    }

    @Override
    public String toString() {
        return altura != null ? calle + " " + altura : calle;
    }

}
