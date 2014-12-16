package Tree;

/**
 * Created by Oliver on 2014-12-16.
 */
public abstract class Node {
    Node parent;
    String name;
    public Node() {
        this(null, "");
    }
    public Node(Node parent,String name){
        this.parent = parent;
        this.name = name;
    }

    public int getSize(){
        return -1;
    }

}
