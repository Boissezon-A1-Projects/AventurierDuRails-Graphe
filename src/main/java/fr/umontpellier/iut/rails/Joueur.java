package fr.umontpellier.iut.rails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;

import fr.umontpellier.iut.graphes.Graphe;
import fr.umontpellier.iut.rails.data.*;

public class Joueur {
    public enum CouleurJouer {
        JAUNE, ROUGE, BLEU, VERT, ROSE;
    }
    /**
     * Jeu auquel le joueur est rattaché
     */
    private final Jeu jeu;
    /**
     * Nom du joueur
     */
    private final String nom;
    /**
     * CouleurJouer du joueur (pour représentation sur le plateau)
     */
    private final CouleurJouer couleur;
    /**
     * Liste des villes sur lesquelles le joueur a construit un port
     */
    private final List<Ville> ports;
    /**
     * Liste des routes capturées par le joueur
     */
    private final List<Route> routes;
    /**
     * Nombre de pions wagons que le joueur peut encore poser sur le plateau
     */
    private int nbPionsWagon;
    /**
     * Nombre de pions wagons que le joueur a dans sa réserve (dans la boîte)
     */
    private int nbPionsWagonEnReserve;
    /**
     * Nombre de pions bateaux que le joueur peut encore poser sur le plateau
     */
    private int nbPionsBateau;
    /**
     * Nombre de pions bateaux que le joueur a dans sa réserve (dans la boîte)
     */
    private int nbPionsBateauEnReserve;
    /**
     * Liste des destinations à réaliser pendant la partie
     */
    private final List<Destination> destinations;
    /**
     * Liste des cartes que le joueur a en main
     */
    private final List<CarteTransport> cartesTransport;
    /**
     * Liste temporaire de cartes transport que le joueur est en train de jouer pour
     * payer la capture d'une route ou la construction d'un port
     */
    private final List<CarteTransport> cartesTransportPosees;
    /**
     * Score courant du joueur (somme des valeurs des routes capturées, et points
     * perdus lors des échanges de pions)
     */
    private int score;

    public Joueur(String nom, Jeu jeu, CouleurJouer couleur) {
        this.nom = nom;
        this.jeu = jeu;
        this.couleur = couleur;
        this.ports = new ArrayList<>();
        this.routes = new ArrayList<>();
        this.nbPionsWagon = 0;
        this.nbPionsWagonEnReserve = 25;
        this.nbPionsBateau = 0;
        this.nbPionsBateauEnReserve = 50;
        this.cartesTransport = new ArrayList<>();
        this.cartesTransportPosees = new ArrayList<>();
        this.destinations = new ArrayList<>();
        this.score = 0;
    }

    public String getNom() {
        return nom;
    }

    public Jeu getJeu() {
        return jeu;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public CouleurJouer getCouleur() {
        return couleur;
    }

    public List<Destination> getDestinations() {
        return destinations;
    }

    public List<CarteTransport> getCartesTransport() {
        return cartesTransport;
    }

    public List<CarteTransport> getCartesTransportPosees() {
        return cartesTransportPosees;
    }

    public int getNbPionsWagon() {
        return nbPionsWagon;
    }

    public int getNbPionsWagonEnReserve() {
        return nbPionsWagonEnReserve;
    }

    public int getNbPionsBateau() {
        return nbPionsBateau;
    }

    public int getNbPionsBateauEnReserve() {
        return nbPionsBateauEnReserve;
    }

    public int getScore() {
        return score;
    }

    /**
     * Ajoute une carte transport à la main du joueur
     *
     * @param c la carte à ajouter
     */
    public void ajouterCarteTransport(CarteTransport c) {
        cartesTransport.add(c);
    }

    /**
     * Déplace une carte transport de la main du joueur vers sa pile de cartes
     * posées
     *
     * @param c carte transport à poser
     */
    private void poserCarteTransport(CarteTransport c) {
        if (cartesTransport.remove(c)) {
            cartesTransportPosees.add(c);
        }
    }

    /**
     * Déplace une carte transport de la main du joueur vers sa pile de cartes
     * posées
     *
     * @param nom Nom de la carte transport à poser
     */
    public void poserCarteTransport(String nom) {
        for (CarteTransport c : cartesTransport) {
            if (c.getNom().equals(nom)) {
                poserCarteTransport(c);
                return;
            }
        }
    }

    /**
     * Défausse toutes les cartes transport posées devant le joueur.
     * Elles sont mises dans la pile de défausse du jeu correspondante à leur type
     * (wagon ou bateau)
     */
    private void defausserCartesTransportPosees() {
        log(String.format("%s défausse %s", toLog(), CarteTransport.listToLog(cartesTransportPosees)));
        while (!cartesTransportPosees.isEmpty()) {
            jeu.defausserCarteTransport(cartesTransportPosees.remove(0));
        }
    }

    /**
     * Attend une entrée de la part du joueur (au clavier ou sur la websocket) et
     * renvoie le choix du joueur.
     * <p>
     * Cette méthode lit les entrées du jeu ({@code Jeu.lireligne()}) jusqu'à ce
     * qu'un choix valide (un élément de {@code choix} ou de {@code boutons} ou
     * éventuellement la chaîne vide si l'utilisateur est autorisé à passer) soit
     * reçu.
     * Lorsqu'un choix valide est obtenu, il est renvoyé par la fonction.
     * <p>
     * Exemple d'utilisation pour demander à un joueur de répondre à une question
     * par "oui" ou "non" :
     * <p>
     * {@code
     * List<String> choix = Arrays.asList("oui", "non");
     * String input = choisir("Voulez vous faire ceci ?", choix, null, false);
     * }
     * <p>
     * Si par contre on voulait proposer les réponses à l'aide de boutons, on
     * pourrait utiliser :
     * <p>
     * {@code
     * List<String> boutons = Arrays.asList(new Bouton("Un", "1"), new Bouton("Deux", "2"));
     * String input = choisir("Choisissez un nombre.", null, boutons, false);
     * }
     * (ici le premier bouton a le label "Un" et envoie la String "1" s'il est
     * cliqué, le second a le label "Deux" et envoie la String "2" lorsqu'il est
     * cliqué)
     *
     * @param instruction message à afficher à l'écran pour indiquer au joueur la
     *                    nature du choix qui est attendu
     * @param choix       une collection de chaînes de caractères correspondant aux
     *                    choix valides attendus du joueur
     * @param boutons     une collection d'objets de type Bouton définis par deux
     *                    chaînes de caractères (label, valeur) correspondant aux
     *                    choix valides attendus du joueur qui doivent être
     *                    représentés par des boutons sur l'interface graphique (le
     *                    label est affiché sur le bouton, la valeur est ce qui est
     *                    envoyé au jeu quand le bouton est cliqué)
     * @param peutPasser  booléen indiquant si le joueur a le droit de passer sans
     *                    faire de choix. S'il est autorisé à passer, c'est la
     *                    chaîne de caractères vide ("") qui signifie qu'il désire
     *                    passer.
     * @return le choix de l'utilisateur (un élement de {@code choix}, ou la valeur
     *         d'un élément de {@code boutons} ou la chaîne vide)
     */
    public String choisir(
            String instruction,
            Collection<String> choix,
            Collection<Bouton> boutons,
            boolean peutPasser) {
        if (choix == null)
            choix = new ArrayList<>();
        if (boutons == null)
            boutons = new ArrayList<>();

        HashSet<String> choixDistincts = new HashSet<>(choix);
        choixDistincts.addAll(boutons.stream().map(Bouton::valeur).toList());
        if (peutPasser || choixDistincts.isEmpty()) {
            choixDistincts.add("");
        }

        String entree;
        // Lit l'entrée de l'utilisateur jusqu'à obtenir un choix valide
        while (true) {
            jeu.prompt(instruction, boutons, peutPasser);
            entree = jeu.lireLigne();
            // si une réponse valide est obtenue, elle est renvoyée
            if (choixDistincts.contains(entree)) {
                return entree;
            }
        }
    }

    /**
     * Affiche un message dans le log du jeu (visible sur l'interface graphique)
     *
     * @param message le message à afficher (peut contenir des balises html pour la
     *                mise en forme)
     */
    public void log(String message) {
        jeu.log(message);
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner("\n");
        joiner.add(String.format("=== %s (%d pts) ===", nom, score));
        joiner.add(String.format("  Wagons: %d  Bateaux: %d", nbPionsWagon, nbPionsBateau));
        return joiner.toString();
    }

    /**
     * @return une chaîne de caractères contenant le nom du joueur, avec des balises
     *         HTML pour être mis en forme dans le log
     */
    public String toLog() {
        return String.format("<span class=\"joueur\">%s</span>", nom);
    }

    /**
     * Cette méthode est exécutée lorsque le joueur pioche des destinations
     * 
     * @param nbDestinationsPiochees nombre de destinations à proposer au joueur
     * @param nbMinDestinations      nombre minimum de destinations que le joueur
     *                               doit garder
     */
    private void prendreDestinations(int nbDestinationsPiochees, int nbMinDestinations) {
        List<Destination> destinationsPiochees = new ArrayList<>();
        for (int i = 0; i < nbDestinationsPiochees; i++) {
            Destination d = jeu.piocherDestination();
            if (d == null) {
                break;
            }
            destinationsPiochees.add(d);
        }

        while (destinationsPiochees.size() > nbMinDestinations) {
            List<Bouton> boutons = destinationsPiochees.stream().map(d -> new Bouton(d.toString(), d.getNom()))
                    .toList();
            String choix = choisir(
                    String.format("Vous pouvez défausser %d destinations",
                            destinationsPiochees.size() - nbMinDestinations),
                    null,
                    boutons,
                    true);
            if (choix.equals("")) {
                break;
            }
            for (Destination d : destinationsPiochees) {
                if (d.getNom().equals(choix)) {
                    destinationsPiochees.remove(d);
                    jeu.defausserDestination(d);
                    break;
                }
            }
        }

        log(String.format("%s prend %d destinations", toLog(), destinationsPiochees.size()));
        destinations.addAll(destinationsPiochees);
    }

    /**
     * Méthode appelée au début de la partie pour préparer l'état initial du joueur
     * 
     * - distribue 3 cartes wagon et 7 cartes bateau
     * - fait choisir des destinations au joueur (il doit en conserver au moins 3
     * parmi 5 proposées)
     * - demande au joueur le nombre de pions wagon qu'il veut prendre (10-25) et
     * distribue les pions wagon et bateau
     */
    public void preparation() {
        // Piocher 3 cartes wagon et 7 cartes bateau
        for (int i = 0; i < 3; i++) {
            cartesTransport.add(jeu.piocherCarteWagon());
        }
        for (int i = 0; i < 7; i++) {
            cartesTransport.add(jeu.piocherCarteBateau());
        }

        // Piocher 5 destinations. Le joueur doit en garder au moins 3.
        prendreDestinations(5, 3);
        List<Bouton> boutons = new ArrayList<>();
        for (int i = 10; i <= 25; i++) {
            boutons.add(new Bouton(String.valueOf(i)));
        }

        // Choisir le nombre de pions wagon à prendre (10-25)
        String choix = choisir(
                "Choisissez le nombre de pions wagon à prendre",
                null,
                boutons,
                true);

        if (choix.equals("")) {
            // choix par défaut
            choix = "20";
        }
        int n = Integer.parseInt(choix);
        nbPionsWagon = n;
        nbPionsWagonEnReserve = 25 - n;
        nbPionsBateau = 60 - n;
        nbPionsBateauEnReserve = n - 10;
        log(String.format("%s prend %d pions wagon et %d pions bateau.", toLog(), n, 60 - n));
    }

    /**
     * Cette méthode demande au joueur de choisir dans quelle pile de cartes
     * transport prendre les cartes pour remplir les cartes transport visibles.
     * Elle est appelée au début du tour de chaque joueur.
     *
     * S'il y a déjà 6 cartes transport visibles, cette méthode ne fait rien.
     * S'il n'y a aucune carte dans les piles de cartes transport (wagon et bateau),
     * cette méthode ne fait rien.
     */
    void remplirCartesTransportVisibles() {
        while (jeu.getCartesTransportVisibles().size() < 6
                && (!jeu.piocheWagonEstVide() || !jeu.piocheBateauEstVide())) {
            ArrayList<String> choixPossibles = new ArrayList<>();
            if (!jeu.piocheWagonEstVide()) {
                choixPossibles.add("WAGON");
            }
            if (!jeu.piocheBateauEstVide()) {
                choixPossibles.add("BATEAU");
            }
            String choix = choisir(
                    "Choisissez une carte à révéler",
                    choixPossibles,
                    null,
                    false);
            if (choix.equals("WAGON")) {
                jeu.revelerCarteWagon();
            } else if (choix.equals("BATEAU")) {
                jeu.revelerCarteBateau();
            }
        }
    }

    /**
     * Méthode principale qui exécute le tour d'un joueur.
     * Cette méthode fait la liste de tous les choix correspondant aux différentes
     * actions que le joueur peut réaliser à son tour, demande un choix de
     * l'utilisateur et exécute la méthode correspondant à l'action choisie.
     * 
     * Les 5 actions possibles sont :
     * - piocher une carte transport (visible ou dans une pioche)
     * - piocher des destinations
     * - capturer une route
     * - construire un port
     * - échanger des pions wagon/bateau
     * 
     * Remarque : le joueur a le droit de passer son tour en ne faisant aucune
     * action (ce n'est pas ce qui est précisé dans les règles mais c'est plus
     * pratique pour tester le bon fonctionnement du programme)
     */
    void jouerTour() {
        remplirCartesTransportVisibles();
        List<Bouton> boutons = new ArrayList<>();

        List<String> optionsPioche = getOptionsPiocherCarteTransport(true);
        List<String> optionsPorts = getOptionsPorts();
        List<String> optionsRoutes = getOptionsRoutes();
        List<String> optionsChoix = new ArrayList<>(optionsPioche);
        optionsChoix.addAll(optionsPorts);
        optionsChoix.addAll(optionsRoutes);

        if (nbPionsBateau > 0 && nbPionsWagonEnReserve > 0) {
            boutons.add(new Bouton("Prendre des pions wagon", "PIONS WAGON"));
        }
        if (nbPionsWagon > 0 && nbPionsBateauEnReserve > 0) {
            boutons.add(new Bouton("Prendre des pions bateau", "PIONS BATEAU"));
        }

        if (!jeu.pileDestinationsEstVide()) {
            optionsChoix.add("DESTINATION");
        }
        String choix = choisir(
                "Début du tour",
                optionsChoix,
                boutons,
                true);

        if (choix.equals("DESTINATION")) {
            prendreDestinations(4, 1);
        } else if (choix.equals("PIONS WAGON")) {
            prendrePionsWagon();
        } else if (choix.equals("PIONS BATEAU")) {
            prendrePionsBateau();
        } else if (optionsPioche.contains(choix)) {
            piocherCarteTransport(choix, true);
        } else if (optionsPorts.contains(choix)) {
            construirePort(choix);
        } else if (optionsRoutes.contains(choix)) {
            capturerRoute(choix);
        }
    }

    private List<String> getOptionsPiocherCarteTransport(boolean premiereCarte) {
        ArrayList<String> choix = new ArrayList<>();
        for (CarteTransport c : jeu.getCartesTransportVisibles()) {
            if (c.getType() != TypeCarteTransport.JOKER || premiereCarte) {
                choix.add(c.getNom());
            }
        }
        if (!jeu.piocheBateauEstVide()) {
            choix.add("BATEAU");
        }
        if (!jeu.piocheWagonEstVide()) {
            choix.add("WAGON");
        }
        return choix;
    }

    private void prendrePionsBateau() {
        List<Bouton> boutons = new ArrayList<>();
        int max = Math.min(nbPionsBateauEnReserve, nbPionsWagon);
        for (int i = 1; i <= max; i++) {
            boutons.add(new Bouton(String.valueOf(i)));
        }
        String choix = choisir(
                "Choisissez le nombre de pions bateau à prendre",
                null,
                boutons,
                false);

        int n = Integer.parseInt(choix);
        nbPionsBateau += n;
        nbPionsBateauEnReserve -= n;
        nbPionsWagon -= n;
        nbPionsWagonEnReserve += n;
        score -= n;
        log(String.format("%s prend %d pions bateau", toLog(), n));
    }

    private boolean peutPayerPort() {
        CarteTransport.Catalogue catalogue = new CarteTransport.Catalogue(
                cartesTransport.stream().filter(CarteTransport::getAncre).toList());

        int nbJokers = catalogue.getValeur(TypeCarteTransport.JOKER);
        for (Couleur c : Couleur.getCouleursSimples()) {
            int valeurWagon = Math.min(2, catalogue.getValeur(TypeCarteTransport.WAGON, c));
            int valeurBateau = Math.min(2, catalogue.getValeur(TypeCarteTransport.BATEAU, c));
            if (valeurWagon + valeurBateau + nbJokers >= 4) {
                return true;
            }
        }
        return false;
    }

    private void payerPort(Ville ville) {
        while (true) {
            CarteTransport.Catalogue catalogueCartesPosees = new CarteTransport.Catalogue(cartesTransportPosees);
            if (catalogueCartesPosees.getValeur() >= 4) {
                // le port est entièrement payé
                break;
            }
            // on ne compte que les cartes en main ayant une ancre
            CarteTransport.Catalogue catalogueCartesEnMain = new CarteTransport.Catalogue(
                    cartesTransport.stream().filter(CarteTransport::getAncre).toList());
            Couleur couleur = catalogueCartesPosees.getCouleur();
            int nbJokersPoses = catalogueCartesPosees.get(TypeCarteTransport.JOKER, Couleur.GRIS).size();
            int nbJokersEnMain = catalogueCartesEnMain.get(TypeCarteTransport.JOKER, Couleur.GRIS).size();

            List<CarteTransport> optionsCartes = new ArrayList<>(
                    catalogueCartesEnMain.get(TypeCarteTransport.JOKER, Couleur.GRIS));
            if (couleur == Couleur.GRIS) {
                // aucune carte simple (non-joker) posée
                for (Couleur c : Couleur.getCouleursSimples()) {
                    int valeurWagonEnMain = Math.min(2, catalogueCartesEnMain.getValeur(TypeCarteTransport.WAGON, c));
                    int valeurBateauEnMain = Math.min(2, catalogueCartesEnMain.getValeur(TypeCarteTransport.BATEAU, c));
                    if (valeurWagonEnMain + valeurBateauEnMain + nbJokersEnMain + nbJokersPoses >= 4) {
                        // le joueur peut payer le port avec des cartes de la couleur c
                        optionsCartes.addAll(catalogueCartesEnMain.get(TypeCarteTransport.WAGON, c));
                        optionsCartes.addAll(catalogueCartesEnMain.get(TypeCarteTransport.BATEAU, c));
                    }
                }
            } else {
                if (catalogueCartesPosees.getValeur(TypeCarteTransport.WAGON, couleur) < 2) {
                    optionsCartes.addAll(catalogueCartesEnMain.get(TypeCarteTransport.WAGON, couleur));
                }
                if (catalogueCartesPosees.getValeur(TypeCarteTransport.BATEAU, couleur) < 2) {
                    optionsCartes.addAll(catalogueCartesEnMain.get(TypeCarteTransport.BATEAU, couleur));
                }
            }

            String choix = choisir(
                    String.format("Défaussez des cartes pour construire le port à %s", ville.toLog()),
                    optionsCartes.stream().map(CarteTransport::getNom).toList(),
                    null,
                    false);
            poserCarteTransport(choix);
        }
    }

    private List<String> getOptionsPorts() {
        List<String> options = new ArrayList<>();
        if (ports.size() < 3 && peutPayerPort()) {
            for (Ville v : jeu.getPortsLibres()) {
                for (Route r : routes) {
                    if (r.getVille1() == v || r.getVille2() == v) {
                        options.add(v.nom());
                        break;
                    }
                }
            }
        }
        return options;
    }

    private List<String> getOptionsRoutes() {
        List<String> options = new ArrayList<>();
        for (Route r : jeu.getRoutesLibres()) {
            if (r.peutEtreCaptureePar(this)) {
                options.add(r.getNom());
            }
        }
        return options;
    }

    private void prendrePionsWagon() {
        List<Bouton> boutons = new ArrayList<>();
        int max = Math.min(nbPionsWagonEnReserve, nbPionsBateau);
        for (int i = 1; i <= max; i++) {
            boutons.add(new Bouton(String.valueOf(i)));
        }
        String choix = choisir(
                "Choisissez le nombre de pions wagon à prendre",
                null,
                boutons,
                false);

        int n = Integer.parseInt(choix);
        nbPionsBateau -= n;
        nbPionsBateauEnReserve += n;
        nbPionsWagon += n;
        nbPionsWagonEnReserve -= n;
        score -= n;
        log(String.format("%s prend %d pions wagon", toLog(), n));
    }

    private void piocherCarteTransport(String choix, boolean premiereCarte) {
        if (choix.equals("WAGON")) {
            log(String.format("%s pioche une carte wagon", toLog()));
            CarteTransport c = jeu.piocherCarteWagon();
            cartesTransport.add(c);
        } else if (choix.equals("BATEAU")) {
            log(String.format("%s pioche une carte bateau", toLog()));
            CarteTransport c = jeu.piocherCarteBateau();
            cartesTransport.add(c);
        } else {
            for (CarteTransport c : jeu.getCartesTransportVisibles()) {
                if (c.getNom().equals(choix)) {
                    log(String.format("%s prend %s", toLog(), c.toLog()));
                    jeu.retirerCarteTransportVisible(c);
                    cartesTransport.add(c);
                    if (c.getType() == TypeCarteTransport.JOKER) {
                        premiereCarte = false; // si on prend un Joker, on ne peut pas piocher une seconde carte
                    }
                    remplirCartesTransportVisibles();
                    break;
                }
            }
        }
        if (premiereCarte) {
            List<String> optionsPioche = getOptionsPiocherCarteTransport(false);
            choix = choisir("Vous pouvez prendre une deuxième carte", optionsPioche, new ArrayList<>(), true);
            piocherCarteTransport(choix, false);
        }
    }

    private void construirePort(String nom) {
        Ville ville = jeu.retirerPortLibre(nom);
        if (ville != null) {
            log(String.format("%s construit un port à %s", toLog(), ville.toLog()));
            payerPort(ville);
            defausserCartesTransportPosees();
            ports.add(ville);
        }
    }

    private void capturerRoute(String nom) {
        Route r = jeu.retirerRouteLibre(nom);
        if (r != null) {
            log(String.format("%s capture la route %s", toLog(), r.toLog()));
            r.payerPar(this);
            defausserCartesTransportPosees();
            this.nbPionsBateau -= r.getNbPionsBateau();
            this.nbPionsWagon -= r.getNbPionsWagon();
            this.score += r.getScore();
            routes.add(r);
        }
    }

    private Set<String> getNomsVillesVoisines(String nomVille) {
        Set<String> nomsVillesVoisines = new HashSet<>();
        for (Route r : routes) {
            if (r.getVille1().nom().equals(nomVille)) {
                nomsVillesVoisines.add(r.getVille2().nom());
            } else if (r.getVille2().nom().equals(nomVille)) {
                nomsVillesVoisines.add(r.getVille1().nom());
            }
        }
        return nomsVillesVoisines;
    }

    boolean villesSontConnectees(String nomVille1, String nomVille2) {
        List<String> frontiere = new ArrayList<>();
        Set<String> dejaVues = new HashSet<>();
        frontiere.add(nomVille1);
        dejaVues.add(nomVille1);

        while (!frontiere.isEmpty()) {
            String nomVille = frontiere.remove(0);
            if (nomVille.equals(nomVille2)) {
                return true;
            }
            for (String nomVilleVoisine : getNomsVillesVoisines(nomVille)) {
                if (!dejaVues.contains(nomVilleVoisine)) {
                    frontiere.add(nomVilleVoisine);
                    dejaVues.add(nomVilleVoisine);
                }
            }
        }
        return false;
    }

    boolean destinationEstComplete(Destination d) {
        // ATTENTION : cette méthode est incomplète, car elle ne gère pas complètement
        // les Destinations Itinéraire (elle n'est pas forcément la plus adaptée si on
        // veut utiliser des graphes...).
        // Elle est à réécrire en utilisant les Graphes
        if(d.getVilles().size()==2) {
            for (int i = 0; i < d.getVilles().size() - 1; i++) {
                if (!villesSontConnectees(d.getVilles().get(i), d.getVilles().get(i + 1))) {
                    return false;
                }
            }
        }
        return true;
    }

    public int calculerScoreFinal() {
        int scoreFinal = score;
        List<Destination> destinationsCompletes = new ArrayList<>();
        List<Destination> destinationsIncompletes = new ArrayList<>();
        for (Destination d : destinations) {
            if (destinationEstComplete(d)) {
                destinationsCompletes.add(d);
            } else {
                destinationsIncompletes.add(d);
            }
        }
        for (Destination d : destinationsCompletes) {
            scoreFinal += d.getValeurSimple();
        }
        for (Destination d : destinationsIncompletes) {
            scoreFinal -= d.getPenalite();
        }
        for (Ville port : ports) {
            int nbVilles = 0;
            for (Destination d : destinationsCompletes) {
                if (d.getVilles().contains(port.nom())) {
                    nbVilles += 1;
                }
            }
            switch (nbVilles) {
                case 0 -> {
                }
                case 1 -> scoreFinal += 20;
                case 2 -> scoreFinal += 30;
                default -> scoreFinal += 40;
            }
        }
        scoreFinal -= 4 * (3 - ports.size());
        return scoreFinal;
    }

    /**
     * Renvoie une représentation du joueur sous la forme d'un dictionnaire de
     * valeurs sérialisables (qui sera converti en JSON pour l'envoyer à l'interface
     * graphique)
     */
    Map<String, Object> dataMap() {
        List<Destination> destinationsCompletes = destinations.stream().filter(this::destinationEstComplete)
                .toList();
        List<Destination> destinationsIncompletes = destinations.stream().filter(d -> !destinationEstComplete(d))
                .toList();
        List<List<String>> routesPourDestinationsIncompletes = new ArrayList<>();
        for (Destination d : destinationsIncompletes) {
            Collection<Route> routes = routesEnSurbrillancePourDestination(d);
            if (routes == null) {
                routesPourDestinationsIncompletes.add(new ArrayList<>());
            } else {
                routesPourDestinationsIncompletes.add(routes.stream().map(Route::getNom).toList());
            }
        }

        return Map.ofEntries(
                Map.entry("nom", nom),
                Map.entry("couleur", couleur),
                Map.entry("score", score),
                Map.entry("pionsWagon", nbPionsWagon),
                Map.entry("pionsWagonReserve", nbPionsWagonEnReserve),
                Map.entry("pionsBateau", nbPionsBateau),
                Map.entry("pionsBateauReserve", nbPionsBateauEnReserve),
                Map.entry("routesPourDestinations", routesPourDestinationsIncompletes),
                Map.entry("destinationsIncompletes", destinationsIncompletes),
                Map.entry("destinationsCompletes", destinationsCompletes),
                Map.entry("main", cartesTransport.stream().sorted().toList()),
                Map.entry("inPlay", cartesTransportPosees.stream().sorted().toList()),
                Map.entry("ports", ports.stream().map(Ville::nom).toList()),
                Map.entry("routes", routes.stream().map(Route::getNom).toList()));
    }

    public Ville getVilleFromString(String s){
        for(Ville ville : Jeu.getPlateau().getVilles()){
            if(s.equals(ville.nom())){
                return ville;
            }
        }
        return null;
    }

    public Ville getVilleFromId(Integer id){
        for(Ville ville : Jeu.getPlateau().getVilles()){
            if(id.equals(ville.getId())){
                return ville;
            }
        }
        return null;
    }

    public Route routeEntreDeuxVilles(Ville v1, Ville v2){
        for(Route route : Jeu.getPlateau().getRoutes()){
            if((route.getVille1().equals(v1) && route.getVille2().equals(v2)) || (route.getVille2().equals(v1) && route.getVille1().equals(v2))){
                return route;
            }
        }
        return null;
    }


    /**
     * Renvoie une collection contenant un plus court ensemble de routes (en nombre
     * total de pions utilisés) que le joueur peut capturer pour compléter la
     * destination passée en paramètre
     * <p>
     * La méthode renvoie une collection vide si la destination est déjà complète ou
     * s'il n'est pas possible de la compléter
     */
    public Collection<Route> routesPourCompleterDestination(Destination d) {
        Graphe graphe = Jeu.getPlateau().getGraphe();
        //prend la classe de connexité de la ville de départ avec les routes du joueur comme aretes
        // afin de calculer le sommet relié au départ controlé par le joueur le plus proche de l'arrivée
        Graphe grapheControleParJoueur = Jeu.getPlateau().getGraphe(routes);
        Set<Integer> classeConnexiteVilleDepart = new HashSet<>();
        List<Integer> idVillesDestination = new ArrayList<>();
        for(String stringVille : d.getVilles()){
            idVillesDestination.add(getVilleFromString(stringVille).getId());
        }

        Integer idDepart = idVillesDestination.get(0);

        classeConnexiteVilleDepart = grapheControleParJoueur.getClasseConnexite(idDepart);

        Integer idArrivee = idVillesDestination.get(idVillesDestination.size()-1);
        List<Integer> meilleurChemin;

        if(idVillesDestination.size()==2) {
            meilleurChemin = graphe.parcoursSansRepetition(idDepart, idArrivee, true);
            // Si le joueur possède déjà une route partant de la ville de départ
            if (classeConnexiteVilleDepart.contains(idDepart)) {
                for (Integer i : classeConnexiteVilleDepart) {
                    if (graphe.parcoursSansRepetition(i, idArrivee, true).size() < meilleurChemin.size()) {
                        meilleurChemin = graphe.parcoursSansRepetition(i, idArrivee, true);
                    }
                }
            }
        }
        else{
            meilleurChemin = graphe.parcoursSansRepetition(idVillesDestination);
        }
        List<Ville> villesMeilleurChemin = new ArrayList<>();
        for(Integer i : meilleurChemin){
            villesMeilleurChemin.add(getVilleFromId(i));
        }
        List<Route> routesMeilleurChemin = new ArrayList<>();
        for(int i = 0; i< villesMeilleurChemin.size()-1; i++){
            routesMeilleurChemin.add(routeEntreDeuxVilles(villesMeilleurChemin.get(i), villesMeilleurChemin.get(i+1)));
        }
        return routesMeilleurChemin;
    }

    /**
     * Renvoie une collection contenant un plus court ensemble de routes (en nombre
     * total de pions utilisés) que le joueur peut capturer pour compléter la
     * destination passée en paramètre en utilisant les pions dont le joueur dispose
     * actuellement
     * <p>
     * La méthode renvoie une collection vide si la destination est déjà complète ou
     * s'il n'est pas possible de la compléter.
     */
    public Collection<Route> routesPourCompleterDestinationAvecPions(Destination d) {
        // Cette fonction est à réécrire entièrement
        throw new RuntimeException("Méthode non implémentée");
    }

    /**
     * Renvoie une collection de routes que l'on souhaite mettre en surbrillance
     * dans l'interface graphique lorsque le curseur passe sur le nom d'une
     * destination.
     * 
     * @return
     */
    public Collection<Route> routesEnSurbrillancePourDestination(Destination d) {
        return null;
    }
}
