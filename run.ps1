# Verificar que Docker esté instalado
if (-not (Get-Command docker -ErrorAction SilentlyContinue)) {
    Write-Error "Docker no está instalado o no está en el PATH."
    Write-Host ""
    Write-Host "Instala Docker Desktop desde:"
    Write-Host "  https://www.docker.com/products/docker-desktop"
    Write-Host ""
    Write-Host "Asegúrate de que Docker Desktop esté corriendo antes de ejecutar este script."
    exit 1
}

# Determinar comando de compose (docker compose o docker-compose)
if (Get-Command docker-compose -ErrorAction SilentlyContinue) {
    $composeCmd = "docker-compose"
} else {
    $composeCmd = "docker compose"
}

Write-Host "Motor de contenedores: Docker"

# 1. Construir la imagen usando docker-compose (lee variables del .env)
Write-Host "Construyendo imagen de SQL Server..."
& $composeCmd build
if ($LASTEXITCODE -ne 0) {
    Write-Error "Error al compilar la imagen."
    exit $LASTEXITCODE
}

# 2. Levantar el contenedor
Write-Host "Iniciando SQL Server..."
& $composeCmd up -d
if ($LASTEXITCODE -ne 0) {
    Write-Error "Error al levantar el contenedor de la base de datos."
    exit $LASTEXITCODE
}

# 3. Esperar inicialización y ejecución de scripts SQL
Write-Host "Esperando 30 segundos para inicialización..."
Start-Sleep -Seconds 30

# 4. Compilar el proyecto Java
Write-Host "Compilando el proyecto..."
javac -d out\production\Prog2-Integrador src\concesionaria\*.java
if ($LASTEXITCODE -ne 0) {
    Write-Error "Error al compilar el proyecto."
    exit $LASTEXITCODE
}

# 5. Ejecutar el main del proyecto
Write-Host "Ejecutando el main del proyecto..."
java -cp out\production\Prog2-Integrador concesionaria.Main