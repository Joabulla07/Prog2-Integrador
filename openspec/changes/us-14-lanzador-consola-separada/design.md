# Design

## Context

US-13 ya presenta el menú y opera contra MSSQL. La consola actual conserva o acumula el contenido anterior. US-14 separa el proceso de lanzamiento de la implementación del menú y mejora la navegación visual sin cambiar el dominio.

## Goals / Non-Goals

**Goals:**
- Abrir una ventana separada en Windows mediante `ejecutar.bat`.
- Añadir un launcher Java reusable opcional.
- Limpiar pantalla antes de operaciones y después de volver.
- Ofrecer un retorno visible desde cada pantalla secundaria.
- Integrar la apertura en `run.ps1` y `run.sh`.

**Non-Goals:**
- Cambiar reglas de vehículos o persistencia.
- Crear una interfaz gráfica.
- Cambiar el protocolo MSSQL.
- Añadir autenticación o configuración interactiva.

## Decisions

- Windows usará `ejecutar.bat` como entrada principal.
  - Ejecutará `chcp 65001 >nul`.
  - Abrirá `cmd /k` con título, `cls` y Java usando `out;lib/*`.
  - Alternativa descartada: usar solo `Main`, que no garantiza ventana separada.

- `LauncherConsola.java` será opcional y solo encapsulará `ProcessBuilder`.
  - En Windows ejecutará `cmd.exe /c start`.
  - La implementación Linux/Mac podrá usar terminales comunes o solo informar el comando si no detecta una terminal compatible.

- `ConsolaApp` tendrá una operación `limpiarPantalla()` con soporte ANSI opcional y comandos de plataforma.
  - Windows usa `cls`; Unix usa `clear` o ANSI.
  - Las pruebas no dependerán del resultado físico de la limpieza.

- Cada opción seguirá mostrando una pantalla secundaria y esperará una opción de volver o Enter.
  - El usuario puede volver sin repetir toda la operación.
  - El código 0 seguirá siendo salida global desde el menú principal.

- `run.ps1` y `run.sh` compilarán primero y luego lanzarán `ejecutar.bat` o el launcher cuando corresponda.
  - En Linux/Mac no abrirán una consola `.bat`; invocarán el launcher Java o una terminal disponible.

## Risks / Trade-offs

- [Terminal no detectada en Linux/Mac] → Mantener ejecución normal por consola como fallback.
- [Escape ANSI no soportado] → Usar `cls`/`clear` según plataforma antes de fallback ANSI.
- [Proceso launcher sin compilar] → Los scripts y el launcher verifican `out/` o ejecutan `javac` antes de iniciar.
- [Pantalla secundaria sin retorno] → Toda operación tendrá una llamada común que ofrece volver.

## Migration Plan

1. Agregar `ejecutar.bat` y `LauncherConsola.java`.
2. Actualizar `ConsolaApp` para limpiar pantalla y ofrecer retorno.
3. Integrar launcher en `run.ps1` y `run.sh`.
4. Probar Windows y fallback normal; ejecutar tests de consola existentes.

Rollback: retirar launcher y mantener `Main` ejecutando `ConsolaApp` directamente; las pantallas continúan sin limpieza aunque no mejora US-14.

## Open Questions

- Ninguna decisión bloqueante. La implementación Linux/Mac puede usar una terminal disponible sin modificar el alcance de US-14.
