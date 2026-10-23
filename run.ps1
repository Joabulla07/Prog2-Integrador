# Detectar automáticamente si usar Podman o Docker
if (Get-Command podman -ErrorAction SilentlyContinue) {
    $engine = "podman"
    $composeCmd = @("podman", "compose")
} elseif (Get-Command docker -ErrorAction SilentlyContinue) {
    $engine = "docker"
    $composeCmd = @("docker", "compose")
} else {
    Write-Error "No se encontró ni Podman ni Docker en el sistema."
    exit 1
}

Write-Host "Motor de contenedores detectado: $engine"

# 1. Construir la imagen
Write-Host "Construyendo imagen de SQL Server..."
& $engine build -t sql-concesionaria:latest .
if ($LASTEXITCODE -ne 0) {
    Write-Error "Error al compilar la imagen."
    exit $LASTEXITCODE
}

# 2. Levantar el contenedor
Write-Host "Iniciando SQL Server..."
& $composeCmd[0] $composeCmd[1] up -d
if ($LASTEXITCODE -ne 0) {
    Write-Error "Error al levantar el contenedor de la base de datos."
    exit $LASTEXITCODE
}

# 3. Esperar inicialización y ejecución de scripts SQL
Write-Host "Esperando 30 segundos para inicialización..."
Start-Sleep -Seconds 30

# 4. Compilar y ejecutar Java
Write-Host "Compilando y ejecutando..."
$javaFiles = Get-ChildItem -Path src/concesionaria -Recurse -Filter *.java | Select-Object -ExpandProperty FullName
javac -cp ".;lib/*" -d out $javaFiles
java -cp "out;lib/*" concesionaria.Main