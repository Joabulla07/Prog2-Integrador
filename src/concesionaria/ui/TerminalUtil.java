package concesionaria.ui;

import org.jline.terminal.Terminal;

/**
 * Utilidades para manejo de terminal y formateo de texto en consola.
 * Proporciona métodos para obtener dimensiones del terminal, centrar texto
 * y limpiar la pantalla de forma multiplataforma.
 */
public class TerminalUtil {

    private final Terminal terminal;

    /**
     * Constructor por defecto que crea un TerminalUtil sin terminal específico.
     * Usa valores por defecto para dimensiones (80x24).
     */
    public TerminalUtil() {
        this(null);
    }

    /**
     * Constructor que permite especificar un Terminal para obtener dimensiones reales.
     *
     * @param terminal el Terminal JLine para obtener dimensiones (puede ser null)
     */
    public TerminalUtil(Terminal terminal) {
        this.terminal = terminal;
    }

    /**
     * Centra un texto horizontalmente según el ancho del terminal.
     * Si el texto tiene varias líneas, centra cada una por separado.
     *
     * @param texto el texto a centrar
     * @return el texto con espacios de padding para centrarlo
     */
    public String centrarTexto(String texto) {
        if (texto == null) {
            return "";
        }
        int ancho = obtenerAnchoTerminal();
        String[] lineas = texto.split("\n", -1);
        StringBuilder centrado = new StringBuilder();
        for (int i = 0; i < lineas.length; i++) {
            if (i > 0) {
                centrado.append('\n');
            }
            String linea = lineas[i];
            int padding = Math.max(0, (ancho - linea.length()) / 2);
            centrado.append(" ".repeat(padding)).append(linea);
        }
        return centrado.toString();
    }

    /**
     * Agrega líneas en blanco para centrar verticalmente el contenido.
     *
     * @param lineasContenido cantidad de líneas que ocupará el contenido
     */
    public void centrarVerticalmente(int lineasContenido) {
        int altura = obtenerAlturaTerminal();
        int paddingVertical = (altura - lineasContenido) / 2;
        if (paddingVertical < 0) paddingVertical = 0;
        for (int i = 0; i < paddingVertical; i++) {
            System.out.println();
        }
    }

    /**
     * Obtiene el ancho actual del terminal.
     * Retorna 80 como valor por defecto si no se puede obtener.
     *
     * @return ancho del terminal en caracteres
     */
    public int obtenerAnchoTerminal() {
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

    /**
     * Obtiene la altura actual del terminal.
     * Retorna 24 como valor por defecto si no se puede obtener.
     *
     * @return altura del terminal en líneas
     */
    public int obtenerAlturaTerminal() {
        if (terminal != null) {
            try {
                int altura = terminal.getHeight();
                if (altura > 0 && altura < 10000) {
                    return altura;
                }
            } catch (Exception e) {
            }
        }
        return 24;
    }

    /**
     * Limpia la pantalla de la consola según el sistema operativo.
     * Usa cls en Windows y clear en Unix/Linux.
     */
    public static void limpiarPantalla() {
        try {
            String os = System.getProperty("os.name", "").toLowerCase();
            if (os.contains("win")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }
    }
}
