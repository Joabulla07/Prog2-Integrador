package concesionaria.ui;

import java.lang.reflect.Method;
import java.util.Locale;
import java.util.Scanner;

/**
 * Controles reutilizables para leer datos seguros desde consola.
 */
public class ValidadorEntrada {

    private final Scanner scanner;

    public ValidadorEntrada() {
        this(new Scanner(System.in));
    }

    public ValidadorEntrada(Scanner scanner) {
        if (scanner == null) {
            throw new IllegalArgumentException("El Scanner no puede ser null");
        }
        this.scanner = scanner;
    }

    public String leerTextoObligatorio(String etiqueta) {
        while (true) {
            System.out.print(etiqueta + ": ");
            if (!scanner.hasNextLine()) {
                continue;
            }
            String texto = scanner.nextLine().trim();
            if (!texto.isEmpty()) {
                return texto;
            }
            System.out.println("Error: " + etiqueta + " no puede estar vacío.");
        }
    }

    public Integer leerEnteroWrapper(String etiqueta, int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("El mínimo no puede ser mayor que el máximo");
        }
        while (true) {
            System.out.print(etiqueta + " (" + min + " a " + max + "): ");
            if (!scanner.hasNextLine()) {
                continue;
            }
            String texto = scanner.nextLine().trim();
            try {
                Integer valor = Integer.valueOf(texto);
                if (valor >= min && valor <= max) {
                    return valor;
                }
                System.out.println("Error: " + etiqueta + " debe estar entre " + min + " y " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("Error: " + etiqueta + " debe ser un número entero.");
            }
        }
    }

    public Double leerDecimalWrapper(String etiqueta, double min) {
        while (true) {
            System.out.print(etiqueta + " (mínimo " + min + "): ");
            if (!scanner.hasNextLine()) {
                continue;
            }
            String texto = scanner.nextLine().trim();
            try {
                Double valor = Double.valueOf(texto);
                if (!valor.isNaN() && !valor.isInfinite() && valor >= min) {
                    return valor;
                }
                System.out.println("Error: " + etiqueta + " debe ser un número decimal válido igual o mayor a " + min + ".");
            } catch (NumberFormatException e) {
                System.out.println("Error: " + etiqueta + " debe ser un número decimal.");
            }
        }
    }

    public <E extends Enum<E>> E leerEnum(String etiqueta, Class<E> enumClass) {
        if (enumClass == null) {
            throw new IllegalArgumentException("La clase enum no puede ser null");
        }
        E[] valores = enumClass.getEnumConstants();
        while (true) {
            System.out.print(etiqueta + " " + opcionesEnum(enumClass, valores) + ": ");
            if (!scanner.hasNextLine()) {
                continue;
            }
            String texto = scanner.nextLine().trim();
            if (texto.isEmpty()) {
                System.out.println("Error: " + etiqueta + " no puede estar vacío.");
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
            System.out.println("Error: valor inválido para " + etiqueta + ". Opciones: " + opcionesEnum(enumClass, valores) + ".");
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
