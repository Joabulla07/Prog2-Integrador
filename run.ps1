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

# 4. Compilar e iniciar el launcher de consola
Write-Host "Compilando aplicación Java..."
$sourceFiles = Get-ChildItem src/concesionaria -Recurse -Filter '*.java' | ForEach-Object { $_.FullName }
New-Item -ItemType Directory -Force out | Out-Null
javac -encoding UTF-8 -cp ".;lib/*" -d out $sourceFiles
if ($LASTEXITCODE -ne 0) {
    Write-Error "Error al compilar la aplicación Java."
    exit $LASTEXITCODE
}

if ([System.Environment]::OSVersion.Platform -eq 'Win32NT') {
    Write-Host "Abriendo consola separada..."
    Start-Process -FilePath ".\ejecutar.bat"
} else {
    Write-Host "Iniciando launcher de consola..."
    java -cp "out:lib/*" concesionaria.ui.LauncherConsola
}
