package modelo;

public class Camara extends Dispositivo {

    private String tipo;
    private int vehiculosDetectados;
    private double velocidadPromedio;
   //  private String interseccion;

    public Camara(String id, Direccion ubicacion, String interseccion, String tipo) {
        super(id, ubicacion);
        // this.interseccion = interseccion;
        this.tipo = tipo;
        this.vehiculosDetectados = 0;
        this.velocidadPromedio = 0.0;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getVehiculosDetectados() {
        return vehiculosDetectados;
    }

    public void setVehiculosDetectados(int vehiculosDetectados) {
        this.vehiculosDetectados = vehiculosDetectados;
    }

    public double getVelocidadPromedio() {
        return velocidadPromedio;
    }

    public void setVelocidadPromedio(double velocidadPromedio) {
        this.velocidadPromedio = velocidadPromedio;
    }

    /*
    public String getInterseccion() {
        return interseccion;
    }

    public void setInterseccion(String interseccion) {
        this.interseccion = interseccion;
    }

    /*
     */

    @Override
    public String toString() {
        return "Camara " + getId() +
                " | Tipo: " + tipo +
                // " | Interseccion: " + interseccion +
                " | Vehiculos detectados: " + vehiculosDetectados +
                " | Velocidad promedio: " + velocidadPromedio + " km/h" +
                " | Activo: " + isActivo();
    }
}
