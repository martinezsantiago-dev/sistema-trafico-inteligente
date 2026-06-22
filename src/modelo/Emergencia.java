package modelo;

public class    Emergencia {

    private String id;
    private int gravedad; // 1 a 5. 5 es la más grave.
    private String interseccionId;
    private long timestamp;
    private String descripcion;
    private String zona;

    // Constructor original que ya tenías — NO lo toques
    public Emergencia(String id, int gravedad, String interseccionId, String descripcion) {
        this.id = id;
        this.gravedad = gravedad;
        this.interseccionId = interseccionId;
        this.descripcion = descripcion;
        this.timestamp = System.currentTimeMillis();
        this.zona = "Sin zona";  // solo agregá esta línea acá
    }

    // Constructor nuevo con zona
    public Emergencia(String id, int gravedad, String interseccionId, String descripcion, String zona) {
        this(id, gravedad, interseccionId, descripcion);
        this.zona = zona;
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

    public void setGravedad(int gravedad) {
        this.gravedad = gravedad;
    }

    public void setInterseccionId(String interseccionId) {
        this.interseccionId = interseccionId;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "Emergencia " + id +
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

