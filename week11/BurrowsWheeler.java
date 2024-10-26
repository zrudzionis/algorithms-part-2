import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
  private static final int R = 256; // extended ASCII

  // apply Burrows-Wheeler transform,
  // reading from standard input and writing to standard output
  public static void transform() {
    String s = BinaryStdIn.readString();
    CircularSuffixArray csa = new CircularSuffixArray(s);
    int n = s.length();
    char[] t = new char[n];
    for (int i = 0; i < n; i++) {
      int index = csa.index(i);
      if (index == 0) {
        BinaryStdOut.write(i);
      }
      char ch = s.charAt((n - 1 + index) % n);
      t[i] = ch;
    }
    for (int i = 0; i < n; i++) {
      BinaryStdOut.write(t[i]);
    }
    BinaryStdOut.flush();
  }

  // apply Burrows-Wheeler inverse transform,
  // reading from standard input and writing to standard output
  public static void inverseTransform() {
    int first = BinaryStdIn.readInt();
    String t = BinaryStdIn.readString();

    int n = t.length();
    int[] charCount = new int[R + 1];
    // count characters
    for (int i = 0; i < n; i++) {
        charCount[t.charAt(i) + 1]++;
    }
    // making "counting sort" array
    for (int i = 1; i < R + 1; i++) {
        charCount[i] += charCount[i - 1];
    }

    // we start from char in t. We find the index of the char in the sorted t.
    // We increment index preparing for the next char.
    int[] next = new int[n];
    char[] tSorted = new char[n];
    for (int i = 0; i < n; i++) {
        int sortedIndex = charCount[t.charAt(i)];
        charCount[t.charAt(i)] += 1;
        // this will be needed later to reconstruct the original string
        tSorted[sortedIndex] = t.charAt(i);
        next[sortedIndex] = i;
    }

    for (int i = 0; i < n; i++) {
        BinaryStdOut.write(tSorted[first]);
        first = next[first];
    }

    BinaryStdOut.flush();
  }

  // if args[0] is "-", apply Burrows-Wheeler transform
  // if args[0] is "+", apply Burrows-Wheeler inverse transform
  public static void main(String[] args) {
      String command = args[0];
      if (command.equals("-")) {
        transform();
      } else if (command.equals("+")) {
        inverseTransform();
      } else {
        throw new IllegalArgumentException("Invalid command");
      }
  }
}
