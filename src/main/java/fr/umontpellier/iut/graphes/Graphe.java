package fr.umontpellier.iut.graphes;

import java.util.*;

/**
 * (Multi) Graphe non-orienté pondéré. Le poids de chaque arête correspond à la longueur de la route correspondante.
 * Pour une paire de sommets fixée {i,j}, il est possible d'avoir plusieurs arêtes
 * d'extrémités i et j et de longueur identique, du moment que leurs routes sont différentes.
 * Par exemple, il est possible d'avoir les deux arêtes suivantes dans le graphe :
 * Arete a1 = new Arete(i,j,new RouteTerrestre(villes.get("Lima"), villes.get("Valparaiso"), Couleur.GRIS, 2))
 * et
 * Arete a2 = new Arete(i,j,new RouteTerrestre(villes.get("Lima"), villes.get("Valparaiso"), Couleur.GRIS, 2))
 * Dans cet exemple (issus du jeu), a1 et a2 sont deux arêtes différentes, même si leurs routes sont très similaires
 * (seul l'attribut nom est différent).
 */
public class Graphe {

    /**
     * Liste d'incidences :
     * mapAretes.get(1) donne l'ensemble d'arêtes incidentes au sommet dont l'identifiant est 1
     * Si mapAretes.get(u) contient l'arête {u,v} alors, mapAretes.get(v) contient aussi cette arête
     */
    private Map<Integer, HashSet<Arete>> mapAretes;


    /**
     * Construit un graphe à n sommets 0..n-1 sans arêtes
     */
    public Graphe(int n) {
        mapAretes= new HashMap<>();
        for (int i = 0; i < n; i++) {
            mapAretes.put(i, new HashSet<>());
        }
    }

    /**
     * Construit un graphe vide
     */
    public Graphe() {
        mapAretes = new HashMap<>();
    }

    /**
     * Construit un graphe à partir d'une collection d'arêtes.
     *
     * @param aretes la collection d'arêtes
     */
    public Graphe(Collection<Arete> aretes) {
        this();
        for(Arete arete : aretes){
            int s1 = arete.i();
            int s2 = arete.j();
            if(!mapAretes.containsKey(s1)){
                mapAretes.put(s1, new HashSet<>());
            }
            if(!mapAretes.containsKey(s2)){
                mapAretes.put(s2, new HashSet<>());
            }
            mapAretes.get(s1).add(arete);
            mapAretes.get(s2).add(arete);
        }
    }

    /**
     * À partir d'un graphe donné, construit un sous-graphe induit
     * par un ensemble de sommets, sans modifier le graphe donné
     *
     * @param graphe le graphe à partir duquel on construit le sous-graphe
     * @param X      l'ensemble de sommets qui définissent le sous-graphe
     *               prérequis : X inclus dans V()
     */
    public Graphe(Graphe graphe, Set<Integer> X) {
        this();
        for (Integer sommet: X) {
            for (Arete a : graphe.mapAretes.get(sommet)) {
                if(X.contains(a.i()) && X.contains(a.j())){
                    this.ajouterArete(a);
                }
            }

        }
    }

    /**
     * @return l'ensemble de sommets du graphe
     */
    public Set<Integer> ensembleSommets() {
       return mapAretes.keySet();
    }

    /**
     * @return l'ordre du graphe (le nombre de sommets)
     */
    public int nbSommets() {
        return ensembleSommets().size();
    }

    /**
     * @return le nombre d'arêtes du graphe (ne pas oublier que this est un multigraphe : si plusieurs arêtes sont présentes entre un même coupe de sommets {i,j}, il faut
     * toutes les compter)
     */
    public int nbAretes() {
        int somme=0;
        for (HashSet<Arete> a: mapAretes.values()) {
            somme += a.size();
        }
        return somme/2;
    }


    public boolean contientSommet(Integer v) {
        return mapAretes.containsKey(v);
    }

    /**
     * Ajoute un sommet au graphe s'il n'est pas déjà présent
     *
     * @param v le sommet à ajouter
     */
    public void ajouterSommet(Integer v) {
       if(!mapAretes.containsKey(v)){
           mapAretes.put(v,new HashSet<>());
       }
    }

    /**
     * Ajoute une arête au graphe si elle n'est pas déjà présente
     *
     * @param a l'arête à ajouter. Si les 2 sommets {i,j} de a ne sont pas dans l'ensemble,
     *          alors les sommets sont automatiquement ajoutés à l'ensemble de sommets du graphe
     */
    public void ajouterArete(Arete a) {
        int s1 = a.i();
        int s2 = a.j();

        if(!mapAretes.containsKey(s1)){
            mapAretes.put(s1, new HashSet<>());
        }
        if(!mapAretes.containsKey(s2)){
            mapAretes.put(s2, new HashSet<>());
        }
        mapAretes.get(s1).add(a);
        mapAretes.get(s2).add(a);
    }

    /**
     * Supprime une arête du graphe si elle est présente, sinon ne fait rien
     *
     * @param a arête à supprimer
     *
     */
    public void supprimerArete(Arete a) {
        if(mapAretes.get(a.i()).contains(a)){
            mapAretes.get(a.i()).remove(a);
            mapAretes.get(a.j()).remove(a);
        }
    }

    /**
     * @param a l'arête dont on veut tester l'existence
     * @return true si a est présente dans le graphe
     */
    public boolean existeArete(Arete a) {
        int s1 = a.i();
        int s2 = a.j();
        if(mapAretes.containsKey(s1) && mapAretes.containsKey(s2)){
            return mapAretes.get(s1).contains(a);
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Integer v : mapAretes.keySet()) {
            sb.append("sommet").append(v).append(" : ").append(mapAretes.get(v)).append("\n");
        }
        return sb.toString();
    }

    /**
     * Retourne l'ensemble des sommets voisins d'un sommet donné.
     * Si le sommet n'existe pas, l'ensemble retourné est vide.
     *
     * @param v l'identifiant du sommet dont on veut le voisinage
     */
    public Set<Integer> getVoisins(int v) {
        if(!mapAretes.containsKey(v)){
            return null;
        }else{
            Set<Integer> s = new HashSet<>();
            for (Arete a: mapAretes.get(v)) {
                s.add(a.getAutreSommet(v));
            }
            return s;
        }
    }

    /**
     * Supprime un sommet du graphe, ainsi que toutes les arêtes incidentes à ce sommet
     *
     * @param v le sommet à supprimer
     */
    public void supprimerSommet(int v) {
        for(Arete a : mapAretes.get(v)){
            mapAretes.get(a.getAutreSommet(v)).remove(a);
        }
        mapAretes.remove(v);
    }

    public int degre(int v) {
        return mapAretes.get(v).size();
    }

    /**
     *
     * @return le degré max, et Integer.Min_VALUE si le graphe est vide
     */
    public int degreMax(){
        if(mapAretes.isEmpty()){
            return  Integer.MIN_VALUE;
        }else{
            int max = 0;
            for (Integer s : mapAretes.keySet()) {
                int degS = degre(s);
                if(degS >=max){
                    max= degS;
                }
            }
            return max;
        }
    }

    public boolean estSimple(){
        for (HashSet<Arete> listA: mapAretes.values()) {
            for (Arete a1: listA) {
                for (Arete a2: listA) {
                    if(!a1.equals(a2)){
                        if(a1.estLaMeme(a2)){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * @return ture ssi pour tous sommets i,j de this avec (i!=j), alors this contient une arête {i,j}
     *
     */
    public boolean estComplet() {
        int ordre = nbSommets();
        return nbAretes() == ((ordre*(ordre-1))/2);
    }

    /**Fait par nous*/
    public List<Integer> sequenceSommets(){
        List<Integer> list = new ArrayList<>();
        for(Integer i : mapAretes.keySet()) {
            list.add(degre(i));
        }
        Collections.sort(list);
        return list;
    }


    /**
     * @return true ssi this est une chaîne. Attention, être une chaîne
     * implique en particulier que l'on a une seule arête (et pas plusieurs en parallèle) entre
     * les sommets successifs de la chaîne. On considère que le graphe vide est une chaîne.
     */
    public boolean estUneChaine() {
        int nbSommetsDeg1 = 0;
        List<Integer> list = sequenceSommets();

        int premierSommet1 = -1;
        int deuxiemeSommet1 = -1;

        if(list.size() > 2 ){
            if(list.get(2) != 2) {
                return false;
            }
            for(Integer i : mapAretes.keySet()){
                if(degre(i) == 1){
                    if(premierSommet1 == -1){
                        premierSommet1 = i;
                    }
                    else{
                        deuxiemeSommet1 = i;
                    }
                }
            }
            if(getVoisins(premierSommet1).contains(deuxiemeSommet1)){
                return false;
            }
        }
        else{
            if(list.get(0) != 1 || list.get(1) != 1){
                return false;
            }
        }

        for(Integer i : list){
            if(list.get(i) == 1){
                nbSommetsDeg1++;
                if(nbSommetsDeg1 == 3){
                    return false;
                }
            }

            else if(!(list.get(i) == 2)){
                return false;
            }
        }
        return true;
    }


    /**
     * @return true ssi this est un cycle. Attention, être un cycle implique
     * en particulier que l'on a une seule arête (et pas plusieurs en parallèle) entre
     * les sommets successifs du cycle.
     * On considère que dans le cas où G n'a que 2 sommets {i,j}, et 2 arêtes parallèles {i,j}, alors G n'est PAS un cycle.
     * On considère que le graphe vide est un cycle.
     */
    public boolean estUnCycle() {
        List<Integer> seq = sequenceSommets();
        if(this.mapAretes.isEmpty()){
            return true;
        } else if (nbSommets()==2) {
            return false;
        }else{
            for (Integer i:seq) {
                if(i!=2){
                    return false;
                }
            }
            List<Integer> sommetsParcourus = new ArrayList<>();
            Map.Entry<Integer, HashSet<Arete>> firstEntry = mapAretes.entrySet().iterator().next();
            List<Integer> sommetsAParcourir = new ArrayList<>(); sommetsAParcourir.add(firstEntry.getKey());
            while(!sommetsAParcourir.isEmpty()) {
                Integer s = sommetsAParcourir.get(0);
                sommetsParcourus.add(s);
                Set<Integer> voisinsS = getVoisins(s);
                Iterator<Integer> it = voisinsS.iterator();
                while(it.hasNext()) {
                    Integer sommet = it.next();
                    if (!sommetsParcourus.contains(sommet) && !sommetsAParcourir.contains(sommet)) {
                        sommetsAParcourir.add(sommet);
                    }
                    it.remove();
                }
                sommetsAParcourir.remove(s);
            }

            if(sommetsParcourus.size()==mapAretes.keySet().size()){
                return new HashSet<>(sommetsParcourus).containsAll(mapAretes.keySet()) && mapAretes.keySet().containsAll(sommetsParcourus);
            }
            else{
                return false;
            }
        }
    }

    /**FAIT PAR NOUS*/
    public boolean aUnCycle(){
        return nbAretes() > nbSommets() - 1;
    }

    public boolean estUnArbre(){
        return getEnsembleClassesConnexite().size()==1 && nbAretes()==(nbSommets()-1);
    }

    public boolean estUneForet() {
        Set<Set<Integer>> ensembleClassesConnexite = getEnsembleClassesConnexite();
        Set<Graphe> ensembleSousGrapheInduitsClassesCOnnexes = new HashSet<>();

        for (Set<Integer> classe : ensembleClassesConnexite) {
            ensembleSousGrapheInduitsClassesCOnnexes.add(new Graphe(this,classe));
        }
        for (Graphe g: ensembleSousGrapheInduitsClassesCOnnexes) {
            System.out.println(g.toString());
            if(!g.estUnArbre()){
                return false;
            }
        }
        return true;
    }

    public Set<Integer> getClasseConnexite(int v) {
        List<Integer> bleu = new ArrayList<>();
        Set<Integer> rouge = new HashSet<>();
        bleu.add(v);
        while(!bleu.isEmpty()){
            Integer sommetCourant = bleu.get(0);
            rouge.add(sommetCourant);
            Set<Integer> voisins = getVoisins(sommetCourant);
            for (Integer sommetVoisin: voisins ) {
                if(!bleu.contains(sommetVoisin) && !rouge.contains(sommetVoisin)){
                    bleu.add(sommetVoisin);
                }
            }

            bleu.remove(sommetCourant);

        }

        return rouge;
    }

    public Set<Set<Integer>> getEnsembleClassesConnexite() {
        Set<Set<Integer>>  res = new HashSet<>();

        for (Integer sommet: mapAretes.keySet()) {
            Set<Integer> clSommet = getClasseConnexite(sommet);
            if(!res.contains(clSommet)){
                res.add(clSommet);
            }
        }
        return res;
    }

    /**
     * @return true si et seulement si l'arête passée en paramètre est un isthme dans le graphe.
     */
    public boolean estUnIsthme(Arete a) {
        Set<Integer> classeConnex = getClasseConnexite(a.i());
        supprimerArete(a);
        Set<Integer> nouvelleClasseConnex = getClasseConnexite(a.i());
        ajouterArete(a);
        return !classeConnex.equals(nouvelleClasseConnex);
    }

    public boolean sontAdjacents(int i, int j) {
        for (Arete a : mapAretes.get(i)) {
            if(a.i()== j || a.j()==j){
                return true;
            }
        }
        return false;
    }

    /**
     * Fusionne les deux sommets passés en paramètre.
     * Toutes les arêtes reliant i à j doivent être supprimées (pas de création de boucle).
     * L'entier correspondant au sommet nouvellement créé sera le min{i,j}. Le voisinage du nouveau sommet
     * est l'union des voisinages des deux sommets fusionnés.
     * Si un des sommets n'est pas présent dans le graphe, alors cette fonction ne fait rien.
     */
    public void fusionnerSommets(int i, int j) {
        /*demander si on peut fusionner deux sommet qui ne sont pas de la meme classe de connexité*/

        if(mapAretes.containsKey(i) && mapAretes.containsKey(j)){
            HashSet<Arete> collectionAreteVoisines= new HashSet<>();
            int nouveauSommet = Math.min(i,j);
            for (Arete a : mapAretes.get(i)) {
                if(a.i()== j || a.j()==j){
                    supprimerArete(a);
                }else{
                    if(a.i()==i){
                        collectionAreteVoisines.add(new Arete(nouveauSommet,a.j(),a.route()));
                    } else if (a.j()==i) {
                        collectionAreteVoisines.add(new Arete(a.i(),nouveauSommet,a.route()));
                    }
                }
            }
            for (Arete a : mapAretes.get(j)) {
                if(a.i()== i || a.j()==i){
                    supprimerArete(a);
                }
                else{
                        if (a.i() == j) {
                            Arete a2 = new Arete(nouveauSommet, a.j(), a.route());
                            if (!a2.contientArete(collectionAreteVoisines)) {
                                collectionAreteVoisines.add(a2);
                            }
                        } else if (a.j() == j) {
                            Arete a2 = new Arete(a.i(), nouveauSommet, a.route());
                            if (!a2.contientArete(collectionAreteVoisines)) {
                                collectionAreteVoisines.add(a2);
                            }
                        }
                }
            }
            supprimerSommet(i);supprimerSommet(j);
            for (Arete a : collectionAreteVoisines ) {
                ajouterArete(a);
            }

        }
    }



    /**
     * @return true si et seulement si la séquence d'entiers passée en paramètre correspond à un graphe simple valide.
     * La pondération des arêtes devrait être ignorée.
     */
    public static boolean sequenceEstGraphe(List<Integer> sequence) {
        /* utilisé 'algo de la prof + addition de la sequence*/

        Collections.sort(sequence,Collections.reverseOrder());
        int compteur=0;
        for (Integer degre: sequence) {
            if(compteur+1>= sequence.size()) break;
            int chiffreAenlever = degre;
            System.out.println(sequence);
            int i = compteur+1;
            while(chiffreAenlever!=0 && i < sequence.size()){
                Integer degreCourant = sequence.get(i);
                if(degreCourant>=1 ){
                    sequence.set(i,degreCourant-1);
                }
                chiffreAenlever--;
                i++;
            }
            sequence.set(compteur,0);
            compteur++;
        }
        System.out.println("ap boucle: " +sequence);
        for (Integer degre:sequence ) {
            if(degre!=0){
                return false;
            }
        }
        return true;
    }

    /**
     * @return true si et seulement si la séquence d'entiers passée en paramètre correspond à un graphe valide.
     * La pondération des arêtes devrait être ignorée.
     */
    public static boolean sontIsomorphes(Graphe g1, Graphe g2) {
        /*utiliser les bijections genre sommet a -> sommet 2 etc*/
        List<Integer> s1 = g1.sequenceSommets(); List<Integer> s2 = g2.sequenceSommets();
        Set<Set<Integer>> ecc1 = g1.getEnsembleClassesConnexite(); Set<Set<Integer>> ecc2 = g2.getEnsembleClassesConnexite();

        // Cree toutes les combinaisons possibles du graphe 1
        ArrayList<ArrayList<Integer>> combinaisonsG1 = new ArrayList<>();
        // Mauvaise idée en fait jsp :///


        if(!s1.equals(s2) || ecc1.size() != ecc2.size()){
            return false;
        }
        else{

        }
        return false;
    }

    /**
     * Retourne un plus court chemin entre 2 sommets suivant l'algorithme de Dijkstra.
     * Les poids des arêtes sont les longueurs des routes correspondantes.
     * @param depart le sommet de départ
     * @param arrivee le sommet d'arrivée
     * @param pondere true si les arêtes sont pondérées (pas les longueurs des routes correspondantes dans le jeu)
     *                false si toutes les arêtes ont un poids de 1 (utile lorsque les routes associées sont complètement omises)
     *
     */
    public List<Integer> algoDikstra(int depart, int arrivee, boolean pondere){
        /*donc pondere si true : on doit prendre en compte la ponderation
        * si false: on ne prend pas on compte les ponderation donc juste le plus rapide*/

        throw new RuntimeException("Méthode non implémentée");
    }

    /**
     * Retourne un plus court chemin entre 2 sommets sans répétition de sommets
     * @param depart le sommet de départ
     * @param arrivee le sommet d'arrivée
     * @param pondere true si les arêtes sont pondérées (pas les longueurs des routes correspondantes dans le jeu)
     *                false si toutes les arêtes ont un poids de 1 (utile lorsque les routes associées sont complètement omises)
     */
    public List<Integer> parcoursSansRepetition(int depart, int arrivee, boolean pondere) {
        throw new RuntimeException("Méthode non implémentée");
    }
}