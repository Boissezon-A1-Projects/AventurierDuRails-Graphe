package fr.umontpellier.iut.rails;

import fr.umontpellier.iut.rails.data.*;

import java.lang.reflect.Field;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestUtils {
    public static final long TIMEOUT_VALUE = 500;
    public static final List<Couleur> couleursSimples = Arrays.asList(
            Couleur.NOIR,
            Couleur.BLANC,
            Couleur.JAUNE,
            Couleur.ROUGE,
            Couleur.VERT,
            Couleur.VIOLET);

    /**
     * Renvoie un attribut d'un objet à partir de son nom.
     * La méthode cherche s'il existe un champ déclaré dans la classe de l'objet et
     * si ce n'est pas le cas remonte dans la hiérarchie des classes jusqu'à trouver
     * un champ avec le nom voulu ou renvoie null.
     * 
     * @param object objet dont on cherche le champ
     * @param name   nom du champ
     * @return le champ de l'objet, avec un type statique Object
     */
    public static Object getAttribute(Object object, String name) {
        Class<?> c = object.getClass();
        while (c != null) {
            try {
                Field field = c.getDeclaredField(name);
                field.setAccessible(true);
                return field.get(object);
            } catch (NoSuchFieldException e) {
                c = c.getSuperclass();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    /**
     * Setter générique pour forcer la valeur d'un attribut quelconque.
     * La méthode cherche s'il existe un champ déclaré dans la classe de l'objet et
     * si ce n'est pas le cas remonte dans la hiérarchie des classes jusqu'à trouver
     * un champ avec le nom voulu. Lorsque le champ est trouvé, on lui donne la
     * valeur
     * passée en argument.
     * 
     * @param object objet dont on cherche le champ
     * @param name   nom du champ
     * @param value  valeur à donner au champ
     */
    public static void setAttribute(Class<?> c, String name, Object value) {
        while (c != null) {
            try {
                Field field = c.getDeclaredField(name);
                field.setAccessible(true);
                field.set(c, value);
                return;
            } catch (NoSuchFieldException e) {
                c = c.getSuperclass();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return;
            }
        }
        throw new RuntimeException("No such field: " + name);
    }

    /**
     * Setter générique pour forcer la valeur d'un attribut quelconque.
     * La méthode cherche s'il existe un champ déclaré dans la classe de l'objet et
     * si ce n'est pas le cas remonte dans la hiérarchie des classes jusqu'à trouver
     * un champ avec le nom voulu. Lorsque le champ est trouvé, on lui donne la
     * valeur
     * passée en argument.
     * 
     * @param object objet dont on cherche le champ
     * @param name   nom du champ
     * @param value  valeur à donner au champ
     */
    public static void setAttribute(Object object, String name, Object value) {
        Class<?> c = object.getClass();
        while (c != null) {
            try {
                Field field = c.getDeclaredField(name);
                field.setAccessible(true);
                field.set(object, value);
                return;
            } catch (NoSuchFieldException e) {
                c = c.getSuperclass();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return;
            }
        }
        throw new RuntimeException("No such field: " + name);
    }

    /**
     * Met les cartes wagon passées en argument dans la main d'un joueur (la main
     * est vidée avant, pour contenir exactement les cartes indiquées)
     * 
     * @param joueur          le joueur dont on fixe les cartes en main
     * @param cartesTransport les cartes à mettre en main
     */
    public static void setCartesTransport(Joueur joueur, CarteTransport... cartesTransport) {
        List<CarteTransport> mainJoueur = (List<CarteTransport>) getAttribute(joueur, "cartesTransport");
        mainJoueur.clear();
        Collections.addAll(mainJoueur, cartesTransport);
    }

    public static void setCartesTransport(Joueur joueur, List<CarteTransport> cartesTransport) {
        List<CarteTransport> mainJoueur = (List<CarteTransport>) getAttribute(joueur, "cartesTransport");
        mainJoueur.clear();
        mainJoueur.addAll(cartesTransport);
    }

    public static Route getRoute(List<Route> routes, String nom) {
        for (Route r : routes) {
            if (r.getNom().equals(nom)) {
                return r;
            }
        }
        return null;
    }

    public static List<CarteTransport> getCartesTransport(Joueur joueur) {
        return (List<CarteTransport>) getAttribute(joueur, "cartesTransport");
    }

    public static List<CarteTransport> getCartesTransportPosees(Joueur joueur) {
        return (List<CarteTransport>) getAttribute(joueur, "cartesTransportPosees");
    }

    public static List<Destination> getDestinations(Joueur joueur) {
        return (List<Destination>) getAttribute(joueur, "destinations");
    }

    public static String getNom(Destination d) {
        return (String) getAttribute(d, "nom");
    }

    public static int getNbPionsWagon(Joueur joueur) {
        return (int) getAttribute(joueur, "nbPionsWagon");
    }

    public static int getNbPionsWagonEnReserve(Joueur joueur) {
        return (int) getAttribute(joueur, "nbPionsWagonEnReserve");
    }

    public static int getNbPionsBateau(Joueur joueur) {
        return (int) getAttribute(joueur, "nbPionsBateau");
    }

    public static int getNbPionsBateauEnReserve(Joueur joueur) {
        return (int) getAttribute(joueur, "nbPionsBateauEnReserve");
    }

    public static int getScore(Joueur joueur) {
        return (int) getAttribute(joueur, "score");
    }

    public static void setJoueurs(Jeu jeu, List<Joueur> newJoueurs) {
        List<Joueur> joueurs = jeu.getJoueurs();
        joueurs.clear();
        joueurs.addAll(newJoueurs);
        setAttribute(jeu, "joueurCourant", newJoueurs.get(0));
    }

    public static <T> void assertContainsExactly(List<T> list, T... values) {
        assertEquals(values.length, list.size());
        for (T v : values) {
            assertTrue(list.contains(v));
        }
    }

    public static class CatalogueCartes {
        private final Map<TypeCarteTransport, Map<Couleur, List<CarteTransport>>> catalogue = new HashMap<>();

        public CatalogueCartes(List<CarteTransport> cartes) {
            for (TypeCarteTransport t : TypeCarteTransport.values()) {
                catalogue.put(t, new HashMap<>());
                for (Couleur c : Couleur.values()) {
                    catalogue.get(t).put(c, new ArrayList<>());
                }
            }
            for (CarteTransport c : cartes) {
                catalogue.get(c.getType()).get(c.getCouleur()).add(c);
            }
        }

        /**
         * Renvoie la liste des cartes du catalogue correspondant au type et à la
         * couleur
         */
        public List<CarteTransport> get(TypeCarteTransport type, Couleur couleur) {
            return catalogue.get(type).get(couleur);
        }

        public List<CarteTransport> getDoubles(TypeCarteTransport type, Couleur couleur) {
            return catalogue.get(type).get(couleur).stream().filter(CarteTransport::estDouble).toList();
        }

        public List<CarteTransport> getSimples(TypeCarteTransport type, Couleur couleur) {
            return catalogue.get(type).get(couleur).stream().filter(c -> !c.estDouble()).toList();
        }

        /**
         * Renvoie la valeur totale des cartes du catalogue correspondant à un type
         *
         * @param type        le type des cartes à compter
         * @param compteJoker si vrai, les jokers sont comptés comme des cartes du type
         *                    demandé
         */
        public int getValeur(TypeCarteTransport type, boolean compteJoker) {
            int total = 0;
            for (Couleur couleur : Couleur.values()) {
                for (CarteTransport carte : get(type, couleur)) {
                    if (carte.estDouble()) {
                        total += 2;
                    } else {
                        total += 1;
                    }
                }
            }
            if (type != TypeCarteTransport.JOKER && compteJoker) {
                total += getValeur(TypeCarteTransport.JOKER, false);
            }
            return total;
        }

        /**
         * Renvoie la valeur totale des cartes du catalogue correspondant à un type.
         * Les jokers ne sont pas comptés.
         */
        public int getValeur(TypeCarteTransport type) {
            return getValeur(type, false);
        }

        /**
         * Renvoie la valeur totale des cartes du catalogue correspondant à un type et à
         * une couleur
         *
         * @param type        le type des cartes à compter
         * @param couleur     la couleur des cartes comptées. Si couleur est GRIS, la
         *                    méthode renvoie la valeur de la couleur ayant la plus
         *                    grande valeur
         * @param compteJoker si vrai, les jokers sont comptés comme des cartes de la
         *                    couleur demandée
         */
        public int getValeur(TypeCarteTransport type, Couleur couleur, boolean compteJoker) {

            if (couleur == Couleur.GRIS) {
                return couleursSimples.stream()
                        .map(c -> getValeur(type, c, compteJoker))
                        .reduce(0, Integer::max);
            }

            int total = 0;
            for (CarteTransport c : catalogue.get(type).get(couleur)) {
                if (c.estDouble()) {
                    total += 2;
                } else {
                    total += 1;
                }
            }
            if (compteJoker && type != TypeCarteTransport.JOKER) {
                total += getValeur(TypeCarteTransport.JOKER);
            }
            return total;
        }

        /**
         * Renvoie la valeur totale des cartes du catalogue correspondant à un type et à
         * une couleur.
         * Les jokers ne sont pas comptés.
         *
         * @param type    le type des cartes à compter
         * @param couleur la couleur des cartes comptées. Si couleur est GRIS, la
         *                méthode renvoie la valeur de la couleur ayant la plus grande
         *                valeur
         */
        public int getValeur(TypeCarteTransport type, Couleur couleur) {
            return getValeur(type, couleur, false);
        }

        /**
         * Renvoie la valeur totale des cartes du catalogue
         */
        public int getValeur() {
            int total = 0;
            for (TypeCarteTransport t : TypeCarteTransport.values()) {
                total += getValeur(t);
            }
            return total;
        }

        /**
         * Renvoie la première couleur dont le catalogue contient au moins une carte
         * (WAGON ou BATEAU).
         * Si le catalogue ne contient aucune carte de couleur, la méthode renvoie GRIS.
         */
        public Couleur getCouleur() {
            for (Couleur c : couleursSimples) {
                if (!get(TypeCarteTransport.WAGON, c).isEmpty() || !get(TypeCarteTransport.BATEAU, c).isEmpty()) {
                    return c;
                }
            }
            return Couleur.GRIS;
        }
    }

    public static void testIntegrite(Jeu jeu) {
        List<CarteTransport> allCartesTransport = new ArrayList<>();
        allCartesTransport.addAll((List<CarteTransport>) getAttribute(jeu, "cartesTransportVisibles"));
        PilesCartesTransport pilesWagon = (PilesCartesTransport) TestUtils.getAttribute(jeu,
                "pilesDeCartesWagon");
        allCartesTransport.addAll((List<CarteTransport>) getAttribute(pilesWagon, "pilePioche"));
        allCartesTransport.addAll((List<CarteTransport>) getAttribute(pilesWagon, "pileDefausse"));
        PilesCartesTransport pilesBateau = (PilesCartesTransport) TestUtils.getAttribute(jeu,
                "pilesDeCartesBateau");
        allCartesTransport.addAll((List<CarteTransport>) getAttribute(pilesBateau, "pilePioche"));
        allCartesTransport.addAll((List<CarteTransport>) getAttribute(pilesBateau, "pileDefausse"));

        List<Destination> allDestinations = new ArrayList<>();
        allDestinations.addAll((List<Destination>) getAttribute(jeu, "pileDestinations"));

        for (Joueur joueur: jeu.getJoueurs()) {
            allCartesTransport.addAll((List<CarteTransport>) getAttribute(joueur, "cartesTransport"));
            allDestinations.addAll((List<Destination>) getAttribute(joueur, "destinations"));
            int nbPionsWagonPoses = 0;
            int nbPionsBateauPoses = 0;
            for (Route route: (List<Route>) getAttribute(joueur, "routes")) {
                if (route instanceof RouteMaritime) {
                    nbPionsBateauPoses += route.getLongueur();
                } else {
                    nbPionsWagonPoses += route.getLongueur();
                }
            }
            int nbPionsWagon = (int) getAttribute(joueur, "nbPionsWagon");
            int nbPionsBateau = (int) getAttribute(joueur, "nbPionsBateau");
            int nbPionsWagonEnReserve = (int) getAttribute(joueur, "nbPionsWagonEnReserve");
            int nbPionsBateauEnReserve = (int) getAttribute(joueur, "nbPionsBateauEnReserve");
            assertEquals(60, nbPionsWagon + nbPionsWagonPoses + nbPionsBateau + nbPionsBateauPoses);
            assertEquals(25, nbPionsWagon + nbPionsWagonEnReserve + nbPionsWagonPoses);
            assertEquals(50, nbPionsBateau + nbPionsBateauEnReserve + nbPionsBateauPoses);

            List<Ville> ports = (List<Ville>) getAttribute(joueur, "ports");
            assertTrue(ports.size() <= 3);
        }

        assertEquals(140, allCartesTransport.size());
        assertEquals(65, allDestinations.size());
    }
}
