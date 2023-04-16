package fr.umontpellier.iut.rails;

import fr.umontpellier.iut.rails.data.CarteTransport;
import fr.umontpellier.iut.rails.data.Couleur;
import fr.umontpellier.iut.rails.data.TypeCarteTransport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SecretRouteMaritimeTest extends BaseTestClass {

    @BeforeEach
    public void setUp() {
        setUpJeu(4);
        initialisation();
    }

    @Test
    void testCapturerRouteMaritimeSimple() {
        cartesJoueur1.clear();
        CarteTransport[] cartes = new CarteTransport[] {
                new CarteTransport(TypeCarteTransport.WAGON, Couleur.BLANC, false, true),
                new CarteTransport(TypeCarteTransport.BATEAU, Couleur.BLANC, false, true),
                new CarteTransport(TypeCarteTransport.BATEAU, Couleur.BLANC, false, true),
                new CarteTransport(TypeCarteTransport.BATEAU, Couleur.BLANC, false, true),
                new CarteTransport(TypeCarteTransport.BATEAU, Couleur.BLANC, true, false),
                new CarteTransport(TypeCarteTransport.BATEAU, Couleur.JAUNE, true, false),
                new CarteTransport(TypeCarteTransport.BATEAU, Couleur.JAUNE, true, false),
                new CarteTransport(TypeCarteTransport.BATEAU, Couleur.JAUNE, true, false),
        };

        cartesJoueur1.addAll(List.of(cartes));
        Collections.shuffle(cartesJoueur1);

        int nbPionsWagonAvant = TestUtils.getNbPionsWagon(joueur1);
        int nbPionsBateauAvant = TestUtils.getNbPionsBateau(joueur1);

        Route r81 = TestUtils.getRoute(jeu.getRoutesLibres(), "R81"); // route maritime (BLANC, 5)

        jeu.setInput(
                r81.getNom(),
                cartes[0].getNom(),
                cartes[5].getNom(),
                cartes[6].getNom(),
                cartes[7].getNom(),
                cartes[1].getNom(),
                cartes[2].getNom(),
                cartes[3].getNom(),
                cartes[4].getNom());
        joueur1.jouerTour();

        TestUtils.assertContainsExactly(
                cartesJoueur1,
                cartes[0],
                cartes[5],
                cartes[6],
                cartes[7]);
        TestUtils.assertContainsExactly(
                defausseBateau,
                cartes[1],
                cartes[2],
                cartes[3],
                cartes[4]);
        TestUtils.assertContainsExactly(routesJoueur1, r81);
        assertEquals(10, TestUtils.getScore(joueur1));
        assertEquals(nbPionsWagonAvant, TestUtils.getNbPionsWagon(joueur1));
        assertEquals(nbPionsBateauAvant - 5, TestUtils.getNbPionsBateau(joueur1));
    }

    @Test
    void testCapturerRouteMaritimeSimple2() {
        cartesJoueur1.clear();
        CarteTransport[] cartes = new CarteTransport[] {
                new CarteTransport(TypeCarteTransport.WAGON, Couleur.BLANC, false, true),
                new CarteTransport(TypeCarteTransport.BATEAU, Couleur.JAUNE, false, true),
                new CarteTransport(TypeCarteTransport.BATEAU, Couleur.JAUNE, false, true),
                new CarteTransport(TypeCarteTransport.BATEAU, Couleur.BLANC, false, true),
                new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true),
                new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true),
                new CarteTransport(TypeCarteTransport.BATEAU, Couleur.BLANC, true, false),
        };

        cartesJoueur1.addAll(List.of(cartes));
        Collections.shuffle(cartesJoueur1);

        Route r81 = TestUtils.getRoute(jeu.getRoutesLibres(), "R81"); // route maritime (BLANC, 5)

        jeu.setInput(
                r81.getNom(),
                cartes[0].getNom(), // wagon (refusé)
                cartes[1].getNom(), // jaune (refusé)
                cartes[2].getNom(), // jaune (refusé)
                cartes[3].getNom(), // bateau blanc (accepté)
                cartes[4].getNom(), // joker (accepté)
                cartes[5].getNom(), // joker (accepté)
                cartes[6].getNom()); // double blanc (accepté)
        joueur1.jouerTour();

        TestUtils.assertContainsExactly(
                cartesJoueur1,
                cartes[0],
                cartes[1],
                cartes[2]);
        TestUtils.assertContainsExactly(
                defausseBateau,
                cartes[3],
                cartes[6]);
        TestUtils.assertContainsExactly(
                defausseWagon,
                cartes[4],
                cartes[5]);
        TestUtils.assertContainsExactly(routesJoueur1, r81);
        assertEquals(10, TestUtils.getScore(joueur1));
    }

    @Test
    void testCapturerRouteMaritimeDepassement() {
        cartesJoueur1.clear();
        CarteTransport[] cartes = new CarteTransport[] {
                new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true),
                new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true),
                new CarteTransport(TypeCarteTransport.BATEAU, Couleur.BLANC, false, true),
                new CarteTransport(TypeCarteTransport.BATEAU, Couleur.BLANC, true, false),
                new CarteTransport(TypeCarteTransport.BATEAU, Couleur.BLANC, true, false),
        };

        cartesJoueur1.addAll(List.of(cartes));
        Collections.shuffle(cartesJoueur1);

        Route r81 = TestUtils.getRoute(jeu.getRoutesLibres(), "R81"); // route maritime (BLANC, 5)

        jeu.setInput(
                r81.getNom(),
                cartes[0].getNom(), // joker
                cartes[1].getNom(), // joker
                cartes[3].getNom(), // double
                cartes[4].getNom(), // double (refusé)
                cartes[2].getNom()); // simple
        joueur1.jouerTour();

        TestUtils.assertContainsExactly(
                cartesJoueur1,
                cartes[4]);
        TestUtils.assertContainsExactly(
                defausseBateau,
                cartes[3],
                cartes[2]);
        TestUtils.assertContainsExactly(
                defausseWagon,
                cartes[0],
                cartes[1]);
        TestUtils.assertContainsExactly(routesJoueur1, r81);
        assertEquals(10, TestUtils.getScore(joueur1));
    }

    @Test
    void testCapturerRouteMaritimeDepassement2() {
        cartesJoueur1.clear();
        CarteTransport[] cartes = new CarteTransport[] {
                new CarteTransport(TypeCarteTransport.BATEAU, Couleur.BLANC, false, true),
                new CarteTransport(TypeCarteTransport.BATEAU, Couleur.BLANC, false, true),
                new CarteTransport(TypeCarteTransport.BATEAU, Couleur.BLANC, true, false),
                new CarteTransport(TypeCarteTransport.BATEAU, Couleur.BLANC, true, false),
                new CarteTransport(TypeCarteTransport.BATEAU, Couleur.BLANC, true, false),
        };

        cartesJoueur1.addAll(List.of(cartes));
        Collections.shuffle(cartesJoueur1);

        Route r81 = TestUtils.getRoute(jeu.getRoutesLibres(), "R81"); // route maritime (BLANC, 5)

        jeu.setInput(
                r81.getNom(),
                cartes[0].getNom(), // simple
                cartes[1].getNom(), // simple (refusé)
                cartes[3].getNom(), // double
                cartes[4].getNom()); // double
        joueur1.jouerTour();

        TestUtils.assertContainsExactly(
                cartesJoueur1,
                cartes[1],
                cartes[2]);
        TestUtils.assertContainsExactly(
                defausseBateau,
                cartes[0],
                cartes[3],
                cartes[4]);
        TestUtils.assertContainsExactly(routesJoueur1, r81);
        assertEquals(10, TestUtils.getScore(joueur1));
    }

    @Test
    void testCapturerRouteMaritimeDepassement3() {
        cartesJoueur1.clear();
        CarteTransport[] cartes = new CarteTransport[] {
                new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true),
                new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true),
                new CarteTransport(TypeCarteTransport.BATEAU, Couleur.BLANC, true, false),
                new CarteTransport(TypeCarteTransport.BATEAU, Couleur.BLANC, true, false),
                new CarteTransport(TypeCarteTransport.BATEAU, Couleur.BLANC, true, false),
        };

        cartesJoueur1.addAll(List.of(cartes));
        Collections.shuffle(cartesJoueur1);

        Route r81 = TestUtils.getRoute(jeu.getRoutesLibres(), "R81"); // route maritime (BLANC, 5)

        jeu.setInput(
                r81.getNom(),
                cartes[0].getNom(), // simple
                cartes[1].getNom(), // simple (refusé)
                cartes[3].getNom(), // double
                cartes[4].getNom()); // double
        joueur1.jouerTour();

        TestUtils.assertContainsExactly(
                cartesJoueur1,
                cartes[1],
                cartes[2]);
        TestUtils.assertContainsExactly(
                defausseBateau,
                cartes[3],
                cartes[4]);
        TestUtils.assertContainsExactly(
                defausseWagon,
                cartes[0]);
        TestUtils.assertContainsExactly(routesJoueur1, r81);
        assertEquals(10, TestUtils.getScore(joueur1));
    }

    @Test
    void testCapturerRouteMaritimeNonDisponible() {
        cartesJoueur1.clear();
        CarteTransport[] cartes = new CarteTransport[] {
                new CarteTransport(TypeCarteTransport.BATEAU, Couleur.ROUGE, false, true),
                new CarteTransport(TypeCarteTransport.BATEAU, Couleur.ROUGE, false, true),
                new CarteTransport(TypeCarteTransport.BATEAU, Couleur.ROUGE, true, false),
                new CarteTransport(TypeCarteTransport.BATEAU, Couleur.ROUGE, true, false),
        };
        cartesJoueur1.addAll(List.of(cartes));
        Collections.shuffle(cartesJoueur1);

        Route r36 = TestUtils.getRoute(jeu.getRoutesLibres(), "R36"); // route maritime (ROUGE, 5)
        Route r83 = TestUtils.getRoute(jeu.getRoutesLibres(), "R83"); // route maritime (ROUGE, 5)
        List<Route> routesLibres = (List<Route>) TestUtils.getAttribute(jeu, "routesLibres");
        routesLibres.remove(r36); // la route R36 n'est plus disponible
        routesJoueur2.add(r36);

        jeu.setInput(
                r36.getNom(),
                r83.getNom(),
                cartes[0].getNom(), // simple
                cartes[2].getNom(), // double
                cartes[3].getNom()); // double
        joueur1.jouerTour();

        TestUtils.assertContainsExactly(
                cartesJoueur1,
                cartes[1]);
        TestUtils.assertContainsExactly(
                defausseBateau,
                cartes[0],
                cartes[2],
                cartes[3]);
        TestUtils.assertContainsExactly(routesJoueur1, r83);
        assertEquals(10, TestUtils.getScore(joueur1));
    }
}
