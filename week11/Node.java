public class Node<Item> {
  public Item item;
  public Node<Item> next;
  public Node<Item> prev;

  Node() {}

  Node(Item item) {
    this.item = item;
    this.next = null;
    this.prev = null;
  }
}
