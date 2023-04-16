package fr.umontpellier.iut.rails;

import fr.umontpellier.iut.rails.data.CarteTransport;
import fr.umontpellier.iut.rails.data.Couleur;
import fr.umontpellier.iut.rails.data.TypeCarteTransport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SecretRoutePaireTest extends BaseTestClass {

        @BeforeEach
        void setUp() {
                setUpJeu(4);
                initialisation();
        }

        @Test
        void testCaptureRoutePaireSimple1() {
                cartesJoueur1.clear();
                CarteTransport c1 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.VERT, false, true); // C141
                CarteTransport c2 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.JAUNE, false, true); // C142
                CarteTransport c3 = new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true); // C143
                CarteTransport c4 = new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true); // C144
                CarteTransport c5 = new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true); // C145
                CarteTransport c6 = new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true); // C146
                cartesJoueur1.addAll(List.of(c1, c2, c3, c4, c5, c6));

                jeu.setInput(
                                "R22", // Beijing - Lahore (paire 3 GRIS)
                                "C141", // wagon vert
                                "C143", // joker
                                "C144", // joker
                                "C145", // joker
                                "C146", // joker
                                "C142"); // wagon jaune
                joueur1.jouerTour();

                assertEquals(0, cartesJoueur1.size());
                assertEquals(6, defausseWagon.size());
                assertTrue(defausseWagon.contains(c1));
                assertTrue(defausseWagon.contains(c2));
                assertTrue(defausseWagon.contains(c3));
                assertTrue(defausseWagon.contains(c4));
                assertTrue(defausseWagon.contains(c5));
                assertTrue(defausseWagon.contains(c6));
                assertEquals(1, routesJoueur1.size());
                assertEquals("R22", routesJoueur1.get(0).getNom());
                assertEquals(4, TestUtils.getScore(joueur1));
        }

        @Test
        void testCapturerRoutePaireSimple2() {
                cartesJoueur1.clear();
                CarteTransport[] cartes = new CarteTransport[] {
                                new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, true),
                                new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, true),
                                new CarteTransport(TypeCarteTransport.WAGON, Couleur.VERT, false, true),
                                new CarteTransport(TypeCarteTransport.WAGON, Couleur.VERT, false, true),
                };

                cartesJoueur1.addAll(List.of(cartes));
                Collections.shuffle(cartesJoueur1);

                Route r91 = TestUtils.getRoute(jeu.getRoutesLibres(), "R91"); // route paire (GRIS, 2)

                jeu.setInput(
                                r91.getNom(),
                                cartes[0].getNom(),
                                cartes[1].getNom(),
                                cartes[2].getNom(),
                                cartes[3].getNom());
                joueur1.jouerTour();

                assertTrue(cartesJoueur1.isEmpty());
                TestUtils.assertContainsExactly(
                                defausseWagon,
                                cartes[0],
                                cartes[1],
                                cartes[2],
                                cartes[3]);
                TestUtils.assertContainsExactly(routesJoueur1, r91);
                assertEquals(2, TestUtils.getScore(joueur1));
        }

        @Test
        void testCapturerRoutePaireSimpleCartesInutiles() {
                cartesJoueur1.clear();
                CarteTransport[] cartes = new CarteTransport[] {
                                new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, true),
                                new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, true),
                                new CarteTransport(TypeCarteTransport.WAGON, Couleur.VERT, false, true),
                                new CarteTransport(TypeCarteTransport.WAGON, Couleur.VERT, false, true),
                                new CarteTransport(TypeCarteTransport.BATEAU, Couleur.ROUGE, false, true),
                                new CarteTransport(TypeCarteTransport.BATEAU, Couleur.ROUGE, true, false),
                };

                cartesJoueur1.addAll(List.of(cartes));
                Collections.shuffle(cartesJoueur1);

                Route r91 = TestUtils.getRoute(jeu.getRoutesLibres(), "R91"); // route paire (GRIS, 2)

                jeu.setInput(
                                r91.getNom(),
                                cartes[4].getNom(),
                                cartes[5].getNom(),
                                cartes[0].getNom(),
                                cartes[1].getNom(),
                                cartes[2].getNom(),
                                cartes[3].getNom());
                joueur1.jouerTour();

                TestUtils.assertContainsExactly(
                                cartesJoueur1,
                                cartes[4],
                                cartes[5]);
                TestUtils.assertContainsExactly(
                                defausseWagon,
                                cartes[0],
                                cartes[1],
                                cartes[2],
                                cartes[3]);
                TestUtils.assertContainsExactly(routesJoueur1, r91);
                assertEquals(2, TestUtils.getScore(joueur1));
        }

        @Test
        void testCapturerRoutePairePions() {
                cartesJoueur1.clear();
                CarteTransport[] cartes = new CarteTransport[] {
                                new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, true),
                                new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, true),
                                new CarteTransport(TypeCarteTransport.WAGON, Couleur.VERT, false, true),
                                new CarteTransport(TypeCarteTransport.WAGON, Couleur.VERT, false, true),
                                new CarteTransport(TypeCarteTransport.WAGON, Couleur.VERT, false, true),
                                new CarteTransport(TypeCarteTransport.WAGON, Couleur.VERT, false, true),
                };

                cartesJoueur1.addAll(List.of(cartes));
                Collections.shuffle(cartesJoueur1);

                TestUtils.setAttribute(joueur1, "nbPionsWagon", 2);
                Route r22 = TestUtils.getRoute(jeu.getRoutesLibres(), "R22"); // route paire (GRIS, 3)
                Route r91 = TestUtils.getRoute(jeu.getRoutesLibres(), "R91"); // route paire (GRIS, 2)

                jeu.setInput(
                                r22.getNom(),
                                r91.getNom(),
                                cartes[0].getNom(),
                                cartes[1].getNom(),
                                cartes[2].getNom(),
                                cartes[3].getNom());
                joueur1.jouerTour();

                TestUtils.assertContainsExactly(
                                cartesJoueur1,
                                cartes[4],
                                cartes[5]);
                TestUtils.assertContainsExactly(
                                defausseWagon,
                                cartes[0],
                                cartes[1],
                                cartes[2],
                                cartes[3]);
                TestUtils.assertContainsExactly(routesJoueur1, r91);
                assertEquals(2, TestUtils.getScore(joueur1));
        }

        @Test
        void testCapturerRoutePaireDeuxTriplets() {
                cartesJoueur1.clear();
                CarteTransport[] cartes = new CarteTransport[] {
                                new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, true),
                                new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, true),
                                new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, true),
                                new CarteTransport(TypeCarteTransport.WAGON, Couleur.VERT, false, true),
                                new CarteTransport(TypeCarteTransport.WAGON, Couleur.VERT, false, true),
                                new CarteTransport(TypeCarteTransport.WAGON, Couleur.VERT, false, true),
                };

                cartesJoueur1.addAll(List.of(cartes));
                Collections.shuffle(cartesJoueur1);

                Route r22 = TestUtils.getRoute(jeu.getRoutesLibres(), "R22"); // route paire (GRIS, 3)
                Route r91 = TestUtils.getRoute(jeu.getRoutesLibres(), "R91"); // route paire (GRIS, 2)

                jeu.setInput(
                                r22.getNom(),
                                r91.getNom(),
                                cartes[0].getNom(),
                                cartes[1].getNom(),
                                cartes[2].getNom(),
                                cartes[3].getNom(),
                                cartes[4].getNom());
                joueur1.jouerTour();

                TestUtils.assertContainsExactly(
                                cartesJoueur1,
                                cartes[2],
                                cartes[5]);
                TestUtils.assertContainsExactly(
                                defausseWagon,
                                cartes[0],
                                cartes[1],
                                cartes[3],
                                cartes[4]);
                TestUtils.assertContainsExactly(routesJoueur1, r91);
                assertEquals(2, TestUtils.getScore(joueur1));
        }

        @Test
        void testCapturerRoutePaireNonOrdonne() {
                cartesJoueur1.clear();
                CarteTransport[] cartes = new CarteTransport[] {
                                new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, true),
                                new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, true),
                                new CarteTransport(TypeCarteTransport.WAGON, Couleur.VERT, false, true),
                                new CarteTransport(TypeCarteTransport.WAGON, Couleur.VERT, false, true),
                                new CarteTransport(TypeCarteTransport.WAGON, Couleur.VERT, false, true),
                };

                cartesJoueur1.addAll(List.of(cartes));
                Collections.shuffle(cartesJoueur1);

                Route r91 = TestUtils.getRoute(jeu.getRoutesLibres(), "R91"); // route paire (GRIS, 2)

                jeu.setInput(
                                r91.getNom(),
                                cartes[0].getNom(),
                                cartes[2].getNom(),
                                cartes[1].getNom(),
                                cartes[3].getNom());
                joueur1.jouerTour();

                TestUtils.assertContainsExactly(
                                cartesJoueur1,
                                cartes[4]);
                TestUtils.assertContainsExactly(
                                defausseWagon,
                                cartes[0],
                                cartes[1],
                                cartes[2],
                                cartes[3]);
                TestUtils.assertContainsExactly(routesJoueur1, r91);
                assertEquals(2, TestUtils.getScore(joueur1));
        }

        @Test
        void testCapturerRoutePaireNonOrdonneAvecJokers() {
                cartesJoueur1.clear();
                CarteTransport[] cartes = new CarteTransport[] {
                                new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, true), // 0
                                new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, true), // 1
                                new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, true), // 2
                                new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, true), // 3
                                new CarteTransport(TypeCarteTransport.WAGON, Couleur.VERT, false, true), // 4
                                new CarteTransport(TypeCarteTransport.WAGON, Couleur.VERT, false, true), // 5
                                new CarteTransport(TypeCarteTransport.WAGON, Couleur.JAUNE, false, true), // 6
                                new CarteTransport(TypeCarteTransport.WAGON, Couleur.NOIR, false, true), // 7
                                new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true), // 8
                };

                cartesJoueur1.addAll(List.of(cartes));
                Collections.shuffle(cartesJoueur1);

                Route r91 = TestUtils.getRoute(jeu.getRoutesLibres(), "R22"); // route paire (GRIS, 3)

                jeu.setInput(
                                r91.getNom(),
                                cartes[0].getNom(), // rouge
                                cartes[6].getNom(), // jaune
                                cartes[7].getNom(), // noir (refusé)
                                cartes[1].getNom(), // rouge
                                cartes[2].getNom(), // rouge
                                cartes[4].getNom(), // vert (refusé)
                                cartes[8].getNom(), // joker
                                cartes[3].getNom()); // rouge
                joueur1.jouerTour();

                TestUtils.assertContainsExactly(
                                cartesJoueur1,
                                cartes[4],
                                cartes[5],
                                cartes[7]);
                TestUtils.assertContainsExactly(
                                defausseWagon,
                                cartes[0],
                                cartes[1],
                                cartes[2],
                                cartes[3],
                                cartes[6],
                                cartes[8]);
                TestUtils.assertContainsExactly(routesJoueur1, r91);
                assertEquals(4, TestUtils.getScore(joueur1));
        }

        @Test
        void testCapturerRoutePaireNonOrdonneAvecJokers2() {
                cartesJoueur1.clear();
                CarteTransport[] cartes = new CarteTransport[] {
                                new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, true), // 0
                                new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, true), // 1
                                new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, true), // 2
                                new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, true), // 3
                                new CarteTransport(TypeCarteTransport.WAGON, Couleur.VERT, false, true), // 4
                                new CarteTransport(TypeCarteTransport.WAGON, Couleur.VERT, false, true), // 5
                                new CarteTransport(TypeCarteTransport.WAGON, Couleur.JAUNE, false, true), // 6
                                new CarteTransport(TypeCarteTransport.WAGON, Couleur.NOIR, false, true), // 7
                                new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true), // 8
                };

                cartesJoueur1.addAll(List.of(cartes));
                Collections.shuffle(cartesJoueur1);

                Route r91 = TestUtils.getRoute(jeu.getRoutesLibres(), "R22"); // route paire (GRIS, 3)

                jeu.setInput(
                                r91.getNom(),
                                cartes[0].getNom(), // rouge
                                cartes[6].getNom(), // jaune
                                cartes[7].getNom(), // noir (refusé)
                                cartes[1].getNom(), // rouge
                                cartes[4].getNom(), // vert
                                cartes[2].getNom(), // rouge (refusé)
                                cartes[8].getNom(), // joker
                                cartes[5].getNom()); // vert
                joueur1.jouerTour();

                TestUtils.assertContainsExactly(
                                cartesJoueur1,
                                cartes[2],
                                cartes[3],
                                cartes[7]);
                TestUtils.assertContainsExactly(
                                defausseWagon,
                                cartes[0],
                                cartes[1],
                                cartes[4],
                                cartes[5],
                                cartes[6],
                                cartes[8]);
                TestUtils.assertContainsExactly(routesJoueur1, r91);
                assertEquals(4, TestUtils.getScore(joueur1));
        }

        @Test
        void testCapturerRoutePaireNonOrdonneAvecJokers3() {
                cartesJoueur1.clear();
                CarteTransport[] cartes = new CarteTransport[] {
                                new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, true), // 0
                                new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, true), // 1
                                new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, true), // 2
                                new CarteTransport(TypeCarteTransport.WAGON, Couleur.VERT, false, true), // 3
                                new CarteTransport(TypeCarteTransport.WAGON, Couleur.VERT, false, true), // 4
                                new CarteTransport(TypeCarteTransport.WAGON, Couleur.JAUNE, false, true), // 5
                                new CarteTransport(TypeCarteTransport.WAGON, Couleur.NOIR, false, true), // 6
                                new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true), // 7
                };

                cartesJoueur1.addAll(List.of(cartes));
                Collections.shuffle(cartesJoueur1);

                Route r91 = TestUtils.getRoute(jeu.getRoutesLibres(), "R22"); // route paire (GRIS, 3)

                jeu.setInput(
                                r91.getNom(),
                                cartes[0].getNom(), // rouge
                                cartes[7].getNom(), // joker
                                cartes[1].getNom(), // rouge
                                cartes[3].getNom(), // vert
                                cartes[6].getNom(), // noir
                                cartes[2].getNom(), // rouge (refusé)
                                cartes[5].getNom(), // jaune (refusé)
                                cartes[4].getNom()); // vert

                joueur1.jouerTour();

                TestUtils.assertContainsExactly(
                                cartesJoueur1,
                                cartes[2],
                                cartes[5]);
                TestUtils.assertContainsExactly(
                                defausseWagon,
                                cartes[0],
                                cartes[1],
                                cartes[3],
                                cartes[4],
                                cartes[6],
                                cartes[7]);
                TestUtils.assertContainsExactly(routesJoueur1, r91);
                assertEquals(4, TestUtils.getScore(joueur1));
        }

        @Test
        void testCapturerRoutePaireNonOrdonneAvecJokers4() {
                cartesJoueur1.clear();
                CarteTransport[] cartes = new CarteTransport[] {
                                new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, true), // 0
                                new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, true), // 1
                                new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, true), // 2
                                new CarteTransport(TypeCarteTransport.WAGON, Couleur.VERT, false, true), // 3
                                new CarteTransport(TypeCarteTransport.WAGON, Couleur.VERT, false, true), // 4
                                new CarteTransport(TypeCarteTransport.WAGON, Couleur.JAUNE, false, true), // 5
                                new CarteTransport(TypeCarteTransport.WAGON, Couleur.NOIR, false, true), // 6
                                new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true), // 7
                };

                cartesJoueur1.addAll(List.of(cartes));
                Collections.shuffle(cartesJoueur1);

                Route r91 = TestUtils.getRoute(jeu.getRoutesLibres(), "R22"); // route paire (GRIS, 3)

                jeu.setInput(
                                r91.getNom(),
                                cartes[0].getNom(), // rouge
                                cartes[7].getNom(), // joker
                                cartes[1].getNom(), // rouge
                                cartes[3].getNom(), // vert
                                cartes[2].getNom(), // rouge
                                cartes[6].getNom(), // noir (refusé)
                                cartes[5].getNom(), // jaune (refusé)
                                cartes[4].getNom()); // vert

                joueur1.jouerTour();

                TestUtils.assertContainsExactly(
                                cartesJoueur1,
                                cartes[5],
                                cartes[6]);
                TestUtils.assertContainsExactly(
                                defausseWagon,
                                cartes[0],
                                cartes[1],
                                cartes[2],
                                cartes[3],
                                cartes[4],
                                cartes[7]);
                TestUtils.assertContainsExactly(routesJoueur1, r91);
                assertEquals(4, TestUtils.getScore(joueur1));
        }

        @Test
        void testCapturerRoutePaireExemple6() {
                cartesJoueur1.clear();
                CarteTransport c1 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, true); // C141
                CarteTransport c2 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, true); // C142
                CarteTransport c3 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, true); // C143
                CarteTransport c4 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, true); // C144
                CarteTransport c5 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.VERT, false, true); // C145
                CarteTransport c6 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.VERT, false, true); // C146
                CarteTransport c7 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.VIOLET, false, true); // C147
                CarteTransport c8 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.VIOLET, false, true); // C148
                CarteTransport c9 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.JAUNE, false, true); // C149
                cartesJoueur1.addAll(List.of(c1, c2, c3, c4, c5, c6, c7, c8, c9));

                jeu.setInput(
                                "R22", // route paire Lahore - Beijing (longueur 3)
                                "C141", // (ok début d'une paire rouge)
                                "C145", // (ok début d'une paire verte)
                                "C149", // (invalide, pas de paire jaune possible)
                                "C142", // (ok)
                                "C143", // (ok début d'une 2e paire rouge)
                                "C147", // (invalide, on ne peut pas commencer une 4e paire)
                                "C146", // (ok)
                                "C144" // (ok, la route est payée)
                );

                joueur1.jouerTour();

                assertEquals(6, defausseWagon.size());
                assertTrue(defausseWagon.contains(c1));
                assertTrue(defausseWagon.contains(c2));
                assertTrue(defausseWagon.contains(c3));
                assertTrue(defausseWagon.contains(c4));
                assertTrue(defausseWagon.contains(c5));
                assertTrue(defausseWagon.contains(c6));
                assertEquals(3, cartesJoueur1.size());
                assertTrue(cartesJoueur1.contains(c7));
                assertTrue(cartesJoueur1.contains(c8));
                assertTrue(cartesJoueur1.contains(c9));

                assertEquals(1, routesJoueur1.size());
                assertEquals("R22", routesJoueur1.get(0).getNom());
                assertEquals(4, TestUtils.getScore(joueur1));
        }

        @Test
        void testCapturerRoutePaireExemple7() {
                cartesJoueur1.clear();
                CarteTransport c1 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, true); // C141
                CarteTransport c2 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, true); // C142
                CarteTransport c3 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, true); // C143
                CarteTransport c4 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.VERT, false, true); // C144
                CarteTransport c5 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.VIOLET, false, true); // C145
                CarteTransport c6 = new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true); // C146
                cartesJoueur1.addAll(List.of(c1, c2, c3, c4, c5, c6));

                jeu.setInput(
                                "R59", // route paire Dar Es Salaam - Luanda (longueur 2)
                                "C145", // (ok, on peut finir la paire avec le Joker)
                                "C144", // (invalide, pas de paire verte possible)
                                "C141", // (ok)
                                "C142", // (ok)
                                "C143", // (invalide, il faut compléter la paire violette)
                                "C146" // (ok, la route est payée)
                );

                joueur1.jouerTour();

                assertEquals(4, defausseWagon.size());
                assertTrue(defausseWagon.contains(c1));
                assertTrue(defausseWagon.contains(c2));
                assertTrue(defausseWagon.contains(c5));
                assertTrue(defausseWagon.contains(c6));
                assertEquals(2, cartesJoueur1.size());
                assertTrue(cartesJoueur1.contains(c3));
                assertTrue(cartesJoueur1.contains(c4));

                assertEquals(1, routesJoueur1.size());
                assertEquals("R59", routesJoueur1.get(0).getNom());
                assertEquals(2, TestUtils.getScore(joueur1));
        }

        @Test
        void testCaptureRoutePaire() {
                cartesJoueur1.clear();
                CarteTransport c1 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.VERT, false, true); // C141
                CarteTransport c2 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.VERT, false, false); // C142
                CarteTransport c3 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.JAUNE, false, false); // C143
                CarteTransport c4 = new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true); // C144
                CarteTransport c5 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, false); // C145
                CarteTransport c6 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, true); // C146
                cartesJoueur1.addAll(List.of(c1, c2, c3, c4, c5, c6));

                jeu.setInput(
                                "R91", // Lahore - Tehran (paire 2 GRIS)
                                "C141", // wagon vert
                                "C145", // wagon rouge
                                "C144", // joker
                                "C143", // wagon jaune (invalide)
                                "C146"); // wagon rouge
                joueur1.jouerTour();

                assertEquals(2, cartesJoueur1.size());
                assertEquals(4, defausseWagon.size());
                assertTrue(cartesJoueur1.contains(c2));
                assertTrue(cartesJoueur1.contains(c3));
                assertTrue(defausseWagon.contains(c1));
                assertTrue(defausseWagon.contains(c4));
                assertTrue(defausseWagon.contains(c5));
                assertTrue(defausseWagon.contains(c6));
                assertEquals(1, routesJoueur1.size());
                assertEquals("R91", routesJoueur1.get(0).getNom());
                assertEquals(2, TestUtils.getScore(joueur1));
        }
}
