package concesionaria.repository;

import concesionaria.model.Automovil;
import concesionaria.model.Camioneta;
import concesionaria.model.enums.EstadoUnidad;

import java.lang.reflect.Method;

public class TestIndiceGenerico {
    public static void main(String[] args) {
        try {
            System.out.println("=== Test US-10: IndiceGenerico (Generics + Map) ===");

            IndiceGenerico<String, Automovil> indiceAuto = new IndiceGenerico<>();

            Automovil a1 = new Automovil("Toyota", "Corolla", 2026, "Blanco", EstadoUnidad.CERO_KM, 0, 25000000.0, "Sedán");
            Automovil a2 = new Automovil("Honda", "Civic", 2024, "Negro", EstadoUnidad.USADO, 15000, 28000000.0, "Sedán");

            Method assignMethod = Automovil.class.getMethod("asignarCodigoInventario", String.class);
            assignMethod.setAccessible(true);
            assignMethod.invoke(a1, "AUTO-0001");
            assignMethod.invoke(a2, "AUTO-0002");

            indiceAuto.indexar(a1.getCodigoInventario(), a1);
            indiceAuto.indexar(a2.getCodigoInventario(), a2);

            if (indiceAuto.tamano() != 2) {
                throw new AssertionError("Tamaño de IndiceGenerico incorrecto");
            }

            if (!indiceAuto.contieneClave("AUTO-0001")) {
                throw new AssertionError("IndiceGenerico no contiene la clave 'AUTO-0001'");
            }

            Automovil recuperado = indiceAuto.buscarPorClave("AUTO-0001");
            if (recuperado == null || !"Toyota".equals(recuperado.getMarca())) {
                throw new AssertionError("Búsqueda por clave devolvió objeto incorrecto");
            }

            indiceAuto.desindexar("AUTO-0001");
            if (indiceAuto.contieneClave("AUTO-0001") || indiceAuto.tamano() != 1) {
                throw new AssertionError("Desindexar no eliminó la entrada correctamente");
            }

            System.out.println("Tests de IndiceGenerico pasaron exitosamente.");
        } catch (Exception e) {
            System.err.println("Error en TestIndiceGenerico: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
