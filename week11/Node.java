public class Node<Item> {
  private Item item;
  private Node<Item> next;
  private Node<Item> prev;

  Node() { }

  Node(Item item) {
    this.item = item;
    this.next = null;
    this.prev = null;
  }

  public Node<Item> getPrev() {
    return prev;
  }

  public void setPrev(Node<Item> prev) {
    this.prev = prev;
  }

  public Node<Item> getNext() {
    return next;
  }

  public void setNext(Node<Item> next) {
    this.next = next;
  }

  public Item getItem() {
    return item;
  }

  public void setItem(Item item) {
    this.item = item;
  }
}
