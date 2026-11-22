package negocio;

import modelo.Aporte;
import modelo.Canje;
import modelo.Fecha;
import modelo.Participante;
import modelo.Recompensa;

public class GestorRecompensas {

    private Recompensa[] recompensas = new Recompensa[30];
    private int totalRecompensas;
    private GestorParticipantes gestorParticipantes;
    private Fecha hoy;

    public GestorRecompensas(GestorParticipantes gestorParticipantes, Fecha hoy) {
        this.gestorParticipantes = gestorParticipantes;
        this.hoy = hoy;
    }

    public Recompensa registrarRecompensa(String nombre, int costo, int stock) throws ValidacionException {
        validarDatos(nombre, costo, stock);
        if (totalRecompensas == recompensas.length) {
            throw new ValidacionException("Se alcanzó el máximo de recompensas");
        }
        Recompensa recompensa = new Recompensa(nombre.trim(), costo, stock);
        recompensas[totalRecompensas] = recompensa;
        totalRecompensas++;
        return recompensa;
    }

    public int getTotalRecompensas() {
        return totalRecompensas;
    }

    public Recompensa getRecompensa(int posicion) {
        return recompensas[posicion];
    }

    public void modificarRecompensa(Recompensa recompensa, String nombre, int costo, int stock)
            throws ValidacionException {
        validarDatos(nombre, costo, stock);
        recompensa.setNombre(nombre.trim());
        recompensa.setCostoPuntos(costo);
        recompensa.setStock(stock);
    }

    public void eliminarRecompensa(Recompensa recompensa) {
        int posicion = 0;
        for (int i = 0; i < totalRecompensas; i++) {
            if (recompensas[i] == recompensa) {
                posicion = i;
            }
        }
        for (int i = posicion; i < totalRecompensas - 1; i++) {
            recompensas[i] = recompensas[i + 1];
        }
        recompensas[totalRecompensas - 1] = null;
        totalRecompensas--;
    }

    public int calcularPuntos(Aporte aporte, int aportesSemana) {
        double puntos = aporte.getPesoKg() * aporte.getResiduo().puntosPorKg();
        if (aportesSemana >= 3) {
            puntos = puntos * 1.2;
        }
        return (int) (puntos + 0.000001);  
    }

    public int contarAportesSemana(Participante participante, Fecha fecha) {
        int cantidad = 0;
        for (int i = 0; i < participante.getTotalAportes(); i++) {
            Fecha otra = participante.getAporte(i).getFecha();
            if (otra.lunesDeLaSemana().esIgualA(fecha.lunesDeLaSemana())) {
                cantidad++;
            }
        }
        return cantidad;
    }

    public int otorgarPuntos(Participante participante, Aporte aporte) {
        int aportesSemana = contarAportesSemana(participante, aporte.getFecha());
        int total = calcularPuntos(aporte, aportesSemana);
        int base = calcularPuntos(aporte, 1);
        aporte.setPuntos(base, total - base);
        participante.sumarPuntos(total);
        return total;
    }

    public Canje canjear(Participante participante, Recompensa recompensa, Fecha fecha)
            throws ValidacionException {
        if (!recompensa.hayStock()) {
            throw new ValidacionException("Recompensa agotada");
        }
        if (!participante.tienePuntosSuficientes(recompensa.getCostoPuntos())) {
            int faltan = recompensa.getCostoPuntos() - participante.getSaldoPuntos();
            throw new SaldoInsuficienteException("Puntos insuficientes (faltan " + faltan + " puntos)");
        }
        if (fecha.esPosteriorA(hoy)) {
            throw new ValidacionException("La fecha del canje no puede ser futura");
        }
        if (!participante.hayEspacioParaCanje()) {
            throw new ValidacionException("El participante alcanzó el máximo de canjes");
        }
        participante.descontarPuntos(recompensa.getCostoPuntos());
        recompensa.descontarStock();
        Canje canje = new Canje(fecha, participante, recompensa, recompensa.getCostoPuntos());
        participante.agregarCanje(canje);
        return canje;
    }

    public Participante[] consultarRanking(int criterio, Fecha inicio, Fecha fin) throws ValidacionException {
        if (inicio.esPosteriorA(fin)) {
            throw new ValidacionException("La fecha inicial no puede ser posterior a la final");
        }
        int cantidad = 0;
        for (int i = 0; i < gestorParticipantes.getTotalParticipantes(); i++) {
            if (aportesEnPeriodo(gestorParticipantes.getParticipante(i), inicio, fin) > 0) {
                cantidad++;
            }
        }
        if (cantidad == 0) {
            throw new ValidacionException("No hay participaciones en el periodo");
        }
        Participante[] ranking = new Participante[cantidad];
        int posicion = 0;
        for (int i = 0; i < gestorParticipantes.getTotalParticipantes(); i++) {
            Participante p = gestorParticipantes.getParticipante(i);
            if (aportesEnPeriodo(p, inicio, fin) > 0) {
                ranking[posicion] = p;
                posicion++;
            }
        }

        for (int i = 0; i < ranking.length - 1; i++) {
            for (int j = 0; j < ranking.length - 1 - i; j++) {
                if (debeIrDespues(ranking[j], ranking[j + 1], criterio, inicio, fin)) {
                    Participante auxiliar = ranking[j];
                    ranking[j] = ranking[j + 1];
                    ranking[j + 1] = auxiliar;
                }
            }
        }
        return ranking;
    }

    public int puntosEnPeriodo(Participante participante, Fecha inicio, Fecha fin) {
        int total = 0;
        for (int i = 0; i < participante.getTotalAportes(); i++) {
            Aporte aporte = participante.getAporte(i);
            if (!aporte.getFecha().esAnteriorA(inicio) && !aporte.getFecha().esPosteriorA(fin)) {
                total = total + aporte.getPuntosGanados();
            }
        }
        return total;
    }

    public double kgEnPeriodo(Participante participante, Fecha inicio, Fecha fin) {
        double total = 0;
        for (int i = 0; i < participante.getTotalAportes(); i++) {
            Aporte aporte = participante.getAporte(i);
            if (!aporte.getFecha().esAnteriorA(inicio) && !aporte.getFecha().esPosteriorA(fin)) {
                total = total + aporte.getPesoKg();
            }
        }
        return total;
    }

    private int aportesEnPeriodo(Participante participante, Fecha inicio, Fecha fin) {
        int cantidad = 0;
        for (int i = 0; i < participante.getTotalAportes(); i++) {
            Fecha f = participante.getAporte(i).getFecha();
            if (!f.esAnteriorA(inicio) && !f.esPosteriorA(fin)) {
                cantidad++;
            }
        }
        return cantidad;
    }

    private Fecha fechaUltimoAporte(Participante participante, Fecha inicio, Fecha fin) {
        Fecha ultima = inicio;
        for (int i = 0; i < participante.getTotalAportes(); i++) {
            Fecha f = participante.getAporte(i).getFecha();
            if (!f.esAnteriorA(inicio) && !f.esPosteriorA(fin) && f.esPosteriorA(ultima)) {
                ultima = f;
            }
        }
        return ultima;
    }

    private boolean debeIrDespues(Participante a, Participante b, int criterio, Fecha inicio, Fecha fin) {
        double valorA;
        double valorB;
        if (criterio == 1) {
            valorA = puntosEnPeriodo(a, inicio, fin);
            valorB = puntosEnPeriodo(b, inicio, fin);
        } else {
            valorA = kgEnPeriodo(a, inicio, fin);
            valorB = kgEnPeriodo(b, inicio, fin);
        }
        if (valorA < valorB) {
            return true;
        }
        if (valorA > valorB) {
            return false;
        }

        return fechaUltimoAporte(a, inicio, fin).esAnteriorA(fechaUltimoAporte(b, inicio, fin));
    }

    private void validarDatos(String nombre, int costo, int stock) throws ValidacionException {
        if (nombre.trim().isEmpty()) {
            throw new ValidacionException("El nombre de la recompensa es obligatorio");
        }
        if (costo <= 0) {
            throw new ValidacionException("El costo debe ser mayor que 0 puntos");
        }
        if (stock < 0) {
            throw new ValidacionException("El stock no puede ser negativo");
        }
    }
}
