package modelo;


public abstract class Residuo {

    private String nombre;

    public Residuo(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public abstract boolean esCompostable();

    public abstract int puntosPorKg();
}

