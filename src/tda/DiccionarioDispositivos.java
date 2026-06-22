package tda;

import modelo.Dispositivo;
import tda.interfaces.IDiccionarioDispositivos;

public class DiccionarioDispositivos implements IDiccionarioDispositivos {

    private EntradaDiccionario[] tabla;
    private int capacidad;
    private int cantidad;

    public DiccionarioDispositivos(int capacidad) {
        if (capacidad <= 0) {
            capacidad = 31;
        }

        this.capacidad = capacidad;
        this.tabla = new EntradaDiccionario[capacidad];
        this.cantidad = 0;
    }

    private int hash(String clave) {
        int suma = 0;
        String claveNormal = clave.toUpperCase(); // ← así "s3" y "S3" dan el mismo hash
        for (int i = 0; i < claveNormal.length(); i++) {
            suma += claveNormal.charAt(i);
        }
        return suma % capacidad;
    }

    private EntradaDiccionario buscarEntrada(String clave) {
        if (clave == null) return null;
        int posicion = hash(clave.toUpperCase()); // ← normalizar antes del hash
        EntradaDiccionario actual = tabla[posicion];
        while (actual != null) {
            if (actual.clave.equalsIgnoreCase(clave)) { // ← ignorar mayúsculas
                return actual;
            }
            actual = actual.siguiente;
        }
        return null;
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

    public boolean insertar(Dispositivo dispositivo) {
        if (dispositivo == null) {
            return false;
        }

        return insertar(dispositivo.getId(), dispositivo);
    }

    @Override
    public Dispositivo obtener(String clave) {
        EntradaDiccionario entrada = buscarEntrada(clave);

        if (entrada == null) {
            return null;
        }

        return entrada.valor;
    }

    @Override
    public boolean contieneClave(String clave) {
        return buscarEntrada(clave) != null;
    }

    @Override
    public boolean modificarActivo(String clave, boolean activo) {
        EntradaDiccionario entrada = buscarEntrada(clave);

        if (entrada == null) {
            return false;
        }

        entrada.valor.setActivo(activo);
        return true;
    }

    public boolean modificarDispositivo(String clave, Dispositivo nuevoDispositivo) {
        if (nuevoDispositivo == null) {
            return false;
        }

        EntradaDiccionario entrada = buscarEntrada(clave);

        if (entrada == null) {
            return false;
        }

        entrada.valor = nuevoDispositivo;
        return true;
    }

    public boolean eliminar(String clave) {
        if (clave == null) {
            return false;
        }

        int posicion = hash(clave);

        EntradaDiccionario actual = tabla[posicion];
        EntradaDiccionario anterior = null;

        while (actual != null) {
            if (actual.clave.equals(clave)) {

                if (anterior == null) {
                    tabla[posicion] = actual.siguiente;
                } else {
                    anterior.siguiente = actual.siguiente;
                }

                cantidad--;
                return true;
            }

            anterior = actual;
            actual = actual.siguiente;
        }

        return false;
    }

    public ListaEnlazada<Dispositivo> buscarPorInterseccion(String interseccion) {
        ListaEnlazada<Dispositivo> encontrados = new ListaEnlazada<>();

        if (interseccion == null) {
            return encontrados;
        }

        for (int i = 0; i < capacidad; i++) {
            EntradaDiccionario actual = tabla[i];

            while (actual != null) {
                Dispositivo dispositivo = actual.valor;

                if (dispositivo.getInterseccion() != null &&
                        dispositivo.getInterseccion().equalsIgnoreCase(interseccion)) {

                    encontrados.insertarFinal(dispositivo);
                }

                actual = actual.siguiente;
            }
        }

        return encontrados;
    }

    public void listarClaves() {
        if (cantidad == 0) {
            System.out.println("No hay claves cargadas");
            return;
        }

        System.out.println("Claves del diccionario:");

        for (int i = 0; i < capacidad; i++) {
            EntradaDiccionario actual = tabla[i];

            while (actual != null) {
                System.out.println(actual.clave);
                actual = actual.siguiente;
            }
        }
    }

    public void listarValores() {
        if (cantidad == 0) {
            System.out.println("No hay dispositivos cargados");
            return;
        }

        System.out.println("Dispositivos del diccionario:");

        for (int i = 0; i < capacidad; i++) {
            EntradaDiccionario actual = tabla[i];

            while (actual != null) {
                System.out.println(actual.valor);
                actual = actual.siguiente;
            }
        }
    }

    public boolean estaVacio() {
        return cantidad == 0;
    }

    public int tamanio() {
        return cantidad;
    }

    @Override
    public void mostrar() {
        if (cantidad == 0) {
            System.out.println("No hay dispositivos registrados.");
            return;
        }

        System.out.println("Dispositivos registrados (" + cantidad + "):");
        System.out.println("─────────────────────────────────────────");

        for (int i = 0; i < capacidad; i++) {
            EntradaDiccionario actual = tabla[i];
            while (actual != null) {
                System.out.println(actual.valor);
                actual = actual.siguiente;
            }
        }

        System.out.println("─────────────────────────────────────────");
    }


}