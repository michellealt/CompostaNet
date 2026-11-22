package modelo;


public class ResiduoNoCompostable extends Residuo {

    public ResiduoNoCompostable(String nombre) {
        super(nombre);
    }

    @Override
    public boolean esCompostable() {
        return false;
    }

    @Override
    public int puntosPorKg() {
        return 0;
    }
}
