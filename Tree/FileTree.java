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

    public FileTree(){
        root = new Folder(null, "/");
    }

    public ArrayList<Integer> getBlockPositions(String[] path){
        Node file = getNode(path);
        if (file instanceof File)
            return ((File) file).getBlocks();
        else
            return null;
    }

    private Node getNode(String[] path){
        Node walker = root;

        for (String p : path){
            if (walker instanceof Folder)
                walker = root.getChild(p);
            else{
                return null;
            }
        }

        return walker;
    }
}
