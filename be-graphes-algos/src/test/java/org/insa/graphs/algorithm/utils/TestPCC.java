package org.insa.graphs.algorithm.utils;

import org.insa.graphs.algorithm.AbstractSolution.Status ;
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

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.insa.graphs.gui.drawing.Drawing;
import org.insa.graphs.gui.drawing.components.BasicDrawing;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Path;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.BinaryPathReader;
import org.insa.graphs.model.io.GraphReader;
import org.insa.graphs.model.io.PathReader;


@RunWith(Parameterized.class)
public abstract class TestPCC {

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

     public int testScenario(String map, String chemin, int nature_cout, int origine, int destination, char algo) {

        // visit these directory to see the list of available files on commetud.
        final String mapName =
                "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/"+map;
        final String pathName =
                "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Paths/"+chemin;

        final Graph graph;
        final Path path;

        // create a graph reader
        try (final GraphReader reader = new BinaryGraphReader(new DataInputStream(
                new BufferedInputStream(new FileInputStream(mapName))))) {

            // read the graph
            graph = reader.read() ;
        }

        // on teste que l'id des nodes origine et destination sont bien dans la graphe
        if (origine < 0 || destination < 0) {
            System.out.println("l'origine et/ou la destination sort du graphe") ;
        }

        // en fonction de la valeur de nature_cout

        // create the drawing
        final Drawing drawing = createDrawing();

        // draw the graph on the drawing

        drawing.drawGraph(graph) ;

        // create a path reader
        try (final PathReader pathReader = new BinaryPathReader(
            new DataInputStream(new BufferedInputStream(new FileInputStream(pathName))))) {

            // read the path 
            path = pathReader.readPath(graph);
        }

        // draw the path on the drawing
        drawing.drawPath(path) ;

     }

     // test de différents scénarios avec le résultat attendu (utiliser des assert pour pallier le fait qu'on ne peut pas utiliser Bellman-Ford


}