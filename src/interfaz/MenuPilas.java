package interfaz;

import modelo.Cosecha;
import modelo.Fecha;
import modelo.PilaCompost;
import negocio.GestorPilas;
import negocio.ValidacionException;

public class MenuPilas {

    private Consola consola;
    private GestorPilas gestorPilas;

    public MenuPilas(Consola consola, GestorPilas gestorPilas) {
        this.consola = consola;
        this.gestorPilas = gestorPilas;
    }

    public void mostrar() {
        int opcion = -1;
        while (opcion != 0) {
            consola.titulo("MÓDULO 1: PILAS DE COMPOSTAJE");
            System.out.println("1. Registrar pila (RF-01)");
            System.out.println("2. Listar pilas");
            System.out.println("3. Ver detalle de una pila");
            System.out.println("4. Modificar pila");
            System.out.println("5. Eliminar pila");
            System.out.println("6. Actualizar etapa del ciclo (RF-02)");
            System.out.println("7. Registrar cosecha (RF-03)");
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
                        detalle();
                        break;
                    case 4:
                        modificar();
                        break;
                    case 5:
                        eliminar();
                        break;
                    case 6:
                        actualizarEtapa();
                        break;
                    case 7:
                        cosechar();
                        break;
                    default:
                        break;
                }
            } catch (ValidacionException e) {
                consola.mostrarError(e.getMessage());
            }
        }
    }

    private void registrar() throws ValidacionException {
        String codigo = consola.leerTexto("Código de la pila (ej. P-001): ");
        String ubicacion = consola.leerTexto("Ubicación: ");
        double capacidad = consola.leerDecimal("Capacidad máxima en kg (50 a 1000): ");
        Fecha fecha = consola.leerFecha("Fecha de inicio");
        PilaCompost pila = gestorPilas.registrarPila(codigo, ubicacion, capacidad, fecha);
        consola.mostrarExito("Pila " + pila.getCodigo() + " registrada con estado " + pila.getEstado());
    }

    private void listar() {
        consola.titulo("LISTA DE PILAS");
        if (gestorPilas.getTotalPilas() == 0) {
            System.out.println("No hay pilas registradas.");
        }
        for (int i = 0; i < gestorPilas.getTotalPilas(); i++) {
            PilaCompost p = gestorPilas.getPila(i);
            System.out.println(p.getCodigo() + " | " + p.getUbicacion() + " | " + p.getEstado() + " | "
                    + consola.redondear(p.getPesoActualKg()) + " / " + p.getCapacidadMaxKg() + " kg");
        }
    }

    private PilaCompost elegirPila() {
        listar();
        if (gestorPilas.getTotalPilas() == 0) {
            return null;
        }
        String codigo = consola.leerTexto("Código de la pila: ");
        PilaCompost pila = gestorPilas.buscarPila(codigo);
        if (pila == null) {
            consola.mostrarError("No existe una pila con ese código");
        }
        return pila;
    }

    private void detalle() {
        PilaCompost p = elegirPila();
        if (p == null) {
            return;
        }
        consola.titulo("PILA " + p.getCodigo());
        System.out.println("Ubicación: " + p.getUbicacion());
        System.out.println("Fecha de inicio: " + p.getFechaInicio());
        System.out.println("Estado: " + p.getEstado());
        System.out.println("Peso aportado: " + consola.redondear(p.getPesoActualKg()) + " kg de "
                + p.getCapacidadMaxKg() + " kg");
        System.out.println("Mediciones registradas: " + p.getTotalMediciones());
        if (p.getCosecha() != null) {
            System.out.println("Cosecha: " + p.getCosecha().getCantidadKg() + " kg el " + p.getCosecha().getFecha());
        }
        if (p.getTotalHistorial() > 0) {
            System.out.println("Historial de estados:");
            for (int i = 0; i < p.getTotalHistorial(); i++) {
                System.out.println("  " + p.getHistorial(i));
            }
        }
    }

    private void modificar() throws ValidacionException {
        PilaCompost p = elegirPila();
        if (p == null) {
            return;
        }
        String ubicacion = consola.leerTexto("Nueva ubicación: ");
        double capacidad = consola.leerDecimal("Nueva capacidad máxima en kg (50 a 1000): ");
        gestorPilas.modificarPila(p, ubicacion, capacidad);
        consola.mostrarExito("Pila modificada");
    }

    private void eliminar() throws ValidacionException {
        PilaCompost p = elegirPila();
        if (p == null) {
            return;
        }
        String respuesta = consola.leerTexto("¿Seguro que desea eliminar la pila " + p.getCodigo() + "? (s/n): ");
        if (respuesta.equalsIgnoreCase("s")) {
            gestorPilas.eliminarPila(p);
            consola.mostrarExito("Pila eliminada");
        } else {
            System.out.println("No se eliminó la pila.");
        }
    }

    private void actualizarEtapa() throws ValidacionException {
        PilaCompost p = elegirPila();
        if (p == null) {
            return;
        }
        String anterior = p.getEstado();
        String nuevo = gestorPilas.actualizarEtapa(p);
        if (anterior.equals(nuevo)) {
            consola.mostrarExito("La pila sigue en estado: " + nuevo);
        } else {
            consola.mostrarExito("Estado actualizado: " + anterior + " -> " + nuevo);
        }
    }

    private void cosechar() throws ValidacionException {
        PilaCompost p = elegirPila();
        if (p == null) {
            return;
        }
        double kg = consola.leerDecimal("Cantidad cosechada en kg: ");
        Fecha fecha = consola.leerFecha("Fecha de la cosecha");
        Cosecha cosecha = gestorPilas.registrarCosecha(p, kg, fecha);
        consola.mostrarExito("Cosecha de " + cosecha.getCantidadKg() + " kg registrada. La pila quedó como "
                + p.getEstado());
    }
}