package fr.umontpellier.iut.rails;

public abstract class JoueurAuto extends Joueur {

    protected IOJeu jeu;

    public JoueurAuto(String nom, IOJeu jeu, CouleurJouer couleur) {
        super(nom, jeu, couleur);
        this.jeu = jeu;
    }

    void avantJouerTour() {
    };

    void apresJouerTour() {
    };

    @Override
    public void jouerTour() {
        avantJouerTour();
        super.jouerTour();
        apresJouerTour();
    }
}
