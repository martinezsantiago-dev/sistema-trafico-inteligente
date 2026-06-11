package tda.interfaces;

import modelo.Emergencia;

public interface IColaPrioridadEmergencias {

    void insertar(Emergencia emergencia);

    Emergencia atender();

    Emergencia frente();

    boolean estaVacia();

    int tamanio();

    void mostrar();
}
