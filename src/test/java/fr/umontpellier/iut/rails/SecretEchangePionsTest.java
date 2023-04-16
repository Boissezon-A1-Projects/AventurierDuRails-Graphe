package fr.umontpellier.iut.rails;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

public class SecretEchangePionsTest extends BaseTestClass {
    @BeforeEach
    public void setUp() {
        // réinitialisation des compteurs
        setUpJeu(4);
        initialisation();
    }

    @Test
    void testEchangerPionsWagon() {
        jeu.setInput(
            "PIONS WAGON",
             "10", // insuffisant en réserve
             "5");
        joueur2.jouerTour();

        assertEquals(25, TestUtils.getNbPionsWagon(joueur2));
        assertEquals(0, TestUtils.getNbPionsWagonEnReserve(joueur2));
        assertEquals(35, TestUtils.getNbPionsBateau(joueur2));
        assertEquals(15, TestUtils.getNbPionsBateauEnReserve(joueur2));
        assertEquals(-5, TestUtils.getScore(joueur2));
    }
    
    @Test
    void testEchangerPionsQuandPasDePionsWagonAPrendre() {
        TestUtils.setAttribute(joueur2, "nbPionsWagonEnReserve", 0);

        jeu.setInput(
            "PIONS WAGON",  // impossible
            "PIONS BATEAU",
             "5");
        joueur2.jouerTour();
    
        assertEquals(15, TestUtils.getNbPionsWagon(joueur2));
        assertEquals(5, TestUtils.getNbPionsWagonEnReserve(joueur2));
        assertEquals(45, TestUtils.getNbPionsBateau(joueur2));
        assertEquals(5, TestUtils.getNbPionsBateauEnReserve(joueur2));
        assertEquals(-5, TestUtils.getScore(joueur2));
    }

    @Test
    void testEchangerPionsQuandPasDePionsBateauAPrendre() {
        TestUtils.setAttribute(joueur2, "nbPionsBateauEnReserve", 0);

        jeu.setInput(
            "PIONS BATEAU",  // impossible
            "PIONS WAGON",
             "5");
        joueur2.jouerTour();
    
        assertEquals(25, TestUtils.getNbPionsWagon(joueur2));
        assertEquals(0, TestUtils.getNbPionsWagonEnReserve(joueur2));
        assertEquals(35, TestUtils.getNbPionsBateau(joueur2));
        assertEquals(5, TestUtils.getNbPionsBateauEnReserve(joueur2));
        assertEquals(-5, TestUtils.getScore(joueur2));
    }

    @Test
    void testEchangerPionsQuandPasDePionsWagonADonner() {
        TestUtils.setAttribute(joueur2, "nbPionsWagon", 0);

        jeu.setInput(
            "PIONS BATEAU",  // impossible
            "PIONS WAGON",
             "5");
        joueur2.jouerTour();
    
        assertEquals(5, TestUtils.getNbPionsWagon(joueur2));
        assertEquals(0, TestUtils.getNbPionsWagonEnReserve(joueur2));
        assertEquals(35, TestUtils.getNbPionsBateau(joueur2));
        assertEquals(15, TestUtils.getNbPionsBateauEnReserve(joueur2));
        assertEquals(-5, TestUtils.getScore(joueur2));

    }

    @Test
    void testEchangerPionsQuandPasDePionsBateauADonner() {
        TestUtils.setAttribute(joueur2, "nbPionsBateau", 0);

        jeu.setInput(
            "PIONS WAGON",  // impossible
            "PIONS BATEAU",
             "5");
        joueur2.jouerTour();
    
        assertEquals(15, TestUtils.getNbPionsWagon(joueur2));
        assertEquals(10, TestUtils.getNbPionsWagonEnReserve(joueur2));
        assertEquals(5, TestUtils.getNbPionsBateau(joueur2));
        assertEquals(5, TestUtils.getNbPionsBateauEnReserve(joueur2));
        assertEquals(-5, TestUtils.getScore(joueur2));
    }
    
    @Test
    void testEchangerPionsQuandAucunPionAEchanger() {
        TestUtils.setAttribute(joueur2, "nbPionsBateau", 0);
        TestUtils.setAttribute(joueur2, "nbPionsWagon", 0);
    
        jeu.setInput(
            "PIONS WAGON",  // impossible
            "PIONS BATEAU", // impossible
             "WAGON",
             "WAGON");
        joueur2.jouerTour();
    
        assertEquals(0, TestUtils.getNbPionsWagon(joueur2));
        assertEquals(5, TestUtils.getNbPionsWagonEnReserve(joueur2));
        assertEquals(0, TestUtils.getNbPionsBateau(joueur2));
        assertEquals(10, TestUtils.getNbPionsBateauEnReserve(joueur2));
        assertEquals(0, TestUtils.getScore(joueur2));
    }
}
