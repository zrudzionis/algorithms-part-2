public class Outcast {
    private final WordNet net;

    public Outcast(WordNet wordnet) {
        // constructor takes a WordNet object
        nullGuard(wordnet);
        net = wordnet;
    }

    public String outcast(String[] nouns) {
        // given an array of WordNet nouns, return an outcast
        iterableNullGuard(nouns);
        int maxDist = 0;
        String outcastNoun = "";
        for (String noun: nouns) {
            int d = 0;
            for (int i = 0; i < nouns.length; i++) {
                d += net.distance(noun, nouns[i]);
            }
            if (d > maxDist) {
                maxDist = d;
                outcastNoun = noun;
            }
        }
        return outcastNoun;
    }

    private void nullGuard(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Argument cannot be null");
        }
    }

    private void iterableNullGuard(String[] objs) {
        nullGuard(objs);
        for (Object obj: objs) {
            nullGuard(obj);
        }
    }

    public static void main(String[] args) {
        // see test client below
    }
}