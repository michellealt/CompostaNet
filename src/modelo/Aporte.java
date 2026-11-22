package modelo;


public class Aporte {

    private Fecha fecha;
    private Residuo residuo;
    private double pesoKg;
    private Participante participante;
    private PilaCompost pila;
    private int puntosBase;
    private int puntosBono;

    public Aporte(Fecha fecha, Residuo residuo, double pesoKg, Participante participante, PilaCompost pila) {
        this.fecha = fecha;
        this.residuo = residuo;
        this.pesoKg = pesoKg;
        this.participante = participante;
        this.pila = pila;
    }

    public Fecha getFecha() {
        return fecha;
    }

    public Residuo getResiduo() {
        return residuo;
    }

    public double getPesoKg() {
        return pesoKg;
    }

    public Participante getParticipante() {
        return participante;
    }

    public PilaCompost getPila() {
        return pila;
    }

    public int getPuntosBase() {
        return puntosBase;
    }

    public int getPuntosBono() {
        return puntosBono;
    }

    public void setPuntos(int base, int bono) {
        this.puntosBase = base;
        this.puntosBono = bono;
    }

    public int getPuntosGanados() {
        return puntosBase + puntosBono;
    }
}
