import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
  private static final int R = 256;

  public static void encode() {
    char[] chars = new char[R];
    for (int i = 0; i < R; i++) {
      chars[i] = (char) i;
    }

    while (!BinaryStdIn.isEmpty()) {
      char ch = BinaryStdIn.readChar();
      int index = moveToFront(chars, ch);
      BinaryStdOut.write(index, 8);
    }
    BinaryStdOut.flush();
  }

  public static void decode() {
    char[] chars = new char[R];
    for (int i = 0; i < R; i++) {
      chars[i] = (char) i;
    }

    while (!BinaryStdIn.isEmpty()) {
      int index = BinaryStdIn.readInt(8);
      char ch = chars[index];
      moveToFront(chars, index);
      BinaryStdOut.write(ch);
    }
    BinaryStdOut.flush();
  }


  private static int moveToFront(char[] chars, char ch) {
    char next = chars[0];
    int index = 0;
    for (int i = 0; i < chars.length - 1; i++) {
      if (next == ch) {
        break;
      }
      char tmp = chars[i + 1];
      chars[i + 1] = next;
      next = tmp;
      index += 1;
    }
    chars[0] = ch;
    return index;
  }

  private static void moveToFront(char[] chars, int index) {
    char ch = chars[index];
    for (int i = index; i > 0; i--) {
      chars[i] = chars[i - 1];
    }
    chars[0] = ch;
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
