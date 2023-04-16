package fr.umontpellier.iut.rails;

import fr.umontpellier.iut.rails.Joueur.CouleurJouer;
import fr.umontpellier.iut.rails.data.CarteTransport;
import fr.umontpellier.iut.rails.data.Destination;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.List;

@Timeout(value = 1, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
public class SecretPartieCompleteTest {

    @BeforeEach
    public void setUp() {
        // réinitialisation des compteurs
        TestUtils.setAttribute(CarteTransport.class, "compteur", 1);
        TestUtils.setAttribute(Destination.class, "compteur", 1);
        TestUtils.setAttribute(Route.class, "compteur", 1);
    }

    @Test
    // @RepeatedTest(100)
    void testPartieSimple2Joueurs() {
        IOJeu jeu = new IOJeu(new String[] { "J1", "J2" });
        TestUtils.setJoueurs(jeu, List.of(
                new JoueurAutoSimple("J1", jeu, CouleurJouer.JAUNE),
                new JoueurAutoSimple("J2", jeu, CouleurJouer.ROUGE)));

        jeu.setInput("", "20", "", "20");
        jeu.run();
        TestUtils.testIntegrite(jeu);
    }

    @Test
    // @RepeatedTest(100)
    void testPartieAleatoire2Joueurs() {
        IOJeu jeu = new IOJeu(new String[] { "J1", "J2" });
        TestUtils.setJoueurs(jeu, List.of(
                new JoueurAutoAleatoire("J1", jeu, CouleurJouer.JAUNE),
                new JoueurAutoAleatoire("J2", jeu, CouleurJouer.ROUGE)));

        jeu.setInput("", "20", "", "20");
        jeu.run();
        TestUtils.testIntegrite(jeu);
    }

    @Test
    // @RepeatedTest(100)
    void testPartieAleatoire5Joueurs() {
        IOJeu jeu = new IOJeu(new String[] { "J1", "J2", "J3", "J4", "J5" });
        TestUtils.setJoueurs(jeu, List.of(
                new JoueurAutoAleatoire("J1", jeu, CouleurJouer.JAUNE),
                new JoueurAutoAleatoire("J2", jeu, CouleurJouer.ROUGE),
                new JoueurAutoAleatoire("J3", jeu, CouleurJouer.BLEU),
                new JoueurAutoAleatoire("J4", jeu, CouleurJouer.VERT),
                new JoueurAutoAleatoire("J5", jeu, CouleurJouer.ROSE)));

        jeu.setInput("", "20", "", "20", "", "20", "", "20", "", "20");
        jeu.run();
        TestUtils.testIntegrite(jeu);
    }
}
