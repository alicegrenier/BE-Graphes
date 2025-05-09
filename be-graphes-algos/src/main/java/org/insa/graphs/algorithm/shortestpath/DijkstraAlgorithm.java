package org.insa.graphs.algorithm.shortestpath;

import java.util.ArrayList;

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
        /* 
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
        /* tableau de n cases permettant de stocker les labels sans modifier les classes des sommets 
     * les sommets sont numérotés de 1 à n donc le label de la case 1 correspond au sommet 1 
    */

        ArrayList<Label> label_sommets ;
        label_sommets = new ArrayList<Label> () ; // initialisation du tableau des labels
        BinaryHeap tas = new BinaryHeap() ;

        Label origine = new Label(data.getOrigin(), false, 0, null) ;
        label_sommets.set(0,origine) ;
        tas(0, data.getOrigin()) ;

        /* itérations */

        while() {
            
        }

        // when the algorithm terminates, return the solution that has been found
        return solution;
    }

}
