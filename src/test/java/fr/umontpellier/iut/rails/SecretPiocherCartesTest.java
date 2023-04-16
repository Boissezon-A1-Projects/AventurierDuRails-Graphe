package fr.umontpellier.iut.rails;

import fr.umontpellier.iut.rails.data.CarteTransport;
import fr.umontpellier.iut.rails.data.Couleur;
import fr.umontpellier.iut.rails.data.TypeCarteTransport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SecretPiocherCartesTest extends BaseTestClass {
    @BeforeEach
    public void setUp() {
        setUpJeu(2);
        initialisation();
    }

    @Test
    void testPiocherVisibleEnWagonPuisVisibleEnBateau() {
        CarteTransport c1 = cartesTransportVisibles.get(0);
        CarteTransport c2 = cartesTransportVisibles.get(1);
        CarteTransport c3 = cartesTransportVisibles.get(2);
        CarteTransport c4 = cartesTransportVisibles.get(3);
        CarteTransport c5 = cartesTransportVisibles.get(4);
        CarteTransport c6 = cartesTransportVisibles.get(5);

        CarteTransport top1 = piocheWagon.get(0);
        CarteTransport top2 = piocheBateau.get(0);

        jeu.setInput(
            c2.getNom(),
            "WAGON",
            c5.getNom(),
            "BATEAU"
        );
        joueur2.jouerTour();

        TestUtils.assertContainsExactly(cartesTransportVisibles, c1, c3, c4, c6, top1, top2);
        TestUtils.assertContainsExactly(cartesJoueur2, c2, c5);
    }

    @Test
    void testPiocherVisibleEnBateauPuisVisibleEnBateau() {
        CarteTransport c1 = cartesTransportVisibles.get(0);
        CarteTransport c2 = cartesTransportVisibles.get(1);
        CarteTransport c3 = cartesTransportVisibles.get(2);
        CarteTransport c4 = cartesTransportVisibles.get(3);
        CarteTransport c5 = cartesTransportVisibles.get(4);
        CarteTransport c6 = cartesTransportVisibles.get(5);

        CarteTransport top1 = piocheBateau.get(0);
        CarteTransport top2 = piocheBateau.get(1);

        jeu.setInput(
            c2.getNom(),
            "BATEAU",
            c5.getNom(),
            "BATEAU"
        );
        joueur2.jouerTour();

        TestUtils.assertContainsExactly(cartesTransportVisibles, c1, c3, c4, c6, top1, top2);
        TestUtils.assertContainsExactly(cartesJoueur2, c2, c5);
    }

    @Test
    void testPiocherVisibleEnWagonPuisPasse() {
        CarteTransport c1 = cartesTransportVisibles.get(0);
        CarteTransport c2 = cartesTransportVisibles.get(1);
        CarteTransport c3 = cartesTransportVisibles.get(2);
        CarteTransport c4 = cartesTransportVisibles.get(3);
        CarteTransport c5 = cartesTransportVisibles.get(4);
        CarteTransport c6 = cartesTransportVisibles.get(5);

        CarteTransport top1 = piocheWagon.get(0);

        jeu.setInput(
            c4.getNom(),
            "WAGON",
            ""
        );
        joueur2.jouerTour();

        TestUtils.assertContainsExactly(cartesTransportVisibles, c1, c2, c3, c5, c6, top1);
        TestUtils.assertContainsExactly(cartesJoueur2, c4);
    }


    @Test
    void testPiocherVisibleEnBateauPuisBateau() {
        CarteTransport c1 = cartesTransportVisibles.get(0);
        CarteTransport c2 = cartesTransportVisibles.get(1);
        CarteTransport c3 = cartesTransportVisibles.get(2);
        CarteTransport c4 = cartesTransportVisibles.get(3);
        CarteTransport c5 = cartesTransportVisibles.get(4);
        CarteTransport c6 = cartesTransportVisibles.get(5);

        CarteTransport top1 = piocheBateau.get(0);
        CarteTransport top2 = piocheBateau.get(1);

        jeu.setInput(
            c1.getNom(),
            "BATEAU",
            "BATEAU"
        );
        joueur2.jouerTour();

        TestUtils.assertContainsExactly(cartesTransportVisibles, c2, c3, c4, c5, c6, top1);
        TestUtils.assertContainsExactly(cartesJoueur2, c1, top2);
    }

    @Test
    void testPiocherBateauPuisWagon() {
        CarteTransport c1 = cartesTransportVisibles.get(0);
        CarteTransport c2 = cartesTransportVisibles.get(1);
        CarteTransport c3 = cartesTransportVisibles.get(2);
        CarteTransport c4 = cartesTransportVisibles.get(3);
        CarteTransport c5 = cartesTransportVisibles.get(4);
        CarteTransport c6 = cartesTransportVisibles.get(5);

        CarteTransport top1 = piocheBateau.get(0);
        CarteTransport top2 = piocheWagon.get(0);

        jeu.setInput(
            "BATEAU",
            "WAGON"
        );
        joueur2.jouerTour();

        TestUtils.assertContainsExactly(cartesTransportVisibles, c1, c2, c3, c4, c5, c6);
        TestUtils.assertContainsExactly(cartesJoueur2, top1, top2);
    }

    @Test
    void testPiocherJokerVisibleEnBateau() {
        CarteTransport c1 = cartesTransportVisibles.get(0);
        CarteTransport c2 = cartesTransportVisibles.get(1);
        CarteTransport c3 = new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true);
        cartesTransportVisibles.remove(2);
        cartesTransportVisibles.add(2, c3);
        CarteTransport c4 = cartesTransportVisibles.get(3);
        CarteTransport c5 = cartesTransportVisibles.get(4);
        CarteTransport c6 = cartesTransportVisibles.get(5);

        CarteTransport top1 = piocheBateau.get(0);

        jeu.setInput(
            c3.getNom(),
            "BATEAU",
            "BATEAU" // ignoré
        );
        joueur2.jouerTour();

        TestUtils.assertContainsExactly(cartesTransportVisibles, c1, c2, c4, c5, c6, top1);
        TestUtils.assertContainsExactly(cartesJoueur2, c3);
    }

    @Test
    void testBateauPuisJokerVisibleIgnorePuisVisibleEnWagon() {
        CarteTransport c1 = cartesTransportVisibles.get(0);
        CarteTransport c2 = cartesTransportVisibles.get(1);
        CarteTransport c3 = new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true);
        cartesTransportVisibles.remove(2);
        cartesTransportVisibles.add(2, c3);
        CarteTransport c4 = cartesTransportVisibles.get(3);
        CarteTransport c5 = cartesTransportVisibles.get(4);
        CarteTransport c6 = cartesTransportVisibles.get(5);

        CarteTransport top1 = piocheBateau.get(0);
        CarteTransport top2 = piocheWagon.get(0);

        jeu.setInput(
            "BATEAU",
            c3.getNom(), // ignoré
            c6.getNom(),
            "WAGON"
        );
        joueur2.jouerTour();

        TestUtils.assertContainsExactly(cartesTransportVisibles, c1, c2, c3, c4, c5, top2);
        TestUtils.assertContainsExactly(cartesJoueur2, top1, c6);
    }

    @Test
    void testPiocherJokerCachePuisJokerCache() {
        CarteTransport c1 = cartesTransportVisibles.get(0);
        CarteTransport c2 = cartesTransportVisibles.get(1);
        CarteTransport c3 = cartesTransportVisibles.get(2);
        CarteTransport c4 = cartesTransportVisibles.get(3);
        CarteTransport c5 = cartesTransportVisibles.get(4);
        CarteTransport c6 = cartesTransportVisibles.get(5);

        CarteTransport top1 = new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true);
        CarteTransport top2 = new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true);
        piocheWagon.remove(0);
        piocheWagon.remove(0);
        piocheWagon.add(0, top2);
        piocheWagon.add(0, top1);

        jeu.setInput(
            "WAGON",
            "WAGON"
        );
        joueur2.jouerTour();

        TestUtils.assertContainsExactly(cartesTransportVisibles, c1, c2, c3, c4, c5, c6);
        TestUtils.assertContainsExactly(cartesJoueur2, top1, top2);
    }

    @AfterEach
    void testIntegrite() {
        TestUtils.testIntegrite(jeu);
    }

}
