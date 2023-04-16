package fr.umontpellier.iut.rails;

import fr.umontpellier.iut.rails.data.CarteTransport;
import fr.umontpellier.iut.rails.data.Couleur;
import fr.umontpellier.iut.rails.data.TypeCarteTransport;
import fr.umontpellier.iut.rails.data.Ville;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class SecretPortTest extends BaseTestClass {

    @BeforeEach
    public void setUp() {
        setUpJeu(4);
        initialisation();
    }

    @Test
    void testConstructionPortSiRouteTerrestreAdjacente() {
        Route route = getRoute("R107"); // Miami - New York
        Ville miami = getPort("Miami"); // Miami
        routes.remove(route);
        routesJoueur2.add(route);
        CarteTransport c1 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, true);
        CarteTransport c2 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, true);
        CarteTransport c3 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.ROUGE, false, true);
        CarteTransport c4 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.ROUGE, false, true);
        cartesJoueur2.addAll(List.of(c1, c2, c3, c4));
        int nbPorts = ports.size();

        jeu.setInput(
                "Miami",
                c1.getNom(),
                c2.getNom(),
                c3.getNom(),
                c4.getNom());

        joueur2.jouerTour();

        assertEquals(1, portsJoueur2.size());
        assertTrue(portsJoueur2.contains(miami));
        assertEquals(2, defausseWagon.size());
        assertEquals(2, defausseBateau.size());
        assertTrue(defausseWagon.contains(c1));
        assertTrue(defausseWagon.contains(c2));
        assertTrue(defausseBateau.contains(c3));
        assertTrue(defausseBateau.contains(c4));
        assertEquals(nbPorts - 1, ports.size());
        assertTrue(cartesPoseesJoueur2.isEmpty());
    }

    @Test
    void testConstructionPortSiRouteMaritimeAdjacente() {
        Route route = getRoute("R46"); // Caracas - Miami
        Ville miami = getPort("Miami"); // Miami
        routes.remove(route);
        routesJoueur2.add(route);
        CarteTransport c1 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, true);
        CarteTransport c2 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, true);
        CarteTransport c3 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.ROUGE, false, true);
        CarteTransport c4 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.ROUGE, false, true);
        cartesJoueur2.addAll(List.of(c1, c2, c3, c4));
        int nbPorts = ports.size();

        jeu.setInput(
                "Miami",
                c1.getNom(),
                c2.getNom(),
                c3.getNom(),
                c4.getNom());

        joueur2.jouerTour();

        assertEquals(1, portsJoueur2.size());
        assertTrue(portsJoueur2.contains(miami));
        assertEquals(2, defausseWagon.size());
        assertEquals(2, defausseBateau.size());
        assertTrue(defausseWagon.contains(c1));
        assertTrue(defausseWagon.contains(c2));
        assertTrue(defausseBateau.contains(c3));
        assertTrue(defausseBateau.contains(c4));
        assertEquals(nbPorts - 1, ports.size());
        assertTrue(cartesPoseesJoueur2.isEmpty());
    }

    @Test
    void testConstructionSiPortDejaOccupe() {
        Route route = getRoute("R107"); // Miami - New York
        Ville miami = getPort("Miami"); // Miami
        Ville newYork = getPort("New York"); // New York
        routes.remove(route);
        ports.remove(miami);
        routesJoueur2.add(route);
        CarteTransport c1 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, true);
        CarteTransport c2 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, true);
        CarteTransport c3 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.ROUGE, false, true);
        CarteTransport c4 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.ROUGE, false, true);
        cartesJoueur2.addAll(List.of(c1, c2, c3, c4));
        int nbPorts = ports.size();

        jeu.setInput(
                "Miami",
                "New York",
                c1.getNom(),
                c2.getNom(),
                c3.getNom(),
                c4.getNom());

        joueur2.jouerTour();

        assertEquals(1, portsJoueur2.size());
        assertTrue(portsJoueur2.contains(newYork));
        assertEquals(2, defausseWagon.size());
        assertEquals(2, defausseBateau.size());
        assertTrue(defausseWagon.contains(c1));
        assertTrue(defausseWagon.contains(c2));
        assertTrue(defausseBateau.contains(c3));
        assertTrue(defausseBateau.contains(c4));
        assertFalse(ports.contains(newYork));
        assertEquals(nbPorts - 1, ports.size());
        assertTrue(cartesPoseesJoueur2.isEmpty());
    }

    @Test
    void testConstructionSi3PortsOntDejaEteConstruits() {
        Route route1 = getRoute("R107"); // Miami - New York
        Route route2 = getRoute("R46"); // Caracas - Miami
        Route route3 = getRoute("R47"); // Caracas - Rio de Janeiro
        Ville newYork = getPort("New York");
        Ville miami = getPort("Miami");
        Ville caracas = getPort("Caracas");
        Ville rioDeJaneiro = getPort("Rio de Janeiro");
        routes.remove(route1);
        routesJoueur2.add(route1);
        routes.remove(route2);
        routesJoueur2.add(route2);
        routes.remove(route3);
        routesJoueur2.add(route3);
        CarteTransport topCarte1 = piocheWagon.get(0);
        CarteTransport topCarte2 = piocheWagon.get(1);
        int nbPorts = ports.size();

        List<CarteTransport> cartes = new ArrayList<>();
        cartes.add(new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, true));
        cartes.add(new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, true));
        cartes.add(new CarteTransport(TypeCarteTransport.WAGON, Couleur.VERT, false, true));
        cartes.add(new CarteTransport(TypeCarteTransport.WAGON, Couleur.VERT, false, true));
        cartes.add(new CarteTransport(TypeCarteTransport.WAGON, Couleur.JAUNE, false, true));
        cartes.add(new CarteTransport(TypeCarteTransport.WAGON, Couleur.JAUNE, false, true));
        cartes.add(new CarteTransport(TypeCarteTransport.WAGON, Couleur.NOIR, false, true));
        cartes.add(new CarteTransport(TypeCarteTransport.WAGON, Couleur.NOIR, false, true));
        cartes.add(new CarteTransport(TypeCarteTransport.BATEAU, Couleur.ROUGE, false, true));
        cartes.add(new CarteTransport(TypeCarteTransport.BATEAU, Couleur.ROUGE, false, true));
        cartes.add(new CarteTransport(TypeCarteTransport.BATEAU, Couleur.VERT, false, true));
        cartes.add(new CarteTransport(TypeCarteTransport.BATEAU, Couleur.VERT, false, true));
        cartes.add(new CarteTransport(TypeCarteTransport.BATEAU, Couleur.JAUNE, false, true));
        cartes.add(new CarteTransport(TypeCarteTransport.BATEAU, Couleur.JAUNE, false, true));
        cartes.add(new CarteTransport(TypeCarteTransport.BATEAU, Couleur.NOIR, false, true));
        cartes.add(new CarteTransport(TypeCarteTransport.BATEAU, Couleur.NOIR, false, true));
        cartesJoueur2.addAll(cartes);

        jeu.setInput(
                "New York",
                cartes.get(0).getNom(),
                cartes.get(1).getNom(),
                cartes.get(8).getNom(),
                cartes.get(9).getNom());
        joueur2.jouerTour();
        assertEquals(1, portsJoueur2.size());
        assertTrue(portsJoueur2.contains(newYork));
        assertEquals(nbPorts - 1, ports.size());

        jeu.setInput(
                "Miami",
                cartes.get(2).getNom(),
                cartes.get(3).getNom(),
                cartes.get(10).getNom(),
                cartes.get(11).getNom());
        joueur2.jouerTour();
        assertEquals(2, portsJoueur2.size());
        assertTrue(portsJoueur2.contains(miami));
        assertEquals(nbPorts - 2, ports.size());

        jeu.setInput(
                "Caracas",
                cartes.get(4).getNom(),
                cartes.get(5).getNom(),
                cartes.get(12).getNom(),
                cartes.get(13).getNom());
        joueur2.jouerTour();
        assertEquals(3, portsJoueur2.size());
        assertTrue(portsJoueur2.contains(caracas));
        assertEquals(nbPorts - 3, ports.size());

        jeu.setInput(
                "Rio de Janeiro",
                cartes.get(6).getNom(),
                cartes.get(7).getNom(),
                cartes.get(14).getNom(),
                cartes.get(15).getNom(),
                "WAGON",
                "WAGON");
        joueur2.jouerTour();
        assertEquals(3, portsJoueur2.size());
        assertFalse(portsJoueur2.contains(rioDeJaneiro));
        assertEquals(nbPorts - 3, ports.size());

        assertEquals(6, cartesJoueur2.size());
        for (int i = 0; i < 6; i++) {
            assertTrue(defausseWagon.contains(cartes.get(i)));
        }
        for (int i = 6; i < 8; i++) {
            assertTrue(cartesJoueur2.contains(cartes.get(i)));
        }
        for (int i = 8; i < 14; i++) {
            assertTrue(defausseBateau.contains(cartes.get(i)));
        }
        for (int i = 14; i < 16; i++) {
            assertTrue(cartesJoueur2.contains(cartes.get(i)));
        }
        assertTrue(cartesJoueur2.contains(topCarte1));
        assertTrue(cartesJoueur2.contains(topCarte2));
        assertTrue(cartesPoseesJoueur2.isEmpty());
    }

    @Test
    void testConstructionPortAvecCartesAncreUniquement() {
        Route route = getRoute("R107"); // Miami - New York
        Ville miami = getPort("Miami"); // Miami
        routes.remove(route);
        routesJoueur2.add(route);
        CarteTransport c1 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, false);
        CarteTransport c2 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, true);
        CarteTransport c3 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, true);
        CarteTransport c4 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.ROUGE, true, false);
        CarteTransport c5 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.ROUGE, false, true);
        CarteTransport c6 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.ROUGE, false, true);
        cartesJoueur2.addAll(List.of(c1, c2, c3, c4, c5, c6));
        int nbPorts = ports.size();
        jeu.setInput(
                "Miami",
                c1.getNom(),
                c2.getNom(),
                c3.getNom(),
                c4.getNom(),
                c5.getNom(),
                c6.getNom());

        joueur2.jouerTour();

        assertEquals(nbPorts - 1, ports.size());
        assertEquals(1, portsJoueur2.size());
        assertEquals(miami, portsJoueur2.get(0));
        assertEquals(2, defausseWagon.size());
        assertEquals(2, defausseBateau.size());
        assertTrue(defausseWagon.contains(c2));
        assertTrue(defausseWagon.contains(c3));
        assertTrue(defausseBateau.contains(c5));
        assertTrue(defausseBateau.contains(c6));
        assertEquals(2, cartesJoueur2.size());
        assertTrue(cartesJoueur2.contains(c1));
        assertTrue(cartesJoueur2.contains(c4));
        assertTrue(cartesPoseesJoueur2.isEmpty());
    }

    @Test
    void testConstructionPortSiPasDeRouteCaptureeAdjacente() {
        CarteTransport c1 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, true);
        CarteTransport c2 = new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, true);
        CarteTransport c3 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.ROUGE, false, true);
        CarteTransport c4 = new CarteTransport(TypeCarteTransport.BATEAU, Couleur.ROUGE, false, true);
        cartesJoueur2.addAll(List.of(c1, c2, c3, c4));
        CarteTransport topCarte1 = piocheWagon.get(0);
        CarteTransport topCarte2 = piocheWagon.get(1);
        int nbPorts = ports.size();
        jeu.setInput(
                "Miami",
                c1.getNom(),
                c2.getNom(),
                c3.getNom(),
                c4.getNom(),
                "WAGON",
                "WAGON");

        joueur2.jouerTour();

        assertEquals(nbPorts, ports.size());
        assertTrue(portsJoueur2.isEmpty());
        assertTrue(defausseWagon.isEmpty());
        assertTrue(defausseBateau.isEmpty());
        assertEquals(6, cartesJoueur2.size());
        assertTrue(cartesJoueur2.contains(c1));
        assertTrue(cartesJoueur2.contains(c2));
        assertTrue(cartesJoueur2.contains(c3));
        assertTrue(cartesJoueur2.contains(c4));
        assertTrue(cartesJoueur2.contains(topCarte1));
        assertTrue(cartesJoueur2.contains(topCarte2));
        assertTrue(cartesPoseesJoueur2.isEmpty());
    }

    @Test
    void testConstructionPortPaiement1() {
        Route route = getRoute("R107"); // Miami - New York
        Ville miami = getPort("Miami"); // Miami
        routes.remove(route);
        routesJoueur2.add(route);

        List<CarteTransport> cartes = new ArrayList<>();
        cartes.add(new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true));
        cartes.add(new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true));
        cartes.add(new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true));
        cartes.add(new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true));

        cartesJoueur2.addAll(cartes);
        int nbPorts = ports.size();

        List<String> inputs = new ArrayList<>();
        inputs.add("Miami");
        for (CarteTransport carte : cartes) {
            inputs.add(carte.getNom());
        }
        jeu.setInput(inputs);

        joueur2.jouerTour();

        assertEquals(1, portsJoueur2.size());
        assertTrue(portsJoueur2.contains(miami));
        assertEquals(4, defausseWagon.size());
        assertEquals(0, defausseBateau.size());
        assertEquals(0, cartesJoueur2.size());
        assertTrue(defausseWagon.contains(cartes.get(0)));
        assertTrue(defausseWagon.contains(cartes.get(1)));
        assertTrue(defausseWagon.contains(cartes.get(2)));
        assertTrue(defausseWagon.contains(cartes.get(3)));

        assertEquals(nbPorts - 1, ports.size());
        assertTrue(cartesPoseesJoueur2.isEmpty());
    }

    @Test
    void testConstructionPortPaiement2() {
        Route route = getRoute("R107"); // Miami - New York
        Ville miami = getPort("Miami"); // Miami
        routes.remove(route);
        routesJoueur2.add(route);

        List<CarteTransport> cartes = new ArrayList<>();
        cartes.add(new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true));
        cartes.add(new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true));
        cartes.add(new CarteTransport(TypeCarteTransport.WAGON, Couleur.JAUNE, false, true));
        cartes.add(new CarteTransport(TypeCarteTransport.WAGON, Couleur.VIOLET, false, true));
        cartes.add(new CarteTransport(TypeCarteTransport.WAGON, Couleur.JAUNE, false, true));

        cartesJoueur2.addAll(cartes);
        int nbPorts = ports.size();

        List<String> inputs = new ArrayList<>();
        inputs.add("Miami");
        for (CarteTransport carte : cartes) {
            inputs.add(carte.getNom());
        }
        jeu.setInput(inputs);

        joueur2.jouerTour();

        assertEquals(1, portsJoueur2.size());
        assertTrue(portsJoueur2.contains(miami));

        assertEquals(4, defausseWagon.size());
        assertEquals(0, defausseBateau.size());
        assertEquals(1, cartesJoueur2.size());
        assertTrue(defausseWagon.contains(cartes.get(0)));
        assertTrue(defausseWagon.contains(cartes.get(1)));
        assertTrue(defausseWagon.contains(cartes.get(2)));
        assertTrue(cartesJoueur2.contains(cartes.get(3)));
        assertTrue(defausseWagon.contains(cartes.get(4)));

        assertEquals(nbPorts - 1, ports.size());
        assertTrue(cartesPoseesJoueur2.isEmpty());
    }

    @Test
    void testConstructionPortCartesInsuffisantes1() {
        Route route = getRoute("R107"); // Miami - New York
        Ville miami = getPort("Miami"); // Miami
        routes.remove(route);
        routesJoueur2.add(route);

        List<CarteTransport> cartes = new ArrayList<>();
        cartes.add(new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true));
        cartes.add(new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true));
        cartes.add(new CarteTransport(TypeCarteTransport.WAGON, Couleur.JAUNE, false, true));
        cartes.add(new CarteTransport(TypeCarteTransport.WAGON, Couleur.VIOLET, false, true));
        cartes.add(new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, true));

        cartesJoueur2.addAll(cartes);
        int nbPorts = ports.size();

        List<String> inputs = new ArrayList<>();
        inputs.add("Miami");
        for (CarteTransport carte : cartes) {
            inputs.add(carte.getNom());
        }
        inputs.add("WAGON");
        inputs.add("WAGON");
        jeu.setInput(inputs);

        joueur2.jouerTour();

        assertEquals(0, portsJoueur2.size());
        assertTrue(ports.contains(miami));
        assertEquals(nbPorts, ports.size());

        assertEquals(0, defausseWagon.size());
        assertEquals(0, defausseBateau.size());
        assertEquals(7, cartesJoueur2.size());
        assertTrue(cartesJoueur2.contains(cartes.get(0)));
        assertTrue(cartesJoueur2.contains(cartes.get(1)));
        assertTrue(cartesJoueur2.contains(cartes.get(2)));
        assertTrue(cartesJoueur2.contains(cartes.get(3)));
        assertTrue(cartesJoueur2.contains(cartes.get(4)));
        assertTrue(cartesPoseesJoueur2.isEmpty());
    }

    @Test
    void testConstructionPortCartesInsuffisantes2() {
        Route route = getRoute("R107"); // Miami - New York
        Ville miami = getPort("Miami"); // Miami
        routes.remove(route);
        routesJoueur2.add(route);

        List<CarteTransport> cartes = new ArrayList<>();
        cartes.add(new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, true));
        cartes.add(new CarteTransport(TypeCarteTransport.WAGON, Couleur.ROUGE, false, false));
        cartes.add(new CarteTransport(TypeCarteTransport.BATEAU, Couleur.ROUGE, false, true));
        cartes.add(new CarteTransport(TypeCarteTransport.BATEAU, Couleur.ROUGE, false, true));

        cartesJoueur2.addAll(cartes);
        int nbPorts = ports.size();

        List<String> inputs = new ArrayList<>();
        inputs.add("Miami");
        for (CarteTransport carte : cartes) {
            inputs.add(carte.getNom());
        }
        inputs.add("WAGON");
        inputs.add("WAGON");
        jeu.setInput(inputs);

        joueur2.jouerTour();

        assertEquals(0, portsJoueur2.size());
        assertTrue(ports.contains(miami));
        assertEquals(nbPorts, ports.size());

        assertEquals(0, defausseWagon.size());
        assertEquals(0, defausseBateau.size());
        assertEquals(6, cartesJoueur2.size());
        assertTrue(cartesJoueur2.contains(cartes.get(0)));
        assertTrue(cartesJoueur2.contains(cartes.get(1)));
        assertTrue(cartesJoueur2.contains(cartes.get(2)));
        assertTrue(cartesJoueur2.contains(cartes.get(3)));
        assertTrue(cartesPoseesJoueur2.isEmpty());
    }
}
