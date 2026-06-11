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

    public String getCalle() {
        return calle;
    }

    public int getAltura() {
        return altura;
    }

    public String getCalleDesde() {
        return calleDesde;
    }

    public String getCalleHasta() {
        return calleHasta;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }

    public void setCalleDesde(String calleDesde) {
        this.calleDesde = calleDesde;
    }

    public void setCalleHasta(String calleHasta) {
        this.calleHasta = calleHasta;
    }

    @Override
    public String toString() {
        return calle + " " + altura + " entre " + calleDesde + " y " + calleHasta;
    }

}
