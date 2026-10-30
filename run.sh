#!/bin/bash

# Detectar motor de contenedores (Docker o Podman)
if command -v docker &> /dev/null; then
    ENGINE="docker"
elif command -v podman &> /dev/null; then
    ENGINE="podman"
else
    echo "Error: No se encontró Docker ni Podman"
    echo ""
    echo "Instala uno de los dos:"
    echo "  Docker Desktop: https://www.docker.com/products/docker-desktop"
    echo "  Podman:         https://podman.io/docs/installation"
    echo ""
    echo "Asegúrate de que el motor esté corriendo antes de ejecutar este script."
    exit 1
fi

echo "Motor de contenedores: $ENGINE"

# Determinar comando de compose (plugin "<motor> compose" o "<motor>-compose")
if command -v "$ENGINE-compose" &> /dev/null; then
    COMPOSE_CMD="$ENGINE-compose"
elif $ENGINE compose version &> /dev/null; then
    COMPOSE_CMD="$ENGINE compose"
else
    echo "Error: no se encontró compose para $ENGINE"
    echo "Instala '$ENGINE-compose' o el plugin '$ENGINE compose'."
    exit 1
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
