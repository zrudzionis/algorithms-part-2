import java.util.ListIterator;

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
  private static final int R = 256;

  public static void encode() {
    DoublyLinkedList<Character> list = new DoublyLinkedList<>();

    for (int i = 0; i < R; i++) {
      char ch = (char) i;
      list.add(ch);
    }

    while (!BinaryStdIn.isEmpty()) {
      char ch = BinaryStdIn.readChar();
      ListIterator<Node<Character>> iterator = list.iterator();
      int index = 0;
      while (iterator.hasNext()) {
        Node<Character> current = iterator.next();
        if (current.getItem().equals(ch)) {
          list.remove(current);
          list.prepend(current);
          BinaryStdOut.write(index, 8);
          break;
        }
        index += 1;
      }
    }
    BinaryStdOut.flush();
  }

  public static void decode() {
    DoublyLinkedList<Character> list = new DoublyLinkedList<>();

    for (int i = 0; i < R; i++) {
      char ch = (char) i;
      list.add(ch);
    }

    while (!BinaryStdIn.isEmpty()) {
      int index = BinaryStdIn.readInt(8);

      ListIterator<Node<Character>> iterator = list.iterator();
      int i = 0;
      while (iterator.hasNext()) {
        Node<Character> current = iterator.next();
        if (i == index) {
          list.remove(current);
          list.prepend(current);
          BinaryStdOut.write(current.getItem());
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
