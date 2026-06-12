package tda; // a revisar

import modelo.Dispositivo;
import tda.interfaces.IDiccionarioDispositivos;

public class DiccionarioDispositivos implements IDiccionarioDispositivos {

    private EntradaDiccionario[] tabla;
    private int capacidad;
    private int cantidad;

    public DiccionarioDispositivos(int capacidad) {
        this.capacidad = capacidad;
        this.tabla = new EntradaDiccionario[capacidad];
        this.cantidad = 0;
    }

    private int hash(String clave) {
        int suma = 0;

        for (int i = 0; i < clave.length(); i++) {
            suma += clave.charAt(i);
        }

        return suma % capacidad;
    }

    @Override
    public boolean insertar(String clave, Dispositivo dispositivo) {
        if (clave == null || dispositivo == null) {
            return false;
        }

        if (contieneClave(clave)) {
            return false;
        }

        int posicion = hash(clave);

        EntradaDiccionario nuevaEntrada = new EntradaDiccionario(clave, dispositivo);

        nuevaEntrada.siguiente = tabla[posicion];
        tabla[posicion] = nuevaEntrada;

        cantidad++;
        return true;
    }

    @Override
    public Dispositivo obtener(String clave) {
        if (clave == null) {
            return null;
        }

        int posicion = hash(clave);

        EntradaDiccionario actual = tabla[posicion];

        while (actual != null) {
            if (actual.clave.equals(clave)) {
                return actual.valor;
            }

            actual = actual.siguiente;
        }

        return null;
    }

    @Override
    public boolean contieneClave(String clave) {
        return obtener(clave) != null;
    }

    @Override
    public boolean modificarActivo(String clave, boolean activo) {
        Dispositivo dispositivo = obtener(clave);

        if (dispositivo == null) {
            return false;
        }

        dispositivo.setActivo(activo);
        return true;
    }

    @Override
    public void mostrar() {
        if (cantidad == 0) {
            System.out.println("El diccionario de dispositivos está vacío");
            return;
        }

        for (int i = 0; i < capacidad; i++) {
            EntradaDiccionario actual = tabla[i];

            if (actual != null) {
                System.out.print("Posicion " + i + ": ");

                while (actual != null) {
                    System.out.print("[" + actual.clave + " -> " + actual.valor + "]");

                    if (actual.siguiente != null) {
                        System.out.print(" -> ");
                    }

                    actual = actual.siguiente;
                }

                System.out.println();
            }
        }
    }
}
