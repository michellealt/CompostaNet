package modelo;


public class ResiduoSeco extends Residuo {

    public ResiduoSeco(String nombre) {
        super(nombre);
    }

    @Override
    public boolean esCompostable() {
        return true;
    }

    @Override
    public int puntosPorKg() {
        return 5;
    }
}

