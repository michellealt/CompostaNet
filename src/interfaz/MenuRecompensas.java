package interfaz;

import modelo.Canje;
import modelo.Fecha;
import modelo.Participante;
import modelo.Recompensa;
import negocio.GestorParticipantes;
import negocio.GestorRecompensas;
import negocio.SaldoInsuficienteException;
import negocio.ValidacionException;

public class MenuRecompensas {

    private Consola consola;
    private Fecha hoy;
    private GestorParticipantes gestorParticipantes;
    private GestorRecompensas gestorRecompensas;

    public MenuRecompensas(Consola consola, Fecha hoy, GestorParticipantes gestorParticipantes,
                           GestorRecompensas gestorRecompensas) {
        this.consola = consola;
        this.hoy = hoy;
        this.gestorParticipantes = gestorParticipantes;
        this.gestorRecompensas = gestorRecompensas;
    }

    public void mostrar() {
        int opcion = -1;
        while (opcion != 0) {
            consola.titulo("MÓDULO 5: INCENTIVOS Y RECOMPENSAS");
            System.out.println("1. Registrar recompensa");
            System.out.println("2. Listar recompensas");
            System.out.println("3. Modificar recompensa");
            System.out.println("4. Eliminar recompensa");
            System.out.println("5. Canjear recompensa (RF-14)");
            System.out.println("6. Ranking de participación (RF-15)");
            System.out.println("7. Ver puntos de un participante (RF-13)");
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
                        modificar();
                        break;
                    case 4:
                        eliminar();
                        break;
                    case 5:
                        canjear();
                        break;
                    case 6:
                        ranking();
                        break;
                    case 7:
                        verPuntos();
                        break;
                    default:
                        break;
                }
            } catch (SaldoInsuficienteException e) {
                consola.mostrarError(e.getMessage());
            } catch (ValidacionException e) {
                consola.mostrarError(e.getMessage());
            }
        }
    }

    private void listar() {
        consola.titulo("RECOMPENSAS");
        if (gestorRecompensas.getTotalRecompensas() == 0) {
            System.out.println("No hay recompensas registradas.");
        }
        for (int i = 0; i < gestorRecompensas.getTotalRecompensas(); i++) {
            Recompensa r = gestorRecompensas.getRecompensa(i);
            System.out.println((i + 1) + ". " + r.getNombre() + " | " + r.getCostoPuntos() + " puntos | stock "
                    + r.getStock() + " | " + r.getEstado());
        }
    }

    private Recompensa elegirRecompensa() {
        listar();
        if (gestorRecompensas.getTotalRecompensas() == 0) {
            return null;
        }
        int numero = consola.leerEntero("Número de la recompensa: ");
        if (numero < 1 || numero > gestorRecompensas.getTotalRecompensas()) {
            consola.mostrarError("Seleccione una recompensa de la lista");
            return null;
        }
        return gestorRecompensas.getRecompensa(numero - 1);
    }

    private void registrar() throws ValidacionException {
        String nombre = consola.leerTexto("Nombre de la recompensa: ");
        int costo = consola.leerEntero("Costo en puntos: ");
        int stock = consola.leerEntero("Stock: ");
        gestorRecompensas.registrarRecompensa(nombre, costo, stock);
        consola.mostrarExito("Recompensa registrada");
    }

    private void modificar() throws ValidacionException {
        Recompensa r = elegirRecompensa();
        if (r == null) {
            return;
        }
        String nombre = consola.leerTexto("Nuevo nombre: ");
        int costo = consola.leerEntero("Nuevo costo en puntos: ");
        int stock = consola.leerEntero("Nuevo stock: ");
        gestorRecompensas.modificarRecompensa(r, nombre, costo, stock);
        consola.mostrarExito("Recompensa modificada");
    }

    private void eliminar() {
        Recompensa r = elegirRecompensa();
        if (r == null) {
            return;
        }
        String respuesta = consola.leerTexto("¿Seguro que desea eliminar \"" + r.getNombre() + "\"? (s/n): ");
        if (respuesta.equalsIgnoreCase("s")) {
            gestorRecompensas.eliminarRecompensa(r);
            consola.mostrarExito("Recompensa eliminada");
        } else {
            System.out.println("No se eliminó la recompensa.");
        }
    }

    private void canjear() throws ValidacionException {
        Participante p = gestorParticipantes.buscarPorCedula(consola.leerTexto("Cédula del participante: "));
        if (p == null) {
            consola.mostrarError("Participante no encontrado");
            return;
        }
        System.out.println(p.getNombres() + " tiene " + p.getSaldoPuntos() + " puntos.");
        Recompensa r = elegirRecompensa();
        if (r == null) {
            return;
        }
        Canje canje = gestorRecompensas.canjear(p, r, hoy);
        consola.titulo("COMPROBANTE DE CANJE");
        System.out.println("Fecha: " + canje.getFecha());
        System.out.println("Participante: " + p.getNombres());
        System.out.println("Recompensa: " + r.getNombre());
        System.out.println("Puntos usados: " + canje.getPuntosUsados());
        System.out.println("Saldo actual: " + p.getSaldoPuntos() + " puntos");
        System.out.println("Stock restante: " + r.getStock() + " (" + r.getEstado() + ")");
    }

    private void ranking() throws ValidacionException {
        System.out.println("1. Ordenar por puntos");
        System.out.println("2. Ordenar por kg aportados");
        int criterio = consola.leerOpcion(2);
        if (criterio == 0) {
            return;
        }
        Fecha inicio = consola.leerFecha("Fecha inicial");
        Fecha fin = consola.leerFecha("Fecha final");
        Participante[] ranking = gestorRecompensas.consultarRanking(criterio, inicio, fin);
        consola.titulo("RANKING DE PARTICIPACIÓN");
        for (int i = 0; i < ranking.length; i++) {
            System.out.println((i + 1) + ". " + ranking[i].getNombres() + " | "
                    + gestorRecompensas.puntosEnPeriodo(ranking[i], inicio, fin) + " puntos | "
                    + consola.redondear(gestorRecompensas.kgEnPeriodo(ranking[i], inicio, fin)) + " kg");
        }
    }

    private void verPuntos() {
        Participante p = gestorParticipantes.buscarPorCedula(consola.leerTexto("Cédula del participante: "));
        if (p == null) {
            consola.mostrarError("Participante no encontrado");
            return;
        }
        System.out.println(p.mostrarResumen());
        for (int i = 0; i < p.getTotalAportes(); i++) {
            System.out.println("  " + p.getAporte(i).getFecha() + " | " + p.getAporte(i).getResiduo().getNombre()
                    + " | " + p.getAporte(i).getPesoKg() + " kg | base " + p.getAporte(i).getPuntosBase()
                    + " + bono " + p.getAporte(i).getPuntosBono());
        }
    }
}
