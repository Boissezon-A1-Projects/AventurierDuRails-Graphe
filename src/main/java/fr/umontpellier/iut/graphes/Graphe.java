package fr.umontpellier.iut.graphes;

import fr.umontpellier.iut.rails.Route;

import java.awt.image.AreaAveragingScaleFilter;
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

    public Map<Integer, HashSet<Arete>> getMapAretes() {
        return mapAretes;
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
            if(getVoisins(j).size()==1 && getVoisins(j).contains(i) && getVoisins(i).size()==1 && getVoisins(i).contains(j)){
                supprimerSommet(i);supprimerSommet(j);
                ajouterSommet(Math.min(i,j));

            }
            else {
                HashSet<Arete> collectionAreteVoisines = new HashSet<>();
                List<Arete> areteASupprimer = new ArrayList<>();
                int nouveauSommet = Math.min(i, j);
                for (Arete a : mapAretes.get(i)) {
                    if (a.i() == j || a.j() == j) {
                        areteASupprimer.add(a);
                    } else {
                        if (a.i() == i) {
                            collectionAreteVoisines.add(new Arete(nouveauSommet, a.j(), a.route()));
                        } else if (a.j() == i) {
                            collectionAreteVoisines.add(new Arete(a.i(), nouveauSommet, a.route()));
                        }
                    }
                }
                for (Arete a: areteASupprimer) {
                    supprimerArete(a);
                }
                areteASupprimer.clear();
                for (Arete a : mapAretes.get(j)) {
                    if (a.i() == i || a.j() == i) {
                        areteASupprimer.add(a);
                    } else {
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
                for (Arete a: areteASupprimer) {
                    supprimerArete(a);
                }
                supprimerSommet(i);
                supprimerSommet(j);
                for (Arete a : collectionAreteVoisines) {
                    ajouterArete(a);
                }
            }
        }
    }



    /**
     * @return true si et seulement si la séquence d'entiers passée en paramètre correspond à un graphe simple valide.
     * La pondération des arêtes devrait être ignorée.
     */
    public static boolean sequenceEstGraphe(List<Integer> sequence) {
        /* utilisé 'algo de la prof + addition de la sequence*/

        if(sequence.isEmpty()){return false;}
        int nbImpair=0;
        for (Integer deg: sequence ) {
            if(deg%2!=0){
                nbImpair++;
            }
        }
        if(nbImpair%2!=0){
            return false;
        }
        Collections.sort(sequence,Collections.reverseOrder());
        int compteur=0;

        for (Integer degre: sequence) {
            if(compteur+1>= sequence.size()) break;
            int chiffreAenlever = degre;
            System.out.println(sequence);
            int i = compteur+1;
            int nbDegChange=0;
            while(chiffreAenlever!=0 && i < sequence.size()){
                Integer degreCourant = sequence.get(i);
                if(degreCourant>=1 ){
                    sequence.set(i,degreCourant-1);
                    nbDegChange++;
                }
                chiffreAenlever--;
                i++;
            }
            if(nbDegChange>0) {
                if(nbDegChange==degre) {
                    sequence.set(compteur, 0);
                }
            }
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


    public static boolean sontIsoAide(Graphe g1, Graphe g2){
        List<Integer> s1 = g1.sequenceSommets(); List<Integer> s2 = g2.sequenceSommets();
        Set<Set<Integer>> ecc1 = g1.getEnsembleClassesConnexite(); Set<Set<Integer>> ecc2 = g2.getEnsembleClassesConnexite();
        if(!s1.equals(s2)){
            return false;
        } else if (ecc1.size()!=ecc2.size()) {
            return false;
        } else if (g1.nbAretes()!=g2.nbAretes()){
            return false;
        }else {

            Map<Integer, Integer> listeOccurenceDeg = new HashMap<>();
            Map<Integer, List<Integer>> listeSommetsParDeg2 = new HashMap<>();
            for (Integer deg : s1) {
                if (!listeOccurenceDeg.containsKey(deg)) {
                    listeOccurenceDeg.put(deg, Collections.frequency(s1, deg));
                }
            }
            Integer somm1 = 0;
            Integer somm2;
            int minDeg1 = s1.get(0);
            for (Integer so1 : g1.ensembleSommets()) {
                if (g1.getVoisins(so1).size() == minDeg1) {
                    somm1 = so1;
                    break;
                }
            }
            for (Integer so2 : g2.ensembleSommets()) {
                if (!listeSommetsParDeg2.containsKey(g2.getVoisins(so2).size())) {
                    List<Integer> s = new ArrayList<>();
                    s.add(so2);
                    listeSommetsParDeg2.put(g2.getVoisins(so2).size(), s);
                } else {
                    listeSommetsParDeg2.get(g2.getVoisins(so2).size()).add(so2);
                }
            }

            somm2 = listeSommetsParDeg2.get(minDeg1).get(0);

            ArrayList<Integer> sommetsParcourus1 = new ArrayList<>();
            ArrayList<Integer> sommetsAParcourir1 = new ArrayList<>();
            ArrayList<Integer> sommetsParcourus2 = new ArrayList<>();
            ArrayList<Integer> sommetsAParcourir2 = new ArrayList<>();
            ArrayList<Integer> sommetsNonParcourus2 = new ArrayList<>();
            for (Integer s : g2.ensembleSommets()) {
                if (!s.equals(somm2)) {
                    sommetsNonParcourus2.add(s);
                }
            }

            sommetsAParcourir1.add(somm1);
            sommetsAParcourir2.add(somm2);

            while (!sommetsAParcourir1.isEmpty()) {
                int sommetCourantG1 = sommetsAParcourir1.get(0);
                int nbDegCourant = listeOccurenceDeg.get(g1.getVoisins(sommetCourantG1).size());
                int compteur = 0;
                Set<Integer> seqVoisins1 = new HashSet<>();
                for (Integer voisins : g1.getVoisins(sommetCourantG1)) {
                    seqVoisins1.add(g1.getVoisins(voisins).size());
                }


                List<Integer> sommetMemeDeg2 = listeSommetsParDeg2.get(g1.getVoisins(sommetsAParcourir1.get(0)).size());
                for (Integer sCour : sommetsParcourus2) {
                    if (sommetMemeDeg2.contains(sCour)) {
                        sommetMemeDeg2.remove((Integer) sCour);
                    }
                }
                boolean fini = false;
                if (sommetsNonParcourus2.size() == 1) {
                    Set<Integer> seqVoisins2 = new HashSet<>();
                    for (Integer voisin : g2.getVoisins(sommetsNonParcourus2.get(0))) {
                        seqVoisins2.add(g2.getVoisins(voisin).size());
                    }
                    if (seqVoisins1.equals(seqVoisins2)) {
                        break;
                    } else {
                        return false;
                    }
                }
                while (!fini && sommetMemeDeg2.size() > 0) {
                    int sommetCourantG2 = sommetMemeDeg2.get(0);
                    if (compteur == nbDegCourant) {
                        return false;
                    }
                    Set<Integer> seqVoisins2 = new HashSet<>();
                    for (Integer voisin : g2.getVoisins(sommetCourantG2)) {
                        seqVoisins2.add(g2.getVoisins(voisin).size());
                    }
                    if (!seqVoisins1.equals(seqVoisins2)) {
                        compteur++;

                        if (sommetsAParcourir2.get(0).equals((Integer) sommetCourantG2)) {
                            sommetsAParcourir2.remove((Integer) sommetCourantG2);
                            sommetsAParcourir2.add(sommetMemeDeg2.get(1));
                            sommetsNonParcourus2.add(sommetCourantG2);
                            sommetsNonParcourus2.remove((Integer) sommetMemeDeg2.get(1));
                        }
                        sommetMemeDeg2.remove((Integer) sommetCourantG2);

                    } else {
                        sommetsParcourus2.add(sommetCourantG2);
                        sommetsAParcourir2.remove((Integer) sommetCourantG2);
                        if (sommetsNonParcourus2.contains(sommetCourantG2)) {
                            sommetsNonParcourus2.remove((Integer) sommetCourantG2);
                        }
                        if(!sommetsAParcourir2.containsAll(g2.getVoisins(sommetCourantG2))){
                            sommetsAParcourir2.addAll(g2.getVoisins(sommetCourantG2));
                        }
                        fini = true;
                    }
                }


                sommetsParcourus1.add(sommetCourantG1);
                sommetsAParcourir1.remove(Integer.valueOf(sommetCourantG1));
                for (Integer s : g1.getVoisins(sommetCourantG1)) {
                    if (!sommetsParcourus1.contains(s) && !sommetsAParcourir1.contains(s)) {
                        sommetsAParcourir1.add(s);
                    }
                }
            }
            return true;
        }

    }



    /**
     * @return true si et seulement si la séquence d'entiers passée en paramètre correspond à un graphe valide.
     * La pondération des arêtes devrait être ignorée.
     */
    public static boolean sontIsomorphes(Graphe g1, Graphe g2) {

        List<Integer> s1 = g1.sequenceSommets(); List<Integer> s2 = g2.sequenceSommets();
        Set<Set<Integer>> ecc1 = g1.getEnsembleClassesConnexite(); Set<Set<Integer>> ecc2 = g2.getEnsembleClassesConnexite();
        if(!s1.equals(s2)){
            return false;
        } else if (ecc1.size()!=ecc2.size()) {
            return false;
        }else{
            if(ecc1.size()==1 && ecc2.size()==1){
                return sontIsoAide(g1,g2);
            }
            else{
                //map ordre : graphes correspondants pour les deux graphes
                Map<Integer,List<Graphe>> listeccParOrdrecc1 = new HashMap<>();
                for (Set<Integer> cc: ecc1) {
                    Graphe g = new Graphe(g1, cc);
                    if(!listeccParOrdrecc1.containsKey(cc.size())){
                        List<Graphe> l = new ArrayList<>(); l.add(g);
                        listeccParOrdrecc1.put(cc.size(),l);
                    }
                    else {
                        listeccParOrdrecc1.get(cc.size()).add(g);
                    }
                }
                Map<Integer,List<Graphe>> listeccParOrdrecc2 = new HashMap<>();
                for (Set<Integer> cc: ecc2) {
                    Graphe g = new Graphe(g2,cc);
                    if(!listeccParOrdrecc2.containsKey(cc.size())){
                        List<Graphe> l = new ArrayList<>(); l.add(g);
                        listeccParOrdrecc2.put(cc.size(),l);
                    }
                    else {
                        listeccParOrdrecc2.get(cc.size()).add(g);
                    }
                }

                //vérification qu'il y est le meme nombre de classe de connexité par ordre
                for (Integer ordre: listeccParOrdrecc1.keySet() ) {
                    if(!listeccParOrdrecc2.containsKey(ordre)){
                        return false;
                    }else{
                        if(listeccParOrdrecc1.get(ordre).size()!=listeccParOrdrecc2.get(ordre).size()){
                            return false;
                        }
                    }
                }

                List<Graphe> ccParcourues1= new ArrayList<>();
                List<Graphe> ccAParcourir1 = new ArrayList<>();
                //on a joute le premier dans à parcourir
                for (Integer s:listeccParOrdrecc1.keySet()) {
                    ccAParcourir1.add(listeccParOrdrecc1.get(s).get(0));
                    break;
                }
                for (Integer ordre: listeccParOrdrecc1.keySet()) {
                    //pour chaque ordre, tant que les values de l'ordre sont encore là on continue
                    while(!listeccParOrdrecc1.get(ordre).isEmpty()){
                        // on prend le premier de graphe 1 et on cherche s'il y en a un qui correspond
                        Graphe courant1 = listeccParOrdrecc1.get(ordre).get(0);
                        boolean trouve = false;
                        int compteur = listeccParOrdrecc2.get(ordre).size()-1;
                        while(!trouve && compteur>=0){
                            Graphe courant2 = listeccParOrdrecc2.get(ordre).get(compteur);
                            boolean sontIso = sontIsoAide(courant1,courant2);
                            if(sontIso){
                                trouve =true;
                                listeccParOrdrecc2.get(ordre).remove(courant2);
                                listeccParOrdrecc1.get(ordre).remove(courant1);
                            }
                            else{
                                compteur --;
                            }
                        }
                        if(compteur<0){
                            return false;
                        }
                    }


                }
                return true;
            }

        }
    }

    /**
     * Retourne un plus court chemin entre 2 sommets.
     *
     * @param depart  le sommet de départ
     * @param arrivee le sommet d'arrivée
     * @param pondere true si les arêtes sont pondérées (pas les longueurs des routes correspondantes dans le jeu)
     *                false si toutes les arêtes ont un poids de 1 (utile lorsque les routes associées sont complètement omises)
     * @return une liste d'entiers correspondant aux sommets du chemin dans l'ordre : l'élément en position 0 de la liste
     * est le sommet de départ, et l'élément à la dernière position de la liste (taille de la liste - 1) est le somme d'arrivée.
     * Si le chemin n'existe pas, retourne une liste vide (initialisée avec 0 éléments).
     */
    public List<Integer> parcoursSansRepetition(int depart, int arrivee, boolean pondere) {

        if(depart == arrivee){
            List<Integer> liste = new ArrayList<>();
            liste.add(depart);
            return liste;
        }

        Map<Integer, Integer> distancesOrigine = new HashMap<>();
        Map<Integer, Integer> predecesseurs = new HashMap<>();
        List<Integer> aParcourir = new ArrayList<>();

        List<Integer> dejaVisites = new ArrayList<>();

        int indiceMin;

        for(Integer i : mapAretes.keySet()){
            distancesOrigine.put(i,Integer.MAX_VALUE);
            predecesseurs.put(i, null);
        }
        distancesOrigine.put(depart, 0);

        aParcourir.add(depart);

        while(!aParcourir.isEmpty()){

            indiceMin = 0;
            for(int i = 0; i < aParcourir.size(); i++){
                if(distancesOrigine.get(i) <= distancesOrigine.get(aParcourir.get(indiceMin))){
                    indiceMin = i;
                }
            }
            Integer sommetCourant = aParcourir.remove(indiceMin);
            HashSet<Arete> aretesIncidentes = mapAretes.get(sommetCourant);

            for(Arete arete : aretesIncidentes){
                Integer autreSommet = arete.getAutreSommet(sommetCourant);
                int tailleArete = 1;
                if(arete.route() != null) {
                    tailleArete = arete.route().getLongueur();
                }

                if(!dejaVisites.contains(autreSommet) && !aParcourir.contains(autreSommet)){
                    aParcourir.add(autreSommet);
                    if(!pondere){
                        tailleArete = 1;
                    }
                    int distanceTotale = distancesOrigine.get(sommetCourant) + tailleArete;
                    if(distanceTotale < distancesOrigine.get(autreSommet)) {
                        distancesOrigine.put(autreSommet, distanceTotale);
                    }
                    predecesseurs.put(autreSommet, sommetCourant);
                }
            }
            dejaVisites.add(sommetCourant);
        }

        if(predecesseurs.get(arrivee) == null){
            return new ArrayList<>();
        }
        List<Integer> chemin = new ArrayList<>();
        chemin.add(arrivee);
        while(predecesseurs.get(arrivee) != null){
            chemin.add(0, predecesseurs.get(arrivee));
            arrivee = predecesseurs.get(arrivee);
        }
        return chemin;
    }

    /* Fonction créée par Elliot qui prend en compte les sommets présents dans la liste en paramètre lors du parcours, pour ne pas se répéter
       lorsqu'on appelle plusieurs fois la fonction d'à filée
     */
    public List<Integer> parcoursSansRepetition(int depart, int arrivee, boolean pondere, List<Integer> dejaVusPlusTot) {

        if(depart == arrivee){
            List<Integer> liste = new ArrayList<>();
            liste.add(depart);
            return liste;
        }

        Map<Integer, Integer> distancesOrigine = new HashMap<>();
        Map<Integer, Integer> predecesseurs = new HashMap<>();
        List<Integer> aParcourir = new ArrayList<>();

        List<Integer> dejaVisites = new ArrayList<>();

        int indiceMin;

        for(Integer i : mapAretes.keySet()){
            distancesOrigine.put(i,Integer.MAX_VALUE);
            predecesseurs.put(i, null);
        }
        distancesOrigine.put(depart, 0);

        aParcourir.add(depart);

        while(!aParcourir.isEmpty()){

            indiceMin = 0;
            for(int i = 0; i < aParcourir.size(); i++){
                if(distancesOrigine.get(i) <= distancesOrigine.get(aParcourir.get(indiceMin))){
                    indiceMin = i;
                }
            }
            Integer sommetCourant = aParcourir.remove(indiceMin);
            HashSet<Arete> aretesIncidentes = mapAretes.get(sommetCourant);

            for(Arete arete : aretesIncidentes){
                Integer autreSommet = arete.getAutreSommet(sommetCourant);
                int tailleArete = 1;
                if(arete.route() != null) {
                    tailleArete = arete.route().getLongueur();
                }

                if(!dejaVisites.contains(autreSommet) && !dejaVusPlusTot.contains(autreSommet) && !aParcourir.contains(autreSommet)){
                    aParcourir.add(autreSommet);
                    if(!pondere){
                        tailleArete = 1;
                    }
                    int distanceTotale = distancesOrigine.get(sommetCourant) + tailleArete;
                    if(distanceTotale < distancesOrigine.get(autreSommet)) {
                        distancesOrigine.put(autreSommet, distanceTotale);
                    }
                    predecesseurs.put(autreSommet, sommetCourant);
                }
            }
            dejaVisites.add(sommetCourant);
        }

        if(predecesseurs.get(arrivee) == null){
            return new ArrayList<>();
        }
        List<Integer> chemin = new ArrayList<>();
        chemin.add(arrivee);
        while(predecesseurs.get(arrivee) != null){
            chemin.add(0, predecesseurs.get(arrivee));
            arrivee = predecesseurs.get(arrivee);
        }
        return chemin;
    }




    /**
     * Retourne un chemin entre 2 sommets sans répétition de sommets et sans dépasser
     * le nombre de bateaux et wagons disponibles. Cette fonction supposera que `this` est
     * bien un graphe issu du jeu avec des vraies routes (les objets routes ne sont pas null).
     * Dans cette fonction la couleur des routes n'est pas à prendre en compte.
     *
     * @param depart    le sommet de départ
     * @param arrivee   le sommet d'arrivée
     * @param nbBateaux le nombre de bateaux disponibles
     * @param nbWagons  le nombre de wagons disponibles
     * @return une liste d'entiers correspondant aux sommets du chemin, où l'élément en position 0 de la liste
     * et le sommet de départ, et l'élément à la dernière position de la liste (taille de la liste - 1) est le somme d'arrivée.
     * Si le chemin n'existe pas, retourne une liste vide (initialisée avec 0 éléments).
     * Pré-requis le graphe `this` est un graphe avec des routes (les objets routes ne sont pas null).
     */
    public List<Integer> parcoursSansRepetition(int depart, int arrivee, int nbWagons, int nbBateaux) {
        throw new RuntimeException("UwU baka");
    }

    /**
     * Retourne un chemin passant une et une seule fois par tous les sommets d'une liste donnée.
     * Les éléments de la liste en paramètres doivent apparaître dans le même ordre dans la liste de sortie.
     *
     * @param listeSommets la liste de sommets à visiter sans répétition ;
     *                     pré-requis : c'est une sous-liste de la liste retournée
     * @return une liste d'entiers correspondant aux sommets du chemin.
     * Si le chemin n'existe pas, retourne une liste vide.
     */
    public List<Integer> parcoursSansRepetition(List<Integer> listeSommets) {
        List<List<Integer>> listeChemins = new ArrayList<>();
        List<Integer> listePasPossibles = new ArrayList<>();

        for(Integer i : listeSommets){
            listePasPossibles.add(i);
        }
        // Garde en mémoire les chemins de chaque points vers le suivant en vérifiant qu'on ne passe jamais par un endroit ou on est déjà
        // passé dans un précédent appel de parcoursSansRepetition.
        for(int i = 0; i < listeSommets.size()-1; i++){
            Integer sommetCourant = listeSommets.get(i);
            Integer sommetSuivant = listeSommets.get(i+1);
            listePasPossibles.remove(sommetCourant); listePasPossibles.remove(sommetSuivant);
            listeChemins.add(parcoursSansRepetition(sommetCourant,sommetSuivant, false, listePasPossibles));
            if(listeChemins.get(i).size() == 0){
                return new ArrayList<>();
            }
            listePasPossibles.add(sommetCourant); listePasPossibles.add(sommetSuivant);

            listePasPossibles.addAll(listeChemins.get(i));
        }

        // Reconstruit le chemin total avec les chemins entre chaque points
        List<Integer> cheminFinal = new ArrayList<>();
        for(List<Integer> liste : listeChemins){
            for(int i = 0; i < liste.size()-1; i++){
                cheminFinal.add(liste.get(i));
            }
        }
        System.out.println(listeChemins);
        List<Integer> derniereListe = listeChemins.get(listeChemins.size()-1);
        cheminFinal.add(derniereListe.get(derniereListe.size()-1));
        return cheminFinal;
    }

    /**
     * Retourne un plus petit ensemble bloquant de routes entre deux villes. Cette fonction supposera que `this` est
     * bien un graphe issu du jeu avec des vraies routes (les objets routes ne sont pas null).
     * Dans cette fonction la couleur des routes n'est pas à prendre en compte.
     *
     * @return un ensemble de route.
     * Remarque : l'ensemble retourné doit être le plus petit en nombre de routes (et PAS en somme de leurs longueurs).
     * Remarque : il se peut qu'il y ait plusieurs ensemble de cardinalité minimum.
     * Un seul est à retourner (au choix).
     */
    public Set<Route> ensembleBloquant(int ville1, int ville2) {
        throw new RuntimeException("Méthode non implémentée");
    }

}