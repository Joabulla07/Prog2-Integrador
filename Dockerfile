FROM mcr.microsoft.com/mssql/server:2022-latest

ARG MSSQL_SA_PASSWORD
ENV MSSQL_SA_PASSWORD=${MSSQL_SA_PASSWORD}

USER root

# Crear directorio para scripts y copiar los archivos SQL
RUN mkdir -p /scripts
COPY sql/01_esquema.sql /scripts/
COPY sql/02_datos.sql /scripts/

# Script entrypoint para levantar SQL Server e inicializar solo la primera vez
RUN echo '#!/bin/bash\n\
/opt/mssql/bin/sqlservr &\n\
echo "Esperando que SQL Server esté listo..."\n\
while ! /opt/mssql-tools18/bin/sqlcmd -S localhost -U sa -P "$MSSQL_SA_PASSWORD" -C -Q "SELECT 1" > /dev/null 2>&1; do\n\
    sleep 1\n\
done\n\
# Verificar si la base de datos ya fue creada previamente\n\
DB_EXISTE=$(/opt/mssql-tools18/bin/sqlcmd -S localhost -U sa -P "$MSSQL_SA_PASSWORD" -C -h -1 -W -Q "SET NOCOUNT ON; SELECT database_id FROM sys.databases WHERE name = '\''concesionaria'\''")\n\
if [ -z "$DB_EXISTE" ]; then\n\
    echo "Base no encontrada. Creando esquema e insertando datos..."\n\
    /opt/mssql-tools18/bin/sqlcmd -S localhost -U sa -P "$MSSQL_SA_PASSWORD" -C -i /scripts/01_esquema.sql\n\
    /opt/mssql-tools18/bin/sqlcmd -S localhost -U sa -P "$MSSQL_SA_PASSWORD" -C -d concesionaria -i /scripts/02_datos.sql\n\
    echo "Base de datos concesionaria inicializada con éxito."\n\
else\n\
    echo "La base de datos concesionaria ya existe. Omitiendo scripts iniciales."\n\
fi\n\
wait' > /entrypoint.sh && \
    chmod +x /entrypoint.sh

USER mssql

ENTRYPOINT ["/entrypoint.sh"]