import java.util.Arrays;

import edu.princeton.cs.algs4.StdOut;

public class CircularSuffixArray {
  private String s;
  private Node[] nodes;
  private int n;

  private class Node implements Comparable<Node> {
    public int offset;

    public Node(int offset) {
      this.offset = offset;
    }

    public int compareTo(Node that) {
      int i = 0;
      while (i < s.length()) {
        char thisChar = s.charAt((this.offset + i) % n);
        char thatChar = s.charAt((that.offset + i) % n);
        if (thisChar != thatChar) {
          return thisChar - thatChar;
        }
        i++;
      }
      return 0;
    }
  }

  // circular suffix array of s
  public CircularSuffixArray(String s) {
    if (s == null) {
      throw new IllegalArgumentException();
    }
    this.s = s;
    this.n = s.length();
    this.nodes = new Node[n];
    for (int i = 0; i < n; i++) {
      nodes[i] = new Node(i);
    }
    Arrays.sort(nodes);
  }

  // length of s
  public int length() {
    return s.length();
  }

  // returns index of ith sorted suffix
  public int index(int i) {
    if (i < 0 || i >= s.length()) {
      throw new IllegalArgumentException();
    }
    return nodes[i].offset;
  }
  // unit testing (required)
  public static void main(String[] args) {
    String s = "ABRACADABRA!";
    CircularSuffixArray csa = new CircularSuffixArray(s);
    StdOut.println("Length: " + csa.length());
    for (int i = 0; i < csa.length(); i++) {
      StdOut.print(csa.index(i) + " ");
    }
    StdOut.println();
  }
}