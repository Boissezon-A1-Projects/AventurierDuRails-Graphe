package fr.umontpellier.iut.graphes;

import fr.umontpellier.iut.rails.Joueur;
import fr.umontpellier.iut.rails.Route;
import fr.umontpellier.iut.rails.data.Couleur;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DijkstraTest
{

    private Graphe graphe1, graphe2, graphe3;

    @BeforeEach
    public void initGraphes()
    {
        graphe1 = new Graphe(7);
        List<Arete> aretes = Arrays.asList(
                new Arete(0, 1, new RouteTest(1)),
                new Arete(0, 2, new RouteTest(2)),
                new Arete(1, 3, new RouteTest(2)),
                new Arete(1, 5, new RouteTest(3)),
                new Arete(2, 3, new RouteTest(3)),
                new Arete(2, 4, new RouteTest(4)),
                new Arete(3, 4, new RouteTest(2)),
                new Arete(3, 5, new RouteTest(3)),
                new Arete(3, 6, new RouteTest(1)),
                new Arete(4, 6, new RouteTest(5)),
                new Arete(5, 6, new RouteTest(4))
        );
        aretes.forEach(arete -> graphe1.ajouterArete(arete));
    }


    @Test
    public void testChemin1(){
        List<Integer> listeSommets = new ArrayList<>();
        listeSommets.add(0);
        listeSommets.add(3);
        listeSommets.add(2);
        listeSommets.add(6);
        listeSommets.add(2);
        listeSommets.add(3);


        System.out.println(graphe1.parcoursSansRepetition(listeSommets));
        assertTrue(true);
    }

    @Test
    public void testDijsktraNonPondere1()
    {
        List<Integer> correct1 = Arrays.asList(0, 1, 3, 6);
        List<Integer> correct2 = Arrays.asList(0, 2, 3, 6);
        List<Integer> correct3 = Arrays.asList(0, 1, 5, 6);
        List<Integer> correct4 = Arrays.asList(0, 2, 4, 6);

        List<Integer> resultat = graphe1.parcoursSansRepetition(0, 6, false);

        assertTrue(sameOrder(correct1, resultat) || sameOrder(correct2, resultat) ||
                sameOrder(correct3, resultat) || sameOrder(correct4, resultat));
    }

    @Test
    public void testDijsktraNonPondere2()
    {
        assertTrue(sameOrder(Arrays.asList(0, 2, 4), graphe1.parcoursSansRepetition(0, 4, false)));
    }

    @Test
    public void testDijsktraNonPondere3()
    {
        assertTrue(sameOrder(Collections.singletonList(0), graphe1.parcoursSansRepetition(0, 0, false)));
    }

    @Test
    public void testDijsktraPondere1()
    {
        assertTrue(sameOrder(Arrays.asList(0, 1, 3, 6), graphe1.parcoursSansRepetition(0, 6, true)));
    }

    private static boolean sameOrder(Iterable<Integer> listeAttendue, Iterable<Integer> listeObtenue)
    {
        Iterator<Integer> it1 = listeAttendue.iterator();
        Iterator<Integer> it2 = listeObtenue.iterator();
        while (it1.hasNext() && it2.hasNext())
        {
            int elem1 = it1.next();
            int elem2 = it2.next();

            if (elem1 != elem2)
            {
                return false;
            }

        }
        return !it1.hasNext() && !it2.hasNext();
    }

    public static class RouteTest extends Route
    {

        public RouteTest(int longueur)
        {
            super(null, null, Couleur.GRIS, longueur);
        }

        @Override
        public void payerPar(Joueur joueur)
        {
            throw new RuntimeException("This function shouldn't be used as a regular Route.");
        }

    }

}
