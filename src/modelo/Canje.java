package modelo;


public class Canje {

    private Fecha fecha;
    private Participante participante;
    private Recompensa recompensa;
    private int puntosUsados;

    public Canje(Fecha fecha, Participante participante, Recompensa recompensa, int puntosUsados) {
        this.fecha = fecha;
        this.participante = participante;
        this.recompensa = recompensa;
        this.puntosUsados = puntosUsados;
    }

    public Fecha getFecha() {
        return fecha;
    }

    public Participante getParticipante() {
        return participante;
    }

    public Recompensa getRecompensa() {
        return recompensa;
    }

    public int getPuntosUsados() {
        return puntosUsados;
    }
}
