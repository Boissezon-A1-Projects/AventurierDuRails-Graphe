package fr.umontpellier.iut.graphes;

import fr.umontpellier.iut.rails.Route;

import java.util.HashSet;
import java.util.Objects;

public record Arete(int i, int j, Route route) {
    //prérequis id!=j
    //fixme écrire ceux sur joueur, longueur

    public Arete(int i, int j) {
        this(i, j, null);
    }


    public boolean incidenteA(int v) {
        return i == v || j == v;
    }

    public int getAutreSommet(int v) {
        return v == i ? j : i;
    }

}

    /*Arete {
    private int id,j;

    public Arete(int id, int j) {
        this.id = id;
        this.j = j;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Arete arete = (Arete) o;
        return (id == arete.id && j == arete.j) || ((id == arete.j && j == arete.id));
    }

    @Override
    public int hashCode() {
        HashSet<Integer> e= new HashSet<>();
        e.add(id);
        e.add(j);
        return Objects.hash(e);
    }

    public boolean contains(int v){
        return id==v || j==v;
    }
    public int getI() {
        return id;
    }

    public void setI(int id) {
        this.id = id;
    }

    public int getJ() {
        return j;
    }

    public void setJ(int j) {
        this.j = j;
    }

    @Override
    public String toString() {
        return "Arete{" +
                "id=" + id +
                ", j=" + j +
                '}';
    }
}
*/