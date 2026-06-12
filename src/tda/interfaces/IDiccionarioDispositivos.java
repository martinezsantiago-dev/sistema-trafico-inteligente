package tda.interfaces;

import modelo.Dispositivo;
import tda.ListaEnlazada;

public interface IDiccionarioDispositivos {

    boolean insertar(String clave, Dispositivo dispositivo);

    Dispositivo obtener(String clave);

    boolean contieneClave(String clave);

    boolean modificarActivo(String clave, boolean activo);

    boolean eliminar(String clave);

    ListaEnlazada<Dispositivo> buscarPorInterseccion(String interseccion);

    boolean estaVacio();

    int tamanio();

    void mostrar();
}