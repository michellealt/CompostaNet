package interfaz;

import java.util.Scanner;
import modelo.Fecha;

public class Consola {

    private Scanner teclado = new Scanner(System.in);

    public String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return teclado.nextLine().trim();
    }

    public int leerEntero(String mensaje) {
        while (true) {
            try {
                return Integer.parseInt(leerTexto(mensaje));
            } catch (NumberFormatException e) {
                mostrarError("Ingrese un valor numérico entero");
            }
        }
    }

    public double leerDecimal(String mensaje) {
        while (true) {
            try {
                return Double.parseDouble(leerTexto(mensaje).replace(',', '.'));
            } catch (NumberFormatException e) {
                mostrarError("Ingrese un valor numérico");
            }
        }
    }


    public Fecha leerFecha(String mensaje) {
        while (true) {
            String texto = leerTexto(mensaje + " (dd/mm/aaaa): ");
            String[] partes = texto.split("/");
            if (partes.length == 3) {
                try {
                    Fecha fecha = new Fecha(Integer.parseInt(partes[0].trim()),
                            Integer.parseInt(partes[1].trim()), Integer.parseInt(partes[2].trim()));
                    if (fecha.esValida()) {
                        return fecha;
                    }
                } catch (NumberFormatException e) {
                    
                }
            }
            mostrarError("Fecha inválida. Use el formato dd/mm/aaaa, por ejemplo 15/03/2026");
        }
    }

    public int leerOpcion(int maximo) {
        while (true) {
            int opcion = leerEntero("Elija una opción: ");
            if (opcion >= 0 && opcion <= maximo) {
                return opcion;
            }
            mostrarError("Opción no válida");
        }
    }

    public void titulo(String texto) {
        System.out.println();
        System.out.println("=== " + texto + " ===");
    }

    public void mostrarError(String mensaje) {
        System.out.println("[ERROR] " + mensaje);
    }

    public void mostrarExito(String mensaje) {
        System.out.println("[OK] " + mensaje);
    }

    public double redondear(double valor) {
        return Math.round(valor * 100) / 100.0;
    }
}