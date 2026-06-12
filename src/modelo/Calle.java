package modelo;

    public class Calle {
        // esta clase es clave. va a representar una arista del grafo.
        private String origenId;  // desde que interseccion sale. Ejemplo: I1
        private String destinoId; // a que interseccion llega. Ejemplo: I2 / I1 -> I2
        private String nombre;

        private int alturaOrigen;
        private int alturaDestino;

        private int distanciaMetros; // PESO DE LA CALLE POR SI QUIERO CALCULAR LA RUTA MAS CORTA POR DISTANCIA
        private int tiempoBaseMinutos; // tiempo normal que se tarda en recorrer esa calle sin demoras
        private int demoraExtraMinutos; // para representar retrasos por accidentes
        private boolean bloqueada; // indica si la calle esta bloqueada

        public Calle(String origenId, String destinoId, String nombre,
                     int alturaOrigen, int alturaDestino,
                     int distanciaMetros, int tiempoBaseMinutos) {

            this.origenId = origenId;
            this.destinoId = destinoId;
            this.nombre = nombre;
            this.alturaOrigen = alturaOrigen;
            this.alturaDestino = alturaDestino;
            this.distanciaMetros = distanciaMetros;
            this.tiempoBaseMinutos = tiempoBaseMinutos;
            this.demoraExtraMinutos = 0;
            this.bloqueada = false;
        }

        public String getOrigenId() {
            return origenId;
        }

        public String getDestinoId() {
            return destinoId;
        }

        public String getNombre() {
            return nombre;
        }

        public int getAlturaOrigen() {
            return alturaOrigen;
        }

        public int getAlturaDestino() {
            return alturaDestino;
        }

        public int getDistanciaMetros() {
            return distanciaMetros;
        }

        public int getTiempoBaseMinutos() {
            return tiempoBaseMinutos;
        }

        public int getDemoraExtraMinutos() {
            return demoraExtraMinutos;
        }

        public boolean isBloqueada() {
            return bloqueada;
        }

        public void setDemoraExtraMinutos(int demoraExtraMinutos) {
            this.demoraExtraMinutos = demoraExtraMinutos;
        }

        public void setBloqueada(boolean bloqueada) {
            this.bloqueada = bloqueada;
        }

        public int getTiempoTotalMinutos() {
            return tiempoBaseMinutos + demoraExtraMinutos;
        }

        public boolean contieneAltura(String calleBuscada, int altura) {
            if (calleBuscada == null) {
                return false;
            }

            if (!nombre.equalsIgnoreCase(calleBuscada)) {
                return false;
            }

            int menor;
            int mayor;

            if (alturaOrigen < alturaDestino) {
                menor = alturaOrigen;
                mayor = alturaDestino;
            } else {
                menor = alturaDestino;
                mayor = alturaOrigen;
            }

            return altura >= menor && altura <= mayor;
        }

        public String obtenerInterseccionMasCercana(int altura) {
            int distanciaAlOrigen = Math.abs(altura - alturaOrigen);
            int distanciaAlDestino = Math.abs(altura - alturaDestino);

            if (distanciaAlOrigen <= distanciaAlDestino) {
                return origenId;
            } else {
                return destinoId;
            }
        }

        @Override
        public String toString() {
            return nombre + " | " + origenId + " -> " + destinoId +
                    " | Alturas: " + alturaOrigen + "-" + alturaDestino +
                    " | Distancia: " + distanciaMetros + "m" +
                    " | Tiempo: " + getTiempoTotalMinutos() + " min" +
                    " | Bloqueada: " + bloqueada;
        }
    }