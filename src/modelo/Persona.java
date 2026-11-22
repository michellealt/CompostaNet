package modelo;
public abstract class Persona {

    private String cedula;
    private String nombres;
    private String correo;

    public Persona(String cedula, String nombres, String correo) {
        this.cedula = cedula;
        this.nombres = nombres;
        this.correo = correo;
    }

    public String getCedula() {
        return cedula;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
    public abstract String mostrarResumen();
}