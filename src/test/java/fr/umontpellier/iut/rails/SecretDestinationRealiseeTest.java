package fr.umontpellier.iut.rails;

import fr.umontpellier.iut.rails.data.CarteTransport;
import fr.umontpellier.iut.rails.data.Couleur;
import fr.umontpellier.iut.rails.data.Destination;
import fr.umontpellier.iut.rails.data.TypeCarteTransport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

public class SecretDestinationRealiseeTest extends BaseTestClass {
    @BeforeEach
    void setUp() {
        setUpJeu(4);
        initialisation();
    }

    @Test
    void testDestinationCompleteSimple1() {
        routesJoueur1.clear();
        destinationsJoueur1.clear();

        Destination d1 = getDestination("D19"); // Hong Kong - Jakarta (5)
        destinationsJoueur1.add(d1);

        cartesJoueur1.clear();
        cartesJoueur1.add(new CarteTransport(TypeCarteTransport.WAGON, Couleur.VIOLET, false, true)); // C141
        cartesJoueur1.add(new CarteTransport(TypeCarteTransport.BATEAU, Couleur.BLANC, false, true)); // C142
        cartesJoueur1.add(new CarteTransport(TypeCarteTransport.BATEAU, Couleur.BLANC, false, true)); // C143

        assertFalse(joueur1.destinationEstComplete(d1));
        jeu.setInput("R15", // Hong Kong - Bangkok (T, VIOLET, 1)
                "C141");
        joueur1.jouerTour();
        assertFalse(joueur1.destinationEstComplete(d1));
        jeu.setInput("R16", // Bangkok - Jakarta (M, BLANC, 2)
                "C142",
                "C143");
        joueur1.jouerTour();
        assertTrue(joueur1.destinationEstComplete(d1));
        assertEquals(-4, joueur1.calculerScoreFinal()); // 3 (routes) + 5 (dest) - 12 (ports) = -4
    }

    @Test
    void testDestinationCompleteSimple2() {
        routesJoueur1.clear();
        destinationsJoueur1.clear();

        Destination d1 = getDestination("D19"); // Hong Kong - Jakarta (5)
        destinationsJoueur1.add(d1);

        cartesJoueur1.clear();
        cartesJoueur1.add(new CarteTransport(TypeCarteTransport.BATEAU, Couleur.VIOLET, false, true));
        cartesJoueur1.add(new CarteTransport(TypeCarteTransport.BATEAU, Couleur.BLANC, false, true));
        cartesJoueur1.add(new CarteTransport(TypeCarteTransport.BATEAU, Couleur.BLANC, false, true));
        cartesJoueur1.add(new CarteTransport(TypeCarteTransport.BATEAU, Couleur.BLANC, false, true));
        cartesJoueur1.add(new CarteTransport(TypeCarteTransport.BATEAU, Couleur.BLANC, false, true));
        cartesJoueur1.add(new CarteTransport(TypeCarteTransport.BATEAU, Couleur.BLANC, false, true));
        cartesJoueur1.add(new CarteTransport(TypeCarteTransport.BATEAU, Couleur.VERT, false, true));
        cartesJoueur1.add(new CarteTransport(TypeCarteTransport.BATEAU, Couleur.VERT, false, true));
        cartesJoueur1.add(new CarteTransport(TypeCarteTransport.BATEAU, Couleur.VERT, false, true));
        cartesJoueur1.add(new CarteTransport(TypeCarteTransport.BATEAU, Couleur.ROUGE, false, true));
        cartesJoueur1.add(new CarteTransport(TypeCarteTransport.BATEAU, Couleur.NOIR, false, true));
        cartesJoueur1.add(new CarteTransport(TypeCarteTransport.BATEAU, Couleur.NOIR, false, true));

        assertFalse(joueur1.destinationEstComplete(d1));
        jeu.setInput("R77", "C141"); // Hong Kong - Manila (M, VIOLET, 1)
        joueur1.jouerTour();

        assertFalse(joueur1.destinationEstComplete(d1));
        jeu.setInput("R81", "C142", "C143", "C144", "C145", "C146"); // Manila - Honolulu (M, BLANC, 5)
        joueur1.jouerTour();

        assertFalse(joueur1.destinationEstComplete(d1));
        jeu.setInput("R82", "C147", "C148", "C149"); // Honolulu - Port Moresby (M, VERT, 3)
        joueur1.jouerTour();

        assertFalse(joueur1.destinationEstComplete(d1));
        jeu.setInput("R64", "C150"); // Port Moresby - Darwin (M, ROUGE, 1)
        joueur1.jouerTour();

        assertFalse(joueur1.destinationEstComplete(d1));
        jeu.setInput("R62", "C151", "C152"); // Darwin - Jakarta (M, NOIR, 2)
        joueur1.jouerTour();

        assertTrue(joueur1.destinationEstComplete(d1));
        assertEquals(11, joueur1.calculerScoreFinal()); // 18 (routes) + 5 (dest) - 12 (ports) = 11
    }

    @Test
    void testDestinationComplete3() {
        Destination d1 = getDestination("D17"); // Sydney - Jakarta (7 points)
        Destination d2 = getDestination("D6"); // Sydney - Buenos Aires (13 points)
        Destination d3 = getDestination("D14"); // Sydney - Edinburgh (25 points)
        destinationsJoueur1.add(d1);
        destinationsJoueur1.add(d2);
        destinationsJoueur1.add(d3);

        cartesJoueur1.clear();
        cartesJoueur1.add(new CarteTransport(TypeCarteTransport.WAGON, Couleur.VERT, false, true));
        cartesJoueur1.add(new CarteTransport(TypeCarteTransport.WAGON, Couleur.VERT, false, true));
        cartesJoueur1.add(new CarteTransport(TypeCarteTransport.BATEAU, Couleur.NOIR, false, true));
        cartesJoueur1.add(new CarteTransport(TypeCarteTransport.BATEAU, Couleur.NOIR, false, true));
        cartesJoueur1.add(new CarteTransport(TypeCarteTransport.BATEAU, Couleur.VERT, false, true));
        cartesJoueur1.add(new CarteTransport(TypeCarteTransport.BATEAU, Couleur.VERT, false, true));
        cartesJoueur1.add(new CarteTransport(TypeCarteTransport.BATEAU, Couleur.VERT, false, true));
        cartesJoueur1.add(new CarteTransport(TypeCarteTransport.BATEAU, Couleur.JAUNE, false, true));
        cartesJoueur1.add(new CarteTransport(TypeCarteTransport.BATEAU, Couleur.JAUNE, false, true));
        cartesJoueur1.add(new CarteTransport(TypeCarteTransport.BATEAU, Couleur.JAUNE, false, true));
        cartesJoueur1.add(new CarteTransport(TypeCarteTransport.BATEAU, Couleur.JAUNE, false, true));
        cartesJoueur1.add(new CarteTransport(TypeCarteTransport.BATEAU, Couleur.JAUNE, false, true));
        cartesJoueur1.add(new CarteTransport(TypeCarteTransport.BATEAU, Couleur.JAUNE, false, true));
        cartesJoueur1.add(new CarteTransport(TypeCarteTransport.BATEAU, Couleur.JAUNE, false, true));
        cartesJoueur1.add(new CarteTransport(TypeCarteTransport.BATEAU, Couleur.ROUGE, false, true));

        jeu.setInput("R65", "C141", "C142"); // Sydney - Darwin (T, VERT, 2)
        joueur1.jouerTour();
        jeu.setInput("R62", "C143", "C144"); // Darwin - Jakarta (M, NOIR, 2)
        joueur1.jouerTour();
        jeu.setInput("R30", "C145", "C146", "C147"); // Valparaiso - Buenos Aires (M, VERT, 3)
        joueur1.jouerTour();
        jeu.setInput(
                "R54", // Valparaiso - Christchurch (M, JAUNE, 7)
                "C148",
                "C149",
                "C150",
                "C151",
                "C152",
                "C153",
                "C154");
        joueur1.jouerTour();
        jeu.setInput("R53", "C155", "C156", "C157"); // Sydney - Christchurch (M, ROUGE, 1)
        joueur1.jouerTour();

        assertTrue(joueur1.destinationEstComplete(d1));
        assertTrue(joueur1.destinationEstComplete(d2));
        assertFalse(joueur1.destinationEstComplete(d3));

        assertEquals(10, joueur1.calculerScoreFinal()); // 27 (routes) - 5 (dest) - 12 (ports) = 10
    }

    @Test
    void testDestinationNonComplete() {
        routesJoueur1.clear();
        destinationsJoueur1.clear();

        Destination d1 = getDestination("D19"); // Hong Kong - Jakarta (5)
        destinationsJoueur1.add(d1);

        cartesJoueur1.clear();
        cartesJoueur1.add(new CarteTransport(TypeCarteTransport.BATEAU, Couleur.VIOLET, false, true));
        cartesJoueur1.add(new CarteTransport(TypeCarteTransport.BATEAU, Couleur.BLANC, false, true));
        cartesJoueur1.add(new CarteTransport(TypeCarteTransport.BATEAU, Couleur.BLANC, false, true));

        assertFalse(joueur1.destinationEstComplete(d1));
        jeu.setInput("R77", "C141"); // Hong Kong - Manila (M, VIOLET, 1)
        joueur1.jouerTour();

        assertFalse(joueur1.destinationEstComplete(d1));
        jeu.setInput("R16", "C142", "C143"); // Bangkok - Jakarta (M, BLANC, 2)
        joueur1.jouerTour();

        assertFalse(joueur1.destinationEstComplete(d1));
        assertEquals(-14, joueur1.calculerScoreFinal()); // 3 (routes) - 5 (dest) - 12 (ports) = -14
    }

    @Test
    void testItineraireComplet1() {
        routesJoueur1.clear();
        destinationsJoueur1.clear();

        Destination d1 = getDestination("D60"); // Anchorage - Vancouver - Winnipeg - Cambridge Bay (12, 18, 24)
        destinationsJoueur1.add(d1);

        cartesJoueur1.clear();
        cartesJoueur1.add(new CarteTransport(TypeCarteTransport.BATEAU, Couleur.NOIR, false, true));
        cartesJoueur1.add(new CarteTransport(TypeCarteTransport.BATEAU, Couleur.NOIR, false, true));
        cartesJoueur1.add(new CarteTransport(TypeCarteTransport.BATEAU, Couleur.NOIR, false, true));
        cartesJoueur1.add(new CarteTransport(TypeCarteTransport.BATEAU, Couleur.NOIR, false, true));
        cartesJoueur1.add(new CarteTransport(TypeCarteTransport.BATEAU, Couleur.NOIR, false, true));
        cartesJoueur1.add(new CarteTransport(TypeCarteTransport.BATEAU, Couleur.NOIR, false, true));
        cartesJoueur1.add(new CarteTransport(TypeCarteTransport.WAGON, Couleur.NOIR, false, true));
        cartesJoueur1.add(new CarteTransport(TypeCarteTransport.WAGON, Couleur.NOIR, false, true));
        cartesJoueur1.add(new CarteTransport(TypeCarteTransport.WAGON, Couleur.NOIR, false, true));
        cartesJoueur1.add(new CarteTransport(TypeCarteTransport.WAGON, Couleur.NOIR, false, true));
        cartesJoueur1.add(new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, true));
        cartesJoueur1.add(new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, true));
        cartesJoueur1.add(new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, true));
        cartesJoueur1.add(new CarteTransport(TypeCarteTransport.WAGON, Couleur.VERT, false, true));

        assertFalse(joueur1.destinationEstComplete(d1));
        assertEquals(-36, joueur1.calculerScoreFinal()); // -24 (dest) - 12 (ports) = -36
        
        jeu.setInput("R7", "C141", "C142", "C143", "C144", "C145", "C146"); // Anchorage - Cambridge Bay (M, NOIR, 6)
        joueur1.jouerTour();
        
        assertFalse(joueur1.destinationEstComplete(d1));
        assertEquals(-21, joueur1.calculerScoreFinal()); // 15 (routes) - 24 (dest) - 12 (ports) = -21
        
        jeu.setInput("R32", "C147", "C148", "C149", "C150"); // Cambridge Bay - Winninpeg (T, NOIR, 4)
        joueur1.jouerTour();
        
        assertFalse(joueur1.destinationEstComplete(d1));
        assertEquals(-14, joueur1.calculerScoreFinal()); // 22 (routes) - 24 (dest) - 12 (ports) = -14
        
        jeu.setInput("R104", "C151", "C152", "C153"); // Los Angeles - Winnipeg (T, GRIS, 3)
        joueur1.jouerTour();
        
        assertFalse(joueur1.destinationEstComplete(d1));
        assertEquals(-10, joueur1.calculerScoreFinal()); // 26 (routes) - 24 (dest) - 12 (ports) = -10
        
        jeu.setInput("R103", "C154"); // Los Angeles - Vancouver (T, VERT, 1)
        joueur1.jouerTour();

        assertTrue(joueur1.destinationEstComplete(d1));
        assertEquals(27, joueur1.calculerScoreFinal()); // 27 (routes) + 12 (dest) - 12 (ports) = 27
    }

}
