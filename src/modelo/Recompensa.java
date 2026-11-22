package modelo;


public class Recompensa {

    private String nombre;
    private int costoPuntos;
    private int stock;
    private String estado;            

    public Recompensa(String nombre, int costoPuntos, int stock) {
        this.nombre = nombre;
        this.costoPuntos = costoPuntos;
        this.stock = stock;
        actualizarEstado();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCostoPuntos() {
        return costoPuntos;
    }

    public void setCostoPuntos(int costoPuntos) {
        this.costoPuntos = costoPuntos;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
        actualizarEstado();
    }

    public String getEstado() {
        return estado;
    }

    public boolean hayStock() {
        return stock > 0;
    }

    public void descontarStock() {
        stock--;
        actualizarEstado();
    }

    private void actualizarEstado() {
        if (stock > 0) {
            estado = "Disponible";
        } else {
            estado = "Agotada";
        }
    }
}

