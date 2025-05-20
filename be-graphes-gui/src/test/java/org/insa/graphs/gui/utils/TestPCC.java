package org.insa.graphs.gui.utils;

import org.insa.graphs.algorithm.ArcInspector;
import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.algorithm.AbstractSolution.Status ;
import org.insa.graphs.algorithm.shortestpath.AStarAlgorithm;
import org.insa.graphs.algorithm.shortestpath.BellmanFordAlgorithm;
import org.insa.graphs.algorithm.shortestpath.DijkstraAlgorithm;
import org.insa.graphs.algorithm.shortestpath.ShortestPathAlgorithm;
import org.insa.graphs.algorithm.shortestpath.ShortestPathData;
import org.insa.graphs.algorithm.shortestpath.ShortestPathSolution;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.algorithm.utils.ElementNotFoundException;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;


import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;

import org.insa.graphs.gui.drawing.Drawing;
import org.insa.graphs.gui.drawing.components.BasicDrawing;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.BinaryPathReader;
import org.insa.graphs.model.io.GraphReader;
import org.insa.graphs.model.io.PathReader;


public class TestPCC {

 /**
     * Create a new Drawing inside a JFrame an return it.
     *
     * @return The created drawing.
     * @throws Exception if something wrong happens when creating the graph.
     */
    public static Drawing createDrawing() throws Exception {
        BasicDrawing basicDrawing = new BasicDrawing();
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("BE Graphes - Test plus court chemin");
                frame.setLayout(new BorderLayout());
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
                frame.setSize(new Dimension(800, 600));
                frame.setContentPane(basicDrawing);
                frame.validate();
            }
        });
        return basicDrawing;
    }

    // définition de plusieurs scénarios
    /* chaque scénario est défini par
     * - une carte (tester des cartes routières et non routières)
     * - la nature du coût (tester en distance et en temps)
     * - une origine et une destination (tester plusieurs valeurs)
     * ==> sur chaque scénario, vérifier que le chemin construit par l'algo est valide
     * ==> vérifier que le coût du chemin calculé par Dijkstra est le même que celui calculé par la classe path
     * ==> vérifier qu'on obtient les mêmes résultats qu'avec Bellman-Ford
     * ==> sur les chemins plus grands où Bellman-Ford ne marche pas, vérifier avec des test automatiques que le chemin trouvé est le bon
     */

     // calcul de chemin/coût en fonction du scénario

     public double test_scenario(String map, /*String chemin,*/ int nature_cout, int v_ou_p, int id_origine, int id_destination, char algo) {
        /* nature_cout : 
        - 1 = distance 
        - 2 = temps 
        
        v_ou_p :
        - 1 : voiture
        - 2 : piéton */ 

        /* algo :
         * - a : AStar
         * - b : Bellman-Ford
         * - d : Dijkstra
         */

        // visit these directory to see the list of available files on commetud.
        final String mapName =
                "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/"+map;
        /*final String pathName =
                "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Paths/"+chemin;*/

        ShortestPathSolution solution=null;
        final Graph graph;
        double return_value =-1; 
        //final Path path;

        // create a graph reader
        try (final GraphReader reader = new BinaryGraphReader(new DataInputStream(
                new BufferedInputStream(new FileInputStream(mapName))))) {

            // read the graph
            graph = reader.read() ;
        

            // on teste que l'id des nodes origine et destination sont bien dans la graphe
            if (id_origine < 0 || id_destination < 0) {
                System.out.println("l'origine et/ou la destination sort du graphe") ;

            // on teste qu'on a bien une origine et une destination différente 
            } else if (id_origine == id_destination) {
                System.out.println("l'origine et la destination sont la même") ;

            } else {
                // create the drawing
                final Drawing drawing = createDrawing();

                // draw the graph on the drawing

                drawing.drawGraph(graph) ;

                // en fonction de la valeur de nature_cout, choisir FastestPath ou ShortestPath
                // lui donner l'origine et la destination d'où on part
                // lancer l'algo pour obtenir le chemin
                // on récupère le résultat

                ArcInspector type_eval ;

                if (nature_cout == 1 && v_ou_p == 1) { // voiture en distance
                    type_eval = ArcInspectorFactory.getAllFilters().get(1) ;
                } else if (nature_cout == 2 && v_ou_p ==1) { // voiture en temps
                    type_eval = ArcInspectorFactory.getAllFilters().get(2) ;
                } else if (nature_cout == 1 && v_ou_p == 2) { // piéton en distance
                    System.out.println("il n'existe pas de filtre piéton en distance, par défaut on utilise donc le filtre 'pas de filtre en temps'") ;
                    type_eval = ArcInspectorFactory.getAllFilters().get(0) ;
                } else if (nature_cout == 2 && v_ou_p ==2) { // piéton en temps
                    type_eval = ArcInspectorFactory.getAllFilters().get(3) ;
                } else {
                    System.out.println("mauvais argument pour la nature du coût évalué et/ou voiture ou piéton, on met le filtre par défaut") ;
                    type_eval = ArcInspectorFactory.getAllFilters().get(0) ;
                }

                // permet de choisir automatiquement sur quel graphe on fait le calcul, d'où on part et où on va
                ShortestPathData data = new ShortestPathData(graph, graph.get(id_origine), graph.get(id_destination), type_eval) ;

                //choisir le bon algo

                ShortestPathAlgorithm algo_a_utiliser ;

                if (algo != 'a' && algo != 'b' && algo != 'd') {
                    System.out.println("Aucun algo ne correspond à l'argument donné. Rappel : a = AStar, b = Bellman-Ford, d = Dijkstra") ;
                    return 0 ;
                } else {

                    if (algo == 'a') { // AStar
                        algo_a_utiliser = new AStarAlgorithm(data) ;
                    } else if (algo == 'b') { //Bellman-Ford
                        algo_a_utiliser = new BellmanFordAlgorithm(data) ;
                    } else { // Dijkstra
                        algo_a_utiliser = new DijkstraAlgorithm(data) ;
                    }

                    solution = algo_a_utiliser.run() ;

                    if (solution.getStatus()!=Status.INFEASIBLE){ //si les algorithmes ont correctment tourné, ils renvoient leur coût
                        if (nature_cout==1)//distance 
                        {
                            return_value= solution.getPath().getLength();
                        }else if (nature_cout==2)//temps 
                        {
                            return_value= solution.getPath().getMinimumTravelTime();
                        }
                    }
                }

                /*final String pathName =
                    "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Paths/"+chemin;*/

                final Path path;

                // create a path reader
                /*try (final PathReader pathReader = new BinaryPathReader(
                    new DataInputStream(new BufferedInputStream(new FileInputStream(pathName))))) {*/

                // read the path 
                path = solution.getPath();//pathReader.readPath(graph);
                // draw the path on the drawing
                drawing.drawPath(path) ;

                //pathReader.close() ;
                reader.close() ;
            }

        } catch(Exception e){
            System.out.println("Exception levée pour reader");
        }   

        return return_value; 
     }

     // test de différents scénarios avec le résultat attendu (utiliser des assert pour pallier le fait qu'on ne peut pas utiliser Bellman-Ford

     /*-------------------------------------------------------------DIJKSTRA------------------------------------------------------------------------------- */

     /* nature_cout : 
        - 1 = distance 
        - 2 = temps 
        
        v_ou_p :
        - 1 : voiture
        - 2 : piéton */ 

    @Test
     public void test_chemin_inexistant() {
        // l'origine et/ou la destination n'est pas dans le graphe
        String map_choisie = "carre.mapgr" ;
        assertEquals(test_scenario(map_choisie, 1, 1, -1, 0, 'd'), -1, 0);
     }

     @Test
     public void test_chemin_longueur_nulle() {
        // l'origine et la destination sont les mêmes
        String map_choisie = "carre.mapgr" ;
        assertEquals(test_scenario(map_choisie, 1, 1, 1, 1, 'd'), -1, 0);
     }

     @Test
     public void test_trajet_court_voiture_distance() {
        // on va en voiture, sur une petite distance
        // comme c'est une petite distance, on compare les résultats de Dijsktra et Bellman-Ford
        String map_choisie = "carre.mapgr" ;
        assertEquals(test_scenario(map_choisie, 1, 1, 1, 4, 'd'), test_scenario(map_choisie, 1, 1, 1, 4, 'b'), 0);
     }

     @Test
     public void test_trajet_court_voiture_temps() {
        // on va en voiture, sur un petit temps
        // comme c'est un petit temps, on compare les résultats de Dijsktra et Bellman-Ford
        String map_choisie = "carre.mapgr" ;
        assertEquals(test_scenario(map_choisie, 2, 1, 1, 4, 'd'), test_scenario(map_choisie, 2, 1, 1, 4, 'b'), 0);
     }

     @Test
     public void test_trajet_court_pieton() {
        // on va à pied, sur une petite distance
        // comme c'est une petite distance, on compare les résultats de Dijsktra et Bellman-Ford
        String map_choisie = "carre.mapgr" ;
        assertEquals(test_scenario(map_choisie, 1, 2, 1, 3, 'd'), test_scenario(map_choisie, 1, 2, 1, 3, 'b'), 0);
     }

     // tester sur un chemin pas autorisé aux voitures

     /*public void test_trajet_long() {
        // on va à pied sur une grande distance
        String map_choisie = "carre.dense" ;
        assertEquals(test_scenario(map_choisie, 1, 1, 1, 50, 'd'), ??, 0);
     }*/


}