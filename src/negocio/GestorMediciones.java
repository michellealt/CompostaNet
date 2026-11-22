package negocio;

import modelo.Diagnostico;
import modelo.Fecha;
import modelo.Medicion;
import modelo.PilaCompost;


public class GestorMediciones {

    private Fecha hoy;
    private Validador validador = new Validador();
    private String[] olores = {"Normal", "Ácido", "Amoniaco", "Putrefacto"};

    public GestorMediciones(Fecha hoy) {
        this.hoy = hoy;
    }

    public int getTotalOlores() {
        return olores.length;
    }

    public String getOlor(int posicion) {
        return olores[posicion];
    }

   

    public Medicion registrarMedicion(PilaCompost pila, double temperatura, double humedad, String olor,
                                      Fecha fecha) throws ValidacionException {
        validarDatos(pila, temperatura, humedad, olor, fecha);
        if (!pila.hayEspacioParaMedicion()) {
            throw new ValidacionException("La pila alcanzó el máximo de mediciones");
        }
        Medicion medicion = new Medicion(fecha, temperatura, humedad, olor);
        pila.agregarMedicion(medicion);
        return medicion;
    }

   

    public void modificarMedicion(PilaCompost pila, int posicion, double temperatura, double humedad,
                                  String olor, Fecha fecha) throws ValidacionException {
        validarPosicion(pila, posicion);
        validarDatos(pila, temperatura, humedad, olor, fecha);
        Medicion medicion = pila.getMedicion(posicion);
        medicion.setTemperatura(temperatura);
        medicion.setHumedad(humedad);
        medicion.setOlor(olor);
        medicion.setFecha(fecha);
    }

    

    public void eliminarMedicion(PilaCompost pila, int posicion) throws ValidacionException {
        validarPosicion(pila, posicion);
        pila.eliminarMedicion(posicion);
    }

   

    public Diagnostico diagnosticar(PilaCompost pila) throws ValidacionException {
        Medicion ultima = pila.ultimaMedicion();
        if (ultima == null) {
            throw new ValidacionException("No hay mediciones para diagnosticar");
        }
        double temperatura = ultima.getTemperatura();
        double humedad = ultima.getHumedad();
        Diagnostico diagnostico = new Diagnostico();
        int fueraDeRango = 0;

        
        if (humedad < 40) {
            fueraDeRango++;
            diagnostico.agregarHallazgo("Humedad seca (" + redondear(humedad) + " %): lo ideal es de 40 % a 60 %");
            diagnostico.agregarAccion("Regar la pila");
        } else if (humedad > 60) {
            fueraDeRango++;
            diagnostico.agregarHallazgo("Humedad excesiva (" + redondear(humedad) + " %): lo ideal es de 40 % a 60 %");
            diagnostico.agregarAccion("Añadir material seco y voltear");
        } else {
            diagnostico.agregarHallazgo("Humedad óptima (" + redondear(humedad) + " %)");
        }

        
        if (pila.getEstado().equals("Fase activa")) {
            if (temperatura > 65) {
                fueraDeRango++;
                diagnostico.agregarHallazgo("Temperatura alta (" + redondear(temperatura) + " °C): lo ideal es de 45 °C a 65 °C");
                diagnostico.agregarAccion("Voltear la pila");
            } else if (temperatura < 45) {
                fueraDeRango++;
                diagnostico.agregarHallazgo("Temperatura baja (" + redondear(temperatura) + " °C): lo ideal es de 45 °C a 65 °C");
                diagnostico.agregarAccion("Revisar la aireación");
            } else {
                diagnostico.agregarHallazgo("Temperatura óptima (" + redondear(temperatura) + " °C)");
            }
        } else {
            diagnostico.agregarHallazgo("Temperatura registrada: " + redondear(temperatura)
                    + " °C (el rango ideal aplica en fase activa)");
        }

        
        boolean olorAnormal = !ultima.getOlor().equals("Normal");
        if (olorAnormal) {
            diagnostico.agregarHallazgo("Advertencia: olor " + ultima.getOlor().toLowerCase());
            diagnostico.agregarAccion("Revisar la mezcla y voltear para airear");
        }

       
        if (temperatura > 70 || humedad < 30 || humedad > 70 || fueraDeRango >= 2) {
            diagnostico.setNivel("Crítica");
        } else if (fueraDeRango == 1 || olorAnormal) {
            diagnostico.setNivel("Precaución");
        } else {
            diagnostico.setNivel("Óptima");
        }
        return diagnostico;
    }

    

    /** Devuelve las mediciones entre dos fechas, ordenadas de la más antigua a la más reciente. */
    public Medicion[] consultarHistorial(PilaCompost pila, Fecha inicio, Fecha fin) throws ValidacionException {
        validador.validarRango(inicio, fin);

        int cantidad = 0;
        for (int i = 0; i < pila.getTotalMediciones(); i++) {
            Fecha f = pila.getMedicion(i).getFecha();
            if (!f.esAnteriorA(inicio) && !f.esPosteriorA(fin)) {
                cantidad++;
            }
        }
        Medicion[] resultado = new Medicion[cantidad];
        int posicion = 0;
        for (int i = 0; i < pila.getTotalMediciones(); i++) {
            Fecha f = pila.getMedicion(i).getFecha();
            if (!f.esAnteriorA(inicio) && !f.esPosteriorA(fin)) {
                resultado[posicion] = pila.getMedicion(i);
                posicion++;
            }
        }


        for (int i = 0; i < resultado.length - 1; i++) {
            for (int j = 0; j < resultado.length - 1 - i; j++) {
                if (resultado[j].getFecha().esPosteriorA(resultado[j + 1].getFecha())) {
                    Medicion auxiliar = resultado[j];
                    resultado[j] = resultado[j + 1];
                    resultado[j + 1] = auxiliar;
                }
            }
        }
        return resultado;
    }

    

    private void validarPosicion(PilaCompost pila, int posicion) throws ValidacionException {
        if (posicion < 0 || posicion >= pila.getTotalMediciones()) {
            throw new ValidacionException("Seleccione una medición de la lista");
        }
    }

    private void validarDatos(PilaCompost pila, double temperatura, double humedad, String olor,
                              Fecha fecha) throws ValidacionException {
        if (pila.getEstado().equals("Cosechada")) {
            throw new ValidacionException("La pila ya fue cosechada");
        }
        if (!validador.enRango(temperatura, 0, 100)) {
            throw new ValidacionException("La temperatura debe estar entre 0 y 100 °C");
        }
        if (!validador.enRango(humedad, 0, 100)) {
            throw new ValidacionException("La humedad debe estar entre 0 % y 100 %");
        }
        boolean olorValido = false;
        for (int i = 0; i < olores.length; i++) {
            if (olores[i].equals(olor)) {
                olorValido = true;
            }
        }
        if (!olorValido) {
            throw new ValidacionException("Seleccione el olor de la lista");
        }
        if (fecha.esPosteriorA(hoy)) {
            throw new ValidacionException("La fecha de la medición no puede ser futura");
        }
        if (fecha.esAnteriorA(pila.getFechaInicio())) {
            throw new ValidacionException("La fecha de la medición no puede ser anterior al inicio de la pila");
        }
    }

    private double redondear(double valor) {
        return Math.round(valor * 10) / 10.0;
    }
}

