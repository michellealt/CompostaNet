package modelo;


public class ResiduoVerde extends Residuo {

    public ResiduoVerde(String nombre) {
        super(nombre);
    }

    @Override
    public boolean esCompostable() {
        return true;
    }

    @Override
    public int puntosPorKg() {
        return 10;
    }
}
