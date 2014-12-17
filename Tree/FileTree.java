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
    Folder currentDir;
    Boolean[] allocatedBlocks = new Boolean[250];


    public FileTree(){
        root = new Folder(null, "/");
        currentDir = root;
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
        Node node = addDirPaths(currentDir, path);
        if (node != null && node instanceof Folder) {
            for (int i = 0; i < ((Folder) node).children.size(); i++) {
                children.add(((Folder)node).children.entrySet().iterator().next().getValue().getName());
            }
            return children;
        } else {
            return null;
        }
    }

    private Node getNode(String[] path){
        Node walker = currentDir;

        for (int i = 0; i < path.length; i++){
            if (path[i].equals("..")){
                walker = walker.getParent();
            }
            else if (!path[i].equals(".")){
                if (((Folder)walker).getChild(path[i]) instanceof Folder){ //@TODO Maybe NULLPOINTEREXCEPTION DUNNO
                    walker = ((Folder)walker).getChild(path[i]);
                }
                else if (i == path[i].length() - 2){
                    walker = ((Folder)walker).getChild(path[i]);
                }
                else
                    return null;
            }
        }
        return (Folder)walker;
    }

    private String[] removeLast(String[] arr){
        String[] newArr = new String[arr.length-1];
        for (int i = 0; i < arr.length-1; i++){
            newArr[i] = arr[i];
        }
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

    /**
     * Checks if a path is valid (Starting from root).
     * @param path the full path (Starting from root).
     * @return IF VALID: TRUE , ELSE: FALSE
     */
    public boolean isValidPath(String[] path){
        Node walker = root;

        for (String p : path){
            if (walker instanceof Folder){
                walker = ((Folder) walker).getChild(p);
            }
            else
                return false;
        }

        return true;
    }

    /**
     * Adds a string-path to an existing node
     * @param startPath Folder to start from
     * @param addedPath String-path
     * @return IF EXISTS: The directory path; ELSE: null
     */
    public Folder addDirPaths(Folder startPath, String[] addedPath){
        Node walker = startPath;

        for (String p : addedPath){
            if (p.equals("..")){
                walker = walker.getParent();
            }
            else if (!p.equals(".")){
                if (((Folder)walker).getChild(p) instanceof Folder){ //@TODO Maybe NULLPOINTEREXCEPTION DUNNO
                    walker = ((Folder)walker).getChild(p);
                }
                else
                    return null;
            }
        }
        return (Folder)walker;
    }

    /**
     * Changes the current directory node.
     * @param path Path from the current directory
     * @return success of the operation
     */
    public boolean cd(String[] path){
        Folder tmp = addDirPaths(currentDir, path);
        if (tmp != null){
            currentDir = tmp;
        }
        else {
            return false;
        }

        return true;
    }

    // returns a string with the working directory
    public String currentPath(){
        Folder walker = currentDir;
        String path = new String();
        if (walker == root){
            path = "/";
        }
        else {
            while (walker != null){
                if (walker != root){
                    path = "/" + walker.name + path;
                }
                walker = (Folder) walker.parent;
            }
        }

        return path;
    }

    public boolean renameFile(String[] newPath){

    }
}
