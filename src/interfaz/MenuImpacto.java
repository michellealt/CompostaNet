package interfaz;

import modelo.FactorEmision;
import modelo.Fecha;
import modelo.Participante;
import modelo.Residuo;
import modelo.ResumenImpacto;
import negocio.GestorImpacto;
import negocio.GestorParticipantes;
import negocio.ValidacionException;

/** Menú del Módulo 4: impacto individual, reporte comunitario y factores de conversión. */
public class MenuImpacto {

    private Consola consola;
    private GestorParticipantes gestorParticipantes;
    private GestorImpacto gestorImpacto;

    public MenuImpacto(Consola consola, GestorParticipantes gestorParticipantes, GestorImpacto gestorImpacto) {
        this.consola = consola;
        this.gestorParticipantes = gestorParticipantes;
        this.gestorImpacto = gestorImpacto;
    }

    public void mostrar() {
        int opcion = -1;
        while (opcion != 0) {
            consola.titulo("MÓDULO 4: HUELLA ECOLÓGICA E IMPACTO");
            System.out.println("1. Calcular impacto de un participante (RF-10)");
            System.out.println("2. Reporte de impacto comunitario (RF-12)");
            System.out.println("3. Listar factores de conversión");
            System.out.println("4. Configurar factor de un residuo (RF-11)");
            System.out.println("5. Eliminar factor de un residuo");
            System.out.println("0. Volver");
            opcion = consola.leerOpcion(5);
            try {
                switch (opcion) {
                    case 1:
                        impactoParticipante();
                        break;
                    case 2:
                        reporteComunitario();
                        break;
                    case 3:
                        listarFactores();
                        break;
                    case 4:
                        configurarFactor();
                        break;
                    case 5:
                        eliminarFactor();
                        break;
                    default:
                        break;
                }
            } catch (ValidacionException e) {
                consola.mostrarError(e.getMessage());
            }
        }
    }

    private void impactoParticipante() throws ValidacionException {
        Participante p = gestorParticipantes.buscarPorCedula(consola.leerTexto("Cédula del participante: "));
        if (p == null) {
            consola.mostrarError("Participante no encontrado");
            return;
        }
        Fecha inicio = consola.leerFecha("Fecha inicial");
        Fecha fin = consola.leerFecha("Fecha final");
        ResumenImpacto r = gestorImpacto.calcularImpacto(p, inicio, fin);
        consola.titulo("IMPACTO DE " + p.getNombres().toUpperCase());
        System.out.println("Periodo: " + inicio + " a " + fin);
        System.out.println("Residuos desviados: " + consola.redondear(r.getKgDesviados()) + " kg");
        System.out.println("CO2 equivalente evitado: " + consola.redondear(r.getCo2Evitado()) + " kg");
    }

    private void reporteComunitario() throws ValidacionException {
        Fecha inicio = consola.leerFecha("Fecha inicial");
        Fecha fin = consola.leerFecha("Fecha final");
        ResumenImpacto r = gestorImpacto.resumenComunitario(inicio, fin);
        consola.titulo("REPORTE DE IMPACTO COMUNITARIO");
        System.out.println("Periodo: " + inicio + " a " + fin);
        System.out.println("Participantes activos: " + r.getParticipantesActivos());
        System.out.println("Residuos aportados: " + consola.redondear(r.getKgDesviados()) + " kg");
        System.out.println("Compost cosechado: " + consola.redondear(r.getKgCompostCosechado()) + " kg");
        System.out.println("CO2 equivalente evitado: " + consola.redondear(r.getCo2Evitado()) + " kg");
    }

    private void listarFactores() {
        consola.titulo("FACTORES DE CONVERSIÓN (kg CO2e por kg)");
        System.out.println("Los residuos sin factor usan el valor por defecto: 0.5");
        if (gestorImpacto.getTotalFactores() == 0) {
            System.out.println("No hay factores configurados.");
        }
        for (int i = 0; i < gestorImpacto.getTotalFactores(); i++) {
            FactorEmision f = gestorImpacto.getFactorEn(i);
            System.out.println(f.getResiduo().getNombre() + " | " + f.getFactor() + " | fuente: " + f.getFuente());
        }
    }

    private Residuo elegirResiduo() {
        for (int i = 0; i < gestorParticipantes.getTotalResiduos(); i++) {
            System.out.println((i + 1) + ". " + gestorParticipantes.getResiduo(i).getNombre());
        }
        int numero = consola.leerEntero("Tipo de residuo (número): ");
        if (numero < 1 || numero > gestorParticipantes.getTotalResiduos()) {
            consola.mostrarError("Seleccione un tipo de residuo de la lista");
            return null;
        }
        return gestorParticipantes.getResiduo(numero - 1);
    }

    private void configurarFactor() throws ValidacionException {
        Residuo residuo = elegirResiduo();
        if (residuo == null) {
            return;
        }
        double factor = consola.leerDecimal("Factor (kg CO2e por kg): ");
        String fuente = consola.leerTexto("Fuente de referencia: ");
        gestorImpacto.configurarFactor(residuo, factor, fuente);
        consola.mostrarExito("Factor guardado para " + residuo.getNombre());
    }

    private void eliminarFactor() throws ValidacionException {
        Residuo residuo = elegirResiduo();
        if (residuo == null) {
            return;
        }
        gestorImpacto.eliminarFactor(residuo);
        consola.mostrarExito("Factor eliminado. " + residuo.getNombre() + " vuelve al valor por defecto");
    }
}
