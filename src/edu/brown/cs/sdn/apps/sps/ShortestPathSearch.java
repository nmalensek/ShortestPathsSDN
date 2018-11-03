package edu.brown.cs.sdn.apps.sps;

import edu.brown.cs.sdn.apps.util.Host;

import net.floodlightcontroller.routing.Link;
import net.floodlightcontroller.core.IOFSwitch;
import java.util.concurrent.ConcurrentHashMap;
import java.util.*;


/**
*Code heavily adapted from the following sources (retrieved on 2018-09-27):
*https://algs4.cs.princeton.edu/44sp/BellmanFordSP.java.html 
*https://algs4.cs.princeton.edu/44sp/EdgeWeightedDigraph.java.html
*/

class ShortestPathSearch {

    private ConcurrentHashMap<Long, Double> distanceMap = new ConcurrentHashMap<Long, Double>();
    private ConcurrentHashMap<Long, Link> edgeMap = new ConcurrentHashMap<Long, Link>();
    private LinkedList<Long> unvisitedNodes = new LinkedList<Long>();
    private ConcurrentHashMap<Long, LinkedList<Link>> linksPerNode = new ConcurrentHashMap<Long, LinkedList<Link>>();
    private static final double WEIGHT = 1.0;

    public synchronized void calculateShortestPaths(IOFSwitch sourceSwitch, Collection<Link> links, Map<Long, IOFSwitch> switches) {
        long sourceID = sourceSwitch.getId();

        setUpLinksPerNode(links);

        distanceMap.put(sourceID, 0.0); //set source's distance to 0
        unvisitedNodes.add(sourceID);

        //start out with unknown distances
        for (long switchID : switches.keySet()) {
            if (switchID == sourceID) { continue; }
            distanceMap.put(switchID, Double.POSITIVE_INFINITY);
        }

        while (!unvisitedNodes.isEmpty()) {
            long currentNode = unvisitedNodes.remove();

            for (Link link : linksPerNode.get(currentNode)) {
                if (link.getSrc() == currentNode) {
                    
		    if (distanceMap.get(link.getDst()) != null && distanceMap.get(link.getDst()) > distanceMap.get(currentNode) + WEIGHT) {
                        distanceMap.put(link.getDst(), distanceMap.get(currentNode) + WEIGHT);
                        edgeMap.put(link.getDst(), link);
                        //add neighbor to queue to visit
                        if (!unvisitedNodes.contains(link.getDst())) {
                            unvisitedNodes.add(link.getDst());
                        }
                    }
                }
            }
        }
    }

    public synchronized Iterable<Link> pathTo(long destination) {
        Stack<Link> path = new Stack<Link>();
        for (Link edge = edgeMap.get(destination); edge != null; edge = edgeMap.get(edge.getSrc())) {
            path.push(edge);
        }
        return path;
    }

    //log all nodes' links for more efficient retrieval during shortest path calculation.
    private void setUpLinksPerNode(Collection<Link> allLinks) {
        for (Link link : allLinks) {
            if (linksPerNode.get(link.getSrc()) == null) {
                linksPerNode.put(link.getSrc(), new LinkedList<Link>());
            }
                linksPerNode.get(link.getSrc()).add(link);
        }
    }
}
