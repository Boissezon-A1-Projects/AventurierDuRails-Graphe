package fr.umontpellier.iut.rails;

import fr.umontpellier.iut.rails.data.CarteTransport;
import fr.umontpellier.iut.rails.data.Ville;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JoueurAutoAleatoire extends JoueurAuto {

    public JoueurAutoAleatoire(String nom, IOJeu jeu, CouleurJouer couleur) {
        super(nom, jeu, couleur);
    }

    private List<String> getNomsCartesTransport() {
        List<String> nomsCartes = new ArrayList<>();
        for (CarteTransport c : (List<CarteTransport>) TestUtils.getAttribute(this, "cartesTransport")) {
            nomsCartes.add(c.getNom());
        }
        Collections.shuffle(nomsCartes);
        return nomsCartes;
    }

    private List<String> makeChoixCapturerRoute() {
        List<String> choix = new ArrayList<>();
        for (Route r : jeu.getRoutesLibres()) {
            choix.add(r.getNom());
        }
        Collections.shuffle(choix);
        choix.addAll(getNomsCartesTransport());
        return choix;
    }

    private List<String> makeChoixCapturerPort() {
        List<String> choix = new ArrayList<>();
        for (Ville v : jeu.getPortsLibres()) {
            choix.add(v.nom());
        }
        Collections.shuffle(choix);
        choix.addAll(getNomsCartesTransport());
        return choix;
    }

    private List<String> makeChoixPiocherCartes() {
        List<String> nomsCartes = new ArrayList<>();
        for (CarteTransport c : jeu.getCartesTransportVisibles()) {
            nomsCartes.add(c.getNom());
        }
        nomsCartes.add("BATEAU");
        nomsCartes.add("WAGON");
        nomsCartes.add("");
        Collections.shuffle(nomsCartes);

        List<String> choixRemplacement = new ArrayList<>();
        choixRemplacement.add("WAGON");
        choixRemplacement.add("BATEAU");
        Collections.shuffle(choixRemplacement);

        List<String> choix = new ArrayList<>();
        choix.addAll(nomsCartes);
        choix.addAll(choixRemplacement);
        choix.addAll(nomsCartes);
        choix.addAll(choixRemplacement);
        return choix;
    }

    private List<String> makeChoixEchangerPions() {
        List<String> choix = new ArrayList<>();
        choix.add("PIONS WAGON");
        choix.add("PIONS BATEAU");
        Collections.shuffle(choix);

        List<String> nombres = new ArrayList<>();
        for (int i = 0; i <= 75; i++) {
            nombres.add(String.valueOf(i));
        }
        Collections.shuffle(nombres);
        choix.addAll(nombres);
        return choix;
    }

    private List<String> makeChoixPrendreDestinations() {
        List<String> choix = new ArrayList<>();
        for (int i = 1; i <= 65; i++) {
            choix.add("D" + i);
        }
        choix.add("");
        Collections.shuffle(choix);
        choix.add(0, "DESTINATION");
        return choix;
    }

    @Override
    void avantJouerTour() {
        List<List<String>> choixActions = new ArrayList<>();
        choixActions.add(makeChoixCapturerRoute());
        choixActions.add(makeChoixCapturerPort());
        choixActions.add(makeChoixPiocherCartes());
        choixActions.add(makeChoixEchangerPions());
        choixActions.add(makeChoixPrendreDestinations());
        Collections.shuffle(choixActions);

        List<String> choix = new ArrayList<>();
        for (List<String> l : choixActions) {
            choix.addAll(l);
        }
        choix.add("");
        jeu.setInput(choix);
    }
}
