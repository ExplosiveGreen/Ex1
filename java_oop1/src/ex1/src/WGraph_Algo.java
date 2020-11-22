package ex1.src;

import java.io.*;
import java.util.*;

public class WGraph_Algo implements weighted_graph_algorithms , Serializable {

	private static final long serialVersionUID = 1L;
	private weighted_graph graph;

    @Override
    public void init(weighted_graph g) {
        this.graph = g;
    }

    @Override
    public weighted_graph getGraph() {
        return this.graph;
    }

    @Override
    public weighted_graph copy() {
        if (this.graph != null) {
            weighted_graph graph = new WGraph_DS();
            for (node_info node : this.graph.getV()) {
            	graph.addNode(node.getKey());
            	node_info node1 = graph.getNode(node.getKey());
            	node1.setInfo(node.getInfo());
            	node1.setTag(node.getTag());
            }
            for (node_info node : this.graph.getV()) {
                for (node_info n1 : this.graph.getV(node.getKey())) {
                	graph.connect(n1.getKey(), node.getKey(), this.graph.getEdge(n1.getKey(), node.getKey()));
                }
            }
            return graph;
        }
        return null;
    }

    @Override
    public boolean isConnected() {
    	for (node_info node : this.graph.getV())
        	node.setTag(Double.MAX_EXPONENT);
        
        if (graph.getV().size() == 1)
        	return true;
        if (graph.getV().size()<=0)
        	return true;
        node_info tempNode = (node_info)graph.getV().toArray()[0];
        Queue<node_info> list = new LinkedList<>();
        list.add(tempNode);
        tempNode.setTag(1);
        if (graph.getV(tempNode.getKey()).size() == 0)
        	return false;
        while (!list.isEmpty()) {
            node_info inf = list.poll();
            for (node_info n1 : graph.getV(inf.getKey())) {
                if (1 != n1.getTag()) {
                    n1.setTag(1);
                    list.add(n1);
                }
            }
        }
        for (node_info node : graph.getV()) {
            if (node.getTag() != 1)
            	return false;
        }
        return true;
    }

    @Override
    public double shortestPathDist(int st, int end) {
        if (graph.getNode(st) == null || graph.getNode(end) == null)
        	return -1;
        if (st == end) 
        	return 0;
        List<node_info> path = shortestPath(st, end);
        double ans = path.get(path.size() -1).getTag();
        return ans;
    }

    @Override
    public List<node_info> shortestPath(int st, int end) {
    	for (node_info node : this.graph.getV())
        	node.setTag(Double.MAX_EXPONENT);
        PriorityQueue<node_info> que = new PriorityQueue<>(new Comparator<node_info>() {
            @Override
            public int compare(node_info obj1, node_info obj2) {
                if(obj1.getTag() > obj2.getTag())
                    return (int) (obj1.getTag() - obj2.getTag());
                else
                    return (int) (obj2.getTag() - obj1.getTag());
            }
        });
        List<node_info> nodes = new ArrayList<>();
        HashMap<Integer, node_info> prev = new HashMap<>();
        if (st == end) 
        	return nodes;
        if (graph.getNode(st) == null || graph.getNode(end) == null)
        	return null;
        graph.getNode(st).setTag(0);
        que.add(graph.getNode(st));
        while (!que.isEmpty()) {
            node_info node = que.poll();
            for (node_info near : graph.getV(node.getKey())) {
                if (!nodes.contains(near)) {
                    double edge = graph.getEdge(node.getKey(), near.getKey());
                    double sum  = edge + node.getTag();
                    if (near.getTag() - sum > 0) {
                    	near.setTag(sum);
                        prev.put(near.getKey(), node);
                        que.add(near);
                    }
                }
            }
            nodes.add(node);
        }
        return path(prev, st, end);
    }

    @Override
    public boolean save(String f) {
        ObjectOutputStream oOutput;
        try {
            FileOutputStream fOutput = new FileOutputStream(f);
            if(fOutput == null)
            	return false;
            oOutput = new ObjectOutputStream(fOutput);
            if(oOutput == null)
            	return false;
            oOutput.writeObject(this.getGraph());
            return true;
        }
        catch (FileNotFoundException ex) {
            ex.printStackTrace(); 
        }
        catch (IOException e) {
        	e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean load(String f) {
        try {
            FileInputStream fInput = new FileInputStream(f);
            if(fInput == null)
            	return false;
            ObjectInputStream oInput = new ObjectInputStream(fInput);
            if(oInput == null)
            	return false;
            weighted_graph graph = (WGraph_DS) oInput.readObject();
            this.init(graph);
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private List<node_info> path(HashMap<Integer, node_info> prev, int st, int end) {
        List<node_info> path = new ArrayList<>();
        node_info node = graph.getNode(end);
        path.add(node);
        while (!path.contains(prev.get(st))) {
        	node = prev.get(node.getKey());
            path.add(node);
            if (node.getKey() == st) 
            	break;
        }
        Collections.reverse(path);
        return path;
    }
}