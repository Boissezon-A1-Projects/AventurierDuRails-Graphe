package fr.umontpellier.iut.rails;

import fr.umontpellier.iut.rails.data.CarteTransport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JoueurAutoSimple extends JoueurAuto {

    public JoueurAutoSimple(String nom, IOJeu jeu, CouleurJouer couleur) {
        super(nom, jeu, couleur);
    }

    @Override
    void avantJouerTour() {
        List<String> args = new ArrayList<>();
        
        List<String> nomsRoutes = new ArrayList<>();
        for (Route r: jeu.getRoutesLibres()) {
            if (r instanceof RoutePaire) {
                continue;
            }
            nomsRoutes.add(r.getNom());
        }
        Collections.shuffle(nomsRoutes);
        args.addAll(nomsRoutes);

        List<CarteTransport> cartesTransport = (List<CarteTransport>) TestUtils.getAttribute(this, "cartesTransport");
        args.addAll(cartesTransport.stream().map(CarteTransport::getNom).toList());

        args.add("WAGON");
        args.add("BATEAU");
        args.add("WAGON");
        args.add("BATEAU");
        args.add("");
        jeu.setInput(args);
    }
}
