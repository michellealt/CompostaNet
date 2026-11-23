package interfaz;

import modelo.Aporte;
import modelo.Fecha;
import modelo.Participante;
import modelo.PilaCompost;
import negocio.GestorMediciones;
import negocio.GestorParticipantes;
import negocio.GestorPilas;
import negocio.GestorRecompensas;
import negocio.ValidacionException;

/** Carga datos de prueba para poder probar el sistema sin escribir todo a mano. */
public class DatosEjemplo {

    private Consola consola;
    private Fecha hoy;
    private GestorPilas gestorPilas;
    private GestorMediciones gestorMediciones;
    private GestorParticipantes gestorParticipantes;
    private GestorRecompensas gestorRecompensas;

    public DatosEjemplo(Consola consola, Fecha hoy, GestorPilas gestorPilas, GestorMediciones gestorMediciones,
                        GestorParticipantes gestorParticipantes, GestorRecompensas gestorRecompensas) {
        this.consola = consola;
        this.hoy = hoy;
        this.gestorPilas = gestorPilas;
        this.gestorMediciones = gestorMediciones;
        this.gestorParticipantes = gestorParticipantes;
        this.gestorRecompensas = gestorRecompensas;
    }

    public void cargar() {
        if (gestorPilas.buscarPila("P-001") != null) {
            consola.mostrarError("Los datos de ejemplo ya fueron cargados");
            return;
        }
        try {
            // Dos pilas
            PilaCompost p1 = gestorPilas.registrarPila("P-001", "Patio norte", 500, hoy.sumarDias(-70));
            gestorPilas.registrarPila("P-002", "Jardín central", 200, hoy.sumarDias(-10));

            // Cuatro mediciones en la pila P-001
            gestorMediciones.registrarMedicion(p1, 55, 50, "Normal", hoy.sumarDias(-60));
            gestorMediciones.registrarMedicion(p1, 62, 45, "Normal", hoy.sumarDias(-40));
            gestorMediciones.registrarMedicion(p1, 48, 38, "Ácido", hoy.sumarDias(-20));
            gestorMediciones.registrarMedicion(p1, 33, 50, "Normal", hoy.sumarDias(-2));

            // Tres participantes
            Participante ana = gestorParticipantes.registrarParticipante("1710034065", "Ana Torres",
                    "ana@correo.com", "Residente");
            Participante luis = gestorParticipantes.registrarParticipante("1712345675", "Luis Mena",
                    "luis@correo.com", "Estudiante");
            gestorParticipantes.registrarParticipante("1701234567", "Rosa Vega", "rosa@correo.com", "Funcionario");

            // Ana hace 3 aportes el lunes de esta semana (recibe la bonificación del 20 %)
            Fecha lunes = hoy.lunesDeLaSemana();
            aportar(ana, p1, 0, 2, lunes);
            aportar(ana, p1, 4, 3, lunes);
            aportar(ana, p1, 2, 1.5, lunes);
            // Luis hace un aporte
            aportar(luis, p1, 1, 4, hoy.sumarDias(-3));

            consola.mostrarExito("Datos cargados: 2 pilas, 4 mediciones, 3 participantes y 4 aportes");
            System.out.println("Cédulas de prueba: 1710034065 (Ana), 1712345675 (Luis), 1701234567 (Rosa)");
        } catch (ValidacionException e) {
            consola.mostrarError("No se pudieron cargar los datos: " + e.getMessage());
        }
    }

    private void aportar(Participante p, PilaCompost pila, int posicionResiduo, double kg, Fecha fecha)
            throws ValidacionException {
        Aporte aporte = gestorParticipantes.registrarAporte(p, pila, gestorParticipantes.getResiduo(posicionResiduo),
                kg, fecha);
        gestorRecompensas.otorgarPuntos(p, aporte);
    }
}
