package fr.umontpellier.iut.rails;

import fr.umontpellier.iut.rails.data.CarteTransport;
import fr.umontpellier.iut.rails.data.TypeCarteTransport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SecretCasSpeciauxTest extends BaseTestClass {

    @Test
    public void testPreparation3Jokers1Fois() {
        int nbJoueurs = 2;
        setUpJeu(nbJoueurs);
        List<String> instructions = new ArrayList<String>();
        for (int i = 0; i < nbJoueurs; i++) {
            instructions.add("");
            instructions.add("20");
        }
        jeu.setInput(instructions);

        remonterCartePiocheWagon("C6"); // wagon noir
        remonterCartePiocheWagon("C5"); // wagon noir
        remonterCartePiocheWagon("C4"); // wagon noir
        remonterCartePiocheWagon("C132"); // joker
        remonterCartePiocheWagon("C131"); // joker
        remonterCartePiocheWagon("C130"); // joker
        remonterCartePiocheWagon("C3"); // wagon noir
        remonterCartePiocheWagon("C2"); // wagon noir
        remonterCartePiocheWagon("C1"); // wagon noir
        remonterCartePiocheWagon("C129"); // joker
        remonterCartePiocheWagon("C128"); // joker
        remonterCartePiocheWagon("C127"); // joker (haut de pile)

        try {
            jeu.run();
        } catch (IndexOutOfBoundsException e) {
        }

        for (int i = 0; i < nbJoueurs; i++) {
            int nbWagons = 0;
            int nbBateaux = 0;
            for (CarteTransport c : cartesJoueur1) {
                if (c.getType() == TypeCarteTransport.BATEAU) {
                    nbBateaux++;
                } else {
                    nbWagons++;
                }
            }
            assertEquals(3, nbWagons);
            assertEquals(7, nbBateaux);
        }
        assertEquals(80 - 3 * nbJoueurs - 3, piocheWagon.size() + defausseWagon.size());
        assertEquals(60 - 7 * nbJoueurs - 3, piocheBateau.size() + defausseBateau.size());
        assertEquals(3, defausseWagon.size());
        assertEquals(3, defausseBateau.size());
    }

    @Test
    public void testPreparation3Jokers2Fois() {
        int nbJoueurs = 3;
        setUpJeu(nbJoueurs);
        List<String> instructions = new ArrayList<String>();
        for (int i = 0; i < nbJoueurs; i++) {
            instructions.add("");
            instructions.add("20");
        }
        jeu.setInput(instructions);

        remonterCartePiocheWagon("C6"); // wagon noir
        remonterCartePiocheWagon("C5"); // wagon noir
        remonterCartePiocheWagon("C4"); // wagon noir
        remonterCartePiocheWagon("C138"); // joker
        remonterCartePiocheWagon("C137"); // joker
        remonterCartePiocheWagon("C136"); // joker
        remonterCartePiocheWagon("C135"); // joker
        remonterCartePiocheWagon("C134"); // joker
        remonterCartePiocheWagon("C133"); // joker
        remonterCartePiocheWagon("C3"); // wagon noir
        remonterCartePiocheWagon("C2"); // wagon noir
        remonterCartePiocheWagon("C1"); // wagon noir
        remonterCartePiocheWagon("C132"); // joker
        remonterCartePiocheWagon("C131"); // joker
        remonterCartePiocheWagon("C130"); // joker
        remonterCartePiocheWagon("C129"); // joker
        remonterCartePiocheWagon("C128"); // joker
        remonterCartePiocheWagon("C127"); // joker (haut de pile)

        try {
            jeu.run();
        } catch (IndexOutOfBoundsException e) {
        }

        for (int i = 0; i < nbJoueurs; i++) {
            int nbWagons = 0;
            int nbBateaux = 0;
            for (CarteTransport c : cartesJoueur1) {
                if (c.getType() == TypeCarteTransport.BATEAU) {
                    nbBateaux++;
                } else {
                    nbWagons++;
                }
            }
            assertEquals(3, nbWagons);
            assertEquals(7, nbBateaux);
        }
        assertEquals(80 - 3 * nbJoueurs - 3, piocheWagon.size() + defausseWagon.size());
        assertEquals(60 - 7 * nbJoueurs - 3, piocheBateau.size() + defausseBateau.size());
        assertEquals(6, defausseWagon.size());
        assertEquals(6, defausseBateau.size());
    }

    @Test
    void testPiocherPileBateauVide() {
        setUpJeu(2);
        initialisation();

        CarteTransport c1 = cartesTransportVisibles.get(0);
        CarteTransport c2 = cartesTransportVisibles.get(1);
        CarteTransport c3 = cartesTransportVisibles.get(2);
        CarteTransport c4 = cartesTransportVisibles.get(3);
        CarteTransport c5 = cartesTransportVisibles.get(4);
        CarteTransport c6 = cartesTransportVisibles.get(5);

        CarteTransport top1 = piocheWagon.get(0);
        CarteTransport top2 = piocheWagon.get(1);
        cartesJoueur1.addAll(piocheBateau);
        piocheBateau.clear();

        jeu.setInput(
                "BATEAU", // ignoré
                c1.getNom(),
                "BATEAU", // ignoré
                "WAGON",
                c2.getNom(),
                "BATEAU",
                "WAGON");
        joueur2.jouerTour();

        TestUtils.assertContainsExactly(cartesTransportVisibles, c3, c4, c5, c6, top1, top2);
        TestUtils.assertContainsExactly(cartesJoueur2, c1, c2);
    }

    @Test
    void testPiocherPileWagonVide() {
        setUpJeu(2);
        initialisation();

        CarteTransport c1 = cartesTransportVisibles.get(0);
        CarteTransport c2 = cartesTransportVisibles.get(1);
        CarteTransport c3 = cartesTransportVisibles.get(2);
        CarteTransport c4 = cartesTransportVisibles.get(3);
        CarteTransport c5 = cartesTransportVisibles.get(4);
        CarteTransport c6 = cartesTransportVisibles.get(5);

        CarteTransport top1 = piocheBateau.get(0);
        CarteTransport top2 = piocheBateau.get(1);
        cartesJoueur1.addAll(piocheWagon);
        piocheWagon.clear();

        jeu.setInput(
                "WAGON", // ignoré
                "BATEAU",
                "WAGON", // ignoré
                c2.getNom(),
                "WAGON", // ignoré
                "BATEAU");
        joueur2.jouerTour();

        TestUtils.assertContainsExactly(cartesTransportVisibles, c1, c3, c4, c5, c6, top2);
        TestUtils.assertContainsExactly(cartesJoueur2, top1, c2);
    }

    @Test
    void testPiocherPileWagonEtPileBateauVides() {
        setUpJeu(2);
        initialisation();

        CarteTransport c1 = cartesTransportVisibles.get(0);
        CarteTransport c2 = cartesTransportVisibles.get(1);
        CarteTransport c3 = cartesTransportVisibles.get(2);
        CarteTransport c4 = cartesTransportVisibles.get(3);
        CarteTransport c5 = cartesTransportVisibles.get(4);
        CarteTransport c6 = cartesTransportVisibles.get(5);

        cartesJoueur1.addAll(piocheWagon);
        piocheWagon.clear();
        cartesJoueur1.addAll(piocheBateau);
        piocheBateau.clear();

        jeu.setInput(
                "WAGON", // ignoré
                "BATEAU", // ignoré
                c2.getNom(), // pas de remplacement car les piles sont vides
                c3.getNom() // pas de remplacement car les piles sont vides
        );
        joueur2.jouerTour();

        TestUtils.assertContainsExactly(cartesTransportVisibles, c1, c4, c5, c6);
        TestUtils.assertContainsExactly(cartesJoueur2, c2, c3);
    }

    @Test
    void testRemplirCartesVisiblesEnDebutDeTour1() {
        setUpJeu(2);
        initialisation();

        CarteTransport c1 = cartesTransportVisibles.get(0);
        CarteTransport c2 = cartesTransportVisibles.get(1);
        CarteTransport c3 = cartesTransportVisibles.get(2);
        CarteTransport c4 = cartesTransportVisibles.get(3);
        CarteTransport c5 = cartesTransportVisibles.get(4);
        CarteTransport c6 = cartesTransportVisibles.get(5);

        cartesJoueur1.addAll(piocheWagon);
        piocheWagon.clear();
        cartesJoueur1.addAll(piocheBateau);
        piocheBateau.clear();

        CarteTransport br1 = getCarteTransport("C75", cartesJoueur1); // BATEAU ROUGE
        CarteTransport br2 = getCarteTransport("C76", cartesJoueur1); // BATEAU ROUGE

        jeu.setInput(
                c1.getNom(),
                c2.getNom());
        joueur2.jouerTour();

        TestUtils.assertContainsExactly(cartesJoueur2, c1, c2);
        TestUtils.assertContainsExactly(cartesTransportVisibles, c3, c4, c5, c6);

        jeu.setInput(
                "R12", // Athina - Marseille (maritime ROUGE 2)
                br1.getNom(),
                br2.getNom());
        joueur1.jouerTour();

        TestUtils.assertContainsExactly(cartesTransportVisibles, c3, c4, c5, c6);
        TestUtils.assertContainsExactly(defausseBateau, br1, br2);

        jeu.setInput(
                "WAGON", // non valide
                "BATEAU", // révéler une carte Bateau
                "BATEAU", // révéler une carte Bateau
                c3.getNom(), // piocher une carte visible
                "");
        joueur2.jouerTour();

        TestUtils.assertContainsExactly(cartesTransportVisibles, c4, c5, c6, br1, br2);
        TestUtils.assertContainsExactly(cartesJoueur2, c1, c2, c3);
        assertTrue(defausseBateau.isEmpty());
    }

    @Test
    void testRemplirCartesVisiblesEnDebutDeTour2() {
        setUpJeu(2);
        initialisation();

        CarteTransport c1 = cartesTransportVisibles.get(0);
        CarteTransport c2 = cartesTransportVisibles.get(1);
        CarteTransport c3 = cartesTransportVisibles.get(2);
        CarteTransport c4 = cartesTransportVisibles.get(3);
        CarteTransport c5 = cartesTransportVisibles.get(4);
        CarteTransport c6 = cartesTransportVisibles.get(5);

        cartesJoueur1.addAll(piocheWagon);
        piocheWagon.clear();
        cartesJoueur1.addAll(piocheBateau);
        piocheBateau.clear();

        CarteTransport br1 = getCarteTransport("C75", cartesJoueur1); // BATEAU ROUGE

        jeu.setInput(
                c1.getNom(),
                c2.getNom());
        joueur2.jouerTour();

        TestUtils.assertContainsExactly(cartesJoueur2, c1, c2);
        TestUtils.assertContainsExactly(cartesTransportVisibles, c3, c4, c5, c6);

        jeu.setInput(
                "R64", // Darwin - Port Moresby (maritime ROUGE 1)
                br1.getNom());
        joueur1.jouerTour();

        TestUtils.assertContainsExactly(cartesTransportVisibles, c3, c4, c5, c6);
        TestUtils.assertContainsExactly(defausseBateau, br1);

        jeu.setInput(
                "WAGON", // non valide
                "BATEAU", // révéler une carte Bateau
                c3.getNom(), // piocher une carte visible
                "");
        joueur2.jouerTour();

        TestUtils.assertContainsExactly(cartesTransportVisibles, c4, c5, c6, br1);
        TestUtils.assertContainsExactly(cartesJoueur2, c1, c2, c3);
        assertTrue(defausseBateau.isEmpty());
    }

    @AfterEach
    void testIntegrite() {
        TestUtils.testIntegrite(jeu);
    }
}
