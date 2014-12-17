package Tree;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Oliver on 2014-12-16.
 */
public class Folder extends Node {
    private Map<String, Node> children;
    public Folder(Node parent, String name) {
        this.parent = parent;
        this.name = name;
        children = new TreeMap<String, Node>();
    }

    /**
     * Add child to this node and set this node as parent to the specified child.
     * @param child the child
     * @return success of the operation.
     */
    public boolean addChild(Node child){
        boolean success = false;
        if (children.put(child.name, child) != null){
            child.parent = this;
            success = true;
        }
        return success;
    }

    /**
     * get child with specified name.
     * @param name the name
     * @return the Node, IF NOT FOUND: null
     */
    public Node getChild(String name){
        return children.get(name);
    }

    public Node removeChild(String name){
        return children.remove(name);
    }

    public int getSize(){
        int size = 0;
        for (int i = 0; i < children.size(); i++)
            size += children.entrySet().iterator().next().getValue().getSize();
        return size;
    }

    public Map<String, Node> getChildren() {
        return children;
    }
}
