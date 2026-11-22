package negocio;

public class SaldoInsuficienteException extends ValidacionException {

    public SaldoInsuficienteException(String mensaje) {
        super(mensaje);
    }
}