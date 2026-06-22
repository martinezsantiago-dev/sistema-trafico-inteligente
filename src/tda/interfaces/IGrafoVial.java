package tda.interfaces;

import modelo.Interseccion;
import modelo.Vehiculo;

public interface IGrafoVial {
    boolean agregarInterseccion(Interseccion interseccion);
    boolean agregarCalle(String origenId, String destinoId, String nombreCalle,
                         int alturaOrigen, int alturaDestino,
                         int distanciaMetros, int tiempoMinutos, boolean dobleMano);
    boolean eliminarInterseccion(String id);
    boolean eliminarCalle(String origenId, String destinoId);
    Interseccion buscarInterseccion(String id);
    String obtenerInterseccionCercana(String calle, int altura);
    boolean mostrarRutaMenosTramos(String origenId, String destinoId);
    boolean mostrarRutaMasRapida(String origenId, String destinoId);
    boolean mostrarRutaMasCorta(String origenId, String destinoId);
    boolean bloquearCalle(String origenId, String destinoId);
    boolean desbloquearCalle(String origenId, String destinoId);
    boolean registrarDemoraEnCalle(String origenId, String destinoId, int minutos);
    boolean registrarVehiculoEnInterseccion(String interseccionId, Vehiculo vehiculo);
    Vehiculo liberarVehiculoEnInterseccion(String interseccionId);
    void mostrarIntersecciones();
    void mostrarGrafo();
    void mostrarCallesDisponibles();
    boolean estaVacio();
    int cantidadIntersecciones();
}
