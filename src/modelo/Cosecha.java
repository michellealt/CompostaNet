package modelo;

public class Cosecha {

    private Fecha fecha;
    private double cantidadKg;

    public Cosecha(Fecha fecha, double cantidadKg) {
        this.fecha = fecha;
        this.cantidadKg = cantidadKg;
    }

    public Fecha getFecha() {
        return fecha;
    }

    public double getCantidadKg() {
        return cantidadKg;
    }
}