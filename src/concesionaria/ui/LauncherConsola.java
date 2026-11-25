package concesionaria.ui;

import java.io.File;

/**
 * Abre ConsolaApp en una consola separada.
 */
public class LauncherConsola {

    /**
     * Punto de entrada que abre la aplicación en una consola separada.
     * Detecta el sistema operativo y abre una nueva terminal ejecutando ConsolaApp.
     *
     * @param args argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        try {
            String os = System.getProperty("os.name", "").toLowerCase();
            ProcessBuilder launcher;

            if (os.contains("win")) {
                launcher = new ProcessBuilder("cmd.exe", "/c", "start",
                        "Concesionaria Multimarcas - TFI Programacion II",
                        "cmd", "/k", "chcp 65001 >nul && cls && java -cp \"out;lib/*\" concesionaria.Main");
            } else {
                launcher = abrirTerminalUnix();
            }

            launcher.directory(new File(System.getProperty("user.dir")));
            launcher.inheritIO();
            launcher.start();

            System.exit(0);
        } catch (Exception e) {
            System.err.println("No se pudo abrir una consola separada: " + e.getMessage());
            System.err.println("Ejecutando en la consola actual...");
            concesionaria.Main.main(args);
        }
    }

    /**
     * Configura el ProcessBuilder para abrir una terminal en sistemas Unix/Linux.
     * Intenta usar gnome-terminal, konsole, xfce4-terminal o xterm según disponibilidad.
     *
     * @return ProcessBuilder configurado para ejecutar la aplicación
     */
    private static ProcessBuilder abrirTerminalUnix() {
        String[] comandos = {
                "gnome-terminal", "konsole", "xfce4-terminal", "xterm"
        };
        for (String comando : comandos) {
            if (comandoDisponible(comando)) {
                if (comando.equals("gnome-terminal")) {
                    return new ProcessBuilder(comando, "--", "bash", "-lc",
                            "cd \"$PWD\" && java -cp \"out:lib/*\" concesionaria.Main; exec bash");
                }
                if (comando.equals("konsole")) {
                    return new ProcessBuilder(comando, "-e", "bash", "-lc",
                            "java -cp \"out:lib/*\" concesionaria.Main");
                }
                return new ProcessBuilder(comando, "-e", "bash", "-lc",
                        "java -cp \"out:lib/*\" concesionaria.Main");
            }
        }
        return new ProcessBuilder("bash", "-lc", "java -cp \"out:lib/*\" concesionaria.Main");
    }

    /**
     * Verifica si un comando está disponible en el sistema usando 'which'.
     *
     * @param comando el comando a verificar
     * @return true si el comando está disponible, false en caso contrario
     */
    private static boolean comandoDisponible(String comando) {
        try {
            Process proceso = new ProcessBuilder("which", comando).redirectErrorStream(true).start();
            boolean disponible = proceso.waitFor() == 0;
            if (!disponible) {
                proceso.destroy();
            }
            return disponible;
        } catch (Exception e) {
            return false;
        }
    }
}
