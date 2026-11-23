@echo off
chcp 65001 >nul
cd /d "%~dp0"

if not exist "out" mkdir out
javac -encoding UTF-8 -cp ".;lib/*" -d out src\concesionaria\*.java src\concesionaria\model\*.java src\concesionaria\model\enums\*.java src\concesionaria\repository\*.java src\concesionaria\servicio\*.java src\concesionaria\ui\*.java src\concesionaria\exception\*.java src\concesionaria\config\*.java src\concesionaria\interfaces\*.java
if errorlevel 1 (
    echo Error al compilar el proyecto.
    pause
    exit /b 1
)

start "Concesionaria Multimarcas - TFI Programacion II" cmd /c "chcp 65001 >nul && cls && java -cp ""out;lib/*"" concesionaria.Main"
