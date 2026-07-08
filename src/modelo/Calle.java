package modelo;

import java.text.Normalizer;

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
        private long timestampDemora; // momento en que se registró la demora actual, para saber qué tan vieja es
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

        public long getTimestampDemora() {
            return timestampDemora;
        }

        public boolean isBloqueada() {
            return bloqueada;
        }

        public void setDemoraExtraMinutos(int demoraExtraMinutos) {
            this.demoraExtraMinutos = demoraExtraMinutos;
            this.timestampDemora = demoraExtraMinutos > 0 ? System.currentTimeMillis() : 0;
        }

        public void setBloqueada(boolean bloqueada) {
            this.bloqueada = bloqueada;
        }

        public int getTiempoTotalMinutos() {
            return tiempoBaseMinutos + demoraExtraMinutos;
        }

        public static String normalizar(String texto) {
            if (texto == null) return "";
            String sinAcentos = Normalizer.normalize(texto, Normalizer.Form.NFD) // Descompone cada letra con tilde en dos caracteres separados. Ejemplo San Martín
                    .replaceAll("\\p{InCombiningDiacriticalMarks}", "");  // Busca esos acentos que quedaron sueltos del paso anterior, y los borra. Ahora queda solo la i.
            return sinAcentos.toUpperCase().trim();
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