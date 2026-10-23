-- =============================================================================
-- SISTEMA DE GESTIÓN PARA CONCESIONARIA DE AUTOMÓVILES
-- BASE DE DATOS I - TUP - UTN FRRe
-- Script DML: Inserción de Datos de Prueba (Mock Data)
-- Motor: Microsoft SQL Server
-- =============================================================================

USE concesionaria;
GO

BEGIN TRANSACTION;

-- -----------------------------------------------------------------------------
-- 1. TABLA: Marca
-- -----------------------------------------------------------------------------
INSERT INTO Marca (id_marca, nombre, pais_origen) VALUES
(1, 'Toyota', 'Japón'),
(2, 'Ford', 'Estados Unidos'),
(3, 'Volkswagen', 'Alemania'),
(4, 'Fiat', 'Italia'),
(5, 'Peugeot', 'Francia');

-- -----------------------------------------------------------------------------
-- 2. TABLA: Modelo
-- -----------------------------------------------------------------------------
INSERT INTO Modelo (id_modelo, denominacion, version, anio_fabricacion, precio_lista, esta_activo, id_marca) VALUES
(101, 'Hilux', 'SRX 4x4 AT', 2026, 68000000.00, 1, 1),
(102, 'Corolla', 'SEG Hybrid CVT', 2025, 41000000.00, 1, 1),
(103, 'Ranger', 'Limited V6 4WD', 2026, 74000000.00, 1, 2),
(104, 'Amarok', 'Highline V6 AT', 2025, 65000000.00, 1, 3),
(105, 'Cronos', 'Precision 1.3 CVT', 2026, 26000000.00, 1, 4),
(106, '208', 'Feline 1.6 Tiptronic', 2024, 27500000.00, 1, 5),
(107, 'Gol Trend', 'Comfortline 1.6', 2020, 15000000.00, 0, 3);

-- -----------------------------------------------------------------------------
-- 3. TABLA: Sucursal
-- -----------------------------------------------------------------------------
INSERT INTO Sucursal (Codigo, nombre, direccion, telefono, email) VALUES
(1, 'Casa Central Resistencia', 'Av. Sarmiento 1450', '3624441122', 'central@concesionaria.com'),
(2, 'Sucursal Corrientes Costanera', 'Av. 3 de Abril 920', '3794452233', 'corrientes@concesionaria.com'),
(3, 'Sucursal Presidencia Roque Sáenz Peña', 'Calle 12 esq. 7', '3644463344', 'saenzpena@concesionaria.com');

-- -----------------------------------------------------------------------------
-- 4. TABLA: Empleado
-- -----------------------------------------------------------------------------
INSERT INTO Empleado (nro_legajo, email, telefono, tipo_empleado, fcha_ingreso, nombre, apellido, cod_sucursal) VALUES
(1001, 'jperez@concesionaria.com', '3624111222', 'Vendedor', '2023-03-01', 'Juan', 'Pérez', 1),
(1002, 'mgonzalez@concesionaria.com', '3794333444', 'Vendedor', '2023-06-15', 'María', 'González', 2),
(1003, 'lbenitez@concesionaria.com', '3644555666', 'Vendedor', '2026-01-10', 'Lucas', 'Benítez', 3),
(1004, 'rdiaz@concesionaria.com', '3624777888', 'Administrativo', '2022-02-01', 'Romina', 'Díaz', 1),
(1005, 'cromero@concesionaria.com', '3794999000', 'Administrativo', '2024-05-10', 'Carlos', 'Romero', 2);

-- -----------------------------------------------------------------------------
-- 5. TABLA: cliente
-- -----------------------------------------------------------------------------
INSERT INTO cliente (nro_documento, tipo_doc, nombre, apellido, domicilio, email) VALUES
('20355441', 'DNI', 'Martín', 'Gómez', 'Av. Castelli 850, Resistencia', 'martin.gomez@gmail.com'),
('27488992', 'DNI', 'Laura', 'Fernández', 'Junín 1230, Corrientes', 'laura.fernandez@hotmail.com'),
('33655123', 'DNI', 'Esteban', 'Ramírez', 'Calle 5 Nº 430, Sáenz Peña', 'esteban.ramirez@yahoo.com.ar'),
('40123987', 'DNI', 'Nicolás', 'Giménez', 'Av. San Martín 210, Barranqueras', 'nico.gimenez@gmail.com');

-- -----------------------------------------------------------------------------
-- 6. TABLA: contacto_telefonico
-- -----------------------------------------------------------------------------
INSERT INTO contacto_telefonico (num_tel, documento_cliente, nombre) VALUES
('3624888999', '20355441', 'Martín Gómez (Personal)'),
('3624777111', '20355441', 'Ana Gómez (Hermana - Referencia)'),
('3794222333', '27488992', 'Laura Fernández (Móvil)'),
('3644999000', '33655123', 'Esteban Ramírez (Fijo)'),
('3624123456', '40123987', 'Nicolás Giménez (Personal)');

-- -----------------------------------------------------------------------------
-- 7. TABLA: proveedor
-- -----------------------------------------------------------------------------
INSERT INTO proveedor (cuit, razon_social, telefono, email, domicilio) VALUES
('30-70891234-8', 'Toyota Compañía Financiera & Distribución S.A.', '011-48901000', 'ventas@toyota.com.ar', 'Ruta 12 Km 80, Zárate, Bs As'),
('30-50221345-4', 'Ford Argentina S.C.A.', '011-47562000', 'distribuidores@ford.com.ar', 'Av. Henry Ford 300, Gral. Pacheco, Bs As'),
('30-61002456-9', 'Volkswagen Argentina S.A.', '011-43178000', 'corporativo@vw.com.ar', 'Av. de las Industrias 3100, Córdoba'),
('30-71458999-1', 'Comercializadora Automotriz del Litoral S.R.L.', '3624500600', 'contacto@litoralautos.com.ar', 'Ruta 16 Km 15.5, Fontana, Chaco');

-- -----------------------------------------------------------------------------
-- 8. TABLA: comp_adquisicion
-- -----------------------------------------------------------------------------
INSERT INTO comp_adquisicion (nro_comprobante, fecha, tipo_comprobante, importe_total, cuit_proveedor) VALUES
('ADQ-0001-00001234', '2026-01-15', 'Factura A', 135000000.00, '30-70891234-8'),
('ADQ-0001-00001235', '2026-02-10', 'Factura A', 74000000.00,  '30-50221345-4'),
('ADQ-0001-00001236', '2026-03-05', 'Factura A', 65000000.00,  '30-61002456-9');

-- -----------------------------------------------------------------------------
-- 9. TABLA: Vehiculo
-- -----------------------------------------------------------------------------
INSERT INTO Vehiculo (nro_serie, estado, condicion, precio_venta, fcha_ingreso, color, nro_motor, cod_sucursal_asignada, comp_adquisicion, id_modelo) VALUES
('8AJBA3CD7E100001', 'disponible', 'Nueva', 72000000.00, '2026-01-18', 'Blanco Perlado', '1GD-111111', 1, 'ADQ-0001-00001234', 101),
('8AJBA3CD7E100002', 'vendida',    'Nueva', 43000000.00, '2026-01-18', 'Gris Plata',    '2ZR-222222', 1, 'ADQ-0001-00001234', 102),
('8AFAA1FE5F200001', 'vendida',    'Nueva', 77000000.00, '2026-02-12', 'Azul Metal',    '30L-333333', 2, 'ADQ-0001-00001235', 103),
('8AWAA2CD9G300001', 'reservada',  'Nueva', 68000000.00, '2026-03-08', 'Negro Profundo', 'V6-444444',  1, 'ADQ-0001-00001236', 104),
('8AJBA3CD7E100003', 'vendida',    'Nueva', 44000000.00, '2026-01-18', 'Rojo Metalizado', '2ZR-777777', 1, 'ADQ-0001-00001234', 102),
('8AFZZZ21ZKA99901', 'vendida',    'Usada', 16500000.00, '2026-02-01', 'Gris Ceniza',    'CFW-555555', 1, NULL, 107),
('8APZZZ208PA88802', 'disponible', 'Usada', 25500000.00, '2026-03-01', 'Blanco Banquise', 'EC5-666666', 2, NULL, 106);

-- -----------------------------------------------------------------------------
-- 10. TABLA: plan_financiamiento
-- -----------------------------------------------------------------------------
INSERT INTO plan_financiamiento (codigo, descripcion, porcentaje_max_financ, cant_max_cuotas, tasa_interes_anual) VALUES
(1, 'Plan Tradicional 12 Cuotas Tasa Fija', 50.00, 12, 42.50),
(2, 'Plan 0km Litoral 24 Cuotas', 70.00, 24, 38.00),
(3, 'Plan Emprendedor 36 Cuotas UVA', 60.00, 36, 12.00);

-- -----------------------------------------------------------------------------
-- 11. TABLA: boleta_compra_venta
-- -----------------------------------------------------------------------------
INSERT INTO boleta_compra_venta (
    nro_boleta, estado, fcha_emision, importe_total,
    val_reconocido_parte_pago, nro_serie_vehiculo_parte_pago,
    cod_plan_financiacion, cod_empleado, doc_cliente, nro_serie_vehiculo_venta
) VALUES
(5001, 'confirmado', '2026-02-01', 43000000.00, 13800000.00, '8AFZZZ21ZKA99901', 1, 1001, '20355441', '8AJBA3CD7E100002'),
(5002, 'confirmado', '2026-03-01', 77000000.00, 22000000.00, '8APZZZ208PA88802', 2, 1002, '27488992', '8AFAA1FE5F200001'),
(5003, 'confirmado', '2026-03-25', 16500000.00, NULL, NULL, 1, 1001, '40123987', '8AFZZZ21ZKA99901'),
(5004, 'confirmado', '2026-04-10', 44000000.00, NULL, NULL, NULL, 1001, '20355441', '8AJBA3CD7E100003'),
(5005, 'cancelado',  '2026-04-15', 68000000.00, NULL, NULL, 2, 1002, '33655123', '8AWAA2CD9G300001');

-- -----------------------------------------------------------------------------
-- 12. TABLA: Pago_Programado
-- -----------------------------------------------------------------------------
INSERT INTO Pago_Programado (nro_cuota, nro_boleta_compra_venta, fcha_venc, importe, estado) VALUES
(1, 5001, '2026-03-05', 2800000.00, 'pagado'),
(2, 5001, '2026-04-05', 2800000.00, 'pagado'),
(3, 5001, '2026-05-05', 2800000.00, 'pendiente'),
(4, 5001, '2026-06-05', 2800000.00, 'pendiente'),
(1, 5002, '2026-04-05', 3150000.00, 'pagado'),
(2, 5002, '2026-05-05', 3150000.00, 'pendiente'),
(3, 5002, '2026-06-05', 3150000.00, 'pendiente'),
(1, 5003, '2026-04-25', 1450000.00, 'pendiente'),
(2, 5003, '2026-05-25', 1450000.00, 'pendiente');

-- -----------------------------------------------------------------------------
-- 13. TABLA: accesorio
-- -----------------------------------------------------------------------------
INSERT INTO accesorio (Codigo, descripcion, precio, disponible) VALUES
(1, 'Polarizado Antivandálico 200 micras', 280000.00, 1),
(2, 'Juego de Cubrealfombras Termoformadas', 95000.00, 1),
(3, 'Enganche para Tráiler reforzado con instalación eléctrica', 420000.00, 1),
(4, 'Lona Marítima rígida para caja de carga', 460000.00, 1),
(5, 'Tuercas de Seguridad antirrobo para llantas', 85000.00, 1),
(6, 'Kit de Seguridad Reglamentario (Matafuego + Balizas)', 65000.00, 1);

-- -----------------------------------------------------------------------------
-- 14. TABLA: incluye_accesorio
-- -----------------------------------------------------------------------------
INSERT INTO incluye_accesorio (nro_boleta, codigo_accesorio, cantidad) VALUES
(5001, 1, 1),
(5001, 2, 1),
(5002, 1, 1),
(5002, 3, 1),
(5002, 4, 1),
(5003, 5, 1),
(5003, 6, 1),
(5004, 1, 1);

COMMIT TRANSACTION;
GO