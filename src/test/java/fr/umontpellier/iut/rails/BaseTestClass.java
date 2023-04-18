package fr.umontpellier.iut.rails;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Timeout;

import fr.umontpellier.iut.rails.data.CarteTransport;
import fr.umontpellier.iut.rails.data.Destination;
import fr.umontpellier.iut.rails.data.TypeCarteTransport;
import fr.umontpellier.iut.rails.data.Ville;

@Timeout(value = 1, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
public class BaseTestClass {
    protected IOJeu jeu;
    protected List<CarteTransport> piocheWagon;
    protected List<CarteTransport> defausseWagon;
    protected List<CarteTransport> piocheBateau;
    protected List<CarteTransport> defausseBateau;
    protected List<CarteTransport> cartesTransportVisibles;
    protected List<Destination> pileDestinations;
    protected List<Route> routes;
    protected List<Ville> ports;
    protected List<Joueur> joueurs;
    protected Joueur joueur1;
    protected List<CarteTransport> cartesJoueur1;
    protected List<CarteTransport> cartesPoseesJoueur1;
    protected List<Route> routesJoueur1;
    protected List<Ville> portsJoueur1;
    protected List<Destination> destinationsJoueur1;
    protected Joueur joueur2;
    protected List<CarteTransport> cartesJoueur2;
    protected List<CarteTransport> cartesPoseesJoueur2;
    protected List<Route> routesJoueur2;
    protected List<Ville> portsJoueur2;
    protected List<Destination> destinationsJoueur2;

    @BeforeAll
    static void staticInit() {
        System.setOut(new PrintStream(OutputStream.nullOutputStream()));
    }

    public void setUpJeu(int nbJoueurs) {
        // réinitialisation des compteurs
        TestUtils.setAttribute(CarteTransport.class, "compteur", 1);
        TestUtils.setAttribute(Destination.class, "compteur", 1);
        TestUtils.setAttribute(Route.class, "compteur", 1);

        String[] nomsJoueurs = new String[] { "Guybrush", "Largo", "LeChuck", "Elaine", "Herman" };
        jeu = new IOJeu(Arrays.copyOfRange(nomsJoueurs, 0, nbJoueurs));
        PilesCartesTransport pilesWagon = (PilesCartesTransport) TestUtils.getAttribute(jeu,
                "pilesDeCartesWagon");
        PilesCartesTransport pilesBateau = (PilesCartesTransport) TestUtils.getAttribute(jeu,
                "pilesDeCartesBateau");
        piocheWagon = (List<CarteTransport>) TestUtils.getAttribute(pilesWagon, "pilePioche");
        defausseWagon = (List<CarteTransport>) TestUtils.getAttribute(pilesWagon, "pileDefausse");
        piocheBateau = (List<CarteTransport>) TestUtils.getAttribute(pilesBateau, "pilePioche");
        defausseBateau = (List<CarteTransport>) TestUtils.getAttribute(pilesBateau, "pileDefausse");
        cartesTransportVisibles = (List<CarteTransport>) TestUtils.getAttribute(jeu,
                "cartesTransportVisibles");
        pileDestinations = (List<Destination>) TestUtils.getAttribute(jeu, "pileDestinations");
        routes = (List<Route>) TestUtils.getAttribute(jeu, "routesLibres");
        ports = (List<Ville>) TestUtils.getAttribute(jeu, "portsLibres");
        joueurs = (List<Joueur>) TestUtils.getAttribute(jeu, "joueurs");
        // attributs du joueur 1
        joueur1 = joueurs.get(0);
        cartesJoueur1 = (List<CarteTransport>) TestUtils.getAttribute(joueur1, "cartesTransport");
        cartesPoseesJoueur1 = (List<CarteTransport>) TestUtils.getAttribute(joueur1, "cartesTransportPosees");
        routesJoueur1 = (List<Route>) TestUtils.getAttribute(joueur1, "routes");
        portsJoueur1 = (List<Ville>) TestUtils.getAttribute(joueur1, "ports");
        destinationsJoueur1 = (List<Destination>) TestUtils.getAttribute(joueur1, "destinations");

        // attributs du joueur 2
        joueur2 = joueurs.get(1);
        cartesJoueur2 = (List<CarteTransport>) TestUtils.getAttribute(joueur2, "cartesTransport");
        cartesPoseesJoueur2 = (List<CarteTransport>) TestUtils.getAttribute(joueur2, "cartesTransportPosees");
        routesJoueur2 = (List<Route>) TestUtils.getAttribute(joueur2, "routes");
        portsJoueur2 = (List<Ville>) TestUtils.getAttribute(joueur2, "ports");
        destinationsJoueur2 = (List<Destination>) TestUtils.getAttribute(joueur2, "destinations");
    }

    public void initialisation() {
        // initialisation des pions wagon et bateau des joueurs
        for (Joueur j : joueurs) {
            TestUtils.setAttribute(j, "nbPionsWagon", 20);
            TestUtils.setAttribute(j, "nbPionsWagonEnReserve", 5);
            TestUtils.setAttribute(j, "nbPionsBateau", 40);
            TestUtils.setAttribute(j, "nbPionsBateauEnReserve", 10);
            List<CarteTransport> cartesTransport = (List<CarteTransport>) TestUtils.getAttribute(j, "cartesTransport");
            for (CarteTransport c: cartesTransport) {
                if (c.getType() == TypeCarteTransport.BATEAU) {
                    piocheBateau.add(c);
                } else {
                    piocheWagon.add(c);
                }
            }
            cartesTransport.clear();
        }

        // initialisation des cartes visibles
        remonterCartePiocheWagon("C1"); // wagon NOIR
        remonterCartePiocheWagon("C22"); // wagon BLANC
        remonterCartePiocheWagon("C43"); // wagon JAUNE
        remonterCartePiocheBateau("C12"); // bateau NOIR
        remonterCartePiocheBateau("C33"); // bateau BLANC
        remonterCartePiocheBateau("C54"); // bateau JAUNE
        cartesTransportVisibles.add(jeu.piocherCarteWagon());
        cartesTransportVisibles.add(jeu.piocherCarteWagon());
        cartesTransportVisibles.add(jeu.piocherCarteWagon());
        cartesTransportVisibles.add(jeu.piocherCarteBateau());
        cartesTransportVisibles.add(jeu.piocherCarteBateau());
        cartesTransportVisibles.add(jeu.piocherCarteBateau());
    }

    public Destination getDestination(String nom) {
        for (Destination d : pileDestinations) {
            if (TestUtils.getNom(d).equals(nom)) {
                return d;
            }
        }
        return null;
    }

    public Destination remonterDestination(String nom) {
        Destination d = getDestination(nom);
        if (d != null) {
            pileDestinations.remove(d);
            pileDestinations.add(0, d);
        }
        return d;
    }

    public CarteTransport getCarteTransport(String nom, List<CarteTransport> cartes) {
        for (CarteTransport c : cartes) {
            if (c.getNom().equals(nom)) {
                return c;
            }
        }
        return null;
    }

    public CarteTransport remonterCartePiocheWagon(String nom) {
        CarteTransport c = getCarteTransport(nom, piocheWagon);
        if (c != null) {
            piocheWagon.remove(c);
            piocheWagon.add(0, c);
        }
        return c;
    }

    public CarteTransport remonterCartePiocheBateau(String nom) {
        CarteTransport c = getCarteTransport(nom, piocheBateau);
        if (c != null) {
            piocheBateau.remove(c);
            piocheBateau.add(0, c);
        }
        return c;
    }

    public Route getRoute(String nom) {
        for (Route r : routes) {
            if (r.getNom().equals(nom)) {
                return r;
            }
        }
        return null;
    }

    public Ville getPort(String nom) {
        for (Ville v : ports) {
            if (v.nom().equals(nom)) {
                return v;
            }
        }
        return null;
    }
}
