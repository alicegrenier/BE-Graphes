package org.insa.graphs.algorithm.shortestpath;

import java.util.ArrayList;

import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }

    @Override
    protected ShortestPathSolution doRun() {

        // retrieve data from the input problem (getInputData() is inherited from the
        // parent class ShortestPathAlgorithm)
        final ShortestPathData data = getInputData();
        /* structure avec : 
         * private Node origin
         * private Node destination
        */

        // variable that will contain the solution of the shortest path problem
        ShortestPathSolution solution = null;

        // TODO: implement the Dijkstra algorithm
        /* -----------------------------------------------------------initialisation--------------------------------------------------------------------------------
        * initialisation : la file de priorité ne contient que le sommet d'origine
        * - initialisation du tableau des labels
        * - initialisation du tas
        * - parcourir les sommets et leur associer un label avec :
        * ---- marque <= faux
        * ---- cout_realise <= +inf
        * ---- pere <= 0 sommet inexistant
        * - cout(origine) <= 0
        * - Insérer(s,tas)
        */ 
        
        // récupérer le graphe
        Graph graph = data.getGraph();
        int nb_total_sommets = graph.size() ;

        /* 
        * tableau de n cases permettant de stocker les labels sans modifier les classes des sommets 
        * les sommets sont numérotés de 1 à n donc le label de la case 1 correspond au sommet 1 
        */
        ArrayList<Label> label_sommets ;
        label_sommets = new ArrayList<Label> () ; // création du tableau des labels
        Label label_courant = new Label(null, false, 0, null) ;

        // initialisation du tableau des labels
        for (int i = 0; i < nb_total_sommets; i++) {
            
            label_courant.setCost(1000000000) ; // cout(i) = + inf
            label_courant.setMarque(false); // marque(i) = false
            label_courant.setPere(null); // pere(i) + inexistant

            label_sommets.add(i,label_courant) ; // ajout du label complet du sommet au tableau des labels
        }

        // initialisation du tas
        BinaryHeap<Label> tas = new BinaryHeap<Label>() ;

        // Label origine = new Label(data.getOrigin(), false, 0, null) ;
        label_sommets.set(0,label_sommets.get(0)) ;
        tas.arraySet(0, label_sommets.get(0)) ;

        // sommets marqués, compteur des sommets marqués à incrémenter à chaque fois qu'on marque un nouveau sommet
        int sommets_marqués = 0 ;

        /*------------------------------------------------------------itérations ----------------------------------------------------------------*/

        while(sommets_marqués <= nb_total_sommets) {

        }

        // when the algorithm terminates, return the solution that has been found
        return solution;
    }

}
