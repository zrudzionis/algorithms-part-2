import edu.princeton.cs.algs4.SET;

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

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                String dice = String.valueOf(board.getLetter(r, c));
                if (dice.equals("Q") || dice.equals("Qu")) {
                    dice = "QU";
                }

                if (trie.hasPrefix(dice)) {
                    visited[r][c] = true;
                    dfs(r, c, dice, visited, words, board);
                    visited[r][c] = false;
                }
            }
        }
    }

    private void dfs(int i, int j, String word, boolean[][] visited, SET<String> words, BoggleBoard board) {
        int rows = board.rows();
        int columns = board.cols();

        for (int r = i - 1; r <= i + 1; r++) {
            for (int c = j - 1; c <= j + 1; c++) {
                if (r >= 0 && r < rows && c >= 0 && c < columns && !visited[r][c]) {
                    String dice = String.valueOf(board.getLetter(r, c));
                    if (dice.equals("Q") || dice.equals("Qu")) {
                        dice = "QU";
                    }
                    String w = word + dice;

                    if (trie.hasPrefix(w)) {
                        if (w.length() > 2 && trie.contains(w)) {
                            words.add(w);
                        }

                        visited[r][c] = true;
                        dfs(r, c, w, visited, words, board);
                        visited[r][c] = false;
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
}
