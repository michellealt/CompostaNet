package modelo;

/** Resultado del diagnóstico de una pila: nivel de alerta, hallazgos y acciones sugeridas. */
public class Diagnostico {

    private String nivel;             // Óptima, Precaución o Crítica
    private String[] hallazgos = new String[10];
    private int totalHallazgos;
    private String[] acciones = new String[10];
    private int totalAcciones;

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public void agregarHallazgo(String hallazgo) {
        hallazgos[totalHallazgos] = hallazgo;
        totalHallazgos++;
    }

    public int getTotalHallazgos() {
        return totalHallazgos;
    }

    public String getHallazgo(int posicion) {
        return hallazgos[posicion];
    }

    public void agregarAccion(String accion) {
        acciones[totalAcciones] = accion;
        totalAcciones++;
    }

    public int getTotalAcciones() {
        return totalAcciones;
    }

    public String getAccion(int posicion) {
        return acciones[posicion];
    }
}
