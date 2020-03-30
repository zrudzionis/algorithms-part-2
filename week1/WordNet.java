import java.util.ArrayList;
import java.util.HashMap;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Topological;

public class WordNet {
    private Digraph g;
    private final HashMap<String, ArrayList<Integer>> wordToSynsetIds = new HashMap<String, ArrayList<Integer>>();
    private final SAP sap;
    private String[] synsetsById;

    public WordNet(String synsets, String hypernyms) {
        // constructor takes the name of the two input files
        pairNullGuard(synsets, hypernyms);
        readSynsetExpressions(synsets);
        readSynsetHypernims(hypernyms);
        sap = new SAP(g);
    }

    public Iterable<String> nouns() {
        // returns all WordNet nouns
        return wordToSynsetIds.keySet();
    }

    public boolean isNoun(String word) {
        // is the word a WordNet noun?
        nullGuard(word);
        return wordToSynsetIds.containsKey(word);
    }

    public int distance(String nounA, String nounB) {
        // distance between nounA and nounB
        pairNullGuard(nounA, nounB);
        pairNonWordNetNounGuard(nounA, nounB);
        ArrayList<Integer> a = wordToSynsetIds.get(nounA);
        ArrayList<Integer> b = wordToSynsetIds.get(nounB);
        int dist = sap.length(a, b);
        return dist;
    }

    public String sap(String nounA, String nounB) {
        // shortest ancestral path
        pairNullGuard(nounA, nounB);
        pairNonWordNetNounGuard(nounA, nounB);
        ArrayList<Integer> a = wordToSynsetIds.get(nounA);
        ArrayList<Integer> b = wordToSynsetIds.get(nounB);
        Integer ancestor = sap.ancestor(a, b);
        return synsetsById[ancestor];
    }


    private void readSynsetExpressions(String synsetExpressionsFilePath) {
        In synsetsIn = new In(synsetExpressionsFilePath);
        String[] synsetLines = synsetsIn.readAllLines();
        synsetsIn.close();

        synsetsById = new String[synsetLines.length];
        g = new Digraph(synsetLines.length);
        for (int i = 0; i < synsetLines.length; i++) {
            String line = synsetLines[i];
            String[] parts = line.split(",");
            int synsetId = Integer.parseInt(parts[0]);
            String expressionsString = parts[1];
            synsetsById[i] = expressionsString;
            String[] expressions = expressionsString.split(" ");
            for (String expression : expressions) {
                if (!wordToSynsetIds.containsKey(expression)) {
                    wordToSynsetIds.put(expression, new ArrayList<Integer>());
                }
                wordToSynsetIds.get(expression).add(synsetId);
            }
        }
    }

    private void readSynsetHypernims(String hypernimsFilePath) {
        In hyperIn = new In(hypernimsFilePath);
        String[] hypernimlines = hyperIn.readAllLines();
        hyperIn.close();

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
        if (!wordToSynsetIds.containsKey(noun)) {
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
        System.out.println("Program Started");
        String basePath = "/home/zlv/Downloads/week1/";

        //        WordNet wn1 = new WordNet(basePath + "synsets.txt", basePath + "hypernyms.txt");
        //        System.out.println(wn1.sap("garbage_carter", "segno"));
        //        System.out.println(String.valueOf(wn1.distance("broody_hen", "partnership")));

        //            WordNet wn2 = new WordNet(basePath + "synsets15.txt", basePath + "hypernyms15Path.txt");
        //        System.out.println(wn2.sap("a", "a"));

        WordNet wn3 = new WordNet(basePath + "synsets3.txt", basePath + "hypernyms3InvalidCycle.txt");
        WordNet wn4 = new WordNet(basePath + "synsets3.txt", basePath + "hypernyms6InvalidTwoRoots.txt");

    }
}
