# Tasks

## 1. Lanzador Windows

- [x] 1.1 Crear `ejecutar.bat` en la raíz con `chcp 65001`, título de ventana y `start cmd /k cls && java...`, y verificar que abre una consola separada.
- [x] 1.2 Crear opcionalmente `src/concesionaria/ui/LauncherConsola.java` con `ProcessBuilder`, lanzamiento Windows y salida del proceso actual, y verificar la compilación.

## 2. Navegación limpia

- [x] 2.1 Agregar limpieza de consola antes de entrar a una operación y antes de mostrar nuevamente el menú principal.
- [x] 2.2 Agregar opción visible `Volver al menú principal` dentro de cada pantalla secundaria y verificar que retiene el usuario hasta elegir volver.
- [x] 2.3 Verificar que operación válida, error capturado y lista vacía muestran solo su pantalla y ofrecen volver.

## 3. Integración con scripts

- [x] 3.1 Actualizar `run.ps1` para compilar fuentes y iniciar el launcher después de levantar MSSQL.
- [x] 3.2 Actualizar `run.sh` para compilar fuentes y iniciar el launcher o consola separada disponible después de levantar MSSQL.
- [x] 3.3 Mantener compatibilidad Windows y Linux/Mac, verificando los scripts con sus plataformas disponibles.

## 4. Pruebas y regresión

- [x] 4.1 Agregar pruebas del método de limpieza y retorno sin depender de una terminal física.
- [x] 4.2 Verificar que el lanzador no duplica `main` en `ConsolaApp` y que `Main` sigue siendo la entrada normal.
- [x] 4.3 Ejecutar compilación completa y tests existentes; confirmar que US-13 y el menú siguen funcionando.
