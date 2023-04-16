package fr.umontpellier.iut.rails;

import fr.umontpellier.iut.rails.data.CarteTransport;
import fr.umontpellier.iut.rails.data.Couleur;
import fr.umontpellier.iut.rails.data.TypeCarteTransport;
import fr.umontpellier.iut.rails.data.Ville;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class RouteMaritime extends Route {
    public RouteMaritime(Ville ville1, Ville ville2, Couleur couleur, int longueur) {
        super(ville1, ville2, couleur, longueur);
    }

    public int getNbPionsBateau() {
        return getLongueur();
    }

    public boolean peutEtreCaptureePar(Joueur joueur) {
        if (!super.peutEtreCaptureePar(joueur)) {
            return false;
        }
        if (joueur.getNbPionsBateau() < getLongueur()) {
            return false;
        }

        CarteTransport.Catalogue cartesTransport = new CarteTransport.Catalogue(joueur.getCartesTransport());
        return cartesTransport.getValeur(TypeCarteTransport.BATEAU, getCouleur(), true) >= getLongueur();
    }

    Collection<CarteTransport> getOptionsPourPayer(CarteTransport.Catalogue catalogueMain,
            CarteTransport.Catalogue cataloguePosees, Couleur couleur, int valeurRestante) {

        Collection<CarteTransport> options = new HashSet<>();
        boolean aPoseCarteSimple = !cataloguePosees.getSimples(TypeCarteTransport.BATEAU, couleur).isEmpty() ||
                !cataloguePosees.get(TypeCarteTransport.JOKER, Couleur.GRIS).isEmpty();
        List<CarteTransport> cartesSimplesMain = new ArrayList<>(
                catalogueMain.get(TypeCarteTransport.JOKER, Couleur.GRIS));
        cartesSimplesMain.addAll(catalogueMain.getSimples(TypeCarteTransport.BATEAU, couleur));
        List<CarteTransport> cartesDoublesMain = catalogueMain.getDoubles(TypeCarteTransport.BATEAU, couleur);

        if (cartesSimplesMain.size() + 2 * cartesDoublesMain.size() >= valeurRestante) {
            // le joueur a assez de cartes pour payer la route dans la couleur demandée
            if (valeurRestante % 2 == 1 || cartesSimplesMain.size() >= 2) {
                // on pourra toujours atteindre la valeur exacte, donc on autorise le joueur à
                // défausser une carte simple
                options.addAll(cartesSimplesMain);
            }

            if (!aPoseCarteSimple || valeurRestante % 2 == 0
                    || (valeurRestante >= 2 && cartesSimplesMain.size() >= 1)) {
                // on peut défausser une carte double (soit le joueur n'a pas joué de cartes
                // simples, donc on peut dépasser, soit la carte double ne fera pas dépasser et
                // on pourra atteindre la valeur exacte)
                options.addAll(catalogueMain.getDoubles(TypeCarteTransport.BATEAU, couleur));
            }
        }
        return options;
    }

    @Override
    public void payerPar(Joueur joueur) {
        int longueur = getLongueur();
        while (true) {
            CarteTransport.Catalogue cataloguePosees = new CarteTransport.Catalogue(joueur.getCartesTransportPosees());
            int valeurRestante = longueur - cataloguePosees.getValeur();
            if (valeurRestante <= 0) {
                // la route est entièrement payée
                break;
            }

            Collection<CarteTransport> optionsCartes;
            CarteTransport.Catalogue catalogueMain = new CarteTransport.Catalogue(joueur.getCartesTransport());
            Couleur couleur = getCouleur();
            if (couleur == Couleur.GRIS) {
                // si la route est grise mais que le joueur a commencé à payer avec une couleur
                // on n'accepte que des cartes de cette couleur
                couleur = cataloguePosees.getCouleur();
            }
            if (couleur == Couleur.GRIS) {
                optionsCartes = new HashSet<>();
                for (Couleur c : Couleur.getCouleursSimples()) {
                    optionsCartes.addAll(getOptionsPourPayer(catalogueMain, cataloguePosees, c, valeurRestante));
                }
            } else {
                optionsCartes = getOptionsPourPayer(catalogueMain, cataloguePosees, couleur, valeurRestante);
            }

            String choix = joueur.choisir(
                    String.format("Défaussez des cartes pour capturer la route %s", this),
                    optionsCartes.stream().map(CarteTransport::getNom).toList(),
                    null,
                    false);
            joueur.poserCarteTransport(choix);
        }
    }
}
