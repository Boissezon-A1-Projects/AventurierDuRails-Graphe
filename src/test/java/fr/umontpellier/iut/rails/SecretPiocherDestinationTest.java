package fr.umontpellier.iut.rails;

import fr.umontpellier.iut.rails.data.CarteTransport;
import fr.umontpellier.iut.rails.data.Destination;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SecretPiocherDestinationTest extends BaseTestClass {

    @BeforeEach
    public void setUp() {
        setUpJeu(4);
        initialisation();

        pileDestinations.addAll(destinationsJoueur1);
        pileDestinations.addAll(destinationsJoueur2);
        destinationsJoueur1.clear();
        destinationsJoueur2.clear();
    }

    @Test
    void testPrendreDestinationsDefausseAucune() {
        Destination d1 = pileDestinations.get(0);
        Destination d2 = pileDestinations.get(1);
        Destination d3 = pileDestinations.get(2);
        Destination d4 = pileDestinations.get(3);

        jeu.setInput(
                "DESTINATION",
                "");
        joueur2.jouerTour();

        TestUtils.assertContainsExactly(destinationsJoueur2, d1, d2, d3, d4);
        assertFalse(pileDestinations.contains(d1));
        assertFalse(pileDestinations.contains(d2));
        assertFalse(pileDestinations.contains(d3));
        assertFalse(pileDestinations.contains(d4));
    }

    @Test
    void testPrendreDestinationsDefausseMaximum() {
        Destination d1 = pileDestinations.get(0);
        Destination d2 = pileDestinations.get(1);
        Destination d3 = pileDestinations.get(2);
        Destination d4 = pileDestinations.get(3);

        jeu.setInput(
                "DESTINATION",
                TestUtils.getNom(d2),
                TestUtils.getNom(d3),
                TestUtils.getNom(d4));
        joueur2.jouerTour();

        TestUtils.assertContainsExactly(destinationsJoueur2, d1);
        assertFalse(pileDestinations.contains(d1));
        assertTrue(pileDestinations.contains(d2));
        assertTrue(pileDestinations.contains(d3));
        assertTrue(pileDestinations.contains(d4));
    }

    @Test
    void testPrendreDestinationsDefausse2() {
        Destination d1 = pileDestinations.get(0);
        Destination d2 = pileDestinations.get(1);
        Destination d3 = pileDestinations.get(2);
        Destination d4 = pileDestinations.get(3);

        jeu.setInput(
                "DESTINATION",
                TestUtils.getNom(d3),
                TestUtils.getNom(d4),
                "");
        joueur2.jouerTour();

        TestUtils.assertContainsExactly(destinationsJoueur2, d1, d2);
        assertFalse(pileDestinations.contains(d1));
        assertFalse(pileDestinations.contains(d2));
        assertTrue(pileDestinations.contains(d3));
        assertTrue(pileDestinations.contains(d4));
    }

    @Test
    void testPrendreDestinationsPileVide() {
        destinationsJoueur1.addAll(pileDestinations);
        pileDestinations.clear();
        
        cartesJoueur2.clear();
        CarteTransport c1 = piocheWagon.get(0);
        CarteTransport c2 = piocheWagon.get(1);

        jeu.setInput(
                "DESTINATION",
                "WAGON",
                "WAGON");
        joueur2.jouerTour();

        assertTrue(destinationsJoueur2.isEmpty());
        assertTrue(pileDestinations.isEmpty());
        TestUtils.assertContainsExactly(cartesJoueur2, c1, c2);
    }

    @Test
    void testPrendreDestinationsPileContient3DestinationsDefausseAucune() {
        destinationsJoueur2.clear();
        while (pileDestinations.size() > 3) {
            destinationsJoueur1.add(pileDestinations.remove(0));
        }
        Destination d1 = pileDestinations.get(0);
        Destination d2 = pileDestinations.get(1);
        Destination d3 = pileDestinations.get(2);

        jeu.setInput(
                "DESTINATION",
                "");
        joueur2.jouerTour();

        TestUtils.assertContainsExactly(destinationsJoueur2, d1, d2, d3);
        assertTrue(pileDestinations.isEmpty());
    }

    @Test
    void testPrendreDestinationsPileContient3DestinationsDefausseUne() {
        while (pileDestinations.size() > 3) {
            destinationsJoueur1.add(pileDestinations.remove(0));
        }
        Destination d1 = pileDestinations.get(0);
        Destination d2 = pileDestinations.get(1);
        Destination d3 = pileDestinations.get(2);

        jeu.setInput(
                "DESTINATION",
                TestUtils.getNom(d1),
                "");
        joueur2.jouerTour();

        TestUtils.assertContainsExactly(destinationsJoueur2, d2, d3);
        TestUtils.assertContainsExactly(pileDestinations, d1);
    }

    @Test
    void testPrendreDestinationsPileContient2DestinationsDefausseAucune() {
        destinationsJoueur2.clear();
        while (pileDestinations.size() > 2) {
            destinationsJoueur1.add(pileDestinations.remove(0));
        }
        Destination d1 = pileDestinations.get(0);
        Destination d2 = pileDestinations.get(1);

        jeu.setInput(
                "DESTINATION",
                "");
        joueur2.jouerTour();

        TestUtils.assertContainsExactly(destinationsJoueur2, d1, d2);
        assertTrue(pileDestinations.isEmpty());
    }

    @Test
    void testPrendreDestinationsPileContient1DestinationDefausseAucune() {
        destinationsJoueur2.clear();
        while (pileDestinations.size() > 1) {
            destinationsJoueur1.add(pileDestinations.remove(0));
        }
        Destination d1 = pileDestinations.get(0);

        jeu.setInput(
                "DESTINATION",
                "");
        joueur2.jouerTour();

        TestUtils.assertContainsExactly(destinationsJoueur2, d1);
        assertTrue(pileDestinations.isEmpty());
    }

    @Test
    void testPrendreDestinationsPileContient4DestinationsDefausseAucune() {
        destinationsJoueur2.clear();
        while (pileDestinations.size() > 4) {
            destinationsJoueur1.add(pileDestinations.remove(0));
        }
        Destination d1 = pileDestinations.get(0);
        Destination d2 = pileDestinations.get(1);
        Destination d3 = pileDestinations.get(2);
        Destination d4 = pileDestinations.get(3);

        jeu.setInput(
                "DESTINATION",
                "");
        joueur2.jouerTour();

        TestUtils.assertContainsExactly(destinationsJoueur2, d1, d2, d3, d4);
        assertTrue(pileDestinations.isEmpty());
    }

    @AfterEach
    void testIntegrite() {
        TestUtils.testIntegrite(jeu);
    }

}
