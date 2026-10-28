#!/bin/bash

# Verificar que Docker esté instalado
if ! command -v docker &> /dev/null; then
    echo "Error: Docker no está instalado"
    echo ""
    echo "Instala Docker Desktop desde:"
    echo "  https://www.docker.com/products/docker-desktop"
    echo ""
    echo "Asegúrate de que Docker Desktop esté corriendo antes de ejecutar este script."
    exit 1
fi

echo "Motor de contenedores: Docker"

# Determinar comando de compose (docker compose o docker-compose)
if command -v docker-compose &> /dev/null; then
    COMPOSE_CMD="docker-compose"
else
    COMPOSE_CMD="docker compose"
fi

# 1. Construir la imagen usando docker-compose (lee variables del .env)
echo "Construyendo imagen de SQL Server..."
$COMPOSE_CMD build
if [ $? -ne 0 ]; then
    echo "Error al compilar la imagen."
    exit 1
fi

# 2. Levantar el contenedor
echo "Iniciando SQL Server..."
$COMPOSE_CMD up -d
if [ $? -ne 0 ]; then
    echo "Error al levantar el contenedor de la base de datos."
    exit 1
fi

# 3. Esperar inicialización y ejecución de scripts SQL
echo "Esperando 30 segundos para inicialización..."
sleep 30

# 4. Compilar el proyecto Java
echo "Compilando el proyecto..."
javac -d out/production/Prog2-Integrador src/concesionaria/*.java
if [ $? -ne 0 ]; then
    echo "Error al compilar el proyecto."
    exit 1
fi

# 5. Ejecutar el main del proyecto
echo "Ejecutando el main del proyecto..."
java -cp out/production/Prog2-Integrador concesionaria.Main
