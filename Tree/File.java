package Tree;

import java.util.ArrayList;

/**
 * Created by Oliver on 2014-12-16.
 */
public class File extends Node {
    int blockNr;

    public File(Node parent, String name){
        this.parent = parent;
        this.name = name;
        blockNr = -1;
    }

    public File(Node parent, String name, int blockNr) {
        super(parent, name);
        this.blockNr = blockNr;
    }

    public void setBlockNr(int blockNr) {
        this.blockNr = blockNr;
    }

    public int getBlockNr() {
        return blockNr;
    }
}
