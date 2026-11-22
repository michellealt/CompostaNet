package negocio;

import modelo.Fecha;

public class Validador {

    public boolean cedulaValida(String cedula) {
        if (cedula == null || cedula.length() != 10) {
            return false;
        }
        for (int i = 0; i < 10; i++) {
            char c = cedula.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        int provincia = Integer.parseInt(cedula.substring(0, 2));
        int tercerDigito = cedula.charAt(2) - '0';
        if (provincia < 1 || provincia > 24 || tercerDigito > 5) {
            return false;
        }
        int suma = 0;
        for (int i = 0; i < 9; i++) {
            int digito = cedula.charAt(i) - '0';
            if (i % 2 == 0) {              
                digito = digito * 2;
                if (digito > 9) {
                    digito = digito - 9;
                }
            }
            suma = suma + digito;
        }
        int verificador = (10 - suma % 10) % 10;
        return verificador == cedula.charAt(9) - '0';
    }

    public boolean correoValido(String correo) {
        if (correo == null || correo.contains(" ")) {
            return false;
        }
        int arroba = correo.indexOf('@');
        int ultimoPunto = correo.lastIndexOf('.');
        return arroba > 0 && ultimoPunto > arroba + 1 && ultimoPunto < correo.length() - 1
                && correo.indexOf('@', arroba + 1) == -1;
    }

    public boolean soloLetras(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return false;
        }
        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);
            if (!Character.isLetter(c) && c != ' ') {
                return false;
            }
        }
        return true;
    }

    public boolean enRango(double valor, double minimo, double maximo) {
        return valor >= minimo && valor <= maximo;
    }

    public void validarRango(Fecha inicio, Fecha fin) throws ValidacionException {
        if (inicio.esPosteriorA(fin)) {
            throw new ValidacionException("La fecha inicial no puede ser posterior a la final");
        }
    }
}