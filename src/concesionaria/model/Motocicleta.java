package concesionaria.model;

import concesionaria.model.enums.EstadoUnidad;
import concesionaria.model.enums.TipoVehiculo;

/**
 * Subclase que representa una motocicleta en el sistema de concesionaria.
 * Extiende de Vehiculo y agrega el atributo específico de tipo de motocicleta.
 * <p>
 * El tipo de motocicleta se maneja como String libre validado para permitir
 * cualquier tipo (Naked, Enduro, Scooter, Custom, etc.).
 * La validación de que no sea vacío ocurre en la capa de servicio.
 * </p>
 */
public class Motocicleta extends Vehiculo {

    private String tipoMotocicleta;

    /**
     * Constructor público sin argumentos (JavaBean).
     * Crea una instancia con todos los atributos en null.
     */
    public Motocicleta() {
        super();
    }

    /**
     * Constructor parametrizado completo que inicializa todos los atributos de la motocicleta.
     *
     * El código de inventario se asigna después de persistir el vehículo.
     * @param marca marca del vehículo (ej. "Honda")
     * @param modelo modelo del vehículo (ej. "CBR 600")
     * @param anio año del vehículo (ej. 2026)
     * @param color color del vehículo (ej. "Rojo")
     * @param estado estado de la unidad (CERO_KM o USADO)
     * @param kilometraje kilometraje recorrido (null o 0 para 0 km, valor positivo para usados)
     * @param precioLista precio de lista del vehículo
     * @param tipoMotocicleta tipo de motocicleta (ej. "Naked", "Enduro", "Scooter", "Custom")
     */
    public Motocicleta(String marca, String modelo, Integer anio,
                        String color, EstadoUnidad estado, Integer kilometraje, Double precioLista,
                        String tipoMotocicleta) {
        super(marca, modelo, anio, color, estado, kilometraje, precioLista);
        this.tipoMotocicleta = tipoMotocicleta;
    }

    /**
     * Obtiene el tipo de motocicleta.
     *
     * @return tipo de motocicleta (ej. "Naked", "Enduro", "Scooter", "Custom")
     */
    public String getTipoMotocicleta() {
        return tipoMotocicleta;
    }

    /**
     * Establece el tipo de motocicleta.
     *
     * @param tipoMotocicleta tipo de motocicleta a establecer
     */
    public void setTipoMotocicleta(String tipoMotocicleta) {
        this.tipoMotocicleta = tipoMotocicleta;
    }

    /**
     * Obtiene el tipo de vehículo específico para esta instancia.
     *
     * @return TipoVehiculo.MOTOCICLETA
     */
    @Override
    public TipoVehiculo obtenerTipoVehiculo() {
        return TipoVehiculo.MOTOCICLETA;
    }

    /**
     * Obtiene una descripción de la estructura de la motocicleta.
     *
     * @return descripción con el tipo de motocicleta (ej. "Motocicleta tipo Naked")
     */
    @Override
    public String obtenerTipoEstructura() {
        return "Motocicleta tipo " + (tipoMotocicleta != null ? tipoMotocicleta : "[no especificado]");
    }

    /**
     * Obtiene la ficha directiva de la motocicleta para reportes ejecutivos.
     *
     * @return resumen ejecutivo con marca, modelo, año, precio, estado y tipo de motocicleta
     */
    @Override
    public String obtenerFichaDirectiva() {
        StringBuilder ficha = new StringBuilder();
        ficha.append("MOTOCICLETA - ");
        ficha.append(getMarca() != null ? getMarca() : "[sin marca]").append(" ");
        ficha.append(getModelo() != null ? getModelo() : "[sin modelo]").append(" ");
        ficha.append(getAnio() != null ? getAnio() : "[sin año]");
        ficha.append(" | Estado: ").append(getEstado() != null ? getEstado().getDescripcion() : "[sin estado]");
        ficha.append(" | Precio: $").append(getPrecioLista() != null ? getPrecioLista() : "[sin precio]");
        ficha.append(" | Código: ").append(getCodigoInventario() != null ? getCodigoInventario() : "[sin código]");
        ficha.append(" | Tipo: ").append(tipoMotocicleta != null ? tipoMotocicleta : "[sin especificar]");
        return ficha.toString();
    }

    /**
     * Devuelve una representación legible de la motocicleta para debugging.
     * Encadena la representación de la superclase y agrega el tipo de motocicleta.
     *
     * @return String con formato de super.toString() concatenado con tipoMotocicleta
     */
    @Override
    public String toString() {
        String tipoVal = tipoMotocicleta != null ? tipoMotocicleta : "[sin tipo]";
        return super.toString() + ", tipo=" + tipoVal + "]";
    }
}
