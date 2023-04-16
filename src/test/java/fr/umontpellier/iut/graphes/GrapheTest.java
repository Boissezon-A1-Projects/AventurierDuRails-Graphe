package fr.umontpellier.iut.graphes;


import fr.umontpellier.iut.rails.Route;
import fr.umontpellier.iut.rails.RouteMaritime;
import fr.umontpellier.iut.rails.data.Couleur;
import fr.umontpellier.iut.rails.data.Ville;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GrapheTest {
    private Graphe  graphe;

    @BeforeEach
    void setUp() {
        List<Arete> aretes = new ArrayList<>();
        aretes.add(new Arete(0, 1));
        aretes.add(new Arete(0, 3));
        aretes.add(new Arete(1, 2));
        aretes.add(new Arete(2, 3));
        aretes.add(new Arete(8, 42));
        graphe = new Graphe(aretes);
    }

    @Test
    void testNbAretes() {
        assertEquals(5, graphe.nbAretes());
    }


    @Test
    void testContientSommet() {
        assertTrue(graphe.contientSommet(0));
        assertTrue(graphe.contientSommet(1));
        assertTrue(graphe.contientSommet(2));
        assertTrue(graphe.contientSommet(3));
        assertTrue(graphe.contientSommet(8));
        assertTrue(graphe.contientSommet(42));
        assertFalse(graphe.contientSommet(7));
    }

    @Test
    void testAjouterSommet() {
        int nbSommets = graphe.nbSommets();
        graphe.ajouterSommet(59);
        assertTrue(graphe.contientSommet(59));
        assertEquals(nbSommets + 1, graphe.nbSommets());
        graphe.ajouterSommet(59);
        assertEquals(nbSommets + 1, graphe.nbSommets());
    }

    @Test
    void testAjouterArete() {
        int nbAretes = graphe.nbAretes();
        graphe.ajouterArete(new Arete(0, 3));
        assertEquals(nbAretes, graphe.nbAretes());
        graphe.ajouterArete(new Arete(9, 439, null));
        assertEquals(nbAretes + 1, graphe.nbAretes());
        graphe.ajouterArete(new Arete(0, 3, new RouteMaritime(new Ville("Athina", true), new Ville("Marseille", true), Couleur.ROUGE, 2) {
        }));
        assertEquals(nbAretes + 2, graphe.nbAretes());
    }

    @Test
    void testSupprimerArete() {
        int nbAretes = graphe.nbAretes();
        graphe.supprimerArete(new Arete(0, 3));
        assertEquals(nbAretes - 1, graphe.nbAretes());
        graphe.supprimerArete(new Arete(0, 3));
        assertEquals(nbAretes - 1, graphe.nbAretes());
        graphe.supprimerArete(new Arete(0, 3, null));
        assertEquals(nbAretes - 1, graphe.nbAretes());
    }

    @Test
    void testSupprimerSommet() {
        int nbSommets = graphe.nbSommets();
        int nbAretes = graphe.nbAretes();
        graphe.supprimerSommet(42);
        assertEquals(nbSommets - 1, graphe.nbSommets());
        assertEquals(nbAretes - 1, graphe.nbAretes());
        graphe.supprimerSommet(2);
        assertEquals(nbSommets - 2, graphe.nbSommets());
        assertEquals(nbAretes - 3, graphe.nbAretes());
    }

    @Test
    void testExisteArete() {
        assertTrue(graphe.existeArete(new Arete(0, 1)));
        assertTrue(graphe.existeArete(new Arete(0, 3)));
        assertTrue(graphe.existeArete(new Arete(1, 2)));
        assertTrue(graphe.existeArete(new Arete(2, 3)));
        assertTrue(graphe.existeArete(new Arete(8, 42)));
        assertFalse(graphe.existeArete(new Arete(0, 2)));
        assertFalse(graphe.existeArete(new Arete(0, 4)));
        assertFalse(graphe.existeArete(new Arete(1, 3)));
        assertFalse(graphe.existeArete(new Arete(2, 4)));
        assertFalse(graphe.existeArete(new Arete(8, 43)));
    }

    @Test
    void testGetVoisins() {
    }
}