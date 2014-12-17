package Tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Oliver on 2014-12-16.
 */
public class FileTree {
    Folder root;
    Boolean[] allocatedBlocks = new Boolean[250];


    public FileTree(){
        root = new Folder(null, "/");
        for (int i = 0; i < 250; i++) {
            allocatedBlocks[i] = false;
        }
    }

    public ArrayList<Integer> getBlockPositions(String[] path){
        Node file = getNode(path);
        if (file instanceof File)
            return ((File) file).getBlocks();
        else
            return null;
    }

    public boolean createDirectory(String[] path){
        String name = path[path.length-1];
        Node parent = getParentFolder(path);
        if (parent != null){
            ((Folder)parent).addChild(new Folder(parent, name));
            return true;
        }
        else
            return false;
    }

    public ArrayList<String> getChildren(String[] path){
        ArrayList<String> children = new ArrayList<String>();
        Node node = getNode(path);
        if (node instanceof Folder) {
            for (int i = 0; i < ((Folder) node).children.size(); i++) {
                children.add(((Folder)node).children.entrySet().iterator().next().getValue().name);
            }
            return children;
        } else {
            return null;
        }
    }

    private Node getNode(String[] path){
        Node walker = root;

        for (String p : path){
            if (walker instanceof Folder) {
                walker = ((Folder) walker).getChild(p);
            }
            else{
                return null;
            }
        }

        return walker;
    }

    private Node getParentFolder(String[] path){
        Node walker = root;
        for (int i = 0; i < path.length-1; i++){
            if (walker instanceof Folder){
                walker = ((Folder) walker).getChild(path[i]);
            }
            else {
                return null;
            }
        }

        return walker;
    }


}
