package fr.umontpellier.iut.rails;

import fr.umontpellier.iut.rails.data.CarteTransport;
import fr.umontpellier.iut.rails.data.Couleur;
import fr.umontpellier.iut.rails.data.TypeCarteTransport;
import fr.umontpellier.iut.rails.data.Ville;

import java.util.ArrayList;
import java.util.List;

public class RoutePaire extends Route {
    public RoutePaire(Ville ville1, Ville ville2, int longueur) {
        super(ville1, ville2, Couleur.GRIS, longueur);
    }

    @Override
    public int getNbPionsWagon() {
        return getLongueur();
    }

    public boolean peutEtreCaptureePar(Joueur joueur) {
        if (!super.peutEtreCaptureePar(joueur)) {
            return false;
        }
        if (joueur.getNbPionsWagon() < getLongueur()) {
            return false;
        }

        CarteTransport.Catalogue cartesTransport = new CarteTransport.Catalogue(joueur.getCartesTransport());
        int nbPaires = 0;
        int nbIsolees = 0;
        int nbJokers = cartesTransport.get(TypeCarteTransport.JOKER, Couleur.GRIS).size();

        for (Couleur couleur : Couleur.getCouleursSimples()) {
            int n = cartesTransport.get(TypeCarteTransport.WAGON, couleur).size();
            nbPaires += n / 2;
            nbIsolees += n % 2;
        }
        nbPaires += Math.min(nbIsolees, nbJokers);
        if (nbJokers > nbIsolees) {
            nbPaires += (nbJokers - nbIsolees) / 2;
        }

        return nbPaires >= getLongueur();
    }

    public void payerPar(Joueur joueur) {
        // longueur de la route à capturer
        int longueur = getLongueur();
        while (true) {
            int nbCartesPosees = joueur.getCartesTransportPosees().size();
            if (nbCartesPosees >= 2 * longueur) {
                // la route est entièrement payée
                break;
            }

            CarteTransport.Catalogue cataloguePosees = new CarteTransport.Catalogue(joueur.getCartesTransportPosees());
            CarteTransport.Catalogue catalogueMain = new CarteTransport.Catalogue(joueur.getCartesTransport());

            // les jokers sont toujours jouables
            List<CarteTransport> optionsCartes = new ArrayList<>(
                    catalogueMain.get(TypeCarteTransport.JOKER, Couleur.GRIS));

            // nombre de jokers déjà posés
            int nbJokersPoses = cataloguePosees.get(TypeCarteTransport.JOKER, Couleur.GRIS).size();
            // nombre de jokers que le joueur a en main
            int nbJokersMain = catalogueMain.get(TypeCarteTransport.JOKER, Couleur.GRIS).size();
            // couleurs pour lesquelles il y a un nombre impair de cartes posées
            List<Couleur> couleursImpaires = new ArrayList<>();
            // couleurs pour lesquelles il y a un nombre pair de cartes posées
            List<Couleur> couleursPaires = new ArrayList<>();
            // nombre de couleurs impaires dont le joueur n'a pas de cartes en main (doit
            // utiliser un joker pour compléter la paire)
            int nbCouleursImpairesNonCompletables = 0;
            // couleurs dont le joueur a au moins 2 cartes en main (peut poser une nouvelle
            // paire sans utiliser de joker)
            List<Couleur> couleursNombreuses = new ArrayList<>();

            for (Couleur c : Couleur.getCouleursSimples()) {
                if (cataloguePosees.get(TypeCarteTransport.WAGON, c).size() % 2 == 1) {
                    couleursImpaires.add(c);
                    if (catalogueMain.get(TypeCarteTransport.WAGON, c).isEmpty()) {
                        nbCouleursImpairesNonCompletables += 1;
                    }
                } else {
                    couleursPaires.add(c);
                    if (catalogueMain.get(TypeCarteTransport.WAGON, c).size() >= 2) {
                        couleursNombreuses.add(c);
                    }
                }
            }

            // les cartes qui complètent une couleur impaire sont toujours jouables
            for (Couleur c : couleursImpaires) {
                optionsCartes.addAll(catalogueMain.get(TypeCarteTransport.WAGON, c));
            }

            // nombre de paires qui ne sont pas encore complétées
            int deficit = couleursImpaires.size() - nbJokersPoses;
            if (2 * longueur - nbCartesPosees > deficit) {
                // il reste suffisamment de cartes à jouer pour qu'on puisse jouer d'autres
                // cartes que celles qui servent à compléter des paires déjà commmencées
                if (nbJokersPoses + nbJokersMain > nbCouleursImpairesNonCompletables) {
                    // on peut jouer n'importe quelle couleur (la paire peut être complétée par un
                    // joker)
                    for (Couleur c : couleursPaires) {
                        optionsCartes.addAll(catalogueMain.get(TypeCarteTransport.WAGON, c));
                    }
                } else {
                    // on peut jouer n'importe quelle couleur dont on a au moins 2 cartes
                    for (Couleur c : couleursNombreuses) {
                        optionsCartes.addAll(catalogueMain.get(TypeCarteTransport.WAGON, c));
                    }
                }
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
