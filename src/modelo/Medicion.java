package modelo;

public class Medicion {

    private Fecha fecha;
    private double temperatura;
    private double humedad;
    private String olor;

    public Medicion(Fecha fecha, double temperatura, double humedad, String olor) {
        this.fecha = fecha;
        this.temperatura = temperatura;
        this.humedad = humedad;
        this.olor = olor;
    }

    public Fecha getFecha() {
        return fecha;
    }

    public void setFecha(Fecha fecha) {
        this.fecha = fecha;
    }

    public double getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(double temperatura) {
        this.temperatura = temperatura;
    }

    public double getHumedad() {
        return humedad;
    }

    public void setHumedad(double humedad) {
        this.humedad = humedad;
    }

    public String getOlor() {
        return olor;
    }

    public void setOlor(String olor) {
        this.olor = olor;
    }
}