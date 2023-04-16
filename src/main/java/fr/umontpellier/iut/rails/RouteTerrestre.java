package fr.umontpellier.iut.rails;

import fr.umontpellier.iut.rails.data.CarteTransport;
import fr.umontpellier.iut.rails.data.Couleur;
import fr.umontpellier.iut.rails.data.TypeCarteTransport;
import fr.umontpellier.iut.rails.data.Ville;

import java.util.ArrayList;
import java.util.List;

public class RouteTerrestre extends Route {
    public RouteTerrestre(Ville ville1, Ville ville2, Couleur couleur, int longueur) {
        super(ville1, ville2, couleur, longueur);
    }

    @Override
    public int getNbPionsWagon() {
        return getLongueur();
    }

    @Override
    public boolean peutEtreCaptureePar(Joueur joueur) {
        if (!super.peutEtreCaptureePar(joueur)) {
            return false;
        }
        if (joueur.getNbPionsWagon() < getLongueur()) {
            return false;
        }

        CarteTransport.Catalogue cartesTransport = new CarteTransport.Catalogue(joueur.getCartesTransport());
        return (cartesTransport.getValeur(TypeCarteTransport.WAGON, getCouleur(), true) >= getLongueur());
    }

    @Override
    public void payerPar(Joueur joueur) {
        int longueur = getLongueur();
        while (true) {
            CarteTransport.Catalogue cataloguePosees = new CarteTransport.Catalogue(joueur.getCartesTransportPosees());
            int valeurPosee = cataloguePosees.getValeur();
            if (valeurPosee >= longueur) {
                // la route est entièrement payée
                break;
            }

            CarteTransport.Catalogue catalogueMain = new CarteTransport.Catalogue(joueur.getCartesTransport());
            Couleur couleur = getCouleur();
            if (couleur == Couleur.GRIS) {
                // si la route est grise mais que le joueur a commencé à payer avec une couleur
                // on n'accepte que des cartes de cette couleur
                couleur = cataloguePosees.getCouleur();
            }

            List<CarteTransport> optionsCartes = new ArrayList<>(
                    catalogueMain.get(TypeCarteTransport.JOKER, Couleur.GRIS));
            if (couleur == Couleur.GRIS) {
                for (Couleur c : Couleur.getCouleursSimples()) {
                    if (catalogueMain.getValeur(TypeCarteTransport.WAGON, c, true) >= longueur - valeurPosee) {
                        optionsCartes.addAll(catalogueMain.get(TypeCarteTransport.WAGON, c));
                    }
                }
            } else {
                optionsCartes.addAll(catalogueMain.get(TypeCarteTransport.WAGON, couleur));
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
