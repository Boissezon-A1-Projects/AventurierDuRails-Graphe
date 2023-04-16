package fr.umontpellier.iut.rails;

import fr.umontpellier.iut.rails.data.CarteTransport;
import fr.umontpellier.iut.rails.data.TypeCarteTransport;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SecretMiseEnPlaceJeuTest extends BaseTestClass {
    @Test
    public void testPreparation2Joueurs() {
        int nbJoueurs = 2;
        setUpJeu(nbJoueurs);
        List<String> instructions = new ArrayList<String>();
        for (int i = 0; i < nbJoueurs; i++) {
            instructions.add("");
            instructions.add("20");
        }
        jeu.setInput(instructions);

        try {
            jeu.run();
        } catch (IndexOutOfBoundsException e) {
        }

        assertEquals(80 - 3 * nbJoueurs - 3, piocheWagon.size() + defausseWagon.size());
        assertEquals(60 - 7 * nbJoueurs - 3, piocheBateau.size() + defausseBateau.size());
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
    }

    @Test
    public void testPreparation3Joueurs() {
        int nbJoueurs = 3;
        setUpJeu(nbJoueurs);
        List<String> instructions = new ArrayList<String>();
        for (int i = 0; i < nbJoueurs; i++) {
            instructions.add("");
            instructions.add("20");
        }
        jeu.setInput(instructions);

        try {
            jeu.run();
        } catch (IndexOutOfBoundsException e) {
        }

        assertEquals(80 - 3 * nbJoueurs - 3, piocheWagon.size() + defausseWagon.size());
        assertEquals(60 - 7 * nbJoueurs - 3, piocheBateau.size() + defausseBateau.size());
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
    }

    @Test
    public void testPreparation4Joueurs() {
        int nbJoueurs = 2;
        setUpJeu(nbJoueurs);
        List<String> instructions = new ArrayList<String>();
        for (int i = 0; i < nbJoueurs; i++) {
            instructions.add("");
            instructions.add("20");
        }
        jeu.setInput(instructions);

        try {
            jeu.run();
        } catch (IndexOutOfBoundsException e) {
        }

        assertEquals(80 - 3 * nbJoueurs - 3, piocheWagon.size() + defausseWagon.size());
        assertEquals(60 - 7 * nbJoueurs - 3, piocheBateau.size() + defausseBateau.size());
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
    }

    @Test
    public void testPreparation5Joueurs() {
        int nbJoueurs = 5;
        setUpJeu(nbJoueurs);
        List<String> instructions = new ArrayList<String>();
        for (int i = 0; i < nbJoueurs; i++) {
            instructions.add("");
            instructions.add("20");
        }
        jeu.setInput(instructions);

        try {
            jeu.run();
        } catch (IndexOutOfBoundsException e) {
        }

        assertEquals(80 - 3 * nbJoueurs - 3, piocheWagon.size() + defausseWagon.size());
        assertEquals(60 - 7 * nbJoueurs - 3, piocheBateau.size() + defausseBateau.size());
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
    }
}
