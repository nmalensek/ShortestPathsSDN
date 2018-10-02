package edu.brown.cs.sdn.apps.sps;


import net.floodlightcontroller.routing.Link;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

/**
 *  Original code retrieved from https://algs4.cs.princeton.edu/44sp/EdgeWeightedDigraph.java.html on 2018-09-27
 *  For additional documentation,
 *  see <a href="https://algs4.cs.princeton.edu/44sp">Section 4.4</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class EdgeWeightedDigraph {

    private HashMap<Long, LinkedList<Link>> pathMap = new HashMap<Long, LinkedList<Link>>();   //<destination, path>

    public EdgeWeightedDigraph(Collection<Link> links) {
        for (Link link : links) {
            addEdge(link);
        }
    }

    /**
     * Adds the directed edge {@code e} to this edge-weighted digraph.
     *
     * @param  e the edge
     * @throws IllegalArgumentException unless endpoints of edge are between {@code 0}
     *         and {@code V-1}
     */
    public void addEdge(Link e) {
        Long v = e.getSrc();
        if (pathMap.get(v) == null) {
            pathMap.put(v, new LinkedList<Link>());
        }        
//pathMap.computeIfAbsent(v, k -> new LinkedList<>());
        pathMap.get(v).add(e);
    }


    /**
     * Returns the directed edges incident from vertex {@code v}.
     *
     * @param  v the vertex
     * @return the directed edges incident from vertex {@code v} as an Iterable
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public Iterable<Link> adj(Long v) {
        return pathMap.get(v);
    }
}
