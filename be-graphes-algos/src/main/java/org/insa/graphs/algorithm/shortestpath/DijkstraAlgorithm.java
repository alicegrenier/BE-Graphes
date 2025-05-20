package org.insa.graphs.algorithm.shortestpath;

import java.lang.*;
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
        // Label label_courant = new Label(null, false, 0, null) ;
        Label label_courant = null ;

        // initialisation du tableau des labels
        for (int i = 0; i < nb_total_sommets; i++) {
            label_courant = new Label(graph.get(i), false, Double.POSITIVE_INFINITY, null) ;
            /*label_courant.setSommetCourant(graph.get(i));
            label_courant.setCost(Double.POSITIVE_INFINITY) ; // cout(i) = + inf
            label_courant.setMarque(false); // marque(i) = false
            label_courant.setPere(null); // pere(i) = inexistant*/

            label_sommets.add(i,label_courant) ; // ajout du label complet du sommet au tableau des labels
        }

        // initialisation du tas
        BinaryHeap<Label> tas = new BinaryHeap<>() ;

        // création d'un label avec un cout à zéro pour l'origine (on écrase la valeur intialisée dnas la boucle for, qui était + inf)
        Label origine = new Label(data.getOrigin(), false, 0, null) ;
        label_sommets.set(origine.getSommetCourant().getId(), origine) ; // cout(s) = 0
        tas.insert(label_sommets.get(origine.getSommetCourant().getId())) ;
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

        Label min_tas = new Label(null, false, Double.POSITIVE_INFINITY, null) ;
        while(sommets_marques <= nb_total_sommets && !tas.isEmpty() && !destination_trouvee) {
            min_tas = tas.deleteMin() ;
            ordre_marquage.add(min_tas.getSommetCourant()) ;
            min_tas.setMarque(true);
            label_sommets.set(min_tas.getSommetCourant().getId(),min_tas) ;
            sommets_marques++ ;

            if(min_tas.getSommetCourant() == data.getDestination()) {
                destination_trouvee = true ;
            } else {

                for (Arc arc : min_tas.getSommetCourant().getSuccessors()) { // boucle sur tous les successeurs de min_tas

                    Node destinataire = arc.getDestination() ; // sommet étudié
                    Label label_destinataire = label_sommets.get(destinataire.getId()) ; // label du sommet étudié

                    if (label_destinataire.getMarque() == false) { // si le sommet n'est pas marqué
                    // affecter comme coût à ce sommet le minimum entre son coût et le poids de l'arc

                        // Small test to check allowed roads...
                        if (!data.isAllowed(arc)) {
                         continue;
                        }
                        
                        old_cost = label_destinataire.getCost() ;
                        new_cost = min_tas.getCost() + data.getCost(arc) ;

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
                            label_sommets.set(label_destinataire.getSommetCourant().getId(),label_destinataire) ;
                            arcs_precedents[arc.getDestination().getId()]=arc; // on stocke les arcs "optimums" suivant l'id du noeud de destination
                        }
                    }
                }
            }
        }

        /*ArrayList<Node> ordre_final=new ArrayList<Node>();
        if(destination_trouvee){
            ordre_final.add(data.getDestination());
            Node pere =new Node(0,null);
            Node courant=data.getDestination();
            while (courant !=data.getOrigin()){
                pere=label_sommets.get(courant.getId()).getPere() ;
                ordre_final.add(pere);
                courant=pere;
            }
            Collections.reverse(ordre_final);

        }
        
        try {

            Path solution_chemin = new Path(graph) ;
            solution_chemin = Path.createShortestPathFromNodes(graph, ordre_final) ;
            
            solution = new ShortestPathSolution(data, Status.OPTIMAL, solution_chemin) ;

            notifyDestinationReached(data.getDestination());

            
        } catch (IllegalArgumentException e) {
            solution = new ShortestPathSolution(data, Status.INFEASIBLE);
        }*/

        //si la case du noeud de destination est vide, c'est qu'il n'a pas été atteint par l'algo
        if (arcs_precedents[data.getDestination().getId()] == null) {
            solution = new ShortestPathSolution(data, Status.INFEASIBLE);
        }
        else {
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
