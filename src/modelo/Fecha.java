package modelo;
public class Fecha {

    private int dia;
    private int mes;
    private int anio;

    public Fecha(int dia, int mes, int anio) {
        this.dia = dia;
        this.mes = mes;
        this.anio = anio;
    }

    public int getDia() {
        return dia;
    }

    public int getMes() {
        return mes;
    }

    public int getAnio() {
        return anio;
    }

    private boolean esBisiesto(int a) {
        return (a % 4 == 0 && a % 100 != 0) || a % 400 == 0;
    }

    private int diasDelMes(int m, int a) {
        int[] dias = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        if (m == 2 && esBisiesto(a)) {
            return 29;
        }
        return dias[m - 1];
    }

    public boolean esValida() {
        if (anio < 2000 || anio > 2100) {
            return false;
        }
        if (mes < 1 || mes > 12) {
            return false;
        }
        return dia >= 1 && dia <= diasDelMes(mes, anio);
    }

    public int numeroDia() {
        int total = 0;
        for (int a = 2000; a < anio; a++) {
            if (esBisiesto(a)) {
                total = total + 366;
            } else {
                total = total + 365;
            }
        }
        for (int m = 1; m < mes; m++) {
            total = total + diasDelMes(m, anio);
        }
        return total + dia;
    }

    public boolean esAnteriorA(Fecha otra) {
        return numeroDia() < otra.numeroDia();
    }

    public boolean esPosteriorA(Fecha otra) {
        return numeroDia() > otra.numeroDia();
    }

    public boolean esIgualA(Fecha otra) {
        return numeroDia() == otra.numeroDia();
    }

    public int diasHasta(Fecha otra) {
        return otra.numeroDia() - numeroDia();
    }

    public Fecha sumarDias(int cantidad) {
        int d = dia;
        int m = mes;
        int a = anio;
        for (int i = 0; i < cantidad; i++) {
            d++;
            if (d > diasDelMes(m, a)) {
                d = 1;
                m++;
                if (m > 12) {
                    m = 1;
                    a++;
                }
            }
        }
        for (int i = 0; i > cantidad; i--) {
            d--;
            if (d < 1) {
                m--;
                if (m < 1) {
                    m = 12;
                    a--;
                }
                d = diasDelMes(m, a);
            }
        }
        return new Fecha(d, m, a);
    }

    public int diaDeLaSemana() {
        return (numeroDia() - 1 + 5) % 7;
    }

    public Fecha lunesDeLaSemana() {
        return sumarDias(-diaDeLaSemana());
    }

    @Override
    public String toString() {
        String d = "" + dia;
        String m = "" + mes;
        if (dia < 10) {
            d = "0" + dia;
        }
        if (mes < 10) {
            m = "0" + mes;
        }
        return d + "/" + m + "/" + anio;
    }
}