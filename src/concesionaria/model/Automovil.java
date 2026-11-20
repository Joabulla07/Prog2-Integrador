package concesionaria.model;

import concesionaria.model.enums.EstadoUnidad;
import concesionaria.model.enums.TipoVehiculo;

/**
 * Subclase que representa un automóvil en el sistema de concesionaria.
 * Extiende de Vehiculo y agrega el atributo específico de carrocería.
 * <p>
 * La carrocería se maneja como String libre validado para permitir
 * cualquier tipo de carrocería multimarca (Sedán, Hatchback, Coupé, etc.).
 * La validación de que no sea vacío ocurre en la capa de servicio.
 * </p>
 */
public class Automovil extends Vehiculo {

    private String carroceria;

    /**
     * Constructor público sin argumentos (JavaBean).
     * Crea una instancia con todos los atributos en null.
     */
    public Automovil() {
        super();
    }

    /**
     * Constructor parametrizado completo que inicializa todos los atributos del automóvil.
     *
     * @param codigoInventario código único de inventario (ej. "AUTO-001")
     * @param marca marca del vehículo (ej. "Toyota")
     * @param modelo modelo del vehículo (ej. "Corolla")
     * @param anio año del vehículo (ej. 2026)
     * @param color color del vehículo (ej. "Blanco")
     * @param estado estado de la unidad (CERO_KM o USADO)
     * @param kilometraje kilometraje recorrido (null o 0 para 0 km, valor positivo para usados)
     * @param precioLista precio de lista del vehículo
     * @param carroceria tipo de carrocería (ej. "Sedán", "Hatchback", "Coupé")
     */
    public Automovil(String codigoInventario, String marca, String modelo, Integer anio,
                      String color, EstadoUnidad estado, Integer kilometraje, Double precioLista,
                      String carroceria) {
        super(codigoInventario, marca, modelo, anio, color, estado, kilometraje, precioLista);
        this.carroceria = carroceria;
    }

    /**
     * Obtiene el tipo de carrocería del automóvil.
     *
     * @return tipo de carrocería (ej. "Sedán", "Hatchback", "Coupé")
     */
    public String getCarroceria() {
        return carroceria;
    }

    /**
     * Establece el tipo de carrocería del automóvil.
     *
     * @param carroceria tipo de carrocería a establecer
     */
    public void setCarroceria(String carroceria) {
        this.carroceria = carroceria;
    }

    /**
     * Obtiene el tipo de vehículo específico para esta instancia.
     *
     * @return TipoVehiculo.AUTOMOVIL
     */
    @Override
    public TipoVehiculo obtenerTipoVehiculo() {
        return TipoVehiculo.AUTOMOVIL;
    }

    /**
     * Obtiene una descripción de la estructura del automóvil.
     *
     * @return descripción con el tipo de carrocería (ej. "Automóvil con carrocería Sedán")
     */
    @Override
    public String obtenerTipoEstructura() {
        return "Automóvil con carrocería " + (carroceria != null ? carroceria : "[no especificada]");
    }

    /**
     * Obtiene la ficha directiva del automóvil para reportes ejecutivos.
     *
     * @return resumen ejecutivo con marca, modelo, año, precio, estado y carrocería
     */
    @Override
    public String obtenerFichaDirectiva() {
        StringBuilder ficha = new StringBuilder();
        ficha.append("AUTOMÓVIL - ");
        ficha.append(getMarca() != null ? getMarca() : "[sin marca]").append(" ");
        ficha.append(getModelo() != null ? getModelo() : "[sin modelo]").append(" ");
        ficha.append(getAnio() != null ? getAnio() : "[sin año]");
        ficha.append(" | Estado: ").append(getEstado() != null ? getEstado().getDescripcion() : "[sin estado]");
        ficha.append(" | Precio: $").append(getPrecioLista() != null ? getPrecioLista() : "[sin precio]");
        ficha.append(" | Carrocería: ").append(carroceria != null ? carroceria : "[sin especificar]");
        return ficha.toString();
    }
}
