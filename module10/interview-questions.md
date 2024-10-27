# Question #1.
Challenging REs. Construct a regular expression for each of the following languages over the binary alphabet or prove that no such regular expression is possible:

1. All strings except 11 or 111.
2. Strings with 1 in every odd-number bit position.
3. Strings with an equal number of 0s and 1s.
4. Strings with at least two 0s and at most one 1.
5. Strings that when interpreted as a binary integer are a multiple of 3.
6. Strings with no two consecutive 1s.
7. Strings that are palindromes (same forwards and backwards).
8. Strings with an equal number of substrings of the form 01 and 10.

## Answer
1. `(0(0|1)*)|(1$)|(10(0|1)*)|(110(0|1)*)`. Here `$` is end of line symbol.
2. `1(01)*0?`. Here `?` is 0 or 1 symbols.
3. Not possible since it requires memory. We need to keep track of 0s and 1s count.
4. `(100+)|(00+1?0*)|(010+)`.
5. TODO
6. `(1$)|(0+10*)*|(10+)*`.
7. Not possible since it requires memory. We need to keep track of 0s and 1s count.
8. Not possible since it requires memory. We need to keep track of 01s and 10s count.

# Question #2.
Exponential-size DFA.
Design a regular expressions of length n such that any DFA that recognizes the same language has an exponential number of states.

## Answer
Regex: *(0|1){a}*

 * *n* - regex length
 * *a* - number
 * *b* - number of bits in *a*
 * D - number of states in DFA

For *a* we have D=2*(2^b) states.
For added bit to *a* we have D=2*(2^(b + 1)) states and *n* can grow only by one digit so the order of growth is exponential.


# Question #3.
Extensions to NFA. Add to [NFA.java](http://algs4.cs.princeton.edu/54regexp/NFA.java.html) the ability to handle multiway or, wildcard, and the + closure operator.

## Answer
```
/******************************************************************************
 *  Compilation:  javac NFA.java
 *  Execution:    java NFA regexp text
 *  Dependencies: Stack.java Bag.java Digraph.java DirectedDFS.java
 *
 *  % java NFA "(A*B|AC)D" AAAABD
 *  true
 *
 *  % java NFA "(A*B|AC)D" AAAAC
 *  false
 *
 *  % java NFA "(a|(bc)*d)*" abcbcd
 *  true
 *
 *  % java NFA "(a|(bc)*d)*" abcbcbcdaaaabcbcdaaaddd
 *  true
 *
 *  Remarks
 *  -----------
 *  The following features are not supported:
 *    - Metacharacters in the text
 *    - Character classes.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedDFS;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

/**
 *  The {@code NFA} class provides a data type for creating a
 *  <em>nondeterministic finite state automaton</em> (NFA) from a regular
 *  expression and testing whether a given string is matched by that regular
 *  expression.
 *  It supports the following operations: <em>concatenation</em>,
 *  <em>closure</em>, <em>binary or</em>, and <em>parentheses</em>.
 *  It does not support <em>mutiway or</em>, <em>character classes</em>,
 *  <em>metacharacters</em> (either in the text or pattern),
 *  <em>capturing capabilities</em>, <em>greedy</em> or <em>reluctant</em>
 *  modifiers, and other features in industrial-strength implementations
 *  such as {@link java.util.regex.Pattern} and {@link java.util.regex.Matcher}.
 *  <p>
 *  This implementation builds the NFA using a digraph and a stack
 *  and simulates the NFA using digraph search (see the textbook for details).
 *  The constructor takes time proportional to <em>m</em>, where <em>m</em>
 *  is the number of characters in the regular expression.
 *  The <em>recognizes</em> method takes time proportional to <em>m n</em>,
 *  where <em>n</em> is the number of characters in the text.
 *  <p>
 *  For additional documentation,
 *  see <a href="https://algs4.cs.princeton.edu/54regexp">Section 5.4</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class ExtendedNFA {

    private Digraph graph;     // digraph of epsilon transitions
    private String regexp;     // regular expression
    private final int m;       // number of characters in regular expression

    /**
     * Initializes the NFA from the specified regular expression.
     *
     * @param  regexp the regular expression
     */
    public ExtendedNFA(String regexp) {
        this.regexp = regexp;
        m = regexp.length();
        Stack<Integer> ops = new Stack<Integer>();
        Stack<Integer> ors = new Stack<Integer>();
        graph = new Digraph(m+1);
        for (int i = 0; i < m; i++) {
            int lp = i;
            if (regexp.charAt(i) == '(' || regexp.charAt(i) == '|')
                ops.push(i);
            else if (regexp.charAt(i) == ')') {
              while (ops.size() > 0 && regexp.charAt(ops.peek()) == '|') {
                    ors.push(ops.pop());
                }
                lp = ops.pop(); // opening parenthesis
                assert regexp.charAt(lp) == '(';
                while (ors.size() > 0) {
                    int or = ors.pop();
                    graph.addEdge(lp, or+1);
                    graph.addEdge(or, i);
                }
            }

            // Kleen star plus operator (uses 1-character lookahead)
            if (i < m-1 && regexp.charAt(i+1) == '+') {
              graph.addEdge(i+1, lp);
            }
            // Kleen star operator (uses 1-character lookahead)
            if (i < m-1 && regexp.charAt(i+1) == '*') {
                graph.addEdge(lp, i+1);
                graph.addEdge(i+1, lp);
            }
            if (regexp.charAt(i) == '(' || regexp.charAt(i) == '*' || regexp.charAt(i) == '+' || regexp.charAt(i) == '.' || regexp.charAt(i) == ')') {
                graph.addEdge(i, i+1);
            }
        }
        if (ops.size() != 0)
            throw new IllegalArgumentException("Invalid regular expression");
    }

    /**
     * Returns true if the text is matched by the regular expression.
     *
     * @param  txt the text
     * @return {@code true} if the text is matched by the regular expression,
     *         {@code false} otherwise
     */
    public boolean recognizes(String txt) {
        DirectedDFS dfs = new DirectedDFS(graph, 0);
        Bag<Integer> pc = new Bag<Integer>();
        for (int v = 0; v < graph.V(); v++)
            if (dfs.marked(v)) pc.add(v);

        // Compute possible NFA states for txt[i+1]
        for (int i = 0; i < txt.length(); i++) {
            if (txt.charAt(i) == '.' || txt.charAt(i) == '+' || txt.charAt(i) == '*' || txt.charAt(i) == '|' || txt.charAt(i) == '(' || txt.charAt(i) == ')')
                throw new IllegalArgumentException("text contains the metacharacter '" + txt.charAt(i) + "'");

            Bag<Integer> match = new Bag<Integer>();
            for (int v : pc) {
                if (v == m) continue;
                if ((regexp.charAt(v) == txt.charAt(i)) || regexp.charAt(v) == '.')
                    match.add(v+1);
            }
            if (match.isEmpty()) continue;

            dfs = new DirectedDFS(graph, match);
            pc = new Bag<Integer>();
            for (int v = 0; v < graph.V(); v++)
                if (dfs.marked(v)) pc.add(v);

            // optimization if no states reachable
            if (pc.size() == 0) return false;
        }

        // check for accept state
        for (int v : pc)
            if (v == m) return true;
        return false;
    }

    /**
     * Unit tests the {@code NFA} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        // String regexp = "(" + args[0] + ")";
        String regexp = "(z(a|b|c|d)*y)";
        // String regexp = "(za|by)";
        String txt = "zabcdbbddy";
        ExtendedNFA nfa = new ExtendedNFA(regexp);
        StdOut.println(nfa.recognizes(txt));
    }
}
```
