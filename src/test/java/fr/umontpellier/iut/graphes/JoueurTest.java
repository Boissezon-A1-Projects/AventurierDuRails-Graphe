package fr.umontpellier.iut.graphes;

import fr.umontpellier.iut.rails.data.Plateau;
import org.junit.jupiter.api.Test;

public class JoueurTest {

    private Plateau p = Plateau.makePlateauMonde();

    @Test
    void getGraphePlateau(){
        System.out.println(p.getGraphe());
    }

}
