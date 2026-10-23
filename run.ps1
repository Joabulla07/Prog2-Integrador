# 1. Construir la imagen personalizada
Write-Host "Construyendo imagen de SQL Server con sqlcmd..."
podman build -t sql-concesionaria:latest .
if ($LASTEXITCODE -ne 0) { Write-Error "Falló la construcción de la imagen."; exit 1 }

# 2. Levantar el contenedor
Write-Host "Iniciando SQL Server..."
podman compose up -d
if ($LASTEXITCODE -ne 0) { Write-Error "Falló el inicio del contenedor."; exit 1 }

# 3. Esperar a que el motor esté listo y que se ejecuten los scripts de inicialización
Write-Host "Esperando 30 segundos para inicialización..."
Start-Sleep -Seconds 30

# 4. Compilar y ejecutar Java
Write-Host "Compilando y ejecutando..."
$javaFiles = Get-ChildItem -Path src/concesionaria -Recurse -Filter *.java | Select-Object -ExpandProperty FullName
javac -cp ".;lib/*" -d out $javaFiles
java -cp "out;lib/*" concesionaria.Main