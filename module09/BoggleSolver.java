import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.princeton.cs.algs4.In;

public class BoggleSolver
{
    private final TST<Integer> trie = new TST<Integer>();

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        for (String word : dictionary) {
            trie.put(word, getWordScore(word));
        }
    }

    private int getWordScore(String word) {
        int length = word.length();
        if (length < 3) {
            return 0;
        } else if (length < 5) {
            return 1;
        } else if (length < 6) {
            return 2;
        } else if (length < 7) {
            return 3;
        } else if (length < 8) {
            return 5;
        } else {
            return 11;
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        SET<String> words = new SET<String>();
        if (board == null) {
            return words;
        }

        startDfs(words, board);
        return words;
    }

    private void startDfs(SET<String> words, BoggleBoard board) {
        int rows = board.rows();
        int columns = board.cols();
        boolean[][] visited = new boolean[rows][columns];
        StringBuilder word = new StringBuilder(2 * rows * columns);

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                char first = board.getLetter(r, c);
                char second = '\0';
                if (first == 'Q') {
                    second = 'U';
                }
                TST.Node<Integer> node = trie.getFirstMatchingNode(first);
                if (second != '\0' && node != null) {
                    node = trie.getNextMatchingNode(node, second);
                }
                if (node != null) {
                    int initialLength = word.length();
                    word.append(first);
                    if (second != '\0') {
                        word.append(second);
                    }
                    visited[r][c] = true;
                    dfs(r, c, node, word, visited, words, board);
                    visited[r][c] = false;
                    word.setLength(initialLength);
                }
            }
        }
    }

    private void dfs(int i, int j, TST.Node<Integer> previousNode, StringBuilder word, boolean[][] visited, SET<String> words, BoggleBoard board) {
        // StdOut.println("DFS: " + i + ", " + j + " " + word.toString());
        int rows = board.rows();
        int columns = board.cols();

        for (int r = i - 1; r <= i + 1; r++) {
            for (int c = j - 1; c <= j + 1; c++) {
                if (r >= 0 && r < rows && c >= 0 && c < columns && !visited[r][c]) {
                    char first = board.getLetter(r, c);
                    char second = '\0';
                    if (first == 'Q') {
                        second = 'U';
                    }
                    TST.Node<Integer> node = trie.getNextMatchingNode(previousNode, first);
                    if (second != '\0' && node != null) {
                        node = trie.getNextMatchingNode(node, second);
                    }

                    if (node != null) {
                        int initialLength = word.length();
                        word.append(first);
                        if (second != '\0') {
                            word.append(second);
                        }
                        if (node.getVal() != null && word.length() > 2) {
                            words.add(word.toString());
                        }
                        visited[r][c] = true;
                        dfs(r, c, node, word, visited, words, board);
                        visited[r][c] = false;
                        word.setLength(initialLength);
                    }
                }
            }
        }
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (word == null || !trie.contains(word)) {
            return 0;
        }
        return getWordScore(word);
    }

    // private void searchWord(String word) {
    //     StdOut.println("Searching word: " + word);
    //     TST.Node<Integer> node = null;
    //     String foundWord = "";
    //     for (int i = 0; i < word.length(); i++) {
    //         if (i == 0) {
    //             node = trie.getFirstMatchingNode(word.charAt(i));
    //         } else {
    //             node = trie.getNextMatchingNode(node, word.charAt(i));
    //         }
    //         if (node == null) {
    //             StdOut.println("Node is null");
    //             break;
    //         }
    //         foundWord += node.getKey();
    //     }
    //     StdOut.println("Found: " + foundWord);
    // }

    public static void main(String[] args) {
        String command = args[0];
        if (command.equals("test")) {
            test(args);
        } else if (command.equals("benchmark")) {
            benchmark(args);
        }
    }

    private static void test(String[] args) {
        String boardFilename = args[1];
        String dictionaryFilename = args[2];

        StdOut.println("Board: " + boardFilename);
        StdOut.println("Dictionary: " + dictionaryFilename);

        BoggleBoard board = new BoggleBoard(boardFilename);

        In in = new In(dictionaryFilename);
        String[] dictionary = in.readAllStrings();
        in.close();

        BoggleSolver solver = new BoggleSolver(dictionary);

        Iterable<String> words = solver.getAllValidWords(board);

        List<String> wordList = new ArrayList<>();
        for (String word : words) {
            wordList.add(word);
        }
        Collections.sort(wordList);

        StdOut.println("Solution words:");
        for (Object word : wordList) {
            StdOut.println(word);
        }
        StdOut.println("Number of words: " + wordList.size());
    }

    private static void benchmark(String[] args) {
        In in = new In(args[1]);
        String[] dictionary = in.readAllStrings();
        int runCount = Integer.parseInt(args[2]);
        BoggleSolver solver = new BoggleSolver(dictionary);

        for (int i = 0; i < runCount; i++) {
            BoggleBoard board = new BoggleBoard();
            solver.getAllValidWords(board);
        }
    }
}
