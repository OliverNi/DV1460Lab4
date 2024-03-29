package Tree;

import java.io.Serializable;

/**
 * Created by Oliver on 2014-12-16.
 */
public abstract class Node implements Serializable {
    protected Node parent;
    protected String name;
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

    public Node getParent() {
        return parent;
    }

    public String getName() {
        return name;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public void setName(String name) {
        this.name = name;
    }
}
