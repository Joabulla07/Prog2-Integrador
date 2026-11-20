USE ConcesionariaTFI;
GO

TRUNCATE TABLE dbo.Vehiculos;
GO

INSERT INTO dbo.Vehiculos (

    tipo_vehiculo,
    marca,
    modelo,
    anio,
    color,
    estado_unidad,
    kilometraje,
    precio_lista,
    carroceria,
    tipo_motocicleta
)
VALUES
    -- Automóviles (carroceria NOT NULL, tipo_motocicleta NULL)
    ('AUTOMOVIL', 'Toyota', 'Corolla', 2026, 'Blanco Perlado', 'CERO_KM', 0, 28500000.00, 'Sedán', NULL),
    ('AUTOMOVIL', 'Volkswagen', 'Golf GTI', 2022, 'Rojo Tornado', 'USADO', 34200, 31900000.00, 'Hatchback', NULL),
    ('AUTOMOVIL', 'Peugeot', '208', 2026, 'Gris Artense', 'CERO_KM', 0, 23400000.00, 'Hatchback', NULL),
    ('AUTOMOVIL', 'Honda', 'Civic', 2021, 'Negro Cristal', 'USADO', 51800, 26750000.00, 'Sedán', NULL),

    -- Camionetas (carroceria NOT NULL, tipo_motocicleta NULL)
    ('CAMIONETA', 'Toyota', 'Hilux GR-S', 2026, 'Blanco', 'CERO_KM', 0, 58900000.00, 'Pick-up Doble Cabina', NULL),
    ('CAMIONETA', 'Ford', 'Ranger Limited', 2023, 'Azul Belice', 'USADO', 28500, 47200000.00, 'Pick-up Doble Cabina', NULL),
    ('CAMIONETA', 'Jeep', 'Compass Trailhawk', 2026, 'Verde Recon', 'CERO_KM', 0, 39800000.00, 'SUV', NULL),
    ('CAMIONETA', 'Chevrolet', 'Tracker', 2022, 'Plata Shark', 'USADO', 42100, 24600000.00, 'SUV Compacto', NULL),

    -- Motocicletas (tipo_motocicleta NOT NULL, carroceria NULL)
    ('MOTOCICLETA', 'Yamaha', 'MT-03', 2026, 'Azul Racing', 'CERO_KM', 0, 9450000.00, NULL, 'Naked'),
    ('MOTOCICLETA', 'Honda', 'Africa Twin', 2023, 'Tricolor HRC', 'USADO', 14600, 22800000.00, NULL, 'Trail / Adventure'),
    ('MOTOCICLETA', 'Royal Enfield', 'Interceptor 650', 2026, 'Naranja Canyon', 'CERO_KM', 0, 10800000.00, NULL, 'Clásica / Retro'),
    ('MOTOCICLETA', 'Kawasaki', 'Ninja 400', 2021, 'Verde Lima', 'USADO', 19300, 8900000.00, NULL, 'Deportiva');
GO

-- Verificación rápida
SELECT
    id_vehiculo,
    codigo_inventario,
    tipo_vehiculo,
    marca,
    modelo,
    anio,
    estado_unidad,
    kilometraje,
    precio_lista,
    carroceria,
    tipo_motocicleta
FROM dbo.Vehiculos
ORDER BY tipo_vehiculo, codigo_inventario;
GO