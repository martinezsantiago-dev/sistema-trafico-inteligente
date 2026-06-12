package tda;

import modelo.Dispositivo;

public class EntradaDiccionario { // seria el nodo para el diccionario dinamico. parecido al nodo implemetnado pero guarda clave y valor
// la clave sera el id: S1, C1, S2, etc (S por Semaforo y C por Camara)
    public String clave;
    public Dispositivo valor;
    public EntradaDiccionario siguiente;

    public EntradaDiccionario(String clave, Dispositivo valor) {
        this.clave = clave;
        this.valor = valor;
        this.siguiente = null;
    }
}