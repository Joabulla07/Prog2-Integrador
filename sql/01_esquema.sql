-- =============================================================================
-- SISTEMA DE GESTIÓN PARA CONCESIONARIA DE AUTOMÓVILES
-- BASE DE DATOS I - TUP - UTN FRRe
-- Script DDL: Creación de Base de Datos, Tablas, Claves Primarias y Foráneas
-- Motor: Microsoft SQL Server
-- =============================================================================

USE master;
GO

-- Crear la base de datos si no existe previamente
IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = 'concesionaria')
BEGIN
    CREATE DATABASE concesionaria;
END
GO

-- Conectar el contexto de ejecución a la base de datos
USE concesionaria;
GO

-- 1. TABLA: Marca
CREATE TABLE Marca (
    id_marca INT NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    pais_origen VARCHAR(100) NOT NULL,
    CONSTRAINT PK_Marca PRIMARY KEY (id_marca)
);
GO

-- 2. TABLA: Modelo
CREATE TABLE Modelo (
    id_modelo INT NOT NULL,
    denominacion VARCHAR(100) NOT NULL,
    version VARCHAR(50) NOT NULL,
    anio_fabricacion INT NOT NULL,
    precio_lista DECIMAL(14, 2) NOT NULL,
    esta_activo BIT NOT NULL DEFAULT 1,
    id_marca INT NOT NULL,
    CONSTRAINT PK_Modelo PRIMARY KEY (id_modelo),
    CONSTRAINT FK_Modelo_Marca FOREIGN KEY (id_marca)
        REFERENCES Marca(id_marca)
);
GO

-- 3. TABLA: Sucursal
CREATE TABLE Sucursal (
    Codigo INT NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    direccion VARCHAR(200) NOT NULL,
    telefono VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    CONSTRAINT PK_Sucursal PRIMARY KEY (Codigo)
);
GO

-- 4. TABLA: Empleado
CREATE TABLE Empleado (
    nro_legajo INT NOT NULL,
    email VARCHAR(100) NOT NULL,
    telefono VARCHAR(50) NOT NULL,
    tipo_empleado VARCHAR(50) NOT NULL, -- 'Vendedor', 'Administrativo'
    fcha_ingreso DATE NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    cod_sucursal INT NOT NULL,
    CONSTRAINT PK_Empleado PRIMARY KEY (nro_legajo),
    CONSTRAINT FK_Empleado_Sucursal FOREIGN KEY (cod_sucursal)
        REFERENCES Sucursal(Codigo)
);
GO

-- 5. TABLA: cliente
CREATE TABLE cliente (
    nro_documento VARCHAR(20) NOT NULL,
    tipo_doc VARCHAR(10) NOT NULL, -- 'DNI', 'Pasaporte', etc.
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    domicilio VARCHAR(200) NOT NULL,
    email VARCHAR(100) NOT NULL,
    CONSTRAINT PK_Cliente PRIMARY KEY (nro_documento)
);
GO

-- 6. TABLA: contacto_telefonico (Entidad débil dependiente de cliente)
CREATE TABLE contacto_telefonico (
    num_tel VARCHAR(30) NOT NULL,
    documento_cliente VARCHAR(20) NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    CONSTRAINT PK_Contacto_Telefonico PRIMARY KEY (num_tel, documento_cliente),
    CONSTRAINT FK_Contacto_Cliente FOREIGN KEY (documento_cliente)
        REFERENCES cliente(nro_documento)
        ON DELETE CASCADE
);
GO

-- 7. TABLA: proveedor
CREATE TABLE proveedor (
    cuit VARCHAR(20) NOT NULL,
    razon_social VARCHAR(150) NOT NULL,
    telefono VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    domicilio VARCHAR(200) NOT NULL,
    CONSTRAINT PK_Proveedor PRIMARY KEY (cuit)
);
GO

-- 8. TABLA: comp_adquisicion (Comprobantes de compra a terminales/fábricas)
CREATE TABLE comp_adquisicion (
    nro_comprobante VARCHAR(50) NOT NULL,
    fecha DATE NOT NULL,
    tipo_comprobante VARCHAR(20) NOT NULL,
    importe_total DECIMAL(14, 2) NOT NULL,
    cuit_proveedor VARCHAR(20) NOT NULL,
    CONSTRAINT PK_Comp_Adquisicion PRIMARY KEY (nro_comprobante),
    CONSTRAINT FK_CompAdquisicion_Proveedor FOREIGN KEY (cuit_proveedor)
        REFERENCES proveedor(cuit)
);
GO

-- 9. TABLA: Vehiculo
CREATE TABLE Vehiculo (
    nro_serie VARCHAR(50) NOT NULL, -- Número de serie / VIN
    estado VARCHAR(30) NOT NULL,    -- 'disponible', 'reservada', 'vendida', 'fuera de exhibición'
    condicion VARCHAR(20) NOT NULL, -- 'Nueva', 'Usada'
    precio_venta DECIMAL(14, 2) NOT NULL,
    fcha_ingreso DATE NOT NULL,
    color VARCHAR(50) NOT NULL,
    nro_motor VARCHAR(50) NOT NULL,
    cod_sucursal_asignada INT NOT NULL,
    comp_adquisicion VARCHAR(50) NULL, -- Admite NULL: si ingresa usado como parte de pago
    id_modelo INT NOT NULL,
    CONSTRAINT PK_Vehiculo PRIMARY KEY (nro_serie),
    CONSTRAINT FK_Vehiculo_Sucursal FOREIGN KEY (cod_sucursal_asignada)
        REFERENCES Sucursal(Codigo),
    CONSTRAINT FK_Vehiculo_CompAdquisicion FOREIGN KEY (comp_adquisicion)
        REFERENCES comp_adquisicion(nro_comprobante),
    CONSTRAINT FK_Vehiculo_Modelo FOREIGN KEY (id_modelo)
        REFERENCES Modelo(id_modelo)
);
GO

-- 10. TABLA: plan_financiamiento
CREATE TABLE plan_financiamiento (
    codigo INT NOT NULL,
    descripcion VARCHAR(200) NOT NULL,
    porcentaje_max_financ DECIMAL(5, 2) NOT NULL,
    cant_max_cuotas INT NOT NULL,
    tasa_interes_anual DECIMAL(5, 2) NOT NULL,
    CONSTRAINT PK_Plan_Financiamiento PRIMARY KEY (codigo)
);
GO

-- 11. TABLA: boleta_compra_venta
CREATE TABLE boleta_compra_venta (
    nro_boleta INT NOT NULL,
    estado VARCHAR(30) NOT NULL,     -- 'en negociación', 'confirmado', 'cancelado'
    fcha_emision DATE NOT NULL,
    importe_total DECIMAL(14, 2) NOT NULL,
    val_reconocido_parte_pago DECIMAL(14, 2) NULL,        -- NULL si no entrega auto usado
    nro_serie_vehiculo_parte_pago VARCHAR(50) NULL,       -- NULL si no entrega auto usado
    cod_plan_financiacion INT NULL,                       -- NULL si la venta es al contado
    cod_empleado INT NOT NULL,                            -- Vendedor que gestiona la operación
    doc_cliente VARCHAR(20) NOT NULL,
    nro_serie_vehiculo_venta VARCHAR(50) NOT NULL,        -- Unidad física comercializada
    CONSTRAINT PK_Boleta_Compra_Venta PRIMARY KEY (nro_boleta),
    CONSTRAINT FK_Boleta_Vehiculo_PartePago FOREIGN KEY (nro_serie_vehiculo_parte_pago)
        REFERENCES Vehiculo(nro_serie),
    CONSTRAINT FK_Boleta_PlanFinanciamiento FOREIGN KEY (cod_plan_financiacion)
        REFERENCES plan_financiamiento(codigo),
    CONSTRAINT FK_Boleta_Empleado FOREIGN KEY (cod_empleado)
        REFERENCES Empleado(nro_legajo),
    CONSTRAINT FK_Boleta_Cliente FOREIGN KEY (doc_cliente)
        REFERENCES cliente(nro_documento),
    CONSTRAINT FK_Boleta_Vehiculo_Venta FOREIGN KEY (nro_serie_vehiculo_venta)
        REFERENCES Vehiculo(nro_serie)
);
GO

-- 12. TABLA: Pago_Programado (Entidad débil dependiente de boleta_compra_venta)
CREATE TABLE Pago_Programado (
    nro_cuota INT NOT NULL,
    nro_boleta_compra_venta INT NOT NULL,
    fcha_venc DATE NOT NULL,
    importe DECIMAL(14, 2) NOT NULL,
    estado VARCHAR(30) NOT NULL,     -- 'pendiente', 'pagado'
    CONSTRAINT PK_Pago_Programado PRIMARY KEY (nro_cuota, nro_boleta_compra_venta),
    CONSTRAINT FK_PagoProgramado_Boleta FOREIGN KEY (nro_boleta_compra_venta)
        REFERENCES boleta_compra_venta(nro_boleta)
        ON DELETE CASCADE
);
GO

-- 13. TABLA: accesorio
CREATE TABLE accesorio (
    Codigo INT NOT NULL,
    descripcion VARCHAR(150) NOT NULL,
    precio DECIMAL(12, 2) NOT NULL,
    disponible BIT NOT NULL DEFAULT 1,
    CONSTRAINT PK_Accesorio PRIMARY KEY (Codigo)
);
GO

-- 14. TABLA: incluye_accesorio (Resolución de relación Muchos a Muchos N:M)
CREATE TABLE incluye_accesorio (
    nro_boleta INT NOT NULL,
    codigo_accesorio INT NOT NULL,
    cantidad INT NOT NULL DEFAULT 1,
    CONSTRAINT PK_Incluye_Accesorio PRIMARY KEY (nro_boleta, codigo_accesorio),
    CONSTRAINT FK_IncluyeAccesorio_Boleta FOREIGN KEY (nro_boleta)
        REFERENCES boleta_compra_venta(nro_boleta)
        ON DELETE CASCADE,
    CONSTRAINT FK_IncluyeAccesorio_Accesorio FOREIGN KEY (codigo_accesorio)
        REFERENCES accesorio(Codigo)
);
GO