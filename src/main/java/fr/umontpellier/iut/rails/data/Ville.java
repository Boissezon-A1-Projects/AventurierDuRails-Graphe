package fr.umontpellier.iut.rails.data;

import java.util.Objects;

public final class Ville {
    private final String nom;
    private final boolean estPort;
    private final int id;
    private static int compteur = 0;

    public Ville(
            String nom,
            boolean estPort) {
        this.nom = nom;
        this.estPort = estPort;
        id = compteur++;
    }

    @Override
    public String toString() {
        return nom;
    }

    public String toLog() {
        return String.format("<span class=\"ville\">%s</span>", nom);
    }

    public String nom() {
        return nom;
    }

    public boolean estPort() {
        return estPort;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Ville) obj;
        return Objects.equals(this.nom, that.nom) &&
                this.estPort == that.estPort;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nom, estPort);
    }

    public int getId() {
        return id;
    }
}
