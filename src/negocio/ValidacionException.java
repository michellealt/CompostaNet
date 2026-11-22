package negocio;

/** Excepción propia: se lanza cuando un dato o una regla de negocio no se cumple. */
public class ValidacionException extends Exception {

    public ValidacionException(String mensaje) {
        super(mensaje);
    }
}