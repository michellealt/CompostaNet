package interfaz;

import modelo.Diagnostico;
import modelo.Fecha;
import modelo.Medicion;
import modelo.PilaCompost;
import negocio.GestorMediciones;
import negocio.GestorPilas;
import negocio.ValidacionException;


public class MenuMediciones {

    private Consola consola;
    private GestorPilas gestorPilas;
    private GestorMediciones gestorMediciones;

    public MenuMediciones(Consola consola, GestorPilas gestorPilas, GestorMediciones gestorMediciones) {
        this.consola = consola;
        this.gestorPilas = gestorPilas;
        this.gestorMediciones = gestorMediciones;
    }

    public void mostrar() {
        int opcion = -1;
        while (opcion != 0) {
            consola.titulo("MÓDULO 2: MONITOREO Y DIAGNÓSTICO");
            System.out.println("1. Registrar medición (RF-04)");
            System.out.println("2. Listar mediciones de una pila");
            System.out.println("3. Modificar medición");
            System.out.println("4. Eliminar medición");
            System.out.println("5. Diagnosticar pila (RF-05)");
            System.out.println("6. Historial de mediciones por fechas (RF-06)");
            System.out.println("0. Volver");
            opcion = consola.leerOpcion(6);
            try {
                switch (opcion) {
                    case 1:
                        registrar();
                        break;
                    case 2:
                        listar(elegirPila());
                        break;
                    case 3:
                        modificar();
                        break;
                    case 4:
                        eliminar();
                        break;
                    case 5:
                        diagnosticar();
                        break;
                    case 6:
                        historial();
                        break;
                    default:
                        break;
                }
            } catch (ValidacionException e) {
                consola.mostrarError(e.getMessage());
            }
        }
    }

    private PilaCompost elegirPila() {
        if (gestorPilas.getTotalPilas() == 0) {
            consola.mostrarError("Primero registre una pila en el Módulo 1");
            return null;
        }
        for (int i = 0; i < gestorPilas.getTotalPilas(); i++) {
            PilaCompost p = gestorPilas.getPila(i);
            System.out.println(p.getCodigo() + " | " + p.getUbicacion() + " | " + p.getEstado());
        }
        PilaCompost pila = gestorPilas.buscarPila(consola.leerTexto("Código de la pila: "));
        if (pila == null) {
            consola.mostrarError("No existe una pila con ese código");
        }
        return pila;
    }

    private String elegirOlor() {
        for (int i = 0; i < gestorMediciones.getTotalOlores(); i++) {
            System.out.println((i + 1) + ". " + gestorMediciones.getOlor(i));
        }
        int numero = consola.leerEntero("Olor observado (número): ");
        if (numero < 1 || numero > gestorMediciones.getTotalOlores()) {
            return "";     
        }
        return gestorMediciones.getOlor(numero - 1);
    }

    private void listar(PilaCompost pila) {
        if (pila == null) {
            return;
        }
        System.out.println("Mediciones de la pila " + pila.getCodigo() + ":");
        if (pila.getTotalMediciones() == 0) {
            System.out.println("  (sin mediciones)");
        }
        for (int i = 0; i < pila.getTotalMediciones(); i++) {
            imprimir(i + 1, pila.getMedicion(i));
        }
    }

    private void imprimir(int numero, Medicion m) {
        System.out.println("  " + numero + ". " + m.getFecha() + " | " + m.getTemperatura() + " °C | "
                + m.getHumedad() + " % | olor " + m.getOlor());
    }

    private void registrar() throws ValidacionException {
        PilaCompost pila = elegirPila();
        if (pila == null) {
            return;
        }
        double temperatura = consola.leerDecimal("Temperatura en °C: ");
        double humedad = consola.leerDecimal("Humedad en %: ");
        String olor = elegirOlor();
        Fecha fecha = consola.leerFecha("Fecha de la medición");
        gestorMediciones.registrarMedicion(pila, temperatura, humedad, olor, fecha);
        consola.mostrarExito("Medición registrada");
    }

    private void modificar() throws ValidacionException {
        PilaCompost pila = elegirPila();
        if (pila == null) {
            return;
        }
        listar(pila);
        int posicion = consola.leerEntero("Número de la medición a modificar: ") - 1;
        double temperatura = consola.leerDecimal("Nueva temperatura en °C: ");
        double humedad = consola.leerDecimal("Nueva humedad en %: ");
        String olor = elegirOlor();
        Fecha fecha = consola.leerFecha("Nueva fecha");
        gestorMediciones.modificarMedicion(pila, posicion, temperatura, humedad, olor, fecha);
        consola.mostrarExito("Medición modificada");
    }

    private void eliminar() throws ValidacionException {
        PilaCompost pila = elegirPila();
        if (pila == null) {
            return;
        }
        listar(pila);
        int posicion = consola.leerEntero("Número de la medición a eliminar: ") - 1;
        gestorMediciones.eliminarMedicion(pila, posicion);
        consola.mostrarExito("Medición eliminada");
    }

    private void diagnosticar() throws ValidacionException {
        PilaCompost pila = elegirPila();
        if (pila == null) {
            return;
        }
        Diagnostico d = gestorMediciones.diagnosticar(pila);
        consola.titulo("DIAGNÓSTICO DE LA PILA " + pila.getCodigo());
        System.out.println("Nivel de alerta: " + d.getNivel());
        for (int i = 0; i < d.getTotalHallazgos(); i++) {
            System.out.println(" - " + d.getHallazgo(i));
        }
        if (d.getTotalAcciones() > 0) {
            System.out.println("Acciones sugeridas:");
            for (int i = 0; i < d.getTotalAcciones(); i++) {
                System.out.println(" * " + d.getAccion(i));
            }
        }
    }

    private void historial() throws ValidacionException {
        PilaCompost pila = elegirPila();
        if (pila == null) {
            return;
        }
        Fecha inicio = consola.leerFecha("Fecha inicial");
        Fecha fin = consola.leerFecha("Fecha final");
        Medicion[] resultado = gestorMediciones.consultarHistorial(pila, inicio, fin);
        System.out.println("Mediciones entre " + inicio + " y " + fin + ":");
        if (resultado.length == 0) {
            System.out.println("  (no hay mediciones en ese rango)");
        }
        for (int i = 0; i < resultado.length; i++) {
            imprimir(i + 1, resultado[i]);
        }
    }
}

