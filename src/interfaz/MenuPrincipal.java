package interfaz;

import modelo.Fecha;
import negocio.GestorImpacto;
import negocio.GestorMediciones;
import negocio.GestorParticipantes;
import negocio.GestorPilas;
import negocio.GestorRecompensas;

/** Menú principal: da acceso a los cinco módulos. */
public class MenuPrincipal {

    private Consola consola;
    private Fecha hoy;
    private GestorPilas gestorPilas;
    private GestorMediciones gestorMediciones;
    private GestorParticipantes gestorParticipantes;
    private GestorImpacto gestorImpacto;
    private GestorRecompensas gestorRecompensas;

    public MenuPrincipal(Consola consola, Fecha hoy, GestorPilas gestorPilas, GestorMediciones gestorMediciones,
                         GestorParticipantes gestorParticipantes, GestorImpacto gestorImpacto,
                         GestorRecompensas gestorRecompensas) {
        this.consola = consola;
        this.hoy = hoy;
        this.gestorPilas = gestorPilas;
        this.gestorMediciones = gestorMediciones;
        this.gestorParticipantes = gestorParticipantes;
        this.gestorImpacto = gestorImpacto;
        this.gestorRecompensas = gestorRecompensas;
    }

    public void mostrar() {
        MenuPilas menuPilas = new MenuPilas(consola, gestorPilas);
        MenuMediciones menuMediciones = new MenuMediciones(consola, gestorPilas, gestorMediciones);
        MenuParticipantes menuParticipantes = new MenuParticipantes(consola, gestorPilas,
                gestorParticipantes, gestorRecompensas);
        MenuImpacto menuImpacto = new MenuImpacto(consola, gestorParticipantes, gestorImpacto);
        MenuRecompensas menuRecompensas = new MenuRecompensas(consola, hoy, gestorParticipantes, gestorRecompensas);
        DatosEjemplo datosEjemplo = new DatosEjemplo(consola, hoy, gestorPilas, gestorMediciones,
                gestorParticipantes, gestorRecompensas);

        int opcion = -1;
        while (opcion != 0) {
            consola.titulo("COMPOSTANET - MENÚ PRINCIPAL (fecha de hoy: " + hoy + ")");
            System.out.println("1. Módulo 1: Pilas de compostaje y ciclo de vida");
            System.out.println("2. Módulo 2: Monitoreo y diagnóstico del compost");
            System.out.println("3. Módulo 3: Participantes y aportes de residuos");
            System.out.println("4. Módulo 4: Huella ecológica e impacto ambiental");
            System.out.println("5. Módulo 5: Incentivos y recompensas");
            System.out.println("6. Cargar datos de ejemplo");
            System.out.println("0. Salir");
            opcion = consola.leerOpcion(6);
            switch (opcion) {
                case 1:
                    menuPilas.mostrar();
                    break;
                case 2:
                    menuMediciones.mostrar();
                    break;
                case 3:
                    menuParticipantes.mostrar();
                    break;
                case 4:
                    menuImpacto.mostrar();
                    break;
                case 5:
                    menuRecompensas.mostrar();
                    break;
                case 6:
                    datosEjemplo.cargar();
                    break;
                default:
                    break;
            }
        }
        System.out.println("Hasta pronto.");
    }
}
