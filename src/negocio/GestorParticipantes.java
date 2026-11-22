package negocio;

import modelo.Aporte;
import modelo.Fecha;
import modelo.Participante;
import modelo.PilaCompost;
import modelo.Residuo;
import modelo.ResiduoNoCompostable;
import modelo.ResiduoSeco;
import modelo.ResiduoVerde;

public class GestorParticipantes {

    private Participante[] participantes = new Participante[100];
    private int totalParticipantes;
    private Fecha hoy;
    private Validador validador = new Validador();

    private Residuo[] catalogo = new Residuo[11];

    public GestorParticipantes(Fecha hoy) {
        this.hoy = hoy;
        catalogo[0] = new ResiduoVerde("Frutas y verduras");
        catalogo[1] = new ResiduoVerde("Cáscaras");
        catalogo[2] = new ResiduoVerde("Café");
        catalogo[3] = new ResiduoVerde("Cáscara de huevo");
        catalogo[4] = new ResiduoSeco("Hojas secas");
        catalogo[5] = new ResiduoSeco("Restos de jardín");
        catalogo[6] = new ResiduoNoCompostable("Carnes");
        catalogo[7] = new ResiduoNoCompostable("Lácteos");
        catalogo[8] = new ResiduoNoCompostable("Aceites");
        catalogo[9] = new ResiduoNoCompostable("Huesos");
        catalogo[10] = new ResiduoNoCompostable("Excrementos de mascotas");
    }

    public int getTotalResiduos() {
        return catalogo.length;
    }

    public Residuo getResiduo(int posicion) {
        return catalogo[posicion];
    }

    public Participante registrarParticipante(String cedula, String nombres, String correo, String tipo)
            throws ValidacionException {
        if (!validador.cedulaValida(cedula)) {
            throw new ValidacionException("La cédula ingresada no es válida");
        }
        if (buscarPorCedula(cedula) != null) {
            throw new ValidacionException("El participante ya está registrado");
        }
        validarDatos(nombres, correo, tipo);
        if (totalParticipantes == participantes.length) {
            throw new ValidacionException("Se alcanzó el máximo de participantes");
        }
        Participante participante = new Participante(cedula, nombres.trim(), correo.trim(), tipo);
        participantes[totalParticipantes] = participante;
        totalParticipantes++;
        return participante;
    }

    public int getTotalParticipantes() {
        return totalParticipantes;
    }

    public Participante getParticipante(int posicion) {
        return participantes[posicion];
    }

    public Participante buscarPorCedula(String cedula) {
        for (int i = 0; i < totalParticipantes; i++) {
            if (participantes[i].getCedula().equals(cedula.trim())) {
                return participantes[i];
            }
        }
        return null;
    }

    public void modificarParticipante(Participante participante, String nombres, String correo, String tipo)
            throws ValidacionException {
        validarDatos(nombres, correo, tipo);
        participante.setNombres(nombres.trim());
        participante.setCorreo(correo.trim());
        participante.setTipo(tipo);
    }

    public void eliminarParticipante(Participante participante) throws ValidacionException {
        if (participante.getTotalAportes() > 0 || participante.getTotalCanjes() > 0) {
            throw new ValidacionException("No se puede eliminar un participante con aportes o canjes registrados");
        }
        int posicion = 0;
        for (int i = 0; i < totalParticipantes; i++) {
            if (participantes[i] == participante) {
                posicion = i;
            }
        }
        for (int i = posicion; i < totalParticipantes - 1; i++) {
            participantes[i] = participantes[i + 1];
        }
        participantes[totalParticipantes - 1] = null;
        totalParticipantes--;
    }

    public Aporte registrarAporte(Participante participante, PilaCompost pila, Residuo residuo, double pesoKg,
                                  Fecha fecha) throws ValidacionException {
        if (!residuo.esCompostable()) {
            throw new ValidacionException("Este tipo de residuo no es compostable");
        }
        if (pesoKg <= 0) {
            throw new ValidacionException("El peso debe ser mayor que 0");
        }
        if (pesoKg > 50) {
            throw new ValidacionException("El peso por aporte no puede superar 50 kg");
        }
        if (fecha.esPosteriorA(hoy)) {
            throw new ValidacionException("La fecha del aporte no puede ser futura");
        }
        if (!pila.recibeAportes()) {
            throw new ValidacionException("La pila no recibe más aportes");
        }
        if (pesoKg > pila.capacidadDisponible()) {
            throw new ValidacionException("El peso excede la capacidad disponible de la pila ("
                    + redondear(pila.capacidadDisponible()) + " kg)");
        }
        if (!participante.hayEspacioParaAporte()) {
            throw new ValidacionException("El participante alcanzó el máximo de aportes");
        }
        Aporte aporte = new Aporte(fecha, residuo, pesoKg, participante, pila);
        participante.agregarAporte(aporte);
        pila.recibirAporte(pesoKg);
        return aporte;
    }

    public Aporte[] consultarAportes(Participante participante, Fecha inicio, Fecha fin) throws ValidacionException {
        validador.validarRango(inicio, fin);
        int cantidad = 0;
        for (int i = 0; i < participante.getTotalAportes(); i++) {
            Fecha f = participante.getAporte(i).getFecha();
            if (!f.esAnteriorA(inicio) && !f.esPosteriorA(fin)) {
                cantidad++;
            }
        }
        Aporte[] resultado = new Aporte[cantidad];
        int posicion = 0;
        for (int i = 0; i < participante.getTotalAportes(); i++) {
            Fecha f = participante.getAporte(i).getFecha();
            if (!f.esAnteriorA(inicio) && !f.esPosteriorA(fin)) {
                resultado[posicion] = participante.getAporte(i);
                posicion++;
            }
        }
        return resultado;
    }

    public double totalKg(Aporte[] aportes) {
        double total = 0;
        for (int i = 0; i < aportes.length; i++) {
            total = total + aportes[i].getPesoKg();
        }
        return total;
    }

    private void validarDatos(String nombres, String correo, String tipo) throws ValidacionException {
        if (!validador.soloLetras(nombres)) {
            throw new ValidacionException("Los nombres solo pueden contener letras");
        }
        if (!validador.correoValido(correo)) {
            throw new ValidacionException("El correo no tiene un formato válido");
        }
        if (!tipo.equals("Residente") && !tipo.equals("Estudiante") && !tipo.equals("Funcionario")) {
            throw new ValidacionException("Seleccione el tipo de participante");
        }
    }

    private double redondear(double valor) {
        return Math.round(valor * 100) / 100.0;
    }
}
