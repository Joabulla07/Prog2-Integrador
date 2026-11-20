-- =============================================================================
-- SISTEMA DE GESTIÓN PARA CONCESIONARIA DE AUTOMÓVILES
-- BASE DE DATOS I - TUP - UTN FRRe
-- Script DDL: Creación de Base de Datos, Tablas, Claves Primarias y Foráneas
-- Motor: Microsoft SQL Server
-- =============================================================================

USE master;
GO

-- Crear la base de datos si no existe previamente
IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = 'ConcesionariaTFI')
BEGIN
    CREATE DATABASE ConcesionariaTFI;
END
GO

USE ConcesionariaTFI;
GO

-- 2. Crear la tabla Vehiculos
IF OBJECT_ID(N'dbo.Vehiculos', N'U') IS NOT NULL
BEGIN
    DROP TABLE dbo.Vehiculos;
END
GO

CREATE TABLE dbo.Vehiculos (
    id_vehiculo         INT              IDENTITY(1,1) NOT NULL,
    codigo_inventario   AS (
                            CASE tipo_vehiculo
                                WHEN 'AUTOMOVIL' THEN 'AUTO-' 
                                WHEN 'CAMIONETA' THEN 'CAM-' 
                                WHEN 'MOTOCICLETA' THEN 'MOTO-' 
                            END
                            + RIGHT('000000' + CAST(id_vehiculo AS VARCHAR(10)), 6)
                        ) PERSISTED,
    tipo_vehiculo       VARCHAR(20)     NOT NULL,
    marca               VARCHAR(60)     NOT NULL,
    modelo              VARCHAR(60)     NOT NULL,
    anio                INT             NOT NULL,
    color               VARCHAR(40)     NOT NULL,
    estado_unidad       VARCHAR(15)     NOT NULL,
    kilometraje         INT             NOT NULL CONSTRAINT DF_Vehiculos_Kilometraje DEFAULT (0),
    precio_lista        DECIMAL(14, 2)  NOT NULL,
    carroceria          VARCHAR(60)     NULL,
    tipo_motocicleta    VARCHAR(60)     NULL,

    CONSTRAINT PK_Vehiculos PRIMARY KEY (id_vehiculo),
    CONSTRAINT UQ_Vehiculos_Codigo UNIQUE (codigo_inventario),

    CONSTRAINT CK_Vehiculos_Tipo CHECK (
        tipo_vehiculo IN ('AUTOMOVIL', 'CAMIONETA', 'MOTOCICLETA')
    ),

    CONSTRAINT CK_Vehiculos_Estado CHECK (
        estado_unidad IN ('CERO_KM', 'USADO')
    ),

    CONSTRAINT CK_Vehiculos_Estado_Km CHECK (
        (estado_unidad = 'CERO_KM' AND kilometraje = 0)
        OR
        (estado_unidad = 'USADO' AND kilometraje > 0)
    ),

    CONSTRAINT CK_Vehiculos_Estructura CHECK (
        (
            tipo_vehiculo IN ('AUTOMOVIL', 'CAMIONETA')
            AND carroceria IS NOT NULL
            AND LEN(LTRIM(RTRIM(carroceria))) > 0
            AND tipo_motocicleta IS NULL
        )
        OR
        (
            tipo_vehiculo = 'MOTOCICLETA'
            AND tipo_motocicleta IS NOT NULL
            AND LEN(LTRIM(RTRIM(tipo_motocicleta))) > 0
            AND carroceria IS NULL
        )
    )
);
GO