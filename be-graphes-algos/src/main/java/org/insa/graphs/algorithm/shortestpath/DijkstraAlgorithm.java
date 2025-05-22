package org.insa.graphs.algorithm.shortestpath;

import java.util.ArrayList;
import java.util.Collections;

import org.insa.graphs.algorithm.AbstractSolution.Status ;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.algorithm.utils.ElementNotFoundException;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    //le but est de rendre les variables utilisées dans l'algorithme de Dijkstra visibles dans toute la classe. 
    // Ensuite, une méthode les variables Label et il sera possible de l'écraser pour utiliser des LabelStar dans A*

    //Attributs 
    final ShortestPathData data; 
    Graph graph;
    final int nb_total_sommets;
    int id_origine;

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
        this.data = getInputData();
        this.graph=data.getGraph();
        this.nb_total_sommets=graph.size();
        this.id_origine=data.getOrigin().getId();
    }

    protected Label[] init_labels(){
        Label[] label_sommets= new Label[nb_total_sommets]; 
        Label label_courant;
        // initialisation du tableau des labels
        for (int i = 0; i < nb_total_sommets; i++) {
            label_courant = new Label(graph.get(i), false, Double.POSITIVE_INFINITY, null) ;
            label_sommets[i]=label_courant ; // ajout du label complet du sommet au tableau des labels
        }
        // on met l'origine au bon coût 
        Label origine = new Label(data.getOrigin(), false, 0, null) ;
        label_sommets[id_origine]=origine ; // cout(s) = 0
        return label_sommets;
    }

    @Override
    protected ShortestPathSolution doRun() {

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

        /* 
        * tableau de n cases permettant de stocker les labels sans modifier les classes des sommets 
        * les sommets sont numérotés de 1 à n donc le label de la case 1 correspond au sommet 1 
        */
       Label[] labels_sommets=init_labels();

        // initialisation du tas
        BinaryHeap<Label> tas = new BinaryHeap<>() ;

        // création d'un label avec un cout à zéro pour l'origine (on écrase la valeur intialisée dnas la boucle for, qui était + inf)
        tas.insert(labels_sommets[id_origine]);
        notifyOriginProcessed(data.getOrigin());

        // sommets marqués, compteur des sommets marqués à incrémenter à chaque fois qu'on marque un nouveau sommet
        int sommets_marques = 0 ;
        double new_cost ;
        double old_cost ;
        // sommet destination trouvé ?
        boolean destination_trouvee = false ;

        // création d'un tableau contenant les sommets dans l'ordre de marquage
        Arc[] arcs_precedents = new Arc[nb_total_sommets] ;
        ArrayList<Node> ordre_marquage = new ArrayList<Node> () ;

        /*------------------------------------------------------------itérations ----------------------------------------------------------------*/

        Label min_tas;
        while(sommets_marques <= nb_total_sommets && !tas.isEmpty() && !destination_trouvee) {
            min_tas = tas.deleteMin() ;
            ordre_marquage.add(min_tas.getSommetCourant()) ;
            min_tas.setMarque(true);
            labels_sommets[min_tas.getSommetCourant().getId()]=min_tas ;
            sommets_marques++ ;

            if(min_tas.getSommetCourant() == data.getDestination()) {
                destination_trouvee = true ;
            } else {

                for (Arc arc : min_tas.getSommetCourant().getSuccessors()) { // boucle sur tous les successeurs de min_tas

                    Node destinataire = arc.getDestination() ; // sommet étudié
                    Label label_destinataire = labels_sommets[destinataire.getId()]; // label du sommet étudié

                    if (label_destinataire.getMarque() == false) { // si le sommet n'est pas marqué
                    // affecter comme coût à ce sommet le minimum entre son coût et le poids de l'arc

                        // Small test to check allowed roads...
                        if (!data.isAllowed(arc)) {
                         continue;
                        }
                        
                        old_cost = label_destinataire.getTotalCost() ;
                        new_cost = min_tas.getTotalCost() + data.getCost(arc) ;

                        if (Double.isInfinite(old_cost) && Double.isFinite(new_cost)) {
                            notifyNodeReached(arc.getDestination());
                            tas.insert(label_destinataire) ;
                        }

                        if (new_cost < old_cost) { // on regarde quel coût est minimal
                            // si c'est le nouveau coût, on met à jour la valeur du coût du sommet étudié
                            try {
                                tas.remove(label_destinataire) ;
                            } catch (ElementNotFoundException e) {
                                System.out.println("ce noeud n'est pas dans le tas") ;
                            }
                            
                            label_destinataire.setCost(new_cost);
                            tas.insert(label_destinataire) ;
                            label_destinataire.setPere(min_tas.getSommetCourant());
                            labels_sommets[label_destinataire.getSommetCourant().getId()]=label_destinataire;
                            arcs_precedents[arc.getDestination().getId()]=arc; // on stocke les arcs "optimums" suivant l'id du noeud de destination
                        }
                    }
                }
            }
        }

        //si la case du noeud de destination est vide, c'est qu'il n'a pas été atteint par l'algo
        if (arcs_precedents[data.getDestination().getId()] == null) {
            solution = new ShortestPathSolution(data, Status.INFEASIBLE);
        } else {
            // The destination has been found, notify the observers.
            notifyDestinationReached(data.getDestination());
            // Create the path from the array of predecessors...
            ArrayList<Arc> arcs = new ArrayList<>();
            Arc arc = arcs_precedents[data.getDestination().getId()]; // on part du noeud destination
            while (arc != null) { // si l'arc n'existe pas, c'est qu'on a fini de remonter le tableau
                arcs.add(arc);
                arc = arcs_precedents[arc.getOrigin().getId()];// on va au noeud qui commence l'arc puisqu'on a identifié les arcs par leur destination
            }
            // Reverse the path...
            Collections.reverse(arcs);
            // Create the final solution.
            solution = new ShortestPathSolution(data, Status.OPTIMAL,
                    new Path(graph, arcs));
        }

        // when the algorithm terminates, return the solution that has been found
        return solution;
    }

}
