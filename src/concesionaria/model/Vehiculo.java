package concesionaria.model;

import concesionaria.interfaces.EvaluatableComercial;
import concesionaria.interfaces.Identificable;
import concesionaria.model.enums.EstadoUnidad;
import concesionaria.model.enums.TipoVehiculo;

import java.io.Serializable;
import java.util.Objects;

/**
 * Clase abstracta base que representa un vehículo en el sistema de concesionaria.
 * Implementa los patrones JavaBean/POJO con atributos privados, constructores públicos,
 * getters/setters y uso de Wrappers para tipos numéricos.
 * <p>
 * Esta clase no puede instanciarse directamente; se debe usar una de sus subclases
 * concretas (Automovil, Camioneta, Motocicleta) que implementan el comportamiento
 * específico de cada tipo de vehículo.
 * </p>
 */
public abstract class Vehiculo implements Identificable, EvaluatableComercial, Serializable {

    private static final long serialVersionUID = 1L;

    private String codigoInventario;
    private String marca;
    private String modelo;
    private Integer anio;
    private String color;
    private EstadoUnidad estado;
    private Integer kilometraje;
    private Double precioLista;

    /**
     * Constructor público sin argumentos (JavaBean).
     * Crea una instancia con todos los atributos en null.
     * Este constructor es heredado por las subclases.
     */
    public Vehiculo() {
    }

    /**
     * Constructor parametrizado completo que inicializa todos los atributos del vehículo.
     *
     * @param codigoInventario código único de inventario (ej. "AUTO-001")
     * @param marca marca del vehículo (ej. "Toyota")
     * @param modelo modelo del vehículo (ej. "Corolla")
     * @param anio año del vehículo (ej. 2026)
     * @param color color del vehículo (ej. "Blanco")
     * @param estado estado de la unidad (CERO_KM o USADO)
     * @param kilometraje kilometraje recorrido (null o 0 para 0 km, valor positivo para usados)
     * @param precioLista precio de lista del vehículo
     */
    public Vehiculo(String codigoInventario, String marca, String modelo, Integer anio,
                    String color, EstadoUnidad estado, Integer kilometraje, Double precioLista) {
        this.codigoInventario = codigoInventario;
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.color = color;
        this.estado = estado;
        this.kilometraje = kilometraje;
        this.precioLista = precioLista;
    }

    /**
     * Obtiene el código de inventario único del vehículo.
     *
     * @return código de inventario
     */
    @Override
    public String getCodigoInventario() {
        return codigoInventario;
    }

    /**
     * Establece el código de inventario del vehículo.
     *
     * @param codigoInventario código de inventario a establecer
     */
    public void setCodigoInventario(String codigoInventario) {
        this.codigoInventario = codigoInventario;
    }

    /**
     * Obtiene la marca del vehículo.
     *
     * @return marca del vehículo
     */
    public String getMarca() {
        return marca;
    }

    /**
     * Establece la marca del vehículo.
     *
     * @param marca marca a establecer
     */
    public void setMarca(String marca) {
        this.marca = marca;
    }

    /**
     * Obtiene el modelo del vehículo.
     *
     * @return modelo del vehículo
     */
    public String getModelo() {
        return modelo;
    }

    /**
     * Establece el modelo del vehículo.
     *
     * @param modelo modelo a establecer
     */
    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    /**
     * Obtiene el año del vehículo.
     *
     * @return año del vehículo (puede ser null)
     */
    public Integer getAnio() {
        return anio;
    }

    /**
     * Establece el año del vehículo.
     *
     * @param anio año a establecer (puede ser null)
     */
    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    /**
     * Obtiene el color del vehículo.
     *
     * @return color del vehículo
     */
    public String getColor() {
        return color;
    }

    /**
     * Establece el color del vehículo.
     *
     * @param color color a establecer
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * Obtiene el estado de la unidad (0 km o Usado).
     *
     * @return estado de la unidad
     */
    public EstadoUnidad getEstado() {
        return estado;
    }

    /**
     * Establece el estado de la unidad.
     *
     * @param estado estado a establecer
     */
    public void setEstado(EstadoUnidad estado) {
        this.estado = estado;
    }

    /**
     * Obtiene el kilometraje recorrido del vehículo.
     *
     * @return kilometraje (null o 0 para 0 km, valor positivo para usados)
     */
    public Integer getKilometraje() {
        return kilometraje;
    }

    /**
     * Establece el kilometraje del vehículo.
     *
     * @param kilometraje kilometraje a establecer
     */
    public void setKilometraje(Integer kilometraje) {
        this.kilometraje = kilometraje;
    }

    /**
     * Obtiene el precio de lista del vehículo.
     *
     * @return precio de lista (puede ser null)
     */
    public Double getPrecioLista() {
        return precioLista;
    }

    /**
     * Establece el precio de lista del vehículo.
     *
     * @param precioLista precio a establecer (puede ser null)
     */
    public void setPrecioLista(Double precioLista) {
        this.precioLista = precioLista;
    }

    /**
     * Obtiene el tipo de vehículo específico de esta instancia.
     * Cada subclase debe implementar este método para devolver su TipoVehiculo correspondiente.
     *
     * @return tipo de vehículo (AUTOMOVIL, CAMIONETA o MOTOCICLETA)
     */
    public abstract TipoVehiculo obtenerTipoVehiculo();

    /**
     * Obtiene una descripción de la estructura del vehículo.
     * Cada subclase debe implementar este método para describir su estructura específica.
     *
     * @return descripción de la estructura (ej. "Automóvil con carrocería Sedán")
     */
    public abstract String obtenerTipoEstructura();

    /**
     * Obtiene la ficha directiva del vehículo para reportes ejecutivos.
     * Cada subclase debe implementar este método para incluir sus atributos específicos.
     *
     * @return resumen ejecutivo con atributos relevantes para directivos
     */
    public abstract String obtenerFichaDirectiva();

    /**
     * Obtiene la categoría comercial del vehículo.
     * La categoría combina el tipo de vehículo con un rango de precio:
     * - Económico: precio < 15.000.000
     * - Medio: precio entre 15.000.000 y 40.000.000
     * - Premium: precio > 40.000.000
     *
     * @return categoría comercial (ej. "Automóvil Premium 0 km", "Camioneta Media Usada")
     */
    @Override
    public String obtenerCategoriaComercial() {
        if (precioLista == null) {
            return obtenerTipoVehiculo().getDescripcion() + " [sin precio] " + (estado != null ? estado.getDescripcion() : "[sin estado]");
        }

        String rangoPrecio;
        if (precioLista < 15000000) {
            rangoPrecio = "Económico";
        } else if (precioLista <= 40000000) {
            rangoPrecio = "Medio";
        } else {
            rangoPrecio = "Premium";
        }

        return obtenerTipoVehiculo().getDescripcion() + " " + rangoPrecio + " " + (estado != null ? estado.getDescripcion() : "[sin estado]");
    }

    /**
     * Determina si el vehículo es apto para entrega inmediata.
     * - Vehículos 0 km: siempre aptos
     * - Vehículos usados: aptos si kilometraje < 50.000 km Y antigüedad <= 5 años
     *
     * @return true si es apto para entrega inmediata, false en caso contrario
     */
    @Override
    public boolean esAptoEntregaInmediata() {
        if (estado == null) {
            return false;
        }

        if (estado == EstadoUnidad.CERO_KM) {
            return true;
        }

        if (estado == EstadoUnidad.USADO) {
            boolean kilometrajeValido = kilometraje != null && kilometraje < 50000;
            boolean antiguedadValida = anio != null && (java.time.Year.now().getValue() - anio) <= 5;
            return kilometrajeValido && antiguedadValida;
        }

        return false;
    }

    /**
     * Compara esta instancia de Vehiculo con otra objeto basándose en el código de inventario.
     * La comparación normaliza el código de inventario (trim().toUpperCase()) para ignorar
     * diferencias de mayúsculas/minúsculas y espacios.
     *
     * @param o objeto a comparar
     * @return true si el objeto es un Vehiculo con el mismo codigoInventario (normalizado), false en caso contrario
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Vehiculo vehiculo = (Vehiculo) o;
        String thisCodigo = codigoInventario != null ? codigoInventario.trim().toUpperCase() : null;
        String otherCodigo = vehiculo.codigoInventario != null ? vehiculo.codigoInventario.trim().toUpperCase() : null;
        return Objects.equals(thisCodigo, otherCodigo);
    }

    /**
     * Calcula el código hash basándose en el código de inventario normalizado.
     * Es consistente con equals: dos objetos iguales tienen el mismo hashCode.
     *
     * @return código hash basado en codigoInventario
     */
    @Override
    public int hashCode() {
        String normalizedCodigo = codigoInventario != null ? codigoInventario.trim().toUpperCase() : null;
        return Objects.hash(normalizedCodigo);
    }

    /**
     * Devuelve una representación legible del vehículo para debugging.
     * Incluye tipo de vehículo, marca, modelo, año y código de inventario.
     *
     * @return String con formato "Vehiculo[tipo=..., marca=..., modelo=..., anio=..., codigo=...]"
     */
    @Override
    public String toString() {
        String tipo = obtenerTipoVehiculo() != null ? obtenerTipoVehiculo().getDescripcion() : "[sin tipo]";
        String marcaVal = marca != null ? marca : "[sin marca]";
        String modeloVal = modelo != null ? modelo : "[sin modelo]";
        String anioVal = anio != null ? anio.toString() : "[sin año]";
        String codigoVal = codigoInventario != null ? codigoInventario : "[sin código]";
        return "Vehiculo[tipo=" + tipo + ", marca=" + marcaVal + ", modelo=" + modeloVal + ", anio=" + anioVal + ", codigo=" + codigoVal + "]";
    }
}
