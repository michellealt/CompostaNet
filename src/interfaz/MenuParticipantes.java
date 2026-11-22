package interfaz;

import modelo.Aporte;
import modelo.Fecha;
import modelo.Participante;
import modelo.PilaCompost;
import modelo.Residuo;
import negocio.GestorParticipantes;
import negocio.GestorPilas;
import negocio.GestorRecompensas;
import negocio.ValidacionException;

public class MenuParticipantes {

    private Consola consola;
    private GestorPilas gestorPilas;
    private GestorParticipantes gestorParticipantes;
    private GestorRecompensas gestorRecompensas;

    public MenuParticipantes(Consola consola, GestorPilas gestorPilas, GestorParticipantes gestorParticipantes,
                             GestorRecompensas gestorRecompensas) {
        this.consola = consola;
        this.gestorPilas = gestorPilas;
        this.gestorParticipantes = gestorParticipantes;
        this.gestorRecompensas = gestorRecompensas;
    }

    public void mostrar() {
        int opcion = -1;
        while (opcion != 0) {
            consola.titulo("MÓDULO 3: PARTICIPANTES Y APORTES");
            System.out.println("1. Registrar participante (RF-07)");
            System.out.println("2. Listar participantes");
            System.out.println("3. Buscar participante por cédula");
            System.out.println("4. Modificar participante");
            System.out.println("5. Eliminar participante");
            System.out.println("6. Registrar aporte de residuos (RF-08)");
            System.out.println("7. Consultar aportes de un participante (RF-09)");
            System.out.println("0. Volver");
            opcion = consola.leerOpcion(7);
            try {
                switch (opcion) {
                    case 1:
                        registrar();
                        break;
                    case 2:
                        listar();
                        break;
                    case 3:
                        buscar();
                        break;
                    case 4:
                        modificar();
                        break;
                    case 5:
                        eliminar();
                        break;
                    case 6:
                        registrarAporte();
                        break;
                    case 7:
                        consultarAportes();
                        break;
                    default:
                        break;
                }
            } catch (ValidacionException e) {
                consola.mostrarError(e.getMessage());
            }
        }
    }

    private String elegirTipo() {
        System.out.println("1. Residente");
        System.out.println("2. Estudiante");
        System.out.println("3. Funcionario");
        int numero = consola.leerEntero("Tipo de participante (número): ");
        if (numero == 1) {
            return "Residente";
        }
        if (numero == 2) {
            return "Estudiante";
        }
        if (numero == 3) {
            return "Funcionario";
        }
        return "";     
    }

    private void registrar() throws ValidacionException {
        String cedula = consola.leerTexto("Cédula (10 dígitos): ");
        String nombres = consola.leerTexto("Nombres: ");
        String correo = consola.leerTexto("Correo: ");
        String tipo = elegirTipo();
        Participante p = gestorParticipantes.registrarParticipante(cedula, nombres, correo, tipo);
        consola.mostrarExito("Participante registrado con 0 puntos: " + p.getNombres());
    }

    private void listar() {
        consola.titulo("LISTA DE PARTICIPANTES");
        if (gestorParticipantes.getTotalParticipantes() == 0) {
            System.out.println("No hay participantes registrados.");
        }
        for (int i = 0; i < gestorParticipantes.getTotalParticipantes(); i++) {
            System.out.println(gestorParticipantes.getParticipante(i).mostrarResumen());
        }
    }

    private Participante elegirParticipante() {
        listar();
        if (gestorParticipantes.getTotalParticipantes() == 0) {
            return null;
        }
        Participante p = gestorParticipantes.buscarPorCedula(consola.leerTexto("Cédula del participante: "));
        if (p == null) {
            consola.mostrarError("Participante no encontrado");
        }
        return p;
    }

    private void buscar() {
        Participante p = gestorParticipantes.buscarPorCedula(consola.leerTexto("Cédula: "));
        if (p == null) {
            consola.mostrarError("Participante no encontrado");
            return;
        }
        System.out.println(p.mostrarResumen());
        System.out.println("Correo: " + p.getCorreo());
        System.out.println("Aportes: " + p.getTotalAportes() + " | Canjes: " + p.getTotalCanjes());
    }

    private void modificar() throws ValidacionException {
        Participante p = elegirParticipante();
        if (p == null) {
            return;
        }
        String nombres = consola.leerTexto("Nuevos nombres: ");
        String correo = consola.leerTexto("Nuevo correo: ");
        String tipo = elegirTipo();
        gestorParticipantes.modificarParticipante(p, nombres, correo, tipo);
        consola.mostrarExito("Participante modificado");
    }

    private void eliminar() throws ValidacionException {
        Participante p = elegirParticipante();
        if (p == null) {
            return;
        }
        String respuesta = consola.leerTexto("¿Seguro que desea eliminar a " + p.getNombres() + "? (s/n): ");
        if (respuesta.equalsIgnoreCase("s")) {
            gestorParticipantes.eliminarParticipante(p);
            consola.mostrarExito("Participante eliminado");
        } else {
            System.out.println("No se eliminó al participante.");
        }
    }

    private void registrarAporte() throws ValidacionException {
        Participante p = elegirParticipante();
        if (p == null) {
            return;
        }
        if (gestorPilas.getTotalPilas() == 0) {
            consola.mostrarError("Primero registre una pila en el Módulo 1");
            return;
        }
        for (int i = 0; i < gestorPilas.getTotalPilas(); i++) {
            PilaCompost pila = gestorPilas.getPila(i);
            System.out.println(pila.getCodigo() + " | " + pila.getEstado() + " | disponible: "
                    + consola.redondear(pila.capacidadDisponible()) + " kg");
        }
        PilaCompost pila = gestorPilas.buscarPila(consola.leerTexto("Código de la pila destino: "));
        if (pila == null) {
            consola.mostrarError("No existe una pila con ese código");
            return;
        }
        for (int i = 0; i < gestorParticipantes.getTotalResiduos(); i++) {
            Residuo r = gestorParticipantes.getResiduo(i);
            System.out.println((i + 1) + ". " + r.getNombre());
        }
        int numero = consola.leerEntero("Tipo de residuo (número): ");
        if (numero < 1 || numero > gestorParticipantes.getTotalResiduos()) {
            consola.mostrarError("Seleccione un tipo de residuo de la lista");
            return;
        }
        Residuo residuo = gestorParticipantes.getResiduo(numero - 1);
        double peso = consola.leerDecimal("Peso en kg: ");
        Fecha fecha = consola.leerFecha("Fecha del aporte");

        Aporte aporte = gestorParticipantes.registrarAporte(p, pila, residuo, peso, fecha);
        int puntos = gestorRecompensas.otorgarPuntos(p, aporte);     // módulo 5: calcula los puntos
        consola.mostrarExito("Aporte registrado. Puntos ganados: " + puntos + " (base " + aporte.getPuntosBase()
                + " + bonificación " + aporte.getPuntosBono() + "). Saldo actual: " + p.getSaldoPuntos());
    }

    private void consultarAportes() throws ValidacionException {
        Participante p = elegirParticipante();
        if (p == null) {
            return;
        }
        Fecha inicio = consola.leerFecha("Fecha inicial");
        Fecha fin = consola.leerFecha("Fecha final");
        Aporte[] aportes = gestorParticipantes.consultarAportes(p, inicio, fin);
        System.out.println("Aportes de " + p.getNombres() + " entre " + inicio + " y " + fin + ":");
        for (int i = 0; i < aportes.length; i++) {
            Aporte a = aportes[i];
            System.out.println("  " + a.getFecha() + " | " + a.getResiduo().getNombre() + " | " + a.getPesoKg()
                    + " kg | pila " + a.getPila().getCodigo() + " | " + a.getPuntosGanados() + " puntos");
        }
        System.out.println("Total entregado: " + consola.redondear(gestorParticipantes.totalKg(aportes)) + " kg");
    }
}
