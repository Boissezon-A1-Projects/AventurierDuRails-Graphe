package fr.umontpellier.iut.graphes;


import fr.umontpellier.iut.rails.Route;
import fr.umontpellier.iut.rails.RouteMaritime;
import fr.umontpellier.iut.rails.data.Couleur;
import fr.umontpellier.iut.rails.data.Ville;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class GrapheTest {
    private Graphe  graphe;

    @BeforeEach
    void setUp() {
        List<Arete> aretes = new ArrayList<>();
        aretes.add(new Arete(0, 1));
        aretes.add(new Arete(0, 3));
        aretes.add(new Arete(1, 2));
        aretes.add(new Arete(2, 3));
        aretes.add(new Arete(8, 42));
        graphe = new Graphe(aretes);
    }

    @Test
    void testNbAretes() {
        assertEquals(5, graphe.nbAretes());
    }


    @Test
    void testContientSommet() {
        assertTrue(graphe.contientSommet(0));
        assertTrue(graphe.contientSommet(1));
        assertTrue(graphe.contientSommet(2));
        assertTrue(graphe.contientSommet(3));
        assertTrue(graphe.contientSommet(8));
        assertTrue(graphe.contientSommet(42));
        assertFalse(graphe.contientSommet(7));
    }

    @Test
    void testAjouterSommet() {
        int nbSommets = graphe.nbSommets();
        graphe.ajouterSommet(59);
        assertTrue(graphe.contientSommet(59));
        assertEquals(nbSommets + 1, graphe.nbSommets());
        graphe.ajouterSommet(59);
        assertEquals(nbSommets + 1, graphe.nbSommets());
    }

    @Test
    void testAjouterArete() {
        int nbAretes = graphe.nbAretes();
        graphe.ajouterArete(new Arete(0, 3));
        assertEquals(nbAretes, graphe.nbAretes());
        graphe.ajouterArete(new Arete(9, 439, null));
        assertEquals(nbAretes + 1, graphe.nbAretes());
        graphe.ajouterArete(new Arete(0, 3, new RouteMaritime(new Ville("Athina", true), new Ville("Marseille", true), Couleur.ROUGE, 2) {
        }));
        assertEquals(nbAretes + 2, graphe.nbAretes());
    }

    @Test
    void testSupprimerArete() {
        int nbAretes = graphe.nbAretes();
        graphe.supprimerArete(new Arete(0, 3));
        assertEquals(nbAretes - 1, graphe.nbAretes());
        graphe.supprimerArete(new Arete(0, 3));
        assertEquals(nbAretes - 1, graphe.nbAretes());
        graphe.supprimerArete(new Arete(0, 3, null));
        assertEquals(nbAretes - 1, graphe.nbAretes());
    }

    @Test
    void testSupprimerSommet() {
        int nbSommets = graphe.nbSommets();
        int nbAretes = graphe.nbAretes();
        graphe.supprimerSommet(42);
        assertEquals(nbSommets - 1, graphe.nbSommets());
        assertEquals(nbAretes - 1, graphe.nbAretes());
        graphe.supprimerSommet(2);
        assertEquals(nbSommets - 2, graphe.nbSommets());
        assertEquals(nbAretes - 3, graphe.nbAretes());
    }

    @Test
    void testExisteArete() {
        assertTrue(graphe.existeArete(new Arete(0, 1)));
        assertTrue(graphe.existeArete(new Arete(0, 3)));
        assertTrue(graphe.existeArete(new Arete(1, 2)));
        assertTrue(graphe.existeArete(new Arete(2, 3)));
        assertTrue(graphe.existeArete(new Arete(8, 42)));
        assertFalse(graphe.existeArete(new Arete(0, 2)));
        assertFalse(graphe.existeArete(new Arete(0, 4)));
        assertFalse(graphe.existeArete(new Arete(1, 3)));
        assertFalse(graphe.existeArete(new Arete(2, 4)));
        assertFalse(graphe.existeArete(new Arete(8, 43)));
    }

    @Test
    void testGetVoisins() {
    }

    @Test
    void testdegreMax() {
        assertEquals(2, graphe.degreMax());
    }

    @Test
    void testestPasComplet() {
        assertFalse(graphe.estComplet());
    }

    @Test
    void testEstComplet(){
        List<Arete> aretes1 = new ArrayList<>();
        aretes1.add(new Arete(0, 1));
        aretes1.add(new Arete(0, 2));
        aretes1.add(new Arete(1, 2));


        Graphe graphe1 = new Graphe(aretes1);
        System.out.println(graphe1.sequenceSommets());

    }

    @Test
    void testEstSimpleSansMultiples(){
        List<Arete> aretes1 = new ArrayList<>();
        aretes1.add(new Arete(0, 1));
        aretes1.add(new Arete(0, 2));
        aretes1.add(new Arete(1, 2));


        Graphe graphe1 = new Graphe(aretes1);
        assertFalse(graphe1.estSimple());

    }
    @Test
    void testEstSimpleAvecMultiples(){
        List<Arete> aretes1 = new ArrayList<>();
        aretes1.add(new Arete(0, 1));
        aretes1.add(new Arete(0, 2));
        aretes1.add(new Arete(1, 2));
        aretes1.add(new Arete(2, 1));

        Graphe graphe1 = new Graphe(aretes1);

        assertTrue(graphe1.estSimple());
    }

    @Test
    void testEstChaine(){
        List<Arete> aretes1 = new ArrayList<>();
        aretes1.add(new Arete(0, 1));
        aretes1.add(new Arete(1, 2));
        aretes1.add(new Arete(3,2 ));
        Graphe graphe1 = new Graphe(aretes1);

        assertTrue(graphe1.estUneChaine());
    }
    @Test
    void testEstChaine2(){
        assertFalse(graphe.estUneChaine());
    }

    @Test
    void testEstCycle(){
        assertFalse(graphe.estUnCycle());
    }

    @Test
    void testEstCycle2(){
        List<Arete> aretes1 = new ArrayList<>();
        aretes1.add(new Arete(0, 1));

        aretes1.add(new Arete(1,2 ));
        aretes1.add(new Arete(2,3 ));
        aretes1.add(new Arete(3,4 ));
        aretes1.add(new Arete(4, 0));
        Graphe graphe1 = new Graphe(aretes1);

        assertTrue(graphe1.estUnCycle());
    }


    @Test
    void getClasseConnexiteSommet0(){
        Set<Integer> cl0 = new HashSet<>(); cl0.add(0); cl0.add(1); cl0.add(3); cl0.add(2);
        Set<Integer> res = graphe.getClasseConnexite(0);
        System.out.println(cl0.toString());
        System.out.println(res.toString());
        assertTrue(res.containsAll(cl0) && cl0.containsAll(res));
    }

    @Test
    void getClasseConnexiteSommet0et3(){

        Set<Integer> res0 = graphe.getClasseConnexite(0);
        Set<Integer> res3 = graphe.getClasseConnexite(3);
        System.out.println(res0.toString());
        System.out.println(res3.toString());
        assertTrue(res0.containsAll(res3) && res3.containsAll(res0));
    }

    @Test
    void getClasseConnexiteSommet8(){
        Set<Integer> cl8 = new HashSet<>(); cl8.add(42); cl8.add(8);
        Set<Integer> res = graphe.getClasseConnexite(8);
        System.out.println(cl8.toString());
        System.out.println(res.toString());
        assertTrue(res.containsAll(cl8) && cl8.containsAll(res));
    }

    @Test
    void getEnsembleClasseConnexite(){
        Set<Integer> cl0 = graphe.getClasseConnexite(0);
        Set<Integer> cl8 = graphe.getClasseConnexite(8);
        Set<Set<Integer>> ECls = new HashSet<>(); ECls.add(cl0); ECls.add(cl8);
        Set<Set<Integer>> res = graphe.getEnsembleClassesConnexite();
        assertTrue(res.containsAll(ECls) && ECls.containsAll(res));

    }

    @Test
    void testEstChaine3(){
        List<Arete> aretes1 = new ArrayList<>();
        aretes1.add(new Arete(0, 1));
        aretes1.add(new Arete(1, 2));
        aretes1.add(new Arete(0,2 ));
        aretes1.add(new Arete(3,4));
        Graphe graphe1 = new Graphe(aretes1);
        assertFalse(graphe1.estUneChaine());
    }

    @Test
    void testAUnCycle(){
        List<Arete> aretes1 = new ArrayList<>();
        aretes1.add(new Arete(0, 1));
        aretes1.add(new Arete(1, 2));
        aretes1.add(new Arete(2,3));
        aretes1.add(new Arete(3,4));
        aretes1.add(new Arete(4,5));
        aretes1.add(new Arete(5,0));

        Graphe graphe1 = new Graphe(aretes1);
        assertTrue(graphe1.aUnCycle());
    }

    @Test
    void testAUnCycle2(){
        List<Arete> aretes1 = new ArrayList<>();
        aretes1.add(new Arete(0, 1));
        aretes1.add(new Arete(1, 2));
        aretes1.add(new Arete(2,3));
        aretes1.add(new Arete(3,7));
        aretes1.add(new Arete(4,5));
        aretes1.add(new Arete(5,0));

        Graphe graphe1 = new Graphe(aretes1);
        assertFalse(graphe1.aUnCycle());
    }

    @Test
    void estUnArbre1(){
        assertFalse( graphe.estUnArbre());
    }

    @Test
    void estUnArbre2(){
        List<Arete> aretes1 = new ArrayList<>();
        aretes1.add(new Arete(0, 1));

        aretes1.add(new Arete(1,2 ));
        aretes1.add(new Arete(2,3 ));
        aretes1.add(new Arete(3,4 ));
        aretes1.add(new Arete(4, 0));
        Graphe graphe1 = new Graphe(aretes1);
        assertFalse( graphe1.estUnArbre());
    }

    @Test
    void estUnArbre3(){
        List<Arete> aretes1 = new ArrayList<>();
        aretes1.add(new Arete(0, 1));

        aretes1.add(new Arete(1,2 ));
        aretes1.add(new Arete(2,3 ));
        aretes1.add(new Arete(3,4 ));

        Graphe graphe1 = new Graphe(aretes1);
        assertTrue( graphe1.estUnArbre());
    }

    @Test
    void estUneForet1(){
        assertFalse(graphe.estUneForet());
    }

    @Test
    void estUneForet2(){
        List<Arete> aretes1 = new ArrayList<>();
        aretes1.add(new Arete(0, 1));

        aretes1.add(new Arete(1,2 ));
        aretes1.add(new Arete(2,3 ));
        aretes1.add(new Arete(3,4 ));
        aretes1.add(new Arete(3,5));
        aretes1.add(new Arete(6,7 ));
        Graphe graphe1 = new Graphe(aretes1);
        assertTrue(graphe1.estUneForet());

    }

    @Test
    void sontAdjacents1(){
        assertTrue(graphe.sontAdjacents(0,3));
    }

    @Test
    void sontAdjacents2(){
        assertFalse(graphe.sontAdjacents(0,2));
    }

    @Test
    void sontAdjacents3(){
        assertFalse(graphe.sontAdjacents(8,3));
    }

    @Test
    void estIsthme(){
        Arete a = new Arete(8,42);
        assertTrue(graphe.estUnIsthme(a));
    }

    @Test
    void estIsthme2(){
        System.out.println(graphe.toString());
        Arete a = new Arete(0,1);
        assertFalse(graphe.estUnIsthme(a));
        System.out.println(graphe.toString());

    }



    @Test
    void fusionnerSommet1(){

        System.out.println(graphe);
        graphe.fusionnerSommets(42,8);
        System.out.println(graphe);
    }


    @Test
    void equals(){
        Arete a1 = new Arete(1,2);
        Arete a2 = new Arete(2,1);
        assertTrue(a1.equals(a2));
    }

    @Test
    void contains(){
        HashSet<Arete> aretes = new HashSet<>();
        Arete a1 = new Arete(1,2);
        aretes.add(a1);
        Arete a2 = new Arete(2,1);
        assertTrue(a1.equals(a2));
    }

    @Test
    void sequenceEstGraphe1(){
        List<Integer> s = new ArrayList<>(); s.add(1); s.add(1);s.add(2);s.add(2);s.add(3);s.add(3);
        System.out.println(s);
        assertTrue(Graphe.sequenceEstGraphe(s));

    }

    @Test
    void sequenceEstGraphe2(){
        List<Integer> s = new ArrayList<>(); s.add(1); s.add(1);s.add(2);s.add(2);s.add(3);
        System.out.println(s);
        assertFalse(Graphe.sequenceEstGraphe(s));

    }


    @Test
    void sontIsomorphes(){
        Arete a1 = new Arete(0,2);
        Arete a2 = new Arete(0,1);
        Arete a3 = new Arete(1,3);
        Arete a4 = new Arete(3,4);
        Arete a5 = new Arete(4,5);
        Arete a6 = new Arete(4,2);
        Arete a7 = new Arete(5,2);
        HashSet<Arete> aretes = new HashSet<>(); aretes.add(a1);aretes.add(a2);aretes.add(a3);aretes.add(a4);aretes.add(a5);aretes.add(a6);aretes.add(a7);
        Graphe g1 = new Graphe(aretes);


        Arete a8 = new Arete(0,5);
        Arete a9 = new Arete(0,1);
        Arete a10 = new Arete(1,2);
        Arete a11 = new Arete(1,5);
        Arete a12 = new Arete(4,5);
        Arete a13 = new Arete(3,2);
        Arete a14 = new Arete(4,3);
        HashSet<Arete> aretes2 = new HashSet<>(); aretes2.add(a8);aretes2.add(a9);aretes2.add(a10);aretes2.add(a11);aretes2.add(a12);aretes2.add(a13);aretes2.add(a14);
        Graphe g2 = new Graphe(aretes2);
        assertTrue(Graphe.sontIsomorphes(g1,g2));
    }

    @Test
    void sontIsomorphes2(){
        Arete a1 = new Arete(0,1);
        Arete a2 = new Arete(0,2);
        Arete a3 = new Arete(1,2);
        Arete a4 = new Arete(2,3);
        HashSet<Arete> aretes = new HashSet<>(); aretes.add(a1);aretes.add(a2);aretes.add(a3);aretes.add(a4);
        Graphe g1 = new Graphe(aretes);


        Arete a8 = new Arete(0,1);
        Arete a9 = new Arete(1,2);
        Arete a10 = new Arete(1,3);
        Arete a11 = new Arete(2,3);
        HashSet<Arete> aretes2 = new HashSet<>(); aretes2.add(a8);aretes2.add(a9);aretes2.add(a10);aretes2.add(a11);
        Graphe g2 = new Graphe(aretes2);
        assertTrue(Graphe.sontIsomorphes(g1,g2));
    }

    @Test
    void sontIsomorphes3(){
        Arete a1 = new Arete(0,1);
        Arete a2 = new Arete(0,2);
        Arete a3 = new Arete(1,2);
        Arete a4 = new Arete(2,3);
        HashSet<Arete> aretes = new HashSet<>(); aretes.add(a1);aretes.add(a2);aretes.add(a3);aretes.add(a4);
        Graphe g1 = new Graphe(aretes);


        Arete a8 = new Arete(0,1);
        Arete a9 = new Arete(1,2);
        Arete a10 = new Arete(1,3);

        HashSet<Arete> aretes2 = new HashSet<>(); aretes2.add(a8);aretes2.add(a9);aretes2.add(a10);
        Graphe g2 = new Graphe(aretes2);
        assertFalse(Graphe.sontIsomorphes(g1,g2));
    }

    @Test
    void sontIsomorphe4(){
        Arete a1 = new Arete(0,2);
        Arete a2 = new Arete(0,1);
        Arete a3 = new Arete(1,2);
        Arete a4 = new Arete(3,4);
        Arete a5 = new Arete(5,6);
        Arete a6 = new Arete(5,7);
        HashSet<Arete> aretes = new HashSet<>(); aretes.add(a1);aretes.add(a2);aretes.add(a3);aretes.add(a4);aretes.add(a5);aretes.add(a6);
        Graphe g1 = new Graphe(aretes);

        Arete a8 = new Arete(0,1);
        Arete a9 = new Arete(1,2);
        Arete a10 = new Arete(3,4);
        Arete a11 = new Arete(3,5);
        Arete a12 = new Arete(4,5);
        Arete a13 = new Arete(6,7);
        HashSet<Arete> aretes2 = new HashSet<>(); aretes2.add(a8);aretes2.add(a9);aretes2.add(a10);aretes2.add(a11);aretes2.add(a12);aretes2.add(a13);
        Graphe g2 = new Graphe(aretes2);
        assertTrue(Graphe.sontIsomorphes(g1,g2));
    }

    @Test
    void sontIsomorphes5(){
        Arete a1 = new Arete(0,2);
        Arete a2 = new Arete(0,1);
        Arete a3 = new Arete(1,2);
        Arete a4 = new Arete(3,4);
        Arete a5 = new Arete(5,6);
        Arete a6 = new Arete(5,7);
        Arete a7 = new Arete(6,7);
        HashSet<Arete> aretes = new HashSet<>(); aretes.add(a1);aretes.add(a2);aretes.add(a3);aretes.add(a4);aretes.add(a5);aretes.add(a6);aretes.add(a7);
        Graphe g1 = new Graphe(aretes);

        Arete a8 = new Arete(0,1);
        Arete a9 = new Arete(1,2);
        Arete a10 = new Arete(3,4);
        Arete a11 = new Arete(3,5);
        Arete a12 = new Arete(4,5);
        Arete a13 = new Arete(6,7);
        HashSet<Arete> aretes2 = new HashSet<>(); aretes2.add(a8);aretes2.add(a9);aretes2.add(a10);aretes2.add(a11);aretes2.add(a12);aretes2.add(a13);
        Graphe g2 = new Graphe(aretes2);
        assertFalse(Graphe.sontIsomorphes(g1,g2));
    }

    @Test
    void sontIsomorphes6(){
        Arete a1 = new Arete(0,3);
        Arete a2 = new Arete(2,1);
        Arete a3 = new Arete(3,2);
        Arete a4 = new Arete(3,4);
        Arete a5 = new Arete(5,6);
        Arete a6 = new Arete(5,4);
        Arete a7 = new Arete(8,7);
        Arete a8 = new Arete(8,9);
        Arete a9 = new Arete(9,10);
        Arete a10 = new Arete(8,10);
        HashSet<Arete> aretes = new HashSet<>(); aretes.add(a1);aretes.add(a2);aretes.add(a3);aretes.add(a4);aretes.add(a5);aretes.add(a6);aretes.add(a7); aretes.add(a8); aretes.add(a9); aretes.add(a10);
        Graphe g1 = new Graphe(aretes);


        Arete a11 = new Arete(1,0);
        Arete a12 = new Arete(0,2);
        Arete a13 = new Arete(2,5);
        Arete a14 = new Arete(5,3);
        Arete a15 = new Arete(5,6);
        Arete a16 = new Arete(6,4);
        Arete a17 = new Arete(10,7);
        Arete a18 = new Arete(10,8);
        Arete a19 = new Arete(7,8);
        Arete a20 = new Arete(9,8);

        HashSet<Arete> aretes2 = new HashSet<>();aretes2.add(a11);aretes2.add(a12);aretes2.add(a13);aretes2.add(a14);aretes2.add(a15);aretes2.add(a16);aretes2.add(a17);aretes2.add(a18);aretes2.add(a19);aretes2.add(a20);
        Graphe g2 = new Graphe(aretes2);
        assertTrue(Graphe.sontIsomorphes(g1,g2));
    }


}
