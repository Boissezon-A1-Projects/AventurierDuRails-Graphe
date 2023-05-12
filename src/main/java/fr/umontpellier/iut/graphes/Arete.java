package fr.umontpellier.iut.graphes;

import fr.umontpellier.iut.rails.Route;

import java.util.HashSet;
import java.util.Objects;

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

    public boolean estLaMeme(Arete a){return  (i== a.i() && j==a.j()) || (i==a.j() && j==a.i());}

    public int getAutreSommet(int v) {
        return v == i ? j : i;
    }

    public boolean contientArete(HashSet<Arete> aretes){
        for (Arete ar: aretes) {
            if(ar.estLaMeme(this)){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Arete arete = (Arete) o;
        return ((i == arete.i && j == arete.j)|| (i== arete.j && j==arete.i)) && Objects.equals(route, arete.route);
    }

    @Override
    public int hashCode() {
        return Objects.hash(i*j, route);
    }
}