package fr.umontpellier.iut.graphes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class GrapheProfTest {
    private Graphe grapheAbstrait;
    private List<Arete> listeAretesGrapheAbstrait;

    @BeforeEach
    void setUp() {
        grapheAbstrait = new Graphe(5);
        listeAretesGrapheAbstrait = Arrays.asList(new Arete(0, 1),
                new Arete(0, 4),
                new Arete(1, 2),
                new Arete(1, 4),
                new Arete(2, 4),
                new Arete(2, 5),
                new Arete(3, 4),
                new Arete(5, 1),
                new Arete(5, 3));
        listeAretesGrapheAbstrait.forEach(a -> grapheAbstrait.ajouterArete(a));
    }

    private boolean collectionsDansLeMemeOrdre(Iterable<Integer> listeAttendue, Iterable<Integer> listeObtenue) {
        Iterator<Integer> it1 = listeAttendue.iterator();
        Iterator<Integer> it2 = listeObtenue.iterator();
        while (it1.hasNext() && it2.hasNext()) {
            int elem1 = it1.next();
            int elem2 = it2.next();
            if (elem1 != elem2) {
                return false;
            }
        }
        return !it1.hasNext() && !it2.hasNext();
    }

    @Test
    public void test_parcoursSansRepetition_sous_liste() {
        List<Integer> sousListe = Arrays.asList(0, 2, 3);
        List<Integer> parcoursAttendu1 = Arrays.asList(0, 1, 2, 4, 3);
        List<Integer> parcoursAttendu2 = Arrays.asList(0, 1, 2, 5, 3);
        List<Integer> parcoursAttendu3 = Arrays.asList(0, 4, 2, 5, 3);

        List<Integer> resultat = grapheAbstrait.parcoursSansRepetition(sousListe);

        assertTrue(collectionsDansLeMemeOrdre(resultat, parcoursAttendu1) ||
                collectionsDansLeMemeOrdre(resultat, parcoursAttendu2) ||
                collectionsDansLeMemeOrdre(resultat, parcoursAttendu3)
        );

    }

    @Test
    public void test_parcoursSansRepetition_simple() {
        List<Integer> parcoursAttenduCorrecte = Arrays.asList(0, 1, 2);
        List<Integer> parcoursAttenduCorrecte1 = Arrays.asList(0, 4, 2);
        List<Integer> parcoursAttenduIncorrecte1 = Arrays.asList(0, 4, 1, 2);
        List<Integer> parcoursAttenduIncorrecte2 = Arrays.asList(0, 1, 4, 2);
        List<Integer> parcoursAttenduIncorrecte3 = Arrays.asList(2, 1, 0);

        List<Integer> resultat = grapheAbstrait.parcoursSansRepetition(0, 2, false);
        assertTrue(collectionsDansLeMemeOrdre(parcoursAttenduCorrecte, resultat) ||
                collectionsDansLeMemeOrdre(parcoursAttenduCorrecte1, resultat));
        assertFalse(collectionsDansLeMemeOrdre(parcoursAttenduIncorrecte1, resultat) &&
                collectionsDansLeMemeOrdre(parcoursAttenduIncorrecte2, resultat) &&
                collectionsDansLeMemeOrdre(parcoursAttenduIncorrecte3, resultat));
    }
}
