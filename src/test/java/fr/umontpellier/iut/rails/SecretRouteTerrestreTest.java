package fr.umontpellier.iut.rails;

import fr.umontpellier.iut.rails.data.CarteTransport;
import fr.umontpellier.iut.rails.data.Couleur;
import fr.umontpellier.iut.rails.data.TypeCarteTransport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SecretRouteTerrestreTest extends BaseTestClass {

    @BeforeEach
    public void setUp() {
        setUpJeu(4);
        initialisation();
    }

    @Test
    void testCapturerRouteTerrestreSimple() {
        cartesJoueur1.clear();
        CarteTransport[] cartes = new CarteTransport[] {
                new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, true), // 0
                new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, true), // 1
                new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, false), // 2
                new CarteTransport(TypeCarteTransport.WAGON, Couleur.NOIR, false, true), // 3
                new CarteTransport(TypeCarteTransport.WAGON, Couleur.NOIR, false, true), // 4
                new CarteTransport(TypeCarteTransport.BATEAU, Couleur.NOIR, false, true), // 5
        };
        cartesJoueur1.addAll(List.of(cartes));
        Collections.shuffle(cartesJoueur1);

        Route r23 = TestUtils.getRoute(jeu.getRoutesLibres(), "R23"); // route terrestre (NOIR, 3)
        Route r24 = TestUtils.getRoute(jeu.getRoutesLibres(), "R24"); // route terrestre (ROUGE, 3)
        int nbPionsWagonAvant = TestUtils.getNbPionsWagon(joueur1);
        int nbPionsBateauAvant = TestUtils.getNbPionsBateau(joueur1);

        jeu.setInput(
                r23.getNom(), // route terrestre (NOIR, 3)
                r24.getNom(), // route terrestre (ROUGE, 3)
                cartes[0].getNom(),
                cartes[3].getNom(),
                cartes[4].getNom(),
                cartes[5].getNom(),
                cartes[0].getNom(),
                cartes[1].getNom(),
                cartes[2].getNom());
        joueur1.jouerTour();

        TestUtils.assertContainsExactly(cartesJoueur1, cartes[3], cartes[4], cartes[5]);
        TestUtils.assertContainsExactly(defausseWagon, cartes[0], cartes[1], cartes[2]);
        TestUtils.assertContainsExactly(routesJoueur1, r24);
        assertEquals(4, TestUtils.getScore(joueur1));
        assertEquals(nbPionsWagonAvant - 3, TestUtils.getNbPionsWagon(joueur1));
        assertEquals(nbPionsBateauAvant, TestUtils.getNbPionsBateau(joueur1));
    }

    @Test
    void testCapturerRouteTerrestrePionsDisponibles() {
        cartesJoueur1.clear();
        CarteTransport[] cartes = new CarteTransport[] {
                new CarteTransport(TypeCarteTransport.WAGON, Couleur.NOIR, false, true), // 0
                new CarteTransport(TypeCarteTransport.WAGON, Couleur.NOIR, false, true), // 1
                new CarteTransport(TypeCarteTransport.WAGON, Couleur.NOIR, false, false), // 2
                new CarteTransport(TypeCarteTransport.WAGON, Couleur.NOIR, false, false), // 3
        };
        cartesJoueur1.addAll(List.of(cartes));
        Collections.shuffle(cartesJoueur1);

        Route r32 = TestUtils.getRoute(jeu.getRoutesLibres(), "R23"); // route terrestre (NOIR, 4)
        Route r23 = TestUtils.getRoute(jeu.getRoutesLibres(), "R23"); // route terrestre (NOIR, 3)
        TestUtils.setAttribute(joueur1, "nbPionsWagon", 3);

        jeu.setInput(
                r32.getNom(), // pas assez de pions wagon
                r23.getNom(),
                cartes[0].getNom(),
                cartes[1].getNom(),
                cartes[2].getNom());
        joueur1.jouerTour();

        TestUtils.assertContainsExactly(cartesJoueur1, cartes[3]);
        TestUtils.assertContainsExactly(defausseWagon, cartes[0], cartes[1], cartes[2]);
        TestUtils.assertContainsExactly(routesJoueur1, r23);
        assertEquals(4, TestUtils.getScore(joueur1));
        assertEquals(0, TestUtils.getNbPionsWagon(joueur1));
    }

    @Test
    void testCapturerRouteTerrestreToutEnJoker() {
        cartesJoueur1.clear();
        CarteTransport[] cartes = new CarteTransport[] {
                new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true), // 0
                new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true), // 1
                new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true), // 2
                new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true), // 3
        };
        cartesJoueur1.addAll(List.of(cartes));
        Collections.shuffle(cartesJoueur1);

        Route r2 = TestUtils.getRoute(jeu.getRoutesLibres(), "R2"); // route terrestre (GRIS, 3)

        jeu.setInput(
                r2.getNom(),
                cartes[0].getNom(),
                cartes[1].getNom(),
                cartes[2].getNom());
        joueur1.jouerTour();

        TestUtils.assertContainsExactly(cartesJoueur1, cartes[3]);
        TestUtils.assertContainsExactly(defausseWagon, cartes[0], cartes[1], cartes[2]);
        TestUtils.assertContainsExactly(routesJoueur1, r2);
        assertEquals(4, TestUtils.getScore(joueur1));
    }

    @Test
    void testCapturerRouteTerrestreGriseDeuxCouleursPossibles() {
        cartesJoueur1.clear();
        CarteTransport[] cartes = new CarteTransport[] {
                new CarteTransport(TypeCarteTransport.WAGON, Couleur.VERT, false, true),
                new CarteTransport(TypeCarteTransport.WAGON, Couleur.VIOLET, false, true),
                new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true),
                new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true),
        };

        cartesJoueur1.addAll(List.of(cartes));
        Collections.shuffle(cartesJoueur1);

        Route r2 = TestUtils.getRoute(jeu.getRoutesLibres(), "R2"); // route terrestre (GRIS, 3)

        jeu.setInput(
                r2.getNom(),
                cartes[2].getNom(),
                cartes[0].getNom(),
                cartes[1].getNom(),
                cartes[3].getNom());
        joueur1.jouerTour();

        TestUtils.assertContainsExactly(cartesJoueur1, cartes[1]);
        TestUtils.assertContainsExactly(defausseWagon, cartes[0], cartes[2], cartes[3]);
        TestUtils.assertContainsExactly(routesJoueur1, r2);
        assertEquals(4, TestUtils.getScore(joueur1));
    }

    @Test
    void testCapturerRouteTerrestreGrisDeuxCouleursPossibles2() {
        cartesJoueur1.clear();
        CarteTransport[] cartes = new CarteTransport[] {
                new CarteTransport(TypeCarteTransport.WAGON, Couleur.JAUNE, false, true),
                new CarteTransport(TypeCarteTransport.BATEAU, Couleur.JAUNE, false, true),
                new CarteTransport(TypeCarteTransport.BATEAU, Couleur.JAUNE, true, true),
                new CarteTransport(TypeCarteTransport.WAGON, Couleur.VERT, false, true),
                new CarteTransport(TypeCarteTransport.WAGON, Couleur.VERT, false, true),
                new CarteTransport(TypeCarteTransport.WAGON, Couleur.VIOLET, false, true),
                new CarteTransport(TypeCarteTransport.WAGON, Couleur.VIOLET, false, true),
                new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true),
        };

        cartesJoueur1.addAll(List.of(cartes));
        Collections.shuffle(cartesJoueur1);

        Route r2 = TestUtils.getRoute(jeu.getRoutesLibres(), "R2"); // route terrestre (GRIS, 3)

        jeu.setInput(
                r2.getNom(),
                cartes[0].getNom(),
                cartes[7].getNom(),
                cartes[0].getNom(),
                cartes[1].getNom(),
                cartes[2].getNom(),
                cartes[3].getNom(),
                cartes[5].getNom(),
                cartes[4].getNom());
        joueur1.jouerTour();

        TestUtils.assertContainsExactly(
                cartesJoueur1,
                cartes[0],
                cartes[1],
                cartes[2],
                cartes[5],
                cartes[6]);
        TestUtils.assertContainsExactly(defausseWagon, cartes[3], cartes[4], cartes[7]);
        TestUtils.assertContainsExactly(routesJoueur1, r2);
        assertEquals(4, TestUtils.getScore(joueur1));
    }
}
