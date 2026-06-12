package modelo;

public class Direccion {
    private String calle;
    private int altura;

    public Direccion(String calle, int altura, String calleDesde, String calleHasta) {
        this.calle = calle;
        this.altura = altura;
    }

    public String getCalle() {
        return calle;
    }

    public int getAltura() {
        return altura;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }

    @Override
    public String toString() {
        return calle + " " + altura;
    }

}
