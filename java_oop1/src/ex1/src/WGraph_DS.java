package ex1.src;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

public class WGraph_DS implements weighted_graph , Serializable {

	private static final long serialVersionUID = 1L;
    private HashMap<Integer, node_info> graph;
    private HashMap<Integer, HashMap<node_info, Double>> EdgesList;
    private int  edge, mc;

    public WGraph_DS() {
    	this.graph = new HashMap<>();
    	this.EdgesList = new HashMap<>();
        this.edge = 0;
        this.mc = 0;
    }
    
    @Override
    public void addNode(int k) {
        if (!this.graph.containsKey(k)) {
            this.graph.put(k, new Node(k));
            this.EdgesList.put(k,new HashMap<>());
            mc++;
        }
    }

    @Override
    public node_info getNode(int k) {
        return this.graph.get(k);
    }

    @Override
    public boolean hasEdge(int n1, int n2) {
    	return EdgesList.get(n1).containsKey(getNode(n2));
    }

    @Override
    public double getEdge(int n1, int n2) {
        if (hasEdge(n1, n2) )
            return EdgesList.get(n1).get(getNode(n2));
        return -1;
    }

    @Override
    public void connect(int n1, int n2, double w) {
        if (this.graph.containsKey(n1)) {
            if (n1 != n2) {
                if (!hasEdge(n1, n2)) {
                    EdgesList.get(n1).put(getNode(n2), w);
                    EdgesList.get(n2).put(getNode(n1), w);
                    mc++;
                    edge++;
                }
                else
                {
                    EdgesList.get(n1).replace(getNode(n2), w);
                    EdgesList.get(n2).replace(getNode(n1), w);
                    mc++;
                }
            }
        }
    }

    @Override
    public Collection<node_info> getV() {
        return this.graph.values();
    }

    @Override
    public Collection<node_info> getV(int node) {
        return this.EdgesList.get(node).keySet();
    }

    @Override
    public node_info removeNode(int k) {
        if (this.graph.containsKey(k)) {
            for (node_info n : graph.values()) {
                    removeEdge(n.getKey(), k);
            }
            return graph.remove(k);
        }
        return null;
    }

    @Override
    public void removeEdge(int n1, int n2) {
        if (hasEdge(n1, n2)) {
        	EdgesList.get(n1).remove(getNode(n2));
        	EdgesList.get(n2).remove(getNode(n1));
            edge--;
            mc++;
        }
    }

    @Override
    public int nodeSize() {
        return graph.size();
    }

    @Override
    public int edgeSize() {
        return this.edge;
    }

    @Override
    public int getMC() {
        return this.mc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
        	return true;
        if (o == null)
        	return false;
        if(getClass() != o.getClass())
        	return false;

        WGraph_DS gDS = (WGraph_DS) o;
        if(graph.size() == gDS.graph.size() && edge == gDS.edge)
        	return true;
        else
        	return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(edge, mc, graph, EdgesList);
    }

    private class Node implements node_info, Serializable {

		private static final long serialVersionUID = 1L;
        private String nodeInfo;
        private int key; 
        private double tag;


        public Node(int k) {
        	this.nodeInfo = "";
            this.key = k;
            this.tag = 0;
        }


        @Override
        public int getKey() {
            return this.key;
        }

        @Override
        public String getInfo() {
            return this.nodeInfo;
        }

        @Override
        public void setInfo(String s) {
            this.nodeInfo = s;
        }

        @Override
        public double getTag() {
            return this.tag;
        }

        @Override
        public void setTag(double t) {
            this.tag = t;
        }
    }
}
