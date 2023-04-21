package fr.umontpellier.iut.graphes;

import fr.umontpellier.iut.rails.Route;
import fr.umontpellier.iut.rails.data.Plateau;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class AreteProfTest {
    List<Route> routes;

    @BeforeEach
    void setUp() {
        Plateau plateau = Plateau.makePlateauMonde();
        routes = plateau.getRoutes();
    }

    @Test
    public void test_egalite_aretes() {
        Arete a1 = new Arete(0, 1, null);
        Arete a2 = new Arete(0, 1, null);
        Arete a3 = new Arete(0, 1, routes.get(0));
        Arete a4 = new Arete(0, 1, routes.get(0));
        Arete a5 = new Arete(1, 0, routes.get(0));
        Arete a6 = new Arete(0, 1, routes.get(0));
        Arete a7 = new Arete(0, 1, routes.get(1));
        assertEquals(a1, a2);
        assertNotEquals(a1, a3);
        assertEquals(a3, a4);
        assertEquals(a3, a5);
        assertEquals(a5,a6);
        assertNotEquals(a6,a7);
    }

}