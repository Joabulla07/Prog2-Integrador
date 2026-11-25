package concesionaria.ui;

import java.lang.reflect.Method;
import java.util.Scanner;

/**
 * Controles reutilizables para leer datos seguros desde consola.
 */
public class ValidadorEntrada {

    private final Scanner scanner;
    private final TerminalUtil terminalUtil;

    /**
     * Constructor por defecto que crea un ValidadorEntrada con Scanner de System.in.
     */
    public ValidadorEntrada() {
        this(new Scanner(System.in));
    }

    /**
     * Constructor que crea un ValidadorEntrada con el Scanner especificado.
     *
     * @param scanner el Scanner a utilizar para leer entrada
     */
    public ValidadorEntrada(Scanner scanner) {
        this(scanner, new TerminalUtil());
    }

    /**
     * Constructor completo que permite especificar Scanner y TerminalUtil.
     *
     * @param scanner el Scanner a utilizar para leer entrada
     * @param terminalUtil el TerminalUtil para formatear texto (puede ser null)
     * @throws IllegalArgumentException si el Scanner es null
     */
    public ValidadorEntrada(Scanner scanner, TerminalUtil terminalUtil) {
        if (scanner == null) {
            throw new IllegalArgumentException("El Scanner no puede ser null");
        }
        this.scanner = scanner;
        this.terminalUtil = terminalUtil != null ? terminalUtil : new TerminalUtil();
    }


    /**
     * Lee un texto no vacío del usuario.
     * Repite la solicitud hasta que el usuario ingrese un valor válido.
     *
     * @param etiqueta la etiqueta a mostrar al usuario (ej. "Marca")
     * @return el texto ingresado por el usuario
     */
    public String leerTextoObligatorio(String etiqueta) {
        while (true) {
            System.out.print(terminalUtil.centrarTexto(etiqueta + ": "));
            if (!scanner.hasNextLine()) {
                continue;
            }
            String texto = scanner.nextLine().trim();
            if (!texto.isEmpty()) {
                return texto;
            }
            System.out.println(terminalUtil.centrarTexto("Error: " + etiqueta + " no puede estar vacío."));
        }
    }

    /**
     * Lee un entero dentro de un rango especificado.
     * Repite la solicitud hasta que el usuario ingrese un valor válido.
     *
     * @param etiqueta la etiqueta a mostrar al usuario
     * @param min valor mínimo aceptado (inclusive)
     * @param max valor máximo aceptado (inclusive)
     * @return el entero ingresado por el usuario
     * @throws IllegalArgumentException si min > max
     */
    public Integer leerEnteroWrapper(String etiqueta, int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("El mínimo no puede ser mayor que el máximo");
        }
        while (true) {
            System.out.print(terminalUtil.centrarTexto(etiqueta + " (" + min + " a " + max + "): "));
            if (!scanner.hasNextLine()) {
                continue;
            }
            String texto = scanner.nextLine().trim();
            try {
                Integer valor = Integer.valueOf(texto);
                if (valor >= min && valor <= max) {
                    return valor;
                }
                System.out.println(terminalUtil.centrarTexto("Error: " + etiqueta + " debe estar entre " + min + " y " + max + "."));
            } catch (NumberFormatException e) {
                System.out.println(terminalUtil.centrarTexto("Error: " + etiqueta + " debe ser un número entero."));
            }
        }
    }

    /**
     * Espera a que el usuario ingrese 0 para continuar.
     * Utilizado para pausar antes de volver al menú principal.
     */
    public void esperarOpcionCero() {
        while (true) {
            System.out.print(terminalUtil.centrarTexto("0. Volver al menú principal: "));
            if (!scanner.hasNextLine()) {
                continue;
            }
            String texto = scanner.nextLine().trim();
            try {
                Integer valor = Integer.valueOf(texto);
                if (valor == 0) {
                    return;
                }
                System.out.println(terminalUtil.centrarTexto("Error: ingrese 0 para volver al menú principal."));
            } catch (NumberFormatException e) {
                System.out.println(terminalUtil.centrarTexto("Error: ingrese 0 para volver al menú principal."));
            }
        }
    }

    /**
     * Lee un número decimal mayor o igual a un mínimo.
     * Repite la solicitud hasta que el usuario ingrese un valor válido.
     *
     * @param etiqueta la etiqueta a mostrar al usuario
     * @param min valor mínimo aceptado (inclusive)
     * @return el decimal ingresado por el usuario
     */
    public Double leerDecimalWrapper(String etiqueta, double min) {
        while (true) {
            System.out.print(terminalUtil.centrarTexto(etiqueta + " (mínimo " + min + "): "));
            if (!scanner.hasNextLine()) {
                continue;
            }
            String texto = scanner.nextLine().trim();
            try {
                Double valor = Double.valueOf(texto);
                if (!valor.isNaN() && !valor.isInfinite() && valor >= min) {
                    return valor;
                }
                System.out.println(terminalUtil.centrarTexto("Error: " + etiqueta + " debe ser un número decimal válido igual o mayor a " + min + "."));
            } catch (NumberFormatException e) {
                System.out.println(terminalUtil.centrarTexto("Error: " + etiqueta + " debe ser un número decimal."));
            }
        }
    }

    /**
     * Lee un valor de un enum especificado.
     * Acepta el nombre del enum o su descripción (si tiene método getDescripcion).
     *
     * @param etiqueta la etiqueta a mostrar al usuario
     * @param enumClass la clase del enum a leer
     * @param <E> el tipo del enum
     * @return el valor del enum seleccionado
     * @throws IllegalArgumentException si enumClass es null
     */
    public <E extends Enum<E>> E leerEnum(String etiqueta, Class<E> enumClass) {
        if (enumClass == null) {
            throw new IllegalArgumentException("La clase enum no puede ser null");
        }
        E[] valores = enumClass.getEnumConstants();
        while (true) {
            System.out.print(terminalUtil.centrarTexto(etiqueta + " " + opcionesEnum(enumClass, valores) + ": "));
            if (!scanner.hasNextLine()) {
                continue;
            }
            String texto = scanner.nextLine().trim();
            if (texto.isEmpty()) {
                System.out.println(terminalUtil.centrarTexto("Error: " + etiqueta + " no puede estar vacío."));
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
            System.out.println(terminalUtil.centrarTexto("Error: valor inválido para " + etiqueta + ". Opciones: " + opcionesEnum(enumClass, valores) + "."));
        }
    }

    /**
     * Genera un string con las opciones disponibles de un enum.
     * Incluye tanto el nombre como la descripción (si existe).
     *
     * @param enumClass la clase del enum
     * @param valores los valores del enum
     * @param <E> el tipo del enum
     * @return string con las opciones formateadas
     */
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

    /**
     * Obtiene la descripción de un valor enum usando reflexión.
     * Busca un método getDescripcion() y lo invoca.
     *
     * @param valor el valor del enum
     * @return la descripción si existe, null en caso contrario
     */
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
