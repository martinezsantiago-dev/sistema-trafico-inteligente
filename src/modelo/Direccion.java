package modelo;

public class Direccion {
    private String calle;
    private int altura;
    private String calleDesde;
    private String calleHasta;

    public Direccion(String calle, int altura, String calleDesde, String calleHasta) {
        this.calle = calle;
        this.altura = altura;
        this.calleDesde = calleDesde;
        this.calleHasta = calleHasta;
    }
}
