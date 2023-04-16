package fr.umontpellier.iut.graphes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AppGraphes {
    public static void main(String[] args) {
        // Le scénario suivant illustre l'utilisation de la classe Graphe. Le code ne fonctionne pas pour le moment, car
        // les classes Graphes et Arete ne sont pas encore complète.
        Graphe g1 = new Graphe(3);
        List<Arete> listeDAretes = new ArrayList<>();
        // génération d'un graphe où les arêtes sont étiquetés par des entiers aléatoires
        for (int i = 0; i <= 2; i++) {
            Arete a = new Arete(i, (i + 1) % 3);
            listeDAretes.add(a); // stockage de toutes les arêtes
            g1.ajouterArete(a);
        }
        HashSet<Integer> X = new HashSet<>();
        X.add(0);
        X.add(2);
        g1.supprimerSommet(1);
        g1.supprimerSommet(1); // ne fait rien, car le sommet 1 n'existe plus


        Set<Integer> ids = new HashSet<>(g1.ensembleSommets());
        Graphe g3 = new Graphe(g1, ids); // copie de g1

        Graphe g2 = new Graphe(g1, X); // sous-graphe de g1 induit par l'ensemble X
        System.out.println("g1\n" + g1);
        System.out.println("g2\n" + g2);


        g1.supprimerArete(listeDAretes.get(2));
        System.out.println("g1\n" + g1);
        System.out.println("g2\n" + g2);

        System.out.println("g3\n" + g3);
        g3.supprimerSommet(1);
        System.out.println("g3\n" + g3);

    }
}
