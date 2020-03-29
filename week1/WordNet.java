import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.StringJoiner;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Topological;

public class WordNet {
    private Digraph g;
    private final HashMap<String, Integer> wordToSynsetId = new HashMap<String, Integer>();
    private String[] synsetsById;

    public WordNet(String synsets, String hypernyms) {
        // constructor takes the name of the two input files
        pairNullGuard(synsets, hypernyms);
        readSynsetExpressions(synsets);
        readSynsetHypernims(hypernyms);
    }

    public Iterable<String> nouns() {
        // returns all WordNet nouns
        return wordToSynsetId.keySet();
    }

    public boolean isNoun(String word) {
        // is the word a WordNet noun?
        nullGuard(word);
        return wordToSynsetId.containsKey(word);
    }

    public int distance(String nounA, String nounB) {
        // distance between nounA and nounB
        pairNullGuard(nounA, nounB);
        pairNonWordNetNounGuard(nounA, nounB);
        int a = wordToSynsetId.get(nounA);
        int b = wordToSynsetId.get(nounB);
        SAP sap = new SAP(g);
        int dist = sap.length(a, b);

        return dist;
    }

    public String sap(String nounA, String nounB) {
        // shortest ancestral path
        pairNullGuard(nounA, nounB);
        pairNonWordNetNounGuard(nounA, nounB);

        Integer a = wordToSynsetId.get(nounA);
        Integer b = wordToSynsetId.get(nounB);
        SAP sap = new SAP(g);
        Integer ancestor = sap.ancestor(a, b);

        if (ancestor != -1) {
            List<Integer> aToAncestorPath = bfs(a, ancestor);
            List<Integer> bToAncestorPath = bfs(b, ancestor);
            Collections.reverse(bToAncestorPath);

            StringJoiner joiner = new StringJoiner("-");
            joiner.add(nounA);
            for (Integer node : aToAncestorPath) {
                joiner.add(synsetsById[node]);
            }
            joiner.add(synsetsById[ancestor]);
            for (Integer node : bToAncestorPath) {
                joiner.add(synsetsById[node]);
            }
            joiner.add(nounB);
            return joiner.toString();
        } else {
            return "";
        }
    }

    private List<Integer> bfs(Integer source, Integer target) {
        HashMap<Integer, Integer> cameFrom = new HashMap<>();
        Queue<Integer> q = new Queue<>();
        q.enqueue(source);
        while (!q.isEmpty()) {
            Integer node = q.dequeue();
            if (node == target) {
                break;
            }
            for (Integer parent : g.adj(node)) {
                if (!cameFrom.containsKey(parent)) {
                    cameFrom.put(parent, node);
                    q.enqueue(parent);
                }
            }
        }
        Integer node = cameFrom.get(target);
        ArrayList<Integer> path = new ArrayList<>();
        while (node != source) {
            path.add(node);
            node = cameFrom.get(node);
        }
        Collections.reverse(path);
        return path;
    }


    private void readSynsetExpressions(String synsetExpressionsFilePath) {
        In synsetsIn = new In(synsetExpressionsFilePath);
        String[] synsetLines = synsetsIn.readAllLines();
        synsetsIn.close();

        synsetsById = new String[synsetLines.length];
        for (int i = 0; i < synsetLines.length; i++) {
            String line = synsetLines[i];
            String[] parts = line.split(",");
            int synsetId = Integer.parseInt(parts[0]);
            String expressionsString = parts[1];
            synsetsById[i] = expressionsString;
            String[] expressions = expressionsString.split(" ");
            for (String expression : expressions) {
                wordToSynsetId.put(expression, synsetId);
            }
        }
    }

    private void readSynsetHypernims(String hypernimsFilePath) {
        In hyperIn = new In(hypernimsFilePath);
        String[] hypernimlines = hyperIn.readAllLines();
        hyperIn.close();

        g = new Digraph(hypernimlines.length);
        for (String line : hypernimlines)
        {
            String[] parts = line.split(",");
            int synsetId = Integer.parseInt(parts[0]);
            for (int i = 1; i < parts.length; i++) {
                int hypernymId = Integer.parseInt(parts[i]);
                g.addEdge(synsetId, hypernymId);
            }
        }
        rootedAcyclicGuard();
    }

    private void rootedAcyclicGuard() {
        Topological t = new Topological(g);
        if (!t.hasOrder()) {
            throw new IllegalArgumentException("Graph has no order");
        }
    }

    private void nonWordNetNounGuard(String noun) {
        if (!wordToSynsetId.containsKey(noun)) {
            throw new IllegalArgumentException("Noun is not a WordNet noun!");
        }
    }

    private void pairNonWordNetNounGuard(String nounA, String nounB) {
        nonWordNetNounGuard(nounA);
        nonWordNetNounGuard(nounB);
    }

    private void nullGuard(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Argument cannot be null");
        }
    }

    private void pairNullGuard(Object obj1, Object obj2) {
        nullGuard(obj1);
        nullGuard(obj2);
    }

    public static void main(String[] args) {
        // do unit testing of this class
        new WordNet("/home/zlv/Downloads/week1/synsets.txt", "/home/zlv/Downloads/week1/hypernyms.txt");
    }
}
