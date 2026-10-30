# Detectar motor de contenedores (Docker o Podman)
if (Get-Command docker -ErrorAction SilentlyContinue) {
    $engine = "docker"
} elseif (Get-Command podman -ErrorAction SilentlyContinue) {
    $engine = "podman"
} else {
    Write-Error "No se encontró Docker ni Podman en el PATH."
    Write-Host ""
    Write-Host "Instala uno de los dos:"
    Write-Host "  Docker Desktop: https://www.docker.com/products/docker-desktop"
    Write-Host "  Podman:         https://podman.io/docs/installation"
    Write-Host ""
    Write-Host "Asegúrate de que el motor esté corriendo antes de ejecutar este script."
    exit 1
}

Write-Host "Motor de contenedores: $engine"

# Determinar comando de compose (plugin "<motor> compose" o "<motor>-compose")
if (Get-Command "$engine-compose" -ErrorAction SilentlyContinue) {
    $composeExe = "$engine-compose"
    $composePrefix = @()
} else {
    & $engine compose version *> $null
    if ($LASTEXITCODE -ne 0) {
        Write-Error "No se encontró compose para $engine. Instala '$engine-compose' o el plugin '$engine compose'."
        exit 1
    }
    $composeExe = $engine
    $composePrefix = @("compose")
}

# 1. Construir la imagen usando compose (lee variables del .env)
Write-Host "Construyendo imagen de SQL Server..."
& $composeExe @composePrefix build
if ($LASTEXITCODE -ne 0) {
    Write-Error "Error al compilar la imagen."
    exit $LASTEXITCODE
}

# 2. Levantar el contenedor
Write-Host "Iniciando SQL Server..."
& $composeExe @composePrefix up -d
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