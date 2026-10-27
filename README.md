# Prog2-Integrador

## Requisitos Previos

Este proyecto requiere Docker para ejecutar la base de datos SQL Server.

1. Instalar [Docker Desktop](https://www.docker.com/products/docker-desktop)
2. Iniciar Docker Desktop y asegurarse de que esté corriendo

## Ejecución

### Windows (PowerShell)
```powershell
.\run.ps1
```

### Linux/Mac/Windows (Git Bash)
```bash
chmod +x run.sh
./run.sh
```

El script:
- Construye la imagen de SQL Server
- Levanta el contenedor de la base de datos
- Espera 30 segundos para la inicialización

**Importante:** Después de ejecutar el script, debes compilar y ejecutar la aplicación Java por separado:

```bash
# Compilar
javac -cp ".;lib/*" -d out src/concesionaria/*.java  # Windows
javac -cp ".:lib/*" -d out src/concesionaria/*.java  # Linux/Mac

# Ejecutar
java -cp "out;lib/*" concesionaria.Main  # Windows
java -cp "out:lib/*" concesionaria.Main  # Linux/Mac
```