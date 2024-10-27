import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import edu.princeton.cs.algs4.StdOut;

public class Main {
  public static void main(String[] args) {

    // TST<Integer> tst = new TST<Integer>();

    // tst.put("ANNOYING", 1);
    // tst.put("ANOMALIES", 1);
    // tst.put("ANOMALOUS", 1);
    // tst.put("ANOTHER", 1);

    // StdOut.println("Rez: " + String.valueOf(tst.hasPrefix("ANOMALOI")));

    // return;

    String dictionaryFilename = "week9/dictionary-algs4.txt";
    StdOut.println("Reading dictionary: " + dictionaryFilename);
    List<String> lines;
    try {
      StdOut.println(Paths.get(dictionaryFilename).toAbsolutePath().toString());
      lines = Files.readAllLines(Paths.get(dictionaryFilename));
    } catch (IOException e) {
      e.printStackTrace();
      return;
    }

    String[] dictionaryWords = lines.toArray(new String[0]);

    // initialize a 4-by-4 board from a file
    String filename = "board-quinquevalencies.txt";
    StdOut.println("4-by-4 board from file " + filename + ":");
    BoggleBoard board4 = new BoggleBoard(filename);
    StdOut.println(board4);
    StdOut.println();

    BoggleSolver solver = new BoggleSolver(dictionaryWords);

    Iterable<String> validWords = solver.getAllValidWords(board4);
    int score = 0;
    for (String word : validWords) {
        score += solver.scoreOf(word);
    }
    StdOut.println(validWords);
    StdOut.println("Score = " + score);
    StdOut.println();
  }
}
