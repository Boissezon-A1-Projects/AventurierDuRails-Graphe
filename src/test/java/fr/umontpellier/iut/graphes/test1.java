package fr.umontpellier.iut.graphes;

import static org.junit.Assert.assertTrue;
import fr.umontpellier.iut.rails.*;
import fr.umontpellier.iut.rails.data.Couleur;
import fr.umontpellier.iut.rails.data.Destination;
import fr.umontpellier.iut.rails.data.Plateau;
import fr.umontpellier.iut.rails.data.Ville;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


public class test1 {

    private Graphe  graphe;
    private static Joueur joueur;
    private static Destination d;
    private static Destination d1;
    private static List<Route> r;
    private static List<Route> r2;
    private static List<Route> r3;
    private static Jeu jeu;

    @BeforeAll
    static void depart() {
        String[] listeJ = new String[1];
        listeJ[0] = "A";
        jeu = new Jeu(listeJ);
        joueur = new Joueur("A",jeu, Joueur.CouleurJouer.ROUGE);
        d = new Destination("48","50",10);
        d1 = new Destination("48","56",10);

        r = new ArrayList<>();
        Ville v1 = new Ville("a",false);
        Ville v2 = new Ville("b",false);
        Ville v3 = new Ville("c",false);
        r.add(new RouteTerrestre(v1,v2,Couleur.GRIS,1));
        r.add(new RouteTerrestre(v3,v2,Couleur.GRIS,1));

        r2 = new ArrayList<>();
        Ville v4 = new Ville("d",false);
        Ville v5 = new Ville("e",false);
        Ville v6 = new Ville("f",false);
        Ville v7 = new Ville("f",false);
        Ville v8 = new Ville("f",false);
        Ville v9 = new Ville("f",false);
        r2.add(new RouteTerrestre(v1,v2,Couleur.GRIS,1));
        r2.add(new RouteTerrestre(v3,v2,Couleur.GRIS,1));
        r2.add(new RouteTerrestre(v4,v2,Couleur.GRIS,1));
        r2.add(new RouteTerrestre(v7,v2,Couleur.GRIS,1));
        r2.add(new RouteTerrestre(v4,v5,Couleur.GRIS,1));
        r2.add(new RouteTerrestre(v4,v9,Couleur.GRIS,1));
        r2.add(new RouteTerrestre(v7,v6,Couleur.GRIS,1));
        r2.add(new RouteTerrestre(v7,v8,Couleur.GRIS,1));
        r2.add(new RouteTerrestre(v8,v9,Couleur.GRIS,1));

        r3 = new ArrayList<>();
        r3.add(new RouteTerrestre(v1,v2,Couleur.GRIS,1));
        r3.add(new RouteTerrestre(v3,v2,Couleur.GRIS,1));
        r3.add(new RouteTerrestre(v4,v3,Couleur.GRIS,1));
        r3.add(new RouteTerrestre(v4,v5,Couleur.GRIS,1));
        r3.add(new RouteTerrestre(v1,v5,Couleur.GRIS,1));
        r3.add(new RouteTerrestre(v5,v2,Couleur.GRIS,1));
        r3.add(new RouteTerrestre(v2,v4,Couleur.GRIS,1));
        r3.add(new RouteTerrestre(v7,v8,Couleur.GRIS,1));
        r3.add(new RouteTerrestre(v9,v8,Couleur.GRIS,1));
        r3.add(new RouteTerrestre(v7,v6,Couleur.GRIS,1));
    }
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
    void nbSommets() {
        assertEquals(6, graphe.nbSommets());
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
        List<Integer> voisins0 = Arrays.asList(1,3);
        List<Integer> voisin1 = Arrays.asList(0,2);
        assertEquals(2, graphe.getVoisins(0).size());
        assertTrue(graphe.getVoisins(0).containsAll(voisins0));
        assertEquals(2, graphe.getVoisins(1).size());
        assertTrue(graphe.getVoisins(1).containsAll(voisin1));

    }


    @Test
    void estUneChaine() {
        List<Arete> aretes = Arrays.asList(new Arete(0,1), new Arete(1,2), new Arete(2,3));
        Graphe graphe = new Graphe(aretes);
        assertTrue(graphe.estUneChaine());

        graphe.ajouterArete(new Arete(0,2));
        assertFalse(graphe.estUneChaine());

        graphe.ajouterArete(new Arete(3,0));
        graphe.supprimerArete(new Arete(0,2));

        assertFalse(graphe.estUneChaine());
    }

    @Test
    void estComplet() {
        Graphe graphe1 = new Graphe(4);
        assertFalse(graphe1.estComplet());

        List<Arete> aretes = Arrays.asList(new Arete(0,1),
                new Arete(0,2),
                new Arete(0, 3),
                new Arete(1,2),
                new Arete(1,3));

        Graphe graphe2 = new Graphe(aretes);

        assertFalse(graphe2.estComplet());

        graphe2.ajouterArete(new Arete(2,3));
        assertTrue(graphe2.estComplet());

    }

    // Todo : un graphe avec deux sommets non vide est-il un cycle ?
    @Test
    void estUnCycle() {
        Graphe grapheVide = new Graphe(0);
        assertTrue(grapheVide.estUnCycle());

        grapheVide.ajouterArete(new Arete(0,1));
        assertFalse(grapheVide.estUnCycle());

        List<Arete> aretes = Arrays.asList(new Arete(0,1),
                new Arete(1,2),
                new Arete(2,3));

        Graphe graphe = new Graphe(aretes);
        assertFalse(graphe.estUnCycle());

        graphe.ajouterArete(new Arete(0,3));
        assertTrue(graphe.estUnCycle());

        graphe.supprimerArete(new Arete(0,3));
        assertFalse(graphe.estUnCycle());

        List<Arete> aretes2 = Arrays.asList(new Arete(0,1),
                new Arete(0,2),
                new Arete(2,3),
                new Arete(2,4));

        Graphe graphe1 = new Graphe(aretes2);
        assertFalse(graphe1.estUnCycle());
    }

    @Test
    void estUneForet() {
        List<Arete> aretes = Arrays.asList(new Arete(0,1),
                new Arete(0,2),
                new Arete(2,3),
                new Arete(2,4),
                new Arete(5,6));

        Graphe graphe1 = new Graphe(aretes);
        assertTrue(graphe1.estUneForet());

        graphe1.ajouterArete(new Arete(2,1));
        assertFalse(graphe1.estUneForet());

        assertFalse(jeu.getPlateau().getGraphe().estUneForet());
    }

    @Test
    void getClasseConnexite() {
        List<Integer> classeConnexite = Arrays.asList(0,1,3,2);
        List<Integer> classeConnexite2 = Arrays.asList(8,42);

        assertTrue(graphe.getClasseConnexite(0).containsAll(classeConnexite));
        assertTrue(graphe.getClasseConnexite(1).containsAll(classeConnexite));
        assertTrue(graphe.getClasseConnexite(2).containsAll(classeConnexite));
        assertTrue(graphe.getClasseConnexite(3).containsAll(classeConnexite));
        assertFalse(graphe.getClasseConnexite(0).contains(8));
        assertFalse(graphe.getClasseConnexite(0).contains(42));

        assertTrue(graphe.getClasseConnexite(8).containsAll(classeConnexite2));
    }

    @Test
    void sontAdjacents() {
        assertTrue(graphe.sontAdjacents(0,1));
        assertTrue(graphe.sontAdjacents(1,0));
        assertFalse(graphe.sontAdjacents(2,0));
    }

    @Test
    void sontIsomorphes() {
        Graphe g0 = new Graphe();
        Graphe g0bis = new Graphe();

        assertTrue(Graphe.sontIsomorphes(g0, g0bis));

        List<Arete> aretesG1 = Arrays.asList(
                new Arete(1,2),
                new Arete(2,5),
                new Arete(2,3),
                new Arete(3,4),
                new Arete(4,5)
        );
        List<Arete> aretesG2 = Arrays.asList(
                new Arete(1,2),
                new Arete(1,3),
                new Arete(2,4),
                new Arete(3,4),
                new Arete(4,5)
        );
        Graphe g1 = new Graphe(aretesG1);
        Graphe g2 = new Graphe(aretesG2);

        assertTrue(Graphe.sontIsomorphes(g1, g2));

        Graphe g3 = Plateau.makePlateauMonde().getGraphe();
        assertTrue(Graphe.sontIsomorphes(g3, jeu.getPlateau().getGraphe()));
    }

    @Test
    void neSontPasIsomorphes() {
        Graphe g2 = new Graphe(2);
        // Deux graphes qui n'ont pas le même nombre de sommets
        //assertFalse(Graphe.sontIsomorphes(graphe, g2));

        List<Arete> aretesG3 = Arrays.asList(
                new Arete(1,2),
                new Arete(2,3),
                new Arete(3,1),
                new Arete(4,5)
        );
        List<Arete> aretesG4 = Arrays.asList(
                new Arete(1,2),
                new Arete(2,3),
                new Arete(3,4),
                new Arete(5,4),
                new Arete(4,5)
        );

        Graphe g3 = new Graphe(aretesG3);
        Graphe g4 = new Graphe(aretesG4);

        // Deux graphes avec la même séquence
        assertFalse(Graphe.sontIsomorphes(g3,g4));

        List<Arete> aretesG5 = Arrays.asList(
                new Arete(1,2),
                new Arete(2,3),
                new Arete(3,4),
                new Arete(1,3),
                new Arete(1,4),
                new Arete(3,5),
                new Arete(4,5),
                new Arete(6,7),
                new Arete(7,8),
                new Arete(8,9)
        );

        List<Arete> aretesG6 = Arrays.asList(
                new Arete(1,2),
                new Arete(2,3),
                new Arete(2,4),
                new Arete(3,5),
                new Arete(3,6),
                new Arete(3,7),
                new Arete(5,8),
                new Arete(6,9),
                new Arete(7,9),
                new Arete(7,4)
        );

        Graphe g5 = new Graphe(aretesG5);
        Graphe g6 = new Graphe(aretesG6);

        //assertFalse(Graphe.sontIsomorphes(g5,g6));

        List<Arete> aretes7 = Arrays.asList(
                new Arete(1,2),
                new Arete(2,3),
                new Arete(3,4),
                new Arete(3,5),
                new Arete(5,6)
        );

        Graphe g7 = new Graphe(aretes7);

        List<Arete> aretes8 = Arrays.asList(
                new Arete(1,2),
                new Arete(2,3),
                new Arete(3,4),
                new Arete(4,5),
                new Arete(4,6)
        );

        Graphe g8 = new Graphe(aretes8);

        assertFalse(Graphe.sontIsomorphes(g8,g7));

    }

    @Test
    void estGraphe() {
        List<Integer> sequence0 = new ArrayList<>();
        List<Integer> sequence0bis = Arrays.asList(0,0);
        List<Integer> sequence1 = Arrays.asList(0,1,1);
        List<Integer> sequence2 = Arrays.asList(1,1,1);
        List<Integer> sequence3 = Arrays.asList(1,2,2);
        List<Integer> sequence4 = Arrays.asList(0,1,1,1,4);

        assertTrue(Graphe.sequenceEstGraphe(sequence1));
        assertTrue(Graphe.sequenceEstGraphe(sequence0bis));

        assertFalse(Graphe.sequenceEstGraphe(sequence0));
        assertFalse(Graphe.sequenceEstGraphe(sequence2));
        assertFalse(Graphe.sequenceEstGraphe(sequence3));
        assertFalse(Graphe.sequenceEstGraphe(sequence4));
    }




    @Test
    void parcoursSansRepetition1() {
        assertTrue(graphe.parcoursSansRepetition(0,8,false).isEmpty());
        assertEquals(2, graphe.parcoursSansRepetition(0,1, false).size());
    }

    @Test
    void parcoursSansRepetition1_avecCycles() {
        Ville v1 = new Ville("1", false);
        Ville v2 = new Ville("2", false);
        Ville v3 = new Ville("3", false);
        Ville v4 = new Ville("4", false);
        Ville v5 = new Ville("5", false);
        Ville v6 = new Ville("6", false);
        Ville v7 = new Ville("7", false);
        Ville v8 = new Ville("8", false);
        Ville v9 = new Ville("9", false);
        Ville v10 = new Ville("10", false);
        Ville v11 = new Ville("11", false);
        Ville v12 = new Ville("12", false);

        List<Ville> villes = Arrays.asList(v1,v2,v3,v4,v5,v6,v7,v8,v9,v10,v11,v12);

        List<Route> routes = Arrays.asList(
                new RouteMaritime(v1,v4, Couleur.BLANC, 3),
                new RouteTerrestre(v4,v5, Couleur.ROUGE, 4),
                new RouteTerrestre(v5,v6, Couleur.VIOLET, 7),
                new RouteMaritime(v2,v6, Couleur.VIOLET, 3),
                new RouteMaritime(v2,v4, Couleur.VERT, 1),
                new RouteMaritime(v1, v2, Couleur.VERT, 2),
                new RouteMaritime(v1,v3, Couleur.JAUNE, 4),
                new RouteTerrestre(v3,v2, Couleur.ROUGE, 1),
                new RouteTerrestre(v3,v12, Couleur.JAUNE, 3),
                new RouteTerrestre(v12,v2, Couleur.JAUNE, 2),
                new RouteTerrestre(v12, v7, Couleur.VIOLET, 1),
                new RouteTerrestre(v6,v7, Couleur.VERT, 3),
                new RouteMaritime(v6,v9, Couleur.ROUGE, 2),
                new RouteTerrestre(v9,v8, Couleur.NOIR, 1),
                new RouteTerrestre(v7,v8, Couleur.VIOLET, 5),
                new RouteMaritime(v7,v10, Couleur.BLANC, 1),
                new RouteTerrestre(v11,v10, Couleur.ROUGE, 2),
                new RouteTerrestre(v11,v12, Couleur.VIOLET, 1)
        );

        Plateau p = new Plateau(villes, routes);
        Graphe g = p.getGraphe();
        List<Integer> chemin = g.parcoursSansRepetition(v7.getId(),v2.getId(), false);
        List<Integer> chemin2 = g.parcoursSansRepetition(v7.getId(),v2.getId(), true);
    }

    @Test
    void parcoursSansRepetition1_plateau() {
        Graphe g = joueur.getJeu().getPlateau().getGraphe();
        List<Integer> chemin = g.parcoursSansRepetition(1,2,false);
        List<Integer> chemin2 = g.parcoursSansRepetition(1,2,true);
    }

    @Test
    void parcoursSansRepetition2() {
        Ville v1 = new Ville("v1", false);
        Ville v2 = new Ville("v2", false);
        Ville v3 = new Ville("v3", false);
        Ville v4 = new Ville("v4", false);
        Ville v5 = new Ville("v5", false);
        Ville v6 = new Ville("v6", false);

        Route r1 = new RouteTerrestre(v1,v2,Couleur.ROUGE, 1);
        Route r2 = new RouteTerrestre(v2,v4,Couleur.ROUGE,3);
        Route r3 = new RouteMaritime(v4, v5,Couleur.ROUGE, 4);
        Route r4 = new RouteMaritime(v5, v6,Couleur.ROUGE, 1);
        Route r5 = new RouteMaritime(v2, v3,Couleur.ROUGE, 4);
        Route r6 = new RouteMaritime(v3, v5, Couleur.ROUGE, 2);

        List<Route> ensembleDeRoutes = Arrays.asList(r1,r2,r3,r4,r5,r6);

        Plateau plateau = new Plateau(null, null);

        Graphe graphe = plateau.getGraphe(ensembleDeRoutes);

        // Il n'existe pas assez de bateaux et de wagons
        assertTrue(graphe.parcoursSansRepetition(v1.getId(), v6.getId(), 0, 0).isEmpty());
        assertTrue(graphe.parcoursSansRepetition(v1.getId(), v4.getId(), 0,4).isEmpty());

        // Il existe assez de wagons et de bateaux pour prendre le plus court chemin
        assertEquals(2, graphe.parcoursSansRepetition(v1.getId(), v2.getId(), 1, 0).size());

        List<Integer> res1 = Arrays.asList(v1.getId(), v2.getId(), v3.getId(), v5.getId());
        List<Integer> res2 = graphe.parcoursSansRepetition(v1.getId(), v5.getId(), 8, 8);

        assertTrue(collectionsDansLeMemeOrdre(res2, res1));

        res1 = Arrays.asList(v1.getId(), v2.getId(), v4.getId(), v5.getId());
        res2 = graphe.parcoursSansRepetition(v1.getId(), v5.getId(), 4,4);

        // Il n'existe pas assez de wagons et de bateaux pour prendre le plus court chemin donc on prend le prochain plus court chemin
        assertTrue(collectionsDansLeMemeOrdre(res1, res2));

        // Il n'y a pas de chemin pour atteindre le sommet
        graphe.ajouterSommet(34);
        assertTrue(graphe.parcoursSansRepetition(0, 34, 100, 100).isEmpty());

    }

    @Test
    void parcours_sans_repetition2_sommets_22_34() {
        Graphe g = jeu.getPlateau().getGraphe();
        List<Integer> chemin = g.parcoursSansRepetition(22,34,0,7);
        assertFalse(chemin.isEmpty());
    }

    @Test
    void parcoursSansRepetition2_sommets_10_20() {
        Graphe g = jeu.getPlateau().getGraphe();
        List<Integer> chemin = g.parcoursSansRepetition(10,20, 0, 6);
        assertFalse(chemin.isEmpty());
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
    void parcoursSansRepetition3_cheminsInexistants() {
        // Chemins qui n'existent pas
        assertTrue(graphe.parcoursSansRepetition(Arrays.asList(0,48)).isEmpty());
        assertTrue(graphe.parcoursSansRepetition(Arrays.asList(0,1,48)).isEmpty());
    }

    @Test
    void parcoursSansRepetition3_cheminsExistants() {

        // Chemins qui existent
        List<Integer> resAttendu = Arrays.asList(0,1,2,3);
        List<Integer> chemin = graphe.parcoursSansRepetition(Arrays.asList(0,1,3));

        assertTrue(collectionsDansLeMemeOrdre(resAttendu, chemin));

        List<Arete> aretes = Arrays.asList(
                new Arete(1,2),
                new Arete(2,3),
                new Arete(3,9),
                new Arete(9,8),
                new Arete(8,10),
                new Arete(8,7),
                new Arete(7,3),
                new Arete(7,6),
                new Arete(7,4),
                new Arete(6,4),
                new Arete(4,2),
                new Arete(4,5),
                new Arete(2,5)
        );

        Graphe g = new Graphe(aretes);

        chemin = g.parcoursSansRepetition(Arrays.asList(1,7,10));

        Iterator<Integer> it1 = chemin.iterator();
        Iterator<Integer> it2 = Arrays.asList(1,7,10).iterator();

        assertEquals(it1.next(), it2.next());

        while (it2.hasNext() && it1.hasNext()) {
            Integer sommet = it1.next();
            if (Arrays.asList(1,7,10).contains(sommet)) {
                assertEquals(it2.next(), sommet);
            }
        }

    }

    @Test
    void parcoursSansRepetition3_passerPlusieursFoisParMemeSommet() {
        List<Arete> aretes = Arrays.asList(
                new Arete(1,2),
                new Arete(2,3),
                new Arete(2,4)
        );

        Graphe graphe = new Graphe(aretes);

        List<Integer> chemin = graphe.parcoursSansRepetition(Arrays.asList(4,3,1));
        assertFalse(chemin.isEmpty());

        List<Arete> aretes1 = Arrays.asList(
                new Arete(1,2),
                new Arete(3,2),
                new Arete(2,4),
                new Arete(5,2),
                new Arete(6,5),
                new Arete(7,6)
        );

        graphe = new Graphe(aretes1);

        assertFalse(graphe.parcoursSansRepetition(Arrays.asList(1,3,4,7)).isEmpty());
    }

    @Test
    void parcoursSansRepetition3_chainePossible() {
        List<Arete> aretes = Arrays.asList(
                new Arete(1,2),
                new Arete(2,3),
                new Arete(3,4)
        );

        Graphe g = new Graphe(aretes);

        assertFalse(g.parcoursSansRepetition(Arrays.asList(1,3,4)).isEmpty());

    }

    @Test
    void parcoursSansRepetition3_chaineImpossible() {
        List<Arete> aretes = Arrays.asList(
                new Arete(1,2),
                new Arete(2,3),
                new Arete(3,4)
        );

        Graphe g = new Graphe(aretes);

        assertTrue(g.parcoursSansRepetition(Arrays.asList(1,4,3)).isEmpty());
    }


    @Test
    void test_fusionner_sommet_1() {
        Collection<Arete> aretes = new HashSet<>();
        aretes.add(new Arete(1,2));
        aretes.add(new Arete(2,3));
        aretes.add(new Arete(1,3));
        Graphe graphe1 = new Graphe(aretes);
        graphe1.fusionnerSommets(1,2);
        assertEquals(2,graphe1.nbSommets());
        assertEquals(1, graphe1.nbAretes());
    }

    @Test
    void test_fusionner_sommet_2() {
        Collection<Arete> aretes = new HashSet<>();
        aretes.add(new Arete(1,2));
        aretes.add(new Arete(2,3));
        aretes.add(new Arete(1,3));
        aretes.add(new Arete(2,4));
        Graphe graphe1 = new Graphe(aretes);
        graphe1.fusionnerSommets(1,2);
        assertEquals(3,graphe1.nbSommets());
        assertEquals(2, graphe1.nbAretes());
    }

    @Test
    void test_fusionner_sommet_3() {
        Collection<Arete> aretes = new HashSet<>();
        aretes.add(new Arete(1,2));
        aretes.add(new Arete(2,3));
        aretes.add(new Arete(2,3, new RouteTerrestre(new Ville("A",false),new Ville("A",false),Couleur.GRIS, 1)));
        Graphe graphe1 = new Graphe(aretes);
        graphe1.fusionnerSommets(1,2);
        assertEquals(2,graphe1.nbSommets());
        assertEquals(2, graphe1.nbAretes());
    }

    @Test
    void test_fusionner_sommet_4() {
        Collection<Arete> aretes = new HashSet<>();
        aretes.add(new Arete(1,0));
        aretes.add(new Arete(1,2));
        aretes.add(new Arete(2,3));
        aretes.add(new Arete(0,3));
        aretes.add(new Arete(0,2));
        aretes.add(new Arete(1,3));
        Graphe graphe1 = new Graphe(aretes);
        graphe1.fusionnerSommets(0,1);
        assertEquals(3,graphe1.nbSommets());
        assertEquals(3, graphe1.nbAretes());
    }

    @Test
    void test_isthme_1() {
        Collection<Arete> aretes = new HashSet<>();
        Arete a = new Arete(0,1);
        aretes.add(a);
        aretes.add(new Arete(1,2));
        aretes.add(new Arete(2,3));
        aretes.add(new Arete(3,4));
        Graphe graphe1 = new Graphe(aretes);
        assertTrue(graphe1.estUnIsthme(new Arete(1,2)));
    }

    @Test
    void test_isthme_2() {
        Collection<Arete> aretes = new HashSet<>();
        aretes.add(new Arete(2,3));
        aretes.add(new Arete(3,4));
        aretes.add(new Arete(4,5));
        aretes.add(new Arete(5,6));
        aretes.add(new Arete(6,1));
        Graphe graphe1 = new Graphe(aretes);
        assertFalse(graphe1.estUnIsthme(new Arete(1,2)));
    }

    @Test
    void test_isthme_3() {
        Collection<Arete> aretes = new HashSet<>();
        Arete a = new Arete(1,2);
        aretes.add(a);
        aretes.add(new Arete(2,3));
        aretes.add(new Arete(3,4));
        aretes.add(new Arete(4,1));
        aretes.add(new Arete(4,5));
        Graphe graphe1 = new Graphe(aretes);
        assertFalse(graphe1.estUnIsthme(new Arete(1,2)));
    }

    @Test
    void test_isthme_4() {
        Collection<Arete> aretes = new HashSet<>();
        Arete a = new Arete(1,2);
        aretes.add(a);
        aretes.add(new Arete(2,3));
        aretes.add(new Arete(3,4));
        aretes.add(new Arete(4,5));
        aretes.add(new Arete(5,2));
        Graphe graphe1 = new Graphe(aretes);
        assertTrue(graphe1.estUnIsthme(new Arete(1,2)));
    }


}
