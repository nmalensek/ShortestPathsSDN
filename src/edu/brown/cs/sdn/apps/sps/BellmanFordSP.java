package edu.brown.cs.sdn.apps.sps;

import edu.brown.cs.sdn.apps.util.Host;

import net.floodlightcontroller.routing.Link;
import net.floodlightcontroller.core.IOFSwitch;
import java.util.concurrent.ConcurrentHashMap;
import java.util.*;

/**
 * Original code retrieved from: https://algs4.cs.princeton.edu/44sp/BellmanFordSP.java.html on 2018-09-27
 *
 *  For additional documentation,
 *  see <a href="https://algs4.cs.princeton.edu/44sp">Section 4.4</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class BellmanFordSP {
    private ConcurrentHashMap<Long, Double> distanceMap = new ConcurrentHashMap<Long, Double>();
    private ConcurrentHashMap<Long, Link> edgeTo = new ConcurrentHashMap<Long, Link>();
    private Queue<Long> queue;          // queue of vertices to relax
    private static final int WEIGHT = 1;

    /**
     * Computes a shortest paths tree from {@code s} to every other vertex in
     * the edge-weighted digraph {@code G}.
     * @param G the acyclic digraph
     * @param source the source vertex
     * @throws IllegalArgumentException unless {@code 0 <= s < V}
     */
    public BellmanFordSP(EdgeWeightedDigraph G, long source, Map<Long, IOFSwitch> switches) {
        for (IOFSwitch s : switches.values()) {
		if (s.getId() == source) {
			distanceMap.put(s.getId(), 0.0);
		} else {
			distanceMap.put(s.getId(), Double.POSITIVE_INFINITY);
		}
        }

        // Bellman-Ford algorithm
        queue = new LinkedList<Long>();
        queue.add(source);
        while (!queue.isEmpty()) {
            long v = queue.remove();
            relax(G, v);
        }
    }

    // relax vertex v and put other endpoints on queue if changed
    private void relax(EdgeWeightedDigraph G, long v) {
	System.out.println("------------------------------------- GETTING V: " + v + " -----------------------------------");
        for (Link e : G.adj(v)) {
            long w = e.getDst();
            if (distanceMap.get(w) > distanceMap.get(v) + WEIGHT) {
                distanceMap.put(w, distanceMap.get(v) + WEIGHT);
                edgeTo.put(w, e);
                if (!queue.contains(w)) {
                    queue.add(w);
                }
            }
        }
    }

    /**
     * Returns the length of a shortest path from the source vertex {@code s} to vertex {@code v}.
     * @param  v the destination vertex
     * @return the length of a shortest path from the source vertex {@code s} to vertex {@code v};
     *         {@code Double.POSITIVE_INFINITY} if no such path
     * @throws UnsupportedOperationException if there is a negative cost cycle reachable
     *         from the source vertex {@code s}
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public double distTo(long v) {
//        validateVertex(v);
        return distanceMap.get(v);
    }

    /**
     * Is there a path from the source {@code s} to vertex {@code v}?
     * @param  v the destination vertex
     * @return {@code true} if there is a path from the source vertex
     *         {@code s} to vertex {@code v}, and {@code false} otherwise
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public boolean hasPathTo(long v) {
        return distanceMap.get(v) < Double.POSITIVE_INFINITY;
    }

    /**
     * Returns a shortest path from the source {@code s} to vertex {@code v}.
     * @param  v the destination vertex
     * @return a shortest path from the source {@code s} to vertex {@code v}
     *         as an iterable of edges, and {@code null} if no such path
     * @throws UnsupportedOperationException if there is a negative cost cycle reachable
     *         from the source vertex {@code s}
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public Iterable<Link> pathTo(long v) {
        if (!hasPathTo(v)) return null;
        Stack<Link> path = new Stack<Link>();
        for (Link e = edgeTo.get(v); e != null; e = edgeTo.get(e.getSrc())) {
            path.push(e);
        }
        return path;
    }
}
