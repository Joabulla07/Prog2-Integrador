package concesionaria.ui;

import java.lang.reflect.Method;
import java.util.Scanner;
import org.jline.terminal.Terminal;

/**
 * Controles reutilizables para leer datos seguros desde consola.
 */
public class ValidadorEntrada {

    private final Scanner scanner;
    private final Terminal terminal;

    public ValidadorEntrada() {
        this(new Scanner(System.in), null);
    }

    public ValidadorEntrada(Scanner scanner) {
        this(scanner, null);
    }

    public ValidadorEntrada(Scanner scanner, Terminal terminal) {
        if (scanner == null) {
            throw new IllegalArgumentException("El Scanner no puede ser null");
        }
        this.scanner = scanner;
        this.terminal = terminal;
    }

    private String centrarTexto(String texto) {
        int ancho = obtenerAnchoTerminal();
        int padding = (ancho - texto.length()) / 2;
        if (padding < 0) padding = 0;
        return " ".repeat(padding) + texto;
    }

    private int obtenerAnchoTerminal() {
        if (terminal != null) {
            try {
                int ancho = terminal.getWidth();
                if (ancho > 0 && ancho < 10000) {
                    return ancho;
                }
            } catch (Exception e) {
            }
        }
        return 80;
    }

    public String leerTextoObligatorio(String etiqueta) {
        while (true) {
            System.out.print(centrarTexto(etiqueta + ": "));
            if (!scanner.hasNextLine()) {
                continue;
            }
            String texto = scanner.nextLine().trim();
            if (!texto.isEmpty()) {
                return texto;
            }
            System.out.println(centrarTexto("Error: " + etiqueta + " no puede estar vacío."));
        }
    }

    public Integer leerEnteroWrapper(String etiqueta, int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("El mínimo no puede ser mayor que el máximo");
        }
        while (true) {
            System.out.print(centrarTexto(etiqueta + " (" + min + " a " + max + "): "));
            if (!scanner.hasNextLine()) {
                continue;
            }
            String texto = scanner.nextLine().trim();
            try {
                Integer valor = Integer.valueOf(texto);
                if (valor >= min && valor <= max) {
                    return valor;
                }
                System.out.println(centrarTexto("Error: " + etiqueta + " debe estar entre " + min + " y " + max + "."));
            } catch (NumberFormatException e) {
                System.out.println(centrarTexto("Error: " + etiqueta + " debe ser un número entero."));
            }
        }
    }

    public void esperarOpcionCero() {
        while (true) {
            System.out.print(centrarTexto("0. Volver al menú principal: "));
            if (!scanner.hasNextLine()) {
                continue;
            }
            String texto = scanner.nextLine().trim();
            try {
                Integer valor = Integer.valueOf(texto);
                if (valor == 0) {
                    return;
                }
                System.out.println(centrarTexto("Error: ingrese 0 para volver al menú principal."));
            } catch (NumberFormatException e) {
                System.out.println(centrarTexto("Error: ingrese 0 para volver al menú principal."));
            }
        }
    }

    public Double leerDecimalWrapper(String etiqueta, double min) {
        while (true) {
            System.out.print(centrarTexto(etiqueta + " (mínimo " + min + "): "));
            if (!scanner.hasNextLine()) {
                continue;
            }
            String texto = scanner.nextLine().trim();
            try {
                Double valor = Double.valueOf(texto);
                if (!valor.isNaN() && !valor.isInfinite() && valor >= min) {
                    return valor;
                }
                System.out.println(centrarTexto("Error: " + etiqueta + " debe ser un número decimal válido igual o mayor a " + min + "."));
            } catch (NumberFormatException e) {
                System.out.println(centrarTexto("Error: " + etiqueta + " debe ser un número decimal."));
            }
        }
    }

    public <E extends Enum<E>> E leerEnum(String etiqueta, Class<E> enumClass) {
        if (enumClass == null) {
            throw new IllegalArgumentException("La clase enum no puede ser null");
        }
        E[] valores = enumClass.getEnumConstants();
        while (true) {
            System.out.print(centrarTexto(etiqueta + " " + opcionesEnum(enumClass, valores) + ": "));
            if (!scanner.hasNextLine()) {
                continue;
            }
            String texto = scanner.nextLine().trim();
            if (texto.isEmpty()) {
                System.out.println(centrarTexto("Error: " + etiqueta + " no puede estar vacío."));
                continue;
            }

            for (E valor : valores) {
                if (valor.name().equalsIgnoreCase(texto)) {
                    return valor;
                }
                String descripcion = obtenerDescripcion(valor);
                if (descripcion != null && descripcion.equalsIgnoreCase(texto)) {
                    return valor;
                }
            }
            System.out.println(centrarTexto("Error: valor inválido para " + etiqueta + ". Opciones: " + opcionesEnum(enumClass, valores) + "."));
        }
    }

    private <E extends Enum<E>> String opcionesEnum(Class<E> enumClass, E[] valores) {
        StringBuilder opciones = new StringBuilder("[");
        for (int i = 0; i < valores.length; i++) {
            if (i > 0) {
                opciones.append(", ");
            }
            opciones.append(valores[i].name());
            String descripcion = obtenerDescripcion(valores[i]);
            if (descripcion != null) {
                opciones.append("/").append(descripcion);
            }
        }
        opciones.append("]");
        return opciones.toString();
    }

    private String obtenerDescripcion(Enum<?> valor) {
        try {
            Method metodo = valor.getClass().getMethod("getDescripcion");
            Object descripcion = metodo.invoke(valor);
            return descripcion != null ? descripcion.toString() : null;
        } catch (ReflectiveOperationException e) {
            return null;
        }
    }
}
