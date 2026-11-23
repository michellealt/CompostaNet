package interfaz;

import modelo.Fecha;
import negocio.GestorImpacto;
import negocio.GestorMediciones;
import negocio.GestorParticipantes;
import negocio.GestorPilas;
import negocio.GestorRecompensas;
import negocio.ValidacionException;

/** Punto de entrada: pide la fecha de hoy, crea los gestores, carga tres recompensas y abre el menú. */
public class Main {

    public static void main(String[] args) {
        Consola consola = new Consola();
        System.out.println("CompostaNet - Sistema de gestión de compostaje comunitario");
        Fecha hoy = consola.leerFecha("Ingrese la fecha de hoy");

        GestorPilas gestorPilas = new GestorPilas(hoy);
        GestorMediciones gestorMediciones = new GestorMediciones(hoy);
        GestorParticipantes gestorParticipantes = new GestorParticipantes(hoy);
        GestorImpacto gestorImpacto = new GestorImpacto(gestorParticipantes, gestorPilas);
        GestorRecompensas gestorRecompensas = new GestorRecompensas(gestorParticipantes, hoy);

        // Recompensas iniciales (se pueden cambiar desde el módulo 5)
        try {
            gestorRecompensas.registrarRecompensa("Bolsa de compost (1 kg)", 100, 20);
            gestorRecompensas.registrarRecompensa("Planta ornamental", 250, 10);
            gestorRecompensas.registrarRecompensa("Kit de jardinería", 500, 5);
        } catch (ValidacionException e) {
            consola.mostrarError("No se pudieron cargar las recompensas: " + e.getMessage());
        }

        MenuPrincipal menu = new MenuPrincipal(consola, hoy, gestorPilas, gestorMediciones,
                gestorParticipantes, gestorImpacto, gestorRecompensas);
        menu.mostrar();
    }
}
