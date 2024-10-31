import edu.princeton.cs.algs4.Queue;

public class TST<Value> {
    private int n; // size
    private Node<Value> root; // root of TST

    public static class Node<Value> {
        private char c; // key
        private Node<Value> left, mid, right; // left, middle, and right subtries
        private Value val; // value

        public Value getVal() {
            return val;
        }

        public char getKey() {
            return c;
        }

        public Node<Value> getLeft() {
            return left;
        }

        public Node<Value> getMid() {
            return mid;
        }

        public Node<Value> getRight() {
            return right;
        }
    }

    /**
     * Initializes an empty string symbol table.
     */
    public TST() {
    }

    /**
     * Returns the number of key-value pairs in this symbol table.
     *
     * @return the number of key-value pairs in this symbol table
     */
    public int size() {
        return n;
    }

    /**
     * Does this symbol table contain the given key?
     *
     * @param key the key
     * @return {@code true} if this symbol table contains {@code key} and
     *         {@code false} otherwise
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public boolean contains(CharSequence key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to contains() is null");
        }
        return get(key) != null;
    }

    /**
     * Returns the value associated with the given key.
     *
     * @param key the key
     * @return the value associated with the given key if the key is in the symbol
     *         table
     *         and {@code null} if the key is not in the symbol table
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public Value get(CharSequence key) {
        if (key == null) {
            throw new IllegalArgumentException("calls get() with null argument");
        }
        if (key.length() == 0)
            throw new IllegalArgumentException("key must have length >= 1");
        Node<Value> x = get(root, key, 0);
        if (x == null)
            return null;
        return x.val;
    }

    public Node<Value> getFirstMatchingNode(char c) {
        return getFirstMatchingNode(root, c);
    }

    public Node<Value> getFirstMatchingNode(Node<Value> x, char c) {
        if (x == null)
            return null;

        if (c == x.c) {
            return x;
        }

        if (c < x.c) {
            return getFirstMatchingNode(x.left, c);
        } else if (c > x.c) {
            return getFirstMatchingNode(x.right, c);
        } else {
            return x.mid;
        }
    }

    public Node<Value> getNextMatchingNode(Node<Value> x, char c) {
        if (x == null)
            return null;

        return getFirstMatchingNode(x.mid, c);
    }

    // return subtrie corresponding to given key
    private Node<Value> get(Node<Value> x, CharSequence key, int d) {
        if (x == null)
            return null;

        if (key.length() == 0)
            throw new IllegalArgumentException("key must have length >= 1");
        char c = key.charAt(d);
        if (c < x.c) {
            return get(x.left, key, d);
        } else if (c > x.c) {
            return get(x.right, key, d);
        } else if (d < key.length() - 1) {
            return get(x.mid, key, d + 1);
        } else {
            return x;
        }
    }

    /**
     * Inserts the key-value pair into the symbol table, overwriting the old value
     * with the new value if the key is already in the symbol table.
     * If the value is {@code null}, this effectively deletes the key from the
     * symbol table.
     *
     * @param key the key
     * @param val the value
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public void put(String key, Value val) {
        if (key == null) {
            throw new IllegalArgumentException("calls put() with null key");
        }
        if (!contains(key))
            n++;
        else if (val == null)
            n--; // delete existing key
        root = put(root, key, val, 0);
    }

    private Node<Value> put(Node<Value> x, String key, Value val, int d) {
        char c = key.charAt(d);
        if (x == null) {
            x = new Node<Value>();
            x.c = c;
        }
        if (c < x.c)
            x.left = put(x.left, key, val, d);
        else if (c > x.c)
            x.right = put(x.right, key, val, d);
        else if (d < key.length() - 1)
            x.mid = put(x.mid, key, val, d + 1);
        else
            x.val = val;
        return x;
    }

    /**
     * Returns all keys in the symbol table as an {@code Iterable}.
     * To iterate over all of the keys in the symbol table named {@code st},
     * use the foreach notation: {@code for (Key key : st.keys())}.
     *
     * @return all keys in the symbol table as an {@code Iterable}
     */
    public Iterable<String> keys() {
        Queue<String> queue = new Queue<String>();
        collect(root, new StringBuilder(), queue);
        return queue;
    }

    // all keys in subtrie rooted at x with given prefix
    private void collect(Node<Value> x, StringBuilder prefix, Queue<String> queue) {
        if (x == null)
            return;
        collect(x.left, prefix, queue);
        if (x.val != null)
            queue.enqueue(prefix.toString() + x.c);
        collect(x.mid, prefix.append(x.c), queue);
        prefix.deleteCharAt(prefix.length() - 1);
        collect(x.right, prefix, queue);
    }
}
