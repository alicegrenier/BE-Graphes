package org.insa.graphs.algorithm.shortestpath;

public class AStarAlgorithm extends DijkstraAlgorithm {

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
    }

    /* on remplit le tableau de labels avec des LabelStar */

    @Override // on écrase la fonction de la classe mère
    protected Label[] init_labels(){
        Label[] label_sommets= new LabelStar[nb_total_sommets]; 
        LabelStar label_courant;
        // initialisation du tableau des labels
        for (int i = 0; i < nb_total_sommets; i++) {
            label_courant = new LabelStar(graph.get(i), false, Double.POSITIVE_INFINITY, null,data) ;
            label_sommets[i]=label_courant ; // ajout du label complet du sommet au tableau des labels
        }
        // on met l'origine au bon coût 
        LabelStar origine = new LabelStar(data.getOrigin(), false, 0, null,data) ;
        label_sommets[id_origine]=origine ; // cout(s) = 0
        return label_sommets;
    }
}
