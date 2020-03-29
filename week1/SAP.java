import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Queue;

public class SAP {
    private final Digraph g;

    public SAP(Digraph G) {
        // constructor takes a digraph (not necessarily a DAG)
        nullGuard(G);
        g = new Digraph(G);
    }

    public int length(int v, int w) {
        // length of shortest ancestral path between v and w; -1 if no such path
        List<Integer> vl = new ArrayList<>(List.of(v));
        List<Integer> wl = new ArrayList<>(List.of(w));
        CommonAncestor a = findCommondAncestor(vl, wl);
        if (a != null) {
            return a.length;
        }
        return -1;
    }

    public int ancestor(int v, int w) {
        // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
        List<Integer> vl = new ArrayList<>(List.of(v));
        List<Integer> wl = new ArrayList<>(List.of(w));
        CommonAncestor a = findCommondAncestor(vl, wl);
        if (a != null) {
            return a.node;
        }
        return -1;
    }

    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
        iterableNullGuard(v);
        iterableNullGuard(w);
        CommonAncestor a = findCommondAncestor(v, w);
        if (a != null) {
            return a.length;
        }
        return -1;
    }

    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        // a common ancestor that participates in shortest ancestral path; -1 if no such path
        iterableNullGuard(v);
        iterableNullGuard(w);
        CommonAncestor a = findCommondAncestor(v, w);
        if (a != null) {
            return a.node;
        }
        return -1;
    }

    private class CommonAncestor {
        int node;
        int length;

        CommonAncestor(int node, int length) {
            this.node = node;
            this.length = length;
        }
    }

    private CommonAncestor findCommondAncestor(Iterable<Integer> vv, Iterable<Integer> ww) {
        HashMap<Integer, Integer> hmv = new HashMap<>();
        Queue<Integer> q1 = new Queue<>();
        Queue<Integer> q2 = new Queue<>();
        Queue<Integer> tmp;

        for (Integer v : vv) {
            q1.enqueue(v);
            hmv.put(v, 0);
        }
        while (!q1.isEmpty()) {
            while (!q1.isEmpty()) {
                int node = q1.dequeue();
                int distance = hmv.get(node);
                for (Integer parent: g.adj(node)) {
                    if (!hmv.containsKey(parent)) {
                        hmv.put(parent, distance + 1);
                        q2.enqueue(parent);
                    }
                }
            }
            tmp = q1;
            q1 = q2;
            q2 = tmp;
        }

        HashMap<Integer, Integer> hmw = new HashMap<Integer, Integer>();
        q1 = new Queue<Integer>();
        q2 = new Queue<Integer>();

        for (Integer w : ww) {
            q1.enqueue(w);
            hmw.put(w, 0);
        }
        int bestDist = Integer.MAX_VALUE;
        int bestNode = -1;

        while (!q1.isEmpty()) {
            while (!q1.isEmpty()) {
                int node = q1.dequeue();
                int distance = hmw.get(node);
                for (Integer parent: g.adj(node)) {
                    if (!hmw.containsKey(parent)) {
                        hmw.put(parent, distance + 1);
                        q2.enqueue(parent);
                    }
                }
                if (hmv.containsKey(node)) {
                    int d = hmv.get(node) + hmw.get(node);
                    if (d < bestDist) {
                        bestDist = d;
                        bestNode = node;
                    }
                }
            }
            tmp = q1;
            q1 = q2;
            q2 = tmp;
        }
        if (bestNode != -1) {
            return new CommonAncestor(bestNode, bestDist);
        }
        return null;
    }


    private void nullGuard(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Argument cannot be null");
        }
    }

    private void iterableNullGuard(Iterable<Integer> objs) {
        nullGuard(objs);
        for (Object obj: objs) {
            nullGuard(obj);
        }
    }

    public static void main(String[] args) {
        // do unit testing of this class
    }
}
