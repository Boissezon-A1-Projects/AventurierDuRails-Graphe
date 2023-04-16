package fr.umontpellier.iut.rails.data;

import java.util.List;
import java.util.stream.Stream;

/**
 * Représentation des couleurs du jeu utilisées pour les cartes transport et les routes
 */
public enum Couleur {
    NOIR, BLANC, JAUNE, ROUGE, VERT, VIOLET, GRIS;

    /**
     * Renvoie la liste des couleurs simples (toutes les couleurs sauf GRIS).
     */
    public static List<Couleur> getCouleursSimples() {
        return Stream.of(Couleur.values()).filter(c -> c != Couleur.GRIS).toList();
    }

}
