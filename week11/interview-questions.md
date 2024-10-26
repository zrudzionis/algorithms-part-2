# Question #1.
Ternary Huffman codes. Generalize the Huffman algorithm to codewords over the ternary alphabet (0, 1, and 2) instead of the binary alphabet. That is, given a bytestream, find a prefix-free ternary code that uses as few trits (0s, 1s, and 2s) as possible. Prove that it yields optimal prefix-free ternary code.

## Answer
We have ternary trie to store nodes.
First two nodes store alphabet characters and third node store branch to a subtree.
When writing this trie we use 0 for left node, 1 for mid node and 2 for right node.
Values 0, 1 represent leaf nodes.

Optimal tree is formed in a similar fashion where 3 lowest frequency nodes are combined
into one and later treated as a single node to build upper levels.

Optimality proof will be similar to the one in the book.

```
private static class Node implements Comparable<Node> {
  // Huffman trie node
    private char ch;   // unused for internal nodes
    private int freq;  // unused for expand
    private int order; // identifies whether it is left or mid or right node
    private final Node left, mid, right;

    Node(char ch, int freq, int order, Node left, Node mid, Node right) {
      this.ch    = ch;
      this.freq  = freq;
      this.order = order;
      this.left  = left;
      this.mid   = mid;
      this.right = right;
    }

    public boolean isLeaf() {
      return left == null && mid == null && right == null;
    }

    public int compareTo(Node that) {
      return this.freq - that.freq;
    }
}


Expand would look like this:
public static void expand() {
  Node root = readTrie();
  int N = BinaryStdIn.readInt();
  for (int i = 0; i < N; i++) {
    // Expand ith codeword.
    Node x = root;
    while (!x.isLeaf()) {
        int v = BinaryStdIn.readInt();
        if (v == 2) {
            x = x.right;
        } else if (v == 1) {
          x = x.mid;
        } else {
          x = x.left;
        }
    }
    BinaryStdOut.write(x.ch);
  }
  BinaryStdOut.close();
}

private static void writeTrie(Node x) {
  // Write tritstring-encoded trie.
  BinaryStdOut.write(x.order);
  if (x.isLeaf()) {
    BinaryStdOut.write(x.ch);
    return;
  }
  writeTrie(x.left);
  writeTrie(x.mid);
  writeTrie(x.right);
}

private static Node readTrie() {
  int order = BinaryStdIn.readInt();
  if (order < 2) {
    return new Node(BinaryStdIn.readChar(), 0, order, null, null, null);
  }
  return new Node('\0', 0, readTrie(), readTrie(), readTrie());
}
```

# Question #2.
* Identify an optimal uniquely-decodable code that is neither prefix free nor suffix tree.
* Identify two optimal prefix-free codes for the same input that have a different distribution of codeword lengths.

## Answer
TODO

# Question #3.
Move-to-front coding. Design an algorithm to implement move-to-front encoding so that each operation takes logarithmic time in the worst case. That is, maintain alphabet of symbols in a list. A symbol is encoded as the number of symbols that precede it in the list. After encoding a symbol, move it to the front of the list.

## Answer
TODO