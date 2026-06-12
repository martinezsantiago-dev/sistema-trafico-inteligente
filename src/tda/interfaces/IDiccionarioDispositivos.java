package tda.interfaces;

import modelo.Dispositivo;

public interface IDiccionarioDispositivos {

    boolean insertar(String clave, Dispositivo dispositivo);

    Dispositivo obtener(String clave);

    boolean contieneClave(String clave);

    boolean modificarActivo(String clave, boolean activo);

    void mostrar();
}
