package org.insa.graphs.algorithm.shortestpath;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }

    @Override
    protected ShortestPathSolution doRun() {

        // retrieve data from the input problem (getInputData() is inherited from the
        // parent class ShortestPathAlgorithm)
        final ShortestPathData data = getInputData();

        // variable that will contain the solution of the shortest path problem
        ShortestPathSolution solution = null;

        // TODO: implement the Dijkstra algorithm
        /* initialisation : 
        *  - créer un tableau de n cases afin de stocket les labels sans modifier les classes des sommets
        *  - les sommets sont numérotés de 1 à n donc le label de la case 1 correspond au sommet 1 
        *  - parcourir les sommets et leur associer un label avec :
        *  --- marque <= faux
        *  --- cout_realise <= +inf
        *  --- pere <= 0 sommet inexistant*/ 

        // when the algorithm terminates, return the solution that has been found
        return solution;
    }

}
