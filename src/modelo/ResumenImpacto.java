package modelo;

/** Resultado del cálculo de impacto de un periodo. */
public class ResumenImpacto {

    private Fecha fechaInicio;
    private Fecha fechaFin;
    private int participantesActivos;
    private double kgDesviados;
    private double kgCompostCosechado;
    private double co2Evitado;

    public ResumenImpacto(Fecha fechaInicio, Fecha fechaFin, int participantesActivos,
                          double kgDesviados, double kgCompostCosechado, double co2Evitado) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.participantesActivos = participantesActivos;
        this.kgDesviados = kgDesviados;
        this.kgCompostCosechado = kgCompostCosechado;
        this.co2Evitado = co2Evitado;
    }

    public Fecha getFechaInicio() {
        return fechaInicio;
    }

    public Fecha getFechaFin() {
        return fechaFin;
    }

    public int getParticipantesActivos() {
        return participantesActivos;
    }

    public double getKgDesviados() {
        return kgDesviados;
    }

    public double getKgCompostCosechado() {
        return kgCompostCosechado;
    }

    public double getCo2Evitado() {
        return co2Evitado;
    }
}
