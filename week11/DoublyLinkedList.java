/******************************************************************************
 *  Compilation:  javac DoublyLinkedList.java
 *  Execution:    java DoublyLinkedList
 *  Dependencies: StdOut.java
 *
 *  A list implemented with a doubly linked list. The elements are stored
 *  (and iterated over) in the same order that they are inserted.
 *
 *  % java DoublyLinkedList 10
 *  10 random integers between 0 and 99
 *  24 65 2 39 86 24 50 47 13 4
 *
 *  add 1 to each element via next() and set()
 *  25 66 3 40 87 25 51 48 14 5
 *
 *  multiply each element by 3 via previous() and set()
 *  75 198 9 120 261 75 153 144 42 15
 *
 *  remove elements that are a multiple of 4 via next() and remove()
 *  75 198 9 261 75 153 42 15
 *
 *  remove elements that are even via previous() and remove()
 *  75 9 261 75 153 15
 *
 ******************************************************************************/

import java.util.ListIterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class DoublyLinkedList<Item> implements Iterable<Node<Item>> {
  private int n; // number of elements on list
  private Node<Item> pre; // sentinel before first item
  private Node<Item> post; // sentinel after last item

  public DoublyLinkedList() {
    pre = new Node<Item>();
    post = new Node<Item>();
    pre.next = post;
    post.prev = pre;
  }

  public boolean isEmpty() {
    return n == 0;
  }

  public int size() {
    return n;
  }

  public void insertAfter(Node<Item> node, Node<Item> nodeToInsert) {
    Node<Item> nodeAfter = node.next;
    nodeToInsert.prev = node;
    nodeToInsert.next = nodeAfter;
    node.next = nodeToInsert;
    nodeAfter.prev = nodeToInsert;
    n++;
  }

  public void insertBefore(Node<Item> node, Node<Item> nodeToInsert) {
    Node<Item> nodeBefore = node.prev;
    nodeToInsert.prev = nodeBefore;
    nodeToInsert.next = node;
    node.prev = nodeToInsert;
    nodeBefore.next = nodeToInsert;
    n++;
  }

  public void remove(Node<Item> node) {
    Node<Item> before = node.prev;
    Node<Item> after = node.next;
    before.next = after;
    after.prev = before;
    node.prev = null;
    node.next = null;
    n--;
  }

  public void prepend(Node<Item> node) {
    Node<Item> tmp = pre.next;
    pre.next = node;
    node.prev = pre;
    node.next = tmp;
    tmp.prev = node;
    n++;
  }

  public void add(Item item) {
    Node<Item> last = post.prev;
    Node<Item> x = new Node<Item>();
    x.item = item;
    x.next = post;
    x.prev = last;
    post.prev = x;
    last.next = x;
    n++;
  }

  public ListIterator<Node<Item>> iterator() {
    return new DoublyLinkedListIterator();
  }

  // assumes no calls to DoublyLinkedList.add() during iteration
  private class DoublyLinkedListIterator implements ListIterator<Node<Item>> {
    private Node<Item> current = pre.next; // the node that is returned by next()
    private Node<Item> lastAccessed = null; // the last node to be returned by prev() or next()
                                      // reset to null upon intervening remove() or add()
    private int index = 0;

    public boolean hasNext() {
      return index < n;
    }

    public boolean hasPrevious() {
      return index > 0;
    }

    public int previousIndex() {
      return index - 1;
    }

    public int nextIndex() {
      return index;
    }

    public Node<Item> next() {
      if (!hasNext())
        throw new NoSuchElementException();
      lastAccessed = current;
      Node<Item> returnValue = current;
      current = current.next;
      index++;
      return returnValue;
    }

    public Node<Item> previous() {
      if (!hasPrevious())
        throw new NoSuchElementException();
      current = current.prev;
      index--;
      lastAccessed = current;
      return current;
    }

    // replace the item of the element that was last accessed by next() or
    // previous()
    // condition: no calls to remove() or add() after last call to next() or
    // previous()
    public void set(Node<Item> node) {
      if (lastAccessed == null)
        throw new IllegalStateException();
      Node<Item> prev = lastAccessed.prev;
      Node<Item> next = lastAccessed.next;
      node.prev = prev;
      node.next = next;
      lastAccessed.next = null;
      lastAccessed.prev = null;
      lastAccessed = node;
    }

    // remove the element that was last accessed by next() or previous()
    // condition: no calls to remove() or add() after last call to next() or
    // previous()
    public void remove() {
      if (lastAccessed == null)
        throw new IllegalStateException();
      Node<Item> x = lastAccessed.prev;
      Node<Item> y = lastAccessed.next;
      x.next = y;
      y.prev = x;
      n--;
      if (current == lastAccessed)
        current = y;
      else
        index--;
      lastAccessed = null;
    }

    // add element to list
    public void add(Node<Item> y) {
      Node<Item> x = current.prev;
      Node<Item> z = current;
      x.next = y;
      y.next = z;
      z.prev = y;
      y.prev = x;
      n++;
      index++;
      lastAccessed = null;
    }

  }

  public String toString() {
    StringBuilder s = new StringBuilder();
    for (Node<Item> node : this)
      s.append(node.item + " ");
    return s.toString();
  }

  // a test client
  public static void main(String[] args) {
    int n = Integer.parseInt(args[0]);

    // add elements 1, ..., n
    StdOut.println(n + " random integers between 0 and 99");
    DoublyLinkedList<Integer> list = new DoublyLinkedList<Integer>();
    for (int i = 0; i < n; i++)
      list.add(StdRandom.uniform(100));
    StdOut.println(list);
    StdOut.println();

    ListIterator<Node<Integer>> iterator = list.iterator();

    // go forwards with next() and set()
    StdOut.println("add 1 to each element via next() and set()");
    while (iterator.hasNext()) {
      Node<Integer> x = iterator.next();
      Node<Integer> y = new Node<Integer>(x.item + 1);
      iterator.set(y);
    }
    StdOut.println(list);
    StdOut.println();

    // go backwards with previous() and set()
    StdOut.println("multiply each element by 3 via previous() and set()");
    while (iterator.hasPrevious()) {
      Node<Integer> x = iterator.previous();
      iterator.set(new Node<Integer>(x.item * 3));
    }
    StdOut.println(list);
    StdOut.println();

    // remove all elements that are multiples of 4 via next() and remove()
    StdOut.println("remove elements that are a multiple of 4 via next() and remove()");
    while (iterator.hasNext()) {
      Node<Integer> x = iterator.next();
      if (x.item % 4 == 0)
        iterator.remove();
    }
    StdOut.println(list);
    StdOut.println();

    // remove all even elements via previous() and remove()
    StdOut.println("remove elements that are even via previous() and remove()");
    while (iterator.hasPrevious()) {
      Node<Integer> x = iterator.previous();
      if (x.item % 2 == 0)
        iterator.remove();
    }
    StdOut.println(list);
    StdOut.println();

    // add elements via next() and add()
    StdOut.println("add elements via next() and add()");
    while (iterator.hasNext()) {
      Node<Integer> x = iterator.next();
      iterator.add(new Node<Integer>(x.item + 1));
    }
    StdOut.println(list);
    StdOut.println();

    // add elements via previous() and add()
    StdOut.println("add elements via previous() and add()");
    while (iterator.hasPrevious()) {
      Node<Integer> x = iterator.previous();
      iterator.add(new Node<Integer>(x.item * 10));
      iterator.previous();
    }
    StdOut.println(list);
    StdOut.println();
  }
}
