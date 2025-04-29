package org.insa.graphs.model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * Class representing a path between nodes in a graph.
 * </p>
 * <p>
 * A path is represented as a list of {@link Arc} with an origin and not a list of
 * {@link Node} due to the multi-graph nature (multiple arcs between two nodes) of the
 * considered graphs.
 * </p>
 */
public class Path {

    /**
     * Create a new path that goes through the given list of nodes (in order), choosing
     * the fastest route if multiple are available.
     *
     * @param graph Graph containing the nodes in the list.
     * @param nodes List of nodes to build the path.
     * @return A path that goes through the given list of nodes.
     * @throws IllegalArgumentException If the list of nodes is not valid, i.e. two
     *         consecutive nodes in the list are not connected in the graph.
     */
    public static Path createFastestPathFromNodes(Graph graph, List<Node> nodes)
            throws IllegalArgumentException {
        List<Arc> arcs = new ArrayList<Arc>();
        // TODO:
        if (nodes.size() == 1) {
            return new Path(graph, nodes.get(0));
        }
        else {
            Node n_prec = new Node(0, null); // on va stocker le noeud précédent
            int i = 0;
            double time_min = 1000000000;
            Arc arc_selectionne = null;
            boolean found;
            for (Node n : nodes) {
                found = false;
                if (i == 0) {
                    i++;
                }
                else {
                    if (n_prec.hasSuccessors()) {
                        for (Arc a : n_prec.getSuccessors()) {
                            if (a.getDestination().equals(n)) { // on sélectionne les
                                                                // arcs
                                                                // qui ont la
                                                                // destination
                                                                // que l'on veut
                                if (a.getMinimumTravelTime() < time_min) { // on prend
                                                                           // celui
                                                                           // qui a la
                                                                           // durée
                                                                           // la plus
                                                                           // petite
                                    arc_selectionne = a;
                                    found = true;
                                    time_min = a.getMinimumTravelTime();
                                }
                            }
                        }

                    }
                    if (found == false) {
                        throw new IllegalArgumentException();
                    }
                    else {
                        arcs.add(arc_selectionne);
                        time_min = 1000000000;
                    }
                }
                n_prec = n;
            }
            Path path_final = new Path(graph, arcs);
            return path_final;
        }
    }

    /**
     * Create a new path that goes through the given list of nodes (in order), choosing
     * the shortest route if multiple are available.
     *
     * @param graph Graph containing the nodes in the list.
     * @param nodes List of nodes to build the path.
     * @return A path that goes through the given list of nodes.
     * @throws IllegalArgumentException If the list of nodes is not valid, i.e. two
     *         consecutive nodes in the list are not connected in the graph.
     */
    public static Path createShortestPathFromNodes(Graph graph, List<Node> nodes)
            throws IllegalArgumentException {
        List<Arc> arcs = new ArrayList<Arc>();
        // TODO:
        if (nodes.size() == 1) {
            return new Path(graph, nodes.get(0));
        }
        else {
            Node n_prec = null; // on va stocker le noeud précédent
            int i = 0;
            float lenght_min = 100000000; // on essaie de prendre une valeur
                                          // suffisamment
                                          // grande
            boolean found;
            for (Node n : nodes) { // on va avoir besoin du noeud actuel et de son
                                   // précédent
                                   // pour comparer les origines et destinations
                found = false;
                Arc arc_selectionne = null;
                if (i == 0) { // donc pour la première itération, on n'a pas de
                              // précédent
                    i++;
                }
                else {
                    if (n_prec.hasSuccessors()) { // on vérifie que notre noeud
                                                  // précédent a
                                                  // des successeurs
                        for (Arc a : n_prec.getSuccessors()) { // on parcourt les arcs
                                                               // qui
                                                               // ont pour origine notre
                                                               // noeud précédent
                            if (a.getDestination().equals(n)) { // on sélectionne les
                                                                // arcs
                                                                // qui ont la
                                                                // destination
                                                                // que l'on veut
                                if (a.getLength() <= lenght_min) { // on prend celui qui
                                                                   // a
                                                                   // la
                                                                   // plus petite
                                                                   // longueur
                                    arc_selectionne = a;
                                    found = true;
                                    lenght_min = arc_selectionne.getLength();
                                }
                            }
                        }
                    }
                    if (found == false) { // si on n'a trouvé aucun arc qui relie les
                                          // deux
                                          // noeuds, on lève l'exception
                        throw new IllegalArgumentException();
                    }
                    else {
                        arcs.add(arc_selectionne);
                        lenght_min = 100000000;
                    }
                }
                n_prec = n;
            }
            return new Path(graph, arcs);
        }
    }

    /**
     * Concatenate the given paths.
     *
     * @param paths Array of paths to concatenate.
     * @return Concatenated path.
     * @throws IllegalArgumentException if the paths cannot be concatenated (IDs of map
     *         do not match, or the end of a path is not the beginning of the next).
     */
    public static Path concatenate(Path... paths) throws IllegalArgumentException {
        if (paths.length == 0) {
            throw new IllegalArgumentException(
                    "Cannot concatenate an empty list of paths.");
        }
        final String mapId = paths[0].getGraph().getMapId();
        for (int i = 1; i < paths.length; ++i) {
            if (!paths[i].getGraph().getMapId().equals(mapId)) {
                throw new IllegalArgumentException(
                        "Cannot concatenate paths from different graphs.");
            }
        }
        ArrayList<Arc> arcs = new ArrayList<>();
        for (Path path : paths) {
            arcs.addAll(path.getArcs());
        }
        Path path = new Path(paths[0].getGraph(), arcs);
        if (!path.isValid()) {
            throw new IllegalArgumentException(
                    "Cannot concatenate paths that do not form a single path.");
        }
        return path;
    }

    // Graph containing this path.
    private final Graph graph;

    // Origin of the path
    private final Node origin;

    // List of arcs in this path.
    private final List<Arc> arcs;

    /**
     * Create an empty path corresponding to the given graph.
     *
     * @param graph Graph containing the path.
     */
    public Path(Graph graph) {
        this.graph = graph;
        this.origin = null;
        this.arcs = new ArrayList<>();
    }

    /**
     * Create a new path containing a single node.
     *
     * @param graph Graph containing the path.
     * @param node Single node of the path.
     */
    public Path(Graph graph, Node node) {
        this.graph = graph;
        this.origin = node;
        this.arcs = new ArrayList<>();
    }

    /**
     * Create a new path with the given list of arcs.
     *
     * @param graph Graph containing the path.
     * @param arcs Arcs to construct the path.
     */
    public Path(Graph graph, List<Arc> arcs) {
        this.graph = graph;
        this.arcs = arcs;
        this.origin = arcs.size() > 0 ? arcs.get(0).getOrigin() : null;
    }

    /**
     * @return Graph containing the path.
     */
    public Graph getGraph() {
        return graph;
    }

    /**
     * @return First node of the path.
     */
    public Node getOrigin() {
        return origin;
    }

    /**
     * @return Last node of the path.
     */
    public Node getDestination() {
        return arcs.get(arcs.size() - 1).getDestination();
    }

    /**
     * @return List of arcs in the path.
     */
    public List<Arc> getArcs() {
        return Collections.unmodifiableList(arcs);
    }

    /**
     * Check if this path is empty (it does not contain any node).
     *
     * @return true if this path is empty, false otherwise.
     */
    public boolean isEmpty() {
        return this.origin == null;
    }

    /**
     * Get the number of <b>nodes</b> in this path.
     *
     * @return Number of nodes in this path.
     */
    public int size() {
        return isEmpty() ? 0 : 1 + this.arcs.size();
    }

    /**
     * Check if this path is valid. A path is valid if any of the following is true:
     * <ul>
     * <li>it is empty;</li>
     * <li>it contains a single node (without arcs);</li>
     * <li>the first arc has for origin the origin of the path and, for two consecutive
     * arcs, the destination of the first one is the origin of the second one.</li>
     * </ul>
     *
     * @return true if the path is valid, false otherwise.
     */
    public boolean isValid() {
        Arc arc_courant = null;
        Arc arc_suivant = null;

        // tester première condition puis boucler sur la liste
        // récupérer l'arc courant et l'arc suivant
        // tester que arc_courant.destination = arc_suivant.origine

        // si le chemin est vide
        // si le chemin ne contient qu'un noeud, sans arcs
        if (this.isEmpty() || this.size() == 1) {
            return true;

            /*
             * si le premier arc a pour origine l'origine du chemin, et que pour deux
             * arcs consécutifs, la destination du premier est l'origine du second
             */
        }
        else {
            // si, pour deux itérations, la destination du premier arc correspond à
            // l'origine du second
            for (int i = 0; i < getArcs().size() - 1; i++) {
                arc_courant = getArcs().get(i);
                arc_suivant = getArcs().get(i + 1);
                if (arc_courant.getDestination() != arc_suivant.getOrigin()) {
                    return false;
                }

            }
            return true;
        }
    }

    /**
     * Compute the length of this path (in meters).
     *
     * @return Total length of the path (in meters).
     */
    public float getLength() {
        // parcourt le chemin et additionne les valeurs des arcs
        float longueur = 0;
        for (Arc a : arcs) {
            longueur += a.getLength();
        }
        return longueur;
    }

    /**
     * Compute the time required to travel this path if moving at the given speed.
     *
     * @param speed Speed to compute the travel time.
     * @return Time (in seconds) required to travel this path at the given speed (in
     *         kilometers-per-hour).
     */
    public double getTravelTime(double speed) {
        double vitesse = speed / 3.6; // conversion vitesse en m/sec
        double time;
        time = this.getLength() / vitesse;
        return time;
    }

    /**
     * Compute the time to travel this path if moving at the maximum allowed speed on
     * every arc.
     *
     * @return Minimum travel time to travel this path (in seconds).
     */
    public double getMinimumTravelTime() {
        double time = 0;
        for (Arc a : arcs) {
            time += a.getMinimumTravelTime();
        }
        return time;
    }

}
