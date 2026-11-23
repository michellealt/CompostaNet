package modelo;

/** Factor de conversión (kg de CO2e por kg de residuo) de un tipo de residuo. */
public class FactorEmision {

    private Residuo residuo;
    private double factor;
    private String fuente;

    public FactorEmision(Residuo residuo, double factor, String fuente) {
        this.residuo = residuo;
        this.factor = factor;
        this.fuente = fuente;
    }

    public Residuo getResiduo() {
        return residuo;
    }

    public double getFactor() {
        return factor;
    }

    public void setFactor(double factor) {
        this.factor = factor;
    }

    public String getFuente() {
        return fuente;
    }

    public void setFuente(String fuente) {
        this.fuente = fuente;
    }
}
