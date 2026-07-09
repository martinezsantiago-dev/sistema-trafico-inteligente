package modelo;

public class Emergencia {

    private String id;
    private int gravedad; // 1 a 5. 5 es la más grave.
    private String interseccionId;
    private long timestamp;
    private String descripcion;
    private String zona;
    private String tipoIncidente; // CHOQUE, INCENDIO, SEMAFORO_ROTO, CAMARA_ROTA, CALLE_BLOQUEADA, OTRO
    private boolean hayHeridos; // solo relevante si tipoIncidente es CHOQUE
    private String tipoVehiculoRequerido; // solo se usa cuando tipoIncidente es OTRO; null si no requiere despacho
    private String tipoVehiculoDespachado; // se completa cuando el despacho encuentra un vehículo disponible
    private String patenteVehiculoDespachado;

    public Emergencia(String id, int gravedad, String interseccionId, String descripcion, String zona,
                       String tipoIncidente, boolean hayHeridos, String tipoVehiculoRequerido) {
        this.id = id;
        this.gravedad = gravedad;
        this.interseccionId = interseccionId;
        this.descripcion = descripcion;
        this.timestamp = System.currentTimeMillis();
        this.zona = zona;
        this.tipoIncidente = tipoIncidente;
        this.hayHeridos = hayHeridos;
        this.tipoVehiculoRequerido = tipoVehiculoRequerido;
    }


    public String getId() {
        return id;
    }

    public int getGravedad() {
        return gravedad;
    }

    public String getZona() {
        return zona;
    }

    public String getInterseccionId() {
        return interseccionId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getTipoIncidente() {
        return tipoIncidente;
    }

    public boolean isHayHeridos() {
        return hayHeridos;
    }

    public String getTipoVehiculoRequerido() {
        return tipoVehiculoRequerido;
    }

    public void registrarVehiculoDespachado(String tipoVehiculo, String patente) {
        this.tipoVehiculoDespachado = tipoVehiculo;
        this.patenteVehiculoDespachado = patente;
    }

    public String getTipoVehiculoDespachado() {
        return tipoVehiculoDespachado;
    }

    public String getPatenteVehiculoDespachado() {
        return patenteVehiculoDespachado;
    }

    @Override
    public String toString() {
        return "Emergencia " + id +
                " | Tipo: " + (tipoIncidente != null ? tipoIncidente : "sin especificar") +
                (tipoIncidente != null && tipoIncidente.equals("CHOQUE") ? " | Heridos: " + (hayHeridos ? "sí" : "no") : "") +
                " | Gravedad: " + gravedad +
                " | Zona: " + (zona != null ? zona : "sin zona") +
                " | Interseccion: " + interseccionId +
                " | Descripcion: " + descripcion;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (obj == null) return false;

        if (obj.getClass() != Emergencia.class) return false; //   verifica que el objeto pasado como parametro sea una Emergencia

        Emergencia otra = (Emergencia) obj;

        if (id == null && otra.id == null) return true;  // dos objetos de emergencia son iguales si tienen el mismo id
        if (id == null || otra.id == null) return false;
                                                        // es por seguridad por si alguna id llega a ser null, pero se podria sacar este metodo.
        return id.equals(otra.id);
    }
}

