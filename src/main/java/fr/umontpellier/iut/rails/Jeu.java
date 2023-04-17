package fr.umontpellier.iut.rails;

import com.google.gson.Gson;
import fr.umontpellier.iut.gui.GameServer;
import fr.umontpellier.iut.rails.data.*;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Jeu implements Runnable {
    /**
     * Liste des joueurs
     */
    private final List<Joueur> joueurs;
    /**
     * Le joueur dont c'est le tour
     */
    private Joueur joueurCourant;
    /**
     * Liste des villes disponibles sur le plateau de jeu
     */
    private final List<Ville> portsLibres;
    /**
     * Liste des routes disponibles sur le plateau de jeu
     */
    private final List<Route> routesLibres;
    /**
     * Pile de pioche et défausse des cartes wagon
     */
    private final PilesCartesTransport pilesDeCartesWagon;
    /**
     * Pile de pioche et défausse des cartes bateau
     */
    private final PilesCartesTransport pilesDeCartesBateau;
    /**
     * Cartes de la pioche face visible (normalement il y a 6 cartes face visible)
     */
    private final List<CarteTransport> cartesTransportVisibles;
    /**
     * Pile des cartes "Destination"
     */
    private final List<Destination> pileDestinations;
    /**
     * File d'attente des instructions recues par le serveur
     */
    private final BlockingQueue<String> inputQueue;
    /**
     * Messages d'information du jeu
     */
    private final List<String> log;

    private String instruction;
    private Collection<Bouton> boutons;

    public Jeu(String[] nomJoueurs) {
        // initialisation des entrées/sorties
        inputQueue = new LinkedBlockingQueue<>();
        log = new ArrayList<>();

        // création des villes et des routes
        Plateau plateau = Plateau.makePlateauMonde();
        portsLibres = plateau.getPorts();
        routesLibres = plateau.getRoutes();

        // création des piles de pioche et défausses des cartes Transport (wagon et
        // bateau)
        ArrayList<CarteTransport> cartesWagon = new ArrayList<>();
        ArrayList<CarteTransport> cartesBateau = new ArrayList<>();
        for (Couleur c : Couleur.values()) {
            if (c == Couleur.GRIS) {
                continue;
            }
            for (int i = 0; i < 4; i++) {
                // Cartes wagon simples avec une ancre
                cartesWagon.add(new CarteTransport(TypeCarteTransport.WAGON, c, false, true));
            }
            for (int i = 0; i < 7; i++) {
                // Cartes wagon simples sans ancre
                cartesWagon.add(new CarteTransport(TypeCarteTransport.WAGON, c, false, false));
            }
            for (int i = 0; i < 4; i++) {
                // Cartes bateau simples (toutes avec une ancre)
                cartesBateau.add(new CarteTransport(TypeCarteTransport.BATEAU, c, false, true));
            }
            for (int i = 0; i < 6; i++) {
                // Cartes bateau doubles (toutes sans ancre)
                cartesBateau.add(new CarteTransport(TypeCarteTransport.BATEAU, c, true, false));
            }
        }
        for (int i = 0; i < 14; i++) {
            // Cartes wagon joker
            cartesWagon.add(new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true));
        }
        pilesDeCartesWagon = new PilesCartesTransport(cartesWagon);
        pilesDeCartesBateau = new PilesCartesTransport(cartesBateau);

        // création de la liste pile de cartes transport visibles
        // (les cartes seront retournées plus tard, au début de la partie dans run())
        cartesTransportVisibles = new ArrayList<>();

        // création des destinations
        pileDestinations = Destination.makeDestinationsMonde();
        Collections.shuffle(pileDestinations);

        // création des joueurs
        // les cartes des joueurs sont distribuées lors de la préparation du jeu dans
        // run()
        ArrayList<Joueur.CouleurJouer> couleurs = new ArrayList<>(Arrays.asList(Joueur.CouleurJouer.values()));
        Collections.shuffle(couleurs);
        joueurs = new ArrayList<>();
        for (String nomJoueur : nomJoueurs) {
            joueurs.add(new Joueur(nomJoueur, this, couleurs.remove(0)));
        }
        this.joueurCourant = joueurs.get(0);
    }

    public List<Joueur> getJoueurs() {
        return joueurs;
    }

    public List<Ville> getPortsLibres() {
        return new ArrayList<>(portsLibres);
    }

    /**
     * Supprime de la liste des ports disponibles le port dont le nom est passé en
     * argument
     *
     * @return la ville du port supprimé ou null
     */
    public Ville retirerPortLibre(String nom) {
        for (Ville v : portsLibres) {
            if (v.nom().equals(nom)) {
                portsLibres.remove(v);
                return v;
            }
        }
        return null;
    }

    public List<Route> getRoutesLibres() {
        return new ArrayList<>(routesLibres);
    }

    /**
     * Supprime de la liste des routes disponibles la route dont le nom est passé en
     * argument
     *
     * @return la route supprimée ou null
     */
    public Route retirerRouteLibre(String nom) {
        for (Route r : routesLibres) {
            if (r.getNom().equals(nom)) {
                routesLibres.remove(r);
                return r;
            }
        }
        return null;
    }

    /**
     * Renvoie une copie de la liste des cartes transport visibles du jeu
     */
    public List<CarteTransport> getCartesTransportVisibles() {
        return new ArrayList<>(cartesTransportVisibles);
    }

    /**
     * Retire une carte transport de la liste des cartes visibles.
     */
    public void retirerCarteTransportVisible(CarteTransport c) {
        cartesTransportVisibles.remove(c);
    }

    /**
     * Modifie l'attribut joueurCourant pour passer au joueur suivant dans l'ordre
     * du tableau joueurs
     * (le tableau est considéré circulairement)
     */
    public void passeAuJoueurSuivant() {
        int i = joueurs.indexOf(joueurCourant);
        i = (i + 1) % joueurs.size();
        joueurCourant = joueurs.get(i);
    }

    /**
     * Exécute la partie
     */
    public void run() {
        // Retourner 3 cartes wagon et 3 cartes bateau
        for (int i = 0; i < 3; i++) {
            cartesTransportVisibles.add(piocherCarteWagon());
        }
        for (int i = 0; i < 3; i++) {
            cartesTransportVisibles.add(piocherCarteBateau());
        }
        updateCartesTransportVisibles();

        for (int i = 0; i < joueurs.size(); i++) {
            joueurCourant.preparation();
            passeAuJoueurSuivant();
        }

        // Boucle principale (tours des joueurs)
        while (true) {
            log(String.format("    --- %s ---", joueurCourant.toLog()));
            joueurCourant.jouerTour();
            if (joueurCourant.getNbPionsWagon() + joueurCourant.getNbPionsBateau() <= 6) {
                // un joueur a moins de 6 pions restants à la fin de son tour
                // chaque joueur joue encore 2 tours et la partie s'arrête
                passeAuJoueurSuivant();
                break;
            }
            passeAuJoueurSuivant();
        }
        // 2 derniers tours de jeu
        for (int i = 0; i < 2 * joueurs.size(); i++) {
            joueurCourant.jouerTour();
            passeAuJoueurSuivant();
        }
        // Fin de la partie
        log("Fin de la partie.");
        for (Joueur j : joueurs) {
            log(String.format("%s : %d points", j.toLog(), j.calculerScoreFinal()));
        }
        prompt("Fin de la partie.", new ArrayList<>(), true);
    }

    /**
     * Ajoute une carte transport dans la pile de défausse correspondante en
     * fonction de son type
     *
     * @param c carte à défausser
     */
    public void defausserCarteTransport(CarteTransport c) {
        if (c.getType() == TypeCarteTransport.BATEAU) {
            pilesDeCartesBateau.defausser(c);
        } else {
            pilesDeCartesWagon.defausser(c);
        }
    }

    /**
     * Pioche une carte de la pile de pioche des cartes wagon.
     *
     * @return la carte qui a été piochée (ou null si aucune carte disponible)
     */
    public CarteTransport piocherCarteWagon() {
        return pilesDeCartesWagon.piocher();
    }

    /**
     * Révèle (dans la liste des cartes visibles) une carte de la pile de pioche des
     * cartes wagon
     * 
     * @return la carte qui a été retournée (ou null si aucune carte disponible)
     */
    public CarteTransport revelerCarteWagon() {
        CarteTransport c = pilesDeCartesWagon.piocher();
        if (c != null) {
            cartesTransportVisibles.add(c);
            updateCartesTransportVisibles();
        }
        return c;
    }

    /**
     * @return true si les piles de cartes wagon sont vides
     */
    public boolean piocheWagonEstVide() {
        return pilesDeCartesWagon.estVide();
    }

    /**
     * Pioche une carte de la pile de pioche des cartes bateau.
     *
     * @return la carte qui a été piochée (ou null si aucune carte disponible)
     */
    public CarteTransport piocherCarteBateau() {
        return pilesDeCartesBateau.piocher();
    }

    /**
     * Révèle (dans la liste des cartes visibles) une carte de la pile de pioche des
     * cartes bateau
     * 
     * @return la carte qui a été retournée (ou null si aucune carte disponible)
     */
    public CarteTransport revelerCarteBateau() {
        CarteTransport c = pilesDeCartesBateau.piocher();
        if (c != null) {
            cartesTransportVisibles.add(c);
            updateCartesTransportVisibles();
        }
        return c;
    }

    /**
     * @return true si les piles de cartes bateau sont vides
     */
    public boolean piocheBateauEstVide() {
        return pilesDeCartesBateau.estVide();
    }

    /**
     * PilesCartesTransport et renvoie la destination du dessus de la pile de
     * destinations.
     *
     * @return la destination qui a été piochée (ou `null` si aucune destination
     *         disponible)
     */
    public Destination piocherDestination() {
        if (pileDestinations.isEmpty())
            return null;
        return pileDestinations.remove(0);
    }

    /**
     * Replace une liste de destinations à la fin de la pile de destinations
     */
    public void defausserDestination(Destination destination) {
        pileDestinations.add(destination);
    }

    /**
     * Teste si la pile de destinations est vide
     * (pour préserver l'encapsulation du Jeu et de sa pile de destination)
     */
    public boolean pileDestinationsEstVide() {
        return pileDestinations.isEmpty();
    }

    /**
     * Cette méthode redistribue automatiquement les cartes face visible s'il y a au
     * moins 3 jokers face visible (sauf si les cartes disponibles ne permettent pas
     * d'avoir moins de 3 jokers face visible)
     * 
     * Cette méthode est appelée à chaque fois qu'une nouvelle carte transport est
     * retournée face visible, et au début du tour de chaque joueur.
     */
    public void updateCartesTransportVisibles() {
        if (cartesTransportVisibles
                .stream()
                .filter(c -> c.getType() == TypeCarteTransport.JOKER).count() < 3) {
            // il y a < 3 cartes Joker face visible
            // pas besoin de redistribuer les cartes transport visibles
            return;
        }

        int nbCartesWagonSimples = 0;
        int nbCartesBateau = 0;
        List<CarteTransport> cartesTransport = new ArrayList<>();
        cartesTransport.addAll(cartesTransportVisibles);
        cartesTransport.addAll(pilesDeCartesWagon.getCartes());
        cartesTransport.addAll(pilesDeCartesBateau.getCartes());
        for (CarteTransport c : cartesTransport) {
            switch (c.getType()) {
                case WAGON:
                    nbCartesWagonSimples += 1;
                    break;
                case BATEAU:
                    nbCartesBateau += 1;
                    break;
                default:
                    break;
            }
        }

        if (nbCartesWagonSimples == 0 || nbCartesWagonSimples + nbCartesBateau <= 3) {
            // il n'y a pas assez d'autres cartes pour pouvoir avoir moins de 3 cartes
            // Joker face visible. Ce n'est pas la peine de redistribuer les cartes
            return;
        }

        // défausser les 6 cartes transport visibles
        while (!cartesTransportVisibles.isEmpty()) {
            defausserCarteTransport(cartesTransportVisibles.remove(0));
        }
        // retourner des nouvelles cartes
        remplirCartesTransportVisibles();
    }

    /**
     * Retourne 3 cartes wagon et 3 cartes bateau face visible.
     * Si l'une des deux piles contient moins de 3 cartes, on ajoute des cartes de
     * l'autre pile jusqu'à avoir retourné 6 cartes ou épuisé les deux piles.
     */
    public void remplirCartesTransportVisibles() {
        for (int i = 0; i < 3; i++) {
            CarteTransport c = revelerCarteBateau();
            if (c == null)
                break;
        }
        for (int i = 0; i < 3; i++) {
            CarteTransport c = revelerCarteWagon();
            if (c == null)
                break;
        }

        // Si une pioche est vide, on complète à 6 cartes avec des cartes de l'autre
        // pioche
        while (cartesTransportVisibles.size() < 6 && !pilesDeCartesWagon.estVide()) {
            revelerCarteWagon();
        }
        while (cartesTransportVisibles.size() < 6 && !pilesDeCartesBateau.estVide()) {
            revelerCarteBateau();
        }
        updateCartesTransportVisibles();
    }

    /**
     * Ajoute un message au log du jeu
     */
    public void log(String message) {
        log.add(message);
    }

    /**
     * Ajoute un message à la file d'entrées
     */
    public void addInput(String message) {
        inputQueue.add(message);
    }

    /**
     * Lit une ligne de l'entrée standard
     * C'est cette méthode qui doit être appelée à chaque fois qu'on veut lire
     * l'entrée clavier de l'utilisateur (par exemple dans {@code Player.choisir})
     *
     * @return une chaîne de caractères correspondant à l'entrée suivante dans la
     *         file
     */
    public String lireLigne() {
        try {
            return inputQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Envoie l'état de la partie pour affichage aux joueurs avant de faire un choix
     *
     * @param instruction l'instruction qui est donnée au joueur
     * @param boutons     labels des choix proposés s'il y en a
     * @param peutPasser  indique si le joueur peut passer sans faire de choix
     */
    public void prompt(String instruction, Collection<Bouton> boutons, boolean peutPasser) {
        this.instruction = instruction;
        this.boutons = boutons;

        System.out.println();
        System.out.println(this);
        if (boutons.isEmpty()) {
            System.out.printf(">>> %s: %s <<<\n", joueurCourant.getNom(), instruction);
        } else {
            StringJoiner joiner = new StringJoiner(" / ");
            for (Bouton bouton : boutons) {
                joiner.add(bouton.toPrompt());
            }
            System.out.printf(">>> %s: %s [%s] <<<\n", joueurCourant.getNom(), instruction, joiner);
        }
        GameServer.setEtatJeu(new Gson().toJson(dataMap()));
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner("\n");
        for (Joueur j : joueurs) {
            joiner.add(j.toString());
        }
        return joiner.toString();
    }

    /**
     * Renvoie une représentation du jeu sous la forme d'un dictionnaire de
     * valeurs sérialisables (qui sera converti en JSON pour l'envoyer à l'interface
     * graphique)
     */
    public Map<String, Object> dataMap() {
        return Map.ofEntries(
                Map.entry("joueurs", joueurs.stream().map(Joueur::dataMap).toList()),
                Map.entry("joueurCourant", joueurs.indexOf(joueurCourant)),
                Map.entry("piocheWagon", pilesDeCartesWagon.dataMap()),
                Map.entry("piocheBateau", pilesDeCartesBateau.dataMap()),
                Map.entry("cartesTransportVisibles", cartesTransportVisibles),
                Map.entry("nbDestinations", pileDestinations.size()),
                Map.entry("instruction", instruction),
                Map.entry("boutons", boutons),
                Map.entry("log", log));
    }
}
