package fr.umontpellier.iut.graphes;

import fr.umontpellier.iut.rails.Route;

/**
 * Classe modélisant les arêtes. Pour simplifier, vous pouvez supposer le prérequis que i!=j
 */
public record Arete(int i, int j, Route route) {
    public Arete(int i, int j) {
        this(i, j, null);
    }



    public boolean incidenteA(int v) {
        return i == v || j == v;
    }

    public boolean estLaMeme(Arete a){return  i== a.i() && j==a.j();}

    public int getAutreSommet(int v) {
        return v == i ? j : i;
    }

}