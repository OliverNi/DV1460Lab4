package Tree;

import Lab4.BlockDevice;
import Lab4.MemoryBlockDevice;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Oliver on 2014-12-16.
 */
public class FileTree implements Serializable {
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

    public boolean createDirectory(String[] path){
        String name = path[path.length-1];
        Node node = getNode(currentDir, path);
        Node parent = addDirPaths(currentDir, (HelpFunctions.removeLast(path)));
        if (parent != null && node == null){
            ((Folder)parent).addChild(new Folder(parent, name));
            return true;
        }
        else
            return false;
    }


    public boolean createFile(String[] path, byte[] byteArr, BlockDevice mBlockDevice){
        Boolean success = false;
        int blockNr = freeBlock();
        Node node = getNode(currentDir, path);
        if (blockNr != -1 && node == null){
            String name = path[path.length-1];
            Node parent = addDirPaths(currentDir, (HelpFunctions.removeLast(path)));
            if (parent != null){
                File file = new File(parent, name);
                file.setBlockNr(blockNr);
                ((Folder)parent).addChild(file);

                mBlockDevice.writeBlock(blockNr, byteArr);

                allocatedBlocks[blockNr] = true;
                success = true;
            }
        }

        return success;
    }

    /**
     *
     * @return an int of the first found free block, if not found return -1
     */
    private int freeBlock(){
        for (int i = 0; i < 250; i++){
            if (!allocatedBlocks[i]){
                return i;
            }
        }
        return -1;
    }

    /**
     * Remove a node.
     * @param path The path to the node.
     * @return True if success.
     */
    public boolean removeNodePath(String[] path){
        Node node = getNode(currentDir, path);
        if (node != null){
            if (removeNode(node)){
                return true;
            }
        }
        return false;
    }

    /**
     * Deletes a specific node.
     * @param node the node to be deleted.
     * @return true if success.
     */
    private boolean removeNode(Node node){
        if (node instanceof File){
            allocatedBlocks[((File)node).getBlockNr()] = false;
            ((Folder)node.parent).removeChild(node.name);
            return true;
        }
        else if (node instanceof Folder){
            for (Map.Entry<String, Node> entry : ((Folder) node).getChildren().entrySet()){
                removeNode(entry.getValue());
            }
            ((Folder)node.getParent()).removeChild(node.getName());
            return true;
        }
        return false;
    }

    /**
     * Copies one node.
     * @param sourcePath path to the node to be copied.
     * @param destinationPath path to the destination including node name.
     * @return true if successful.
     */
    public boolean copyNode(String[] sourcePath, String[] destinationPath, BlockDevice mBlockDevice){
        Node sourceNode = getNode(currentDir, sourcePath);
        Node destinationNode = getNode(currentDir, HelpFunctions.removeLast(destinationPath));
        if (sourceNode != null && destinationNode != null && destinationNode instanceof Folder){
            if (sourceNode instanceof File){
                if (this.createFile(destinationPath, mBlockDevice.readBlock(((File) sourceNode).getBlockNr()), mBlockDevice)){
                    return true;
                }
            }
            else if (sourceNode instanceof Folder){
                createDirectory(destinationPath);

                for (Map.Entry<String, Node> entry : ((Folder) sourceNode).getChildren().entrySet()){
                    String[] childSourcePath = HelpFunctions.addElement(sourcePath, entry.getKey());
                    String[] childDestinationPath = HelpFunctions.addElement(destinationPath, entry.getKey());
                    copyNode(childSourcePath,  childDestinationPath, mBlockDevice);
                }
                return true;
            }
        }

        return false;
    }

    /**
     * Append the contents of a file to another file.
     * @param sourcePath path to the source file.
     * @param destinationPath path to the destination file.
     * @param mBlockDevice the memory block device.
     * @return true if successful.
     */

    public boolean appendFile(String[] sourcePath, String[] destinationPath, BlockDevice mBlockDevice){
        Node sourceNode = getNode(currentDir, sourcePath);
        Node destinationNode = getNode(currentDir, destinationPath);
        if (sourceNode != null && destinationNode != null && sourceNode instanceof File && destinationNode instanceof File){
            byte[] sourceByteArr = mBlockDevice.readBlock(((File) sourceNode).getBlockNr());
            byte[] destinationByteArr = mBlockDevice.readBlock(((File) destinationNode).getBlockNr());

            int sourceByteArrIndex = 0;
            for (int i = 0; i < 512; i++){
                if (destinationByteArr[i] == 0){
                    destinationByteArr[i] = sourceByteArr[sourceByteArrIndex++];
                }
            }
            mBlockDevice.writeBlock(((File) destinationNode).getBlockNr(), destinationByteArr);
            return true;
        }

        return false;
    }

    public ArrayList<String> getChildren(String[] path){
        ArrayList<String> children = new ArrayList<String>();
        Node node = addDirPaths(currentDir, path);
        if (node != null) {

            for (Map.Entry<String, Node> entry : ((Folder) node).getChildren().entrySet()){
                String name = entry.getKey();
                children.add(name);
            }

            return children;
        } else {
            return null;
        }
    }

    private Node getNode(Node startPath, String[] path){
        Node walker = startPath;

        for (int i = 0; i < path.length; i++){
            if (path[i].equals("..")){
                walker = walker.getParent();
            }
            else if (!path[i].equals(".")){
                if (((Folder)walker).getChild(path[i]) instanceof Folder){
                    walker = ((Folder)walker).getChild(path[i]);
                }
                else if (i == path.length - 1){
                    walker = ((Folder)walker).getChild(path[i]);
                }
                else
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
                if (((Folder)walker).getChild(p) instanceof Folder){
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

    public boolean renameFile(String[] oldPath, String[] newPath){
        Node oldPathNode = getNode(currentDir, oldPath);
        Folder newPathNode = addDirPaths(currentDir, HelpFunctions.removeLast(newPath));
        String newName = newPath[newPath.length-1];
        Node node = getNode(currentDir, newPath);

        if (oldPathNode == null || newPathNode == null || node != null)
            return false;

        //Remove child from current directory
        ((Folder)oldPathNode.getParent()).removeChild(oldPathNode.getName());

        //Rename and move to new directory
        oldPathNode.setName(newName);
        oldPathNode.setParent(newPathNode);
        ((Folder)oldPathNode.getParent()).addChild(oldPathNode);

        return true;
    }

    /**
     * Tiny function that resets the current directory to the root.
     */
    public void resetCurrentDir(){
        currentDir = root;
    }

    /**
     * Returns the total bytes used by a node (including subfolders and files in folders)
     * @param path path to the node
     * @param mBlockDevice the BlockDevice containing blocks
     * @return IF EXISTS: Total size of all files connected to node ; ELSE: -1.
     */
    public int getNodeSize(String[] path, BlockDevice mBlockDevice){
        int size = 0;
        Node node = getNode(currentDir, path);
        if (node instanceof File){
            byte[] block = mBlockDevice.readBlock(((File)node).getBlockNr());
            for (byte b : block){
                if (b == 0)
                    return size;
                size++;
            }
        }
        else {//Folder
            for (Map.Entry<String, Node> entry : ((Folder) node).getChildren().entrySet()){
                String[] childPath = HelpFunctions.addElement(path, entry.getKey());
                size+=getNodeSize(childPath, mBlockDevice);
            }
            return size;
        }
        return -1;
    }

    /**
     * Get the blockNumber for a file
     * @param path path to the file
     * @return IF EXISTS: The blockNr ; ELSE: -1
     */
    public int getBlockNr(String[] path){
        Node file = getNode(currentDir, path);
        if (file != null && file instanceof File){
            return ((File) file).getBlockNr();
        }
        else {
            return -1;
        }
    }
}
