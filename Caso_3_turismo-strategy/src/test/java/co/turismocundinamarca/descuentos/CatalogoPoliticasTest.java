package co.turismocundinamarca.descuentos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import org.junit.jupiter.api.Test;

class CatalogoPoliticasTest {

    private final CatalogoPoliticas catalogo = new CatalogoPoliticas();

    @Test
    void registrarYBuscar() {
        DescuentoPorcentual politica = new DescuentoPorcentual("A", 0.1);
        catalogo.registrar(politica);

        assertEquals(politica, catalogo.buscar("A").orElseThrow());
    }

    @Test
    void buscarInexistente_devuelveVacio() {
        assertTrue(catalogo.buscar("NO_EXISTE").isEmpty());
    }

    @Test
    void registrarConMismoCodigo_reemplaza() {
        catalogo.registrar(new DescuentoPorcentual("A", 0.1));
        DescuentoPorcentual nueva = new DescuentoPorcentual("A", 0.3);
        catalogo.registrar(nueva);

        assertEquals(nueva, catalogo.buscar("A").orElseThrow());
        assertEquals(1, catalogo.codigos().size());
    }

    @Test
    void retirar_indicaSiExistia() {
        catalogo.registrar(new DescuentoPorcentual("A", 0.1));

        assertTrue(catalogo.retirar("A"));
        assertFalse(catalogo.retirar("A"));
    }

    @Test
    void codigos_devuelveCopiaInmutable() {
        catalogo.registrar(new DescuentoPorcentual("A", 0.1));
        Set<String> codigos = catalogo.codigos();

        assertThrows(UnsupportedOperationException.class, () -> codigos.add("X"));
    }

    @Test
    void rechazaPoliticaNula() {
        assertThrows(NullPointerException.class, () -> catalogo.registrar(null));
    }
}
