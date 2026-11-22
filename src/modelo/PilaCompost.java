package modelo;

public class PilaCompost {

    private String codigo;
    private String ubicacion;
    private double capacidadMaxKg;
    private double pesoActualKg;
    private Fecha fechaInicio;
    private String estado;                 
    private boolean faseActivaAlcanzada;
    private Medicion[] mediciones = new Medicion[100];
    private int totalMediciones;
    private String[] historialEstados = new String[50];
    private int totalHistorial;
    private Cosecha cosecha;

    public PilaCompost(String codigo, String ubicacion, double capacidadMaxKg, Fecha fechaInicio) {
        this.codigo = codigo;
        this.ubicacion = ubicacion;
        this.capacidadMaxKg = capacidadMaxKg;
        this.fechaInicio = fechaInicio;
        this.pesoActualKg = 0;
        this.estado = "En formación";
        this.faseActivaAlcanzada = false;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public double getCapacidadMaxKg() {
        return capacidadMaxKg;
    }

    public void setCapacidadMaxKg(double capacidadMaxKg) {
        this.capacidadMaxKg = capacidadMaxKg;
    }

    public double getPesoActualKg() {
        return pesoActualKg;
    }

    public Fecha getFechaInicio() {
        return fechaInicio;
    }

    public String getEstado() {
        return estado;
    }

    public boolean isFaseActivaAlcanzada() {
        return faseActivaAlcanzada;
    }

    public Cosecha getCosecha() {
        return cosecha;
    }

    public void setCosecha(Cosecha cosecha) {
        this.cosecha = cosecha;
    }


    public double capacidadDisponible() {
        return capacidadMaxKg - pesoActualKg;
    }

    public void recibirAporte(double kg) {
        pesoActualKg = pesoActualKg + kg;
    }

    public boolean recibeAportes() {
        return estado.equals("En formación") || estado.equals("Fase activa");
    }


    public void cambiarEstado(String nuevoEstado, Fecha fecha) {
        if (totalHistorial < historialEstados.length) {
            historialEstados[totalHistorial] = fecha + ": " + estado + " -> " + nuevoEstado;
            totalHistorial++;
        }
        if (nuevoEstado.equals("Fase activa")) {
            faseActivaAlcanzada = true;
        }
        estado = nuevoEstado;
    }

    public int getTotalHistorial() {
        return totalHistorial;
    }

    public String getHistorial(int posicion) {
        return historialEstados[posicion];
    }

    public int diasDesdeInicio(Fecha hoy) {
        return fechaInicio.diasHasta(hoy);
    }


    public boolean hayEspacioParaMedicion() {
        return totalMediciones < mediciones.length;
    }

    public void agregarMedicion(Medicion medicion) {
        mediciones[totalMediciones] = medicion;
        totalMediciones++;
    }

    public void eliminarMedicion(int posicion) {
        for (int i = posicion; i < totalMediciones - 1; i++) {
            mediciones[i] = mediciones[i + 1];
        }
        mediciones[totalMediciones - 1] = null;
        totalMediciones--;
    }

    public int getTotalMediciones() {
        return totalMediciones;
    }

    public Medicion getMedicion(int posicion) {
        return mediciones[posicion];
    }

    public Medicion ultimaMedicion() {
        Medicion ultima = null;
        for (int i = 0; i < totalMediciones; i++) {
            if (ultima == null || !mediciones[i].getFecha().esAnteriorA(ultima.getFecha())) {
                ultima = mediciones[i];
            }
        }
        return ultima;
    }
}