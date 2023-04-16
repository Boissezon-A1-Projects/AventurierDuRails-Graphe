package fr.umontpellier.iut.rails.data;

import java.util.*;

public final class CarteTransport implements Comparable<CarteTransport> {

    /**
     * Compteur du nombre de routes instanciées (utilisé pour donner automatiquement
     * un id unique à chaque route)
     */

    private static int compteur = 1;
    private final TypeCarteTransport type;
    private final Couleur couleur;
    private final boolean estDouble;
    private final boolean ancre;
    private final String nom;

    public CarteTransport(TypeCarteTransport type, Couleur couleur, boolean estDouble, boolean ancre) {
        this.type = type;
        this.couleur = couleur;
        this.estDouble = estDouble;
        this.ancre = ancre;
        this.nom = "C" + compteur++;
    }

    @Override
    public int compareTo(CarteTransport o) {
        if (getType() != o.getType()) {
            return getType().compareTo(o.getType());
        }
        if (getCouleur() != o.getCouleur()) {
            return getCouleur().compareTo(o.getCouleur());
        }
        if (estDouble() != o.estDouble()) {
            return Boolean.compare(estDouble(), o.estDouble());
        }
        if (getAncre() != o.getAncre()) {
            return Boolean.compare(getAncre(), o.getAncre());
        }
        return getNom().compareTo(o.getNom());
    }

    public String toLog() {
        String label;
        if (getType() == TypeCarteTransport.JOKER) {
            label = "Joker";
        } else if (getType() == TypeCarteTransport.BATEAU) {
            if (estDouble) {
                label = "Double";
            } else {
                label = "Bateau";
            }
        } else {
            label = "Wagon";
        }

        return String.format(
                "<img class=\"couleur\" src=\"images/symbole-%s.png\"><span class=\"nom-carte %s %s\">%s</span>",
                couleur.name(), type.name().toLowerCase(), couleur.name().toLowerCase(), label);
    }

    public static String listToLog(List<CarteTransport> listeCartes) {
        StringJoiner sj = new StringJoiner(", ");
        List<CarteTransport> cartes;
        for (TypeCarteTransport type : TypeCarteTransport.values()) {
            for (Couleur couleur : Couleur.values()) {
                for (boolean estDouble : new boolean[]{true, false}) {
                    cartes = listeCartes.stream()
                            .filter(c -> c.getType() == type && c.getCouleur() == couleur && c.estDouble() == estDouble)
                            .toList();
                    if (cartes.size() == 1) {
                        sj.add(cartes.get(0).toLog());
                    } else if (cartes.size() > 1) {
                        sj.add(String.format("%s x%d", cartes.get(0).toLog(), cartes.size()));
                    }
                }
            }
        }
        return sj.toString();
    }

    public TypeCarteTransport getType() {
        return type;
    }

    public Couleur getCouleur() {
        return couleur;
    }

    public boolean estDouble() {
        return estDouble;
    }

    public boolean getAncre() {
        return ancre;
    }

    public String getNom() {
        return nom;
    }

    @Override
    public String toString() {
        String label;
        if (type == TypeCarteTransport.JOKER) {
            label = "Joker";
        } else if (type == TypeCarteTransport.BATEAU) {
            if (estDouble) {
                label = "Double";
            } else {
                label = "Bateau";
            }
        } else {
            label = "Wagon";
        }
        return String.format("%s %s (%s)", label, couleur.name(), nom);
    }

    public static class Catalogue {
        private final Map<TypeCarteTransport, Map<Couleur, List<CarteTransport>>> catalogue = new HashMap<>();

        public Catalogue(List<CarteTransport> cartes) {
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
            return catalogue.get(type).get(couleur).stream().filter(c -> !c.estDouble).toList();
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
                return Couleur.getCouleursSimples().stream()
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
            for (Couleur c : Couleur.getCouleursSimples()) {
                if (!get(TypeCarteTransport.WAGON, c).isEmpty() || !get(TypeCarteTransport.BATEAU, c).isEmpty()) {
                    return c;
                }
            }
            return Couleur.GRIS;
        }
    }
}
