package modelo;


public class Participante extends Persona {

    private String tipo;          
    private int saldoPuntos;
    private Aporte[] aportes = new Aporte[200];
    private int totalAportes;
    private Canje[] canjes = new Canje[50];
    private int totalCanjes;

    public Participante(String cedula, String nombres, String correo, String tipo) {
        super(cedula, nombres, correo);
        this.tipo = tipo;
        this.saldoPuntos = 0;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getSaldoPuntos() {
        return saldoPuntos;
    }

    public void sumarPuntos(int puntos) {
        saldoPuntos = saldoPuntos + puntos;
    }

    public void descontarPuntos(int puntos) {
        saldoPuntos = saldoPuntos - puntos;
    }

    public boolean tienePuntosSuficientes(int costo) {
        return saldoPuntos >= costo;
    }

    

    public boolean hayEspacioParaAporte() {
        return totalAportes < aportes.length;
    }

    public void agregarAporte(Aporte aporte) {
        aportes[totalAportes] = aporte;
        totalAportes++;
    }

    public int getTotalAportes() {
        return totalAportes;
    }

    public Aporte getAporte(int posicion) {
        return aportes[posicion];
    }

    

    public boolean hayEspacioParaCanje() {
        return totalCanjes < canjes.length;
    }

    public void agregarCanje(Canje canje) {
        canjes[totalCanjes] = canje;
        totalCanjes++;
    }

    public int getTotalCanjes() {
        return totalCanjes;
    }

    public Canje getCanje(int posicion) {
        return canjes[posicion];
    }

    @Override
    public String mostrarResumen() {
        return getCedula() + " | " + getNombres() + " | " + tipo + " | " + saldoPuntos + " puntos";
    }
}
