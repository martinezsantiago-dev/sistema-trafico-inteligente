package modelo;

public class Direccion {
    private String calle;
    private int altura;

    public Direccion(String calle, int altura) {
        this.calle = calle;
        this.altura = altura;
    }

    @Override
    public String toString() {
        return calle + " " + altura;
    }

}
