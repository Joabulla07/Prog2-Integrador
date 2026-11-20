package concesionaria.model;

import concesionaria.model.enums.EstadoUnidad;
import concesionaria.model.enums.TipoVehiculo;

/**
 * Subclase que representa una camioneta en el sistema de concesionaria.
 * Extiende de Vehiculo y agrega el atributo específico de carrocería.
 * <p>
 * La carrocería se maneja como String libre validado para permitir
 * cualquier tipo de carrocería multimarca (Pick-up, SUV, Furgón, etc.).
 * La validación de que no sea vacío ocurre en la capa de servicio.
 * </p>
 */
public class Camioneta extends Vehiculo {

    private String carroceria;

    /**
     * Constructor público sin argumentos (JavaBean).
     * Crea una instancia con todos los atributos en null.
     */
    public Camioneta() {
        super();
    }

    /**
     * Constructor parametrizado completo que inicializa todos los atributos de la camioneta.
     *
     * El código de inventario se asigna después de persistir el vehículo.
     * @param marca marca del vehículo (ej. "Toyota")
     * @param modelo modelo del vehículo (ej. "Hilux")
     * @param anio año del vehículo (ej. 2026)
     * @param color color del vehículo (ej. "Blanco")
     * @param estado estado de la unidad (CERO_KM o USADO)
     * @param kilometraje kilometraje recorrido (null o 0 para 0 km, valor positivo para usados)
     * @param precioLista precio de lista del vehículo
     * @param carroceria tipo de carrocería (ej. "Pick-up Doble Cabina", "SUV", "Furgón")
     */
    public Camioneta(String marca, String modelo, Integer anio,
                      String color, EstadoUnidad estado, Integer kilometraje, Double precioLista,
                      String carroceria) {
        super(marca, modelo, anio, color, estado, kilometraje, precioLista);
        this.carroceria = carroceria;
    }

    /**
     * Obtiene el tipo de carrocería de la camioneta.
     *
     * @return tipo de carrocería (ej. "Pick-up Doble Cabina", "SUV", "Furgón")
     */
    public String getCarroceria() {
        return carroceria;
    }

    /**
     * Establece el tipo de carrocería de la camioneta.
     *
     * @param carroceria tipo de carrocería a establecer
     */
    public void setCarroceria(String carroceria) {
        this.carroceria = carroceria;
    }

    /**
     * Obtiene el tipo de vehículo específico para esta instancia.
     *
     * @return TipoVehiculo.CAMIONETA
     */
    @Override
    public TipoVehiculo obtenerTipoVehiculo() {
        return TipoVehiculo.CAMIONETA;
    }

    /**
     * Obtiene una descripción de la estructura de la camioneta.
     *
     * @return descripción con el tipo de carrocería (ej. "Camioneta con carrocería Pick-up Doble Cabina")
     */
    @Override
    public String obtenerTipoEstructura() {
        return "Camioneta con carrocería " + (carroceria != null ? carroceria : "[no especificada]");
    }

    /**
     * Obtiene la ficha directiva de la camioneta para reportes ejecutivos.
     *
     * @return resumen ejecutivo con marca, modelo, año, precio, estado y carrocería
     */
    @Override
    public String obtenerFichaDirectiva() {
        StringBuilder ficha = new StringBuilder();
        ficha.append("CAMIONETA - ");
        ficha.append(getMarca() != null ? getMarca() : "[sin marca]").append(" ");
        ficha.append(getModelo() != null ? getModelo() : "[sin modelo]").append(" ");
        ficha.append(getAnio() != null ? getAnio() : "[sin año]");
        ficha.append(" | Estado: ").append(getEstado() != null ? getEstado().getDescripcion() : "[sin estado]");
        ficha.append(" | Precio: $").append(getPrecioLista() != null ? getPrecioLista() : "[sin precio]");
        ficha.append(" | Carrocería: ").append(carroceria != null ? carroceria : "[sin especificar]");
        return ficha.toString();
    }

    /**
     * Devuelve una representación legible de la camioneta para debugging.
     * Encadena la representación de la superclase y agrega el tipo de carrocería.
     *
     * @return String con formato de super.toString() concatenado con carrocería
     */
    @Override
    public String toString() {
        String carroceriaVal = carroceria != null ? carroceria : "[sin carrocería]";
        return super.toString() + ", carrocería=" + carroceriaVal + "]";
    }
}
