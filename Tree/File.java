package Tree;

import java.util.ArrayList;

/**
 * Created by Oliver on 2014-12-16.
 */
public class File extends Node {
    ArrayList<Integer> blocks;

    public File(String name){
        this.name = name;
        blocks = new ArrayList<Integer>();
    }

    /**
     * Add the next block (OBS ORDER)
     * @param arrayPos The block's position in the byte array.
     */
    public void addBlock(int arrayPos){
        blocks.add(arrayPos);
    }

    /**
     * Get a block's position in the byte-array.
     * @param pos position in children-list.
     * @return block's position in the byte-array.
     */
    public int getBlock(int pos){
        return blocks.get(pos);
    }

    public ArrayList<Integer> getBlocks() {
        return blocks;
    }

    public int getSize(){
        return blocks.size() * 512;
    }
}
