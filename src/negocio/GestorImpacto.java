package negocio;

import modelo.Aporte;
import modelo.FactorEmision;
import modelo.Fecha;
import modelo.Participante;
import modelo.PilaCompost;
import modelo.Residuo;
import modelo.ResumenImpacto;

/** Módulo 4: huella ecológica e impacto ambiental (RF-10 a RF-12). Incluye el CRUD de factores. */
public class GestorImpacto {

    private FactorEmision[] factores = new FactorEmision[20];
    private int totalFactores;
    private GestorParticipantes gestorParticipantes;
    private GestorPilas gestorPilas;
    private Validador validador = new Validador();

    public GestorImpacto(GestorParticipantes gestorParticipantes, GestorPilas gestorPilas) {
        this.gestorParticipantes = gestorParticipantes;
        this.gestorPilas = gestorPilas;
    }

    // ---------- RF-11: configurar factores de conversión (Create / Update) ----------

    /** Si el residuo ya tiene factor lo actualiza; si no, lo crea. */
    public void configurarFactor(Residuo residuo, double factor, String fuente) throws ValidacionException {
        if (!residuo.esCompostable()) {
            throw new ValidacionException("Solo se configuran factores para residuos compostables");
        }
        if (factor <= 0) {
            throw new ValidacionException("El factor debe ser un número mayor que 0");
        }
        if (fuente.trim().isEmpty()) {
            throw new ValidacionException("La fuente de referencia es obligatoria");
        }
        FactorEmision existente = buscarFactor(residuo);
        if (existente != null) {
            existente.setFactor(factor);
            existente.setFuente(fuente.trim());
        } else {
            factores[totalFactores] = new FactorEmision(residuo, factor, fuente.trim());
            totalFactores++;
        }
    }

    // ---------- Consultar (Read) ----------

    public int getTotalFactores() {
        return totalFactores;
    }

    public FactorEmision getFactorEn(int posicion) {
        return factores[posicion];
    }

    /** Devuelve el factor configurado para el residuo o null si no tiene. */
    public FactorEmision buscarFactor(Residuo residuo) {
        for (int i = 0; i < totalFactores; i++) {
            if (factores[i].getResiduo() == residuo) {
                return factores[i];
            }
        }
        return null;
    }

    /** Valor del factor del residuo: el configurado o, si no hay, el valor por defecto 0,50. */
    public double getFactor(Residuo residuo) {
        FactorEmision factor = buscarFactor(residuo);
        if (factor == null) {
            return 0.50;
        }
        return factor.getFactor();
    }

    // ---------- Eliminar (Delete) ----------

    /** Al eliminar el factor, el residuo vuelve a usar el valor por defecto. */
    public void eliminarFactor(Residuo residuo) throws ValidacionException {
        FactorEmision factor = buscarFactor(residuo);
        if (factor == null) {
            throw new ValidacionException("Ese residuo no tiene un factor configurado");
        }
        int posicion = 0;
        for (int i = 0; i < totalFactores; i++) {
            if (factores[i] == factor) {
                posicion = i;
            }
        }
        for (int i = posicion; i < totalFactores - 1; i++) {
            factores[i] = factores[i + 1];
        }
        factores[totalFactores - 1] = null;
        totalFactores--;
    }

    // ---------- RF-10: calcular impacto ambiental (regla de negocio) ----------

    /** CO2 evitado de un aporte = peso × factor del tipo de residuo. */
    public double calcularCO2(Aporte aporte) {
        return aporte.getPesoKg() * getFactor(aporte.getResiduo());
    }

    public ResumenImpacto calcularImpacto(Participante participante, Fecha inicio, Fecha fin)
            throws ValidacionException {
        validador.validarRango(inicio, fin);
        double kg = 0;
        double co2 = 0;
        for (int i = 0; i < participante.getTotalAportes(); i++) {
            Aporte aporte = participante.getAporte(i);
            if (!aporte.getFecha().esAnteriorA(inicio) && !aporte.getFecha().esPosteriorA(fin)) {
                kg = kg + aporte.getPesoKg();
                co2 = co2 + calcularCO2(aporte);
            }
        }
        if (kg == 0) {
            throw new ValidacionException("No hay aportes en el periodo seleccionado");
        }
        return new ResumenImpacto(inicio, fin, 1, kg, 0, co2);
    }

    // ---------- RF-12: generar reporte de impacto comunitario ----------

    public ResumenImpacto resumenComunitario(Fecha inicio, Fecha fin) throws ValidacionException {
        validador.validarRango(inicio, fin);
        double kg = 0;
        double co2 = 0;
        int activos = 0;
        for (int i = 0; i < gestorParticipantes.getTotalParticipantes(); i++) {
            Participante participante = gestorParticipantes.getParticipante(i);
            boolean aportoEnElPeriodo = false;
            for (int j = 0; j < participante.getTotalAportes(); j++) {
                Aporte aporte = participante.getAporte(j);
                if (!aporte.getFecha().esAnteriorA(inicio) && !aporte.getFecha().esPosteriorA(fin)) {
                    kg = kg + aporte.getPesoKg();
                    co2 = co2 + calcularCO2(aporte);
                    aportoEnElPeriodo = true;
                }
            }
            if (aportoEnElPeriodo) {
                activos++;
            }
        }
        double compost = 0;
        for (int i = 0; i < gestorPilas.getTotalPilas(); i++) {
            PilaCompost pila = gestorPilas.getPila(i);
            if (pila.getCosecha() != null) {
                Fecha f = pila.getCosecha().getFecha();
                if (!f.esAnteriorA(inicio) && !f.esPosteriorA(fin)) {
                    compost = compost + pila.getCosecha().getCantidadKg();
                }
            }
        }
        return new ResumenImpacto(inicio, fin, activos, kg, compost, co2);
    }
}
