package negocio;

import modelo.Cosecha;
import modelo.Fecha;
import modelo.PilaCompost;

public class GestorPilas {

    private PilaCompost[] pilas = new PilaCompost[50];
    private int totalPilas;
    private Fecha hoy;

    public GestorPilas(Fecha hoy) {
        this.hoy = hoy;
    }

    public PilaCompost registrarPila(String codigo, String ubicacion, double capacidad, Fecha fechaInicio)
            throws ValidacionException {
        if (codigo.trim().isEmpty()) {
            throw new ValidacionException("El código de la pila es obligatorio");
        }
        if (buscarPila(codigo) != null) {
            throw new ValidacionException("Ya existe una pila con ese código");
        }
        validarUbicacionYCapacidad(ubicacion, capacidad);
        if (fechaInicio.esPosteriorA(hoy)) {
            throw new ValidacionException("La fecha de inicio no puede ser futura");
        }
        if (totalPilas == pilas.length) {
            throw new ValidacionException("Se alcanzó el máximo de pilas");
        }
        PilaCompost pila = new PilaCompost(codigo.trim(), ubicacion.trim(), capacidad, fechaInicio);
        pilas[totalPilas] = pila;
        totalPilas++;
        return pila;
    }


    public int getTotalPilas() {
        return totalPilas;
    }

    public PilaCompost getPila(int posicion) {
        return pilas[posicion];
    }

    public PilaCompost buscarPila(String codigo) {
        for (int i = 0; i < totalPilas; i++) {
            if (pilas[i].getCodigo().equalsIgnoreCase(codigo.trim())) {
                return pilas[i];
            }
        }
        return null;
    }



    public void modificarPila(PilaCompost pila, String ubicacion, double capacidad) throws ValidacionException {
        validarUbicacionYCapacidad(ubicacion, capacidad);
        if (capacidad < pila.getPesoActualKg()) {
            throw new ValidacionException("La capacidad no puede ser menor al peso ya aportado ("
                    + redondear(pila.getPesoActualKg()) + " kg)");
        }
        pila.setUbicacion(ubicacion.trim());
        pila.setCapacidadMaxKg(capacidad);
    }


    public void eliminarPila(PilaCompost pila) throws ValidacionException {
        if (pila.getPesoActualKg() > 0) {
            throw new ValidacionException("No se puede eliminar una pila que ya tiene aportes");
        }
        int posicion = 0;
        for (int i = 0; i < totalPilas; i++) {
            if (pilas[i] == pila) {
                posicion = i;
            }
        }
        for (int i = posicion; i < totalPilas - 1; i++) {   
            pilas[i] = pilas[i + 1];
        }
        pilas[totalPilas - 1] = null;
        totalPilas--;
    }



    public String actualizarEtapa(PilaCompost pila) throws ValidacionException {
        if (pila.getEstado().equals("Cosechada")) {
            throw new ValidacionException("La pila ya fue cosechada");
        }
        if (pila.getTotalMediciones() == 0) {
            throw new ValidacionException("Registre al menos una medición para actualizar la etapa");
        }
        int dias = pila.diasDesdeInicio(hoy);
        double temperatura = pila.ultimaMedicion().getTemperatura();
        double humedad = pila.ultimaMedicion().getHumedad();

        String nuevo = pila.getEstado();
        if (dias >= 60 && temperatura <= 35 && humedad >= 40 && humedad <= 60) {
            nuevo = "Lista para cosecha";
        } else if (pila.isFaseActivaAlcanzada() && temperatura < 40) {
            nuevo = "Maduración";
        } else if (dias >= 3 && temperatura >= 45) {
            nuevo = "Fase activa";
        } else if (dias <= 2) {
            nuevo = "En formación";
        }
        if (!nuevo.equals(pila.getEstado())) {
            pila.cambiarEstado(nuevo, hoy);
        }
        return nuevo;
    }

    

    public Cosecha registrarCosecha(PilaCompost pila, double kg, Fecha fecha) throws ValidacionException {
        if (!pila.getEstado().equals("Lista para cosecha")) {
            throw new ValidacionException("La pila aún no está lista para cosecha");
        }
        if (kg <= 0) {
            throw new ValidacionException("La cantidad cosechada debe ser mayor que 0");
        }
        if (kg > pila.getPesoActualKg()) {
            throw new ValidacionException("La cantidad cosechada no puede superar el peso aportado a la pila ("
                    + redondear(pila.getPesoActualKg()) + " kg)");
        }
        if (fecha.esPosteriorA(hoy)) {
            throw new ValidacionException("La fecha de la cosecha no puede ser futura");
        }
        Cosecha cosecha = new Cosecha(fecha, kg);
        pila.setCosecha(cosecha);
        pila.cambiarEstado("Cosechada", fecha);
        return cosecha;
    }



    private void validarUbicacionYCapacidad(String ubicacion, double capacidad) throws ValidacionException {
        if (ubicacion.trim().isEmpty()) {
            throw new ValidacionException("La ubicación es obligatoria");
        }
        if (capacidad < 50 || capacidad > 1000) {
            throw new ValidacionException("La capacidad debe estar entre 50 y 1.000 kg");
        }
    }

    private double redondear(double valor) {
        return Math.round(valor * 100) / 100.0;
    }
}