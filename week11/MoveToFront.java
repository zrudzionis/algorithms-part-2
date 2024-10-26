import java.util.Iterator;
import java.util.LinkedList;

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
  private static final int R = 256;

  public static void encode() {
    LinkedList<Character> list = new LinkedList<>();

    for (int i = 0; i < R; i++) {
      char ch = (char) i;
      list.add(ch);
    }

    while (!BinaryStdIn.isEmpty()) {
      char ch = BinaryStdIn.readChar();
      Iterator<Character> iterator = list.iterator();
      int i = 0;
      while (iterator.hasNext()) {
        char current = iterator.next();
        if (current == ch) {
          list.remove(i);
          list.addFirst(current);
          BinaryStdOut.write(i, 8);
          break;
        }
        i += 1;
      }
    }
    BinaryStdOut.flush();
  }

  public static void decode() {
    LinkedList<Character> list = new LinkedList<>();

    for (int i = 0; i < R; i++) {
      char ch = (char) i;
      list.add(ch);
    }

    while (!BinaryStdIn.isEmpty()) {
      int index = BinaryStdIn.readInt(8);

      Iterator<Character> iterator = list.iterator();
      int i = 0;
      while (iterator.hasNext()) {
        char current = iterator.next();
        if (i == index) {
          list.remove(i);
          list.addFirst(current);
          BinaryStdOut.write(current);
          break;
        }
        i += 1;
      }
    }
    BinaryStdOut.flush();
  }

  // if args[0] is "-", apply move-to-front encoding
  // if args[0] is "+", apply move-to-front decoding
  public static void main(String[] args) {
    String command = args[0];
    if (command.equals("-")) {
      encode();
    } else if (command.equals("+")) {
      decode();
    } else {
      throw new IllegalArgumentException("Invalid command");
    }
  }
}
