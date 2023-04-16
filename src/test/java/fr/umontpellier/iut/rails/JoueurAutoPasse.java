package fr.umontpellier.iut.rails;

public class JoueurAutoPasse extends JoueurAuto {

    public JoueurAutoPasse(String nom, IOJeu jeu, CouleurJouer couleur) {
        super(nom, jeu, couleur);
    }

    @Override
    void avantJouerTour() {
        jeu.setInput("");
    }
}
