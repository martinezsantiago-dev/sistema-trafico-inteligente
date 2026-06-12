package tda;

import modelo.NodoTerritorial;
import tda.interfaces.IArbolTerritorial;

public class ArbolTerritorial implements IArbolTerritorial {

    private NodoTerritorial raiz;
    private int cantidad;

    public ArbolTerritorial() {
        this.raiz = null;
        this.cantidad = 0;
    }

    @Override
    public boolean estaVacio() {
        return raiz == null;
    }

    @Override
    public void crearCiudad(String nombre) {
        if (raiz == null) {
            raiz = new NodoTerritorial(nombre, "Ciudad");
            cantidad = 1;
        }
    }

    @Override
    public boolean agregarZona(String nombreZona) {
        if (estaVacio() || buscar(nombreZona)) return false;
        raiz.agregarHijo(new NodoTerritorial(nombreZona, "Zona"));
        cantidad++;
        return true;
    }

    @Override
    public boolean agregarBarrio(String nombreZona, String nombreBarrio) {
        NodoTerritorial zona = buscarPorNombre(nombreZona);
        if (zona == null || !zona.getTipo().equals("Zona")) return false;
        if (buscar(nombreBarrio)) return false;
        zona.agregarHijo(new NodoTerritorial(nombreBarrio, "Barrio"));
        cantidad++;
        return true;
    }

    @Override
    public boolean agregarManzana(String nombreBarrio, String nombreManzana) {
        NodoTerritorial barrio = buscarPorNombre(nombreBarrio);
        if (barrio == null || !barrio.getTipo().equals("Barrio")) return false;
        if (buscar(nombreManzana)) return false;
        barrio.agregarHijo(new NodoTerritorial(nombreManzana, "Manzana"));
        cantidad++;
        return true;
    }

    @Override
    public boolean buscar(String nombre) {
        return buscarNodo(raiz, nombre) != null;
    }

    @Override
    public NodoTerritorial buscarPorNombre(String nombre) {
        return buscarNodo(raiz, nombre);
    }

    private NodoTerritorial buscarNodo(NodoTerritorial actual, String nombre) {
        if (actual == null) return null;
        if (actual.getNombre().equals(nombre)) return actual;

        Nodo<NodoTerritorial> aux = actual.getHijos().getCabeza();
        while (aux != null) {
            NodoTerritorial encontrado = buscarNodo(aux.dato, nombre);
            if (encontrado != null) return encontrado;
            aux = aux.siguiente;
        }
        return null;
    }

    @Override
    public void incrementarIncidentes(String nombre) {
        NodoTerritorial nodo = buscarPorNombre(nombre);
        if (nodo != null) nodo.incrementarIncidentes();
    }

    @Override
    public int tamanio() {
        return cantidad;
    }

    @Override
    public int altura() {
        return alturaRecursiva(raiz);
    }

    private int alturaRecursiva(NodoTerritorial actual) {
        if (actual == null) return 0;

        int mayorAlturaHijo = 0;
        Nodo<NodoTerritorial> aux = actual.getHijos().getCabeza();
        while (aux != null) {
            int alturaHijo = alturaRecursiva(aux.dato);
            if (alturaHijo > mayorAlturaHijo) mayorAlturaHijo = alturaHijo;
            aux = aux.siguiente;
        }
        return mayorAlturaHijo + 1;
    }

    @Override
    public void mostrar() {
        if (estaVacio()) {
            System.out.println("El árbol está vacío");
            return;
        }
        mostrarRecursivo(raiz, 0);
    }

    private void mostrarRecursivo(NodoTerritorial actual, int nivel) {
        if (actual == null) return;

        for (int i = 0; i < nivel; i++) System.out.print("   ");
        System.out.println("- " + actual);

        Nodo<NodoTerritorial> aux = actual.getHijos().getCabeza();
        while (aux != null) {
            mostrarRecursivo(aux.dato, nivel + 1);
            aux = aux.siguiente;
        }
    }

    public NodoTerritorial getRaiz() {
        return raiz;
    }
}