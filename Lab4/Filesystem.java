package Lab4;

import Tree.FileTree;

import java.io.*;
import java.util.ArrayList;

public class Filesystem
{
  private BlockDevice m_BlockDevice;
  private FileTree fileTree;
  public static int BLOCK_SIZE = 512;
  // private String currentDir = "/";
  public Filesystem(BlockDevice p_BlockDevice)
    {
      m_BlockDevice=p_BlockDevice;
      fileTree = new FileTree();
    }

  public String format()
    {
      m_BlockDevice = new MemoryBlockDevice();
      fileTree = new FileTree();
      return "Diskformat sucessfull";
    }

  public String ls(String[] p_asPath)
    {
      System.out.print("Listing directory ");
      dumpArray(p_asPath);
      System.out.println();
      ArrayList<String> lsList = fileTree.getChildren(p_asPath);
      if (lsList == null) {
        return "";
      }
      for (int i = 0; i < lsList.size(); i++){
        if (i != 0 && (i % 5) == 0)
          System.out.println();
        System.out.print(lsList.get(i) + " ");
      }
      return "";
    }


  public String create(String[] p_asPath,byte[] p_abContents)
    {
      System.out.print("Creating file ");
      dumpArray(p_asPath);
      System.out.print("");

      /*
      for (int i = 0; i < p_abContents.length; i++){
        if (p_abContents[i] == 0)
          break;
        System.out.println(p_abContents[i]);
      }*/

      byte[] temp = new byte[512];
      for (int i = 0; i < 512; i++){
        temp[i] = p_abContents[i];
      }

      if (fileTree.createFile(p_asPath)){
        ArrayList<Integer> blocks = fileTree.getFileBlocks(p_asPath);
        m_BlockDevice.writeBlock(blocks.get(0), temp);

        return "File created";
      }

      return "";
    }

  public String cat(String[] p_asPath)
    {
      System.out.print("Dumping contents of file ");
      dumpArray(p_asPath);
      System.out.println("");

      ArrayList<Integer> blocks = fileTree.getFileBlocks(p_asPath);
      if (blocks != null){
        byte[] byteArr = m_BlockDevice.readBlock(blocks.get(0));

        try {
          String decoded = new String(byteArr, "UTF-8");

          return decoded;
        } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
        }

      }


      return "error";
    }

  public String save(String p_sPath)
    {
      System.out.print("Saving blockdevice to file "+p_sPath);

      ObjectOutputStream out = null;
      try {
        out = new ObjectOutputStream(new FileOutputStream(p_sPath));
        out.writeObject(fileTree);
        out.writeObject(m_BlockDevice);
        out.close();

        return "ok";

      } catch (IOException e) {
        e.printStackTrace();
      }

      return "error";
    }

  public String read(String p_sPath)
    {
      System.out.print("Reading file "+p_sPath+" to blockdevice");

      ObjectInputStream in = null;
      try {
        in = new ObjectInputStream(new FileInputStream(p_sPath));
        Object obj = in.readObject();
        this.fileTree = (FileTree) obj;

        obj = in.readObject();
        this.m_BlockDevice = (MemoryBlockDevice) obj;

        in.close();

        fileTree.resetCurrentDir();

        return "ok";

      } catch (IOException e) {
        e.printStackTrace();
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      }


      return "error";
    }

  public String rm(String[] p_asPath)
    {
      System.out.print("Removing file ");
      dumpArray(p_asPath);
      System.out.println("");

      if (fileTree.removeNodePath(p_asPath)){
        return "Deleted";
      }

      return "Error";
    }

  public String copy(String[] p_asSource,String[] p_asDestination)
    {
      System.out.print("Copying file from ");
      dumpArray(p_asSource);
      System.out.print(" to ");
      dumpArray(p_asDestination);
      System.out.println("");

      if (fileTree.copyNode(p_asSource, p_asDestination)){
        if (fileTree.nodeIsFile(p_asSource)){
          m_BlockDevice.writeBlock(fileTree.getFileBlocks(p_asDestination).get(0), m_BlockDevice.readBlock(fileTree.getFileBlocks(p_asSource).get(0)));
        }
        return "copied";
      }

      return "error";
    }

  public String append(String[] p_asSource,String[] p_asDestination)
    {
      System.out.print("Appending file ");
      dumpArray(p_asSource);
      System.out.print(" to ");
      dumpArray(p_asDestination);
      System.out.print("");

      if (fileTree.appendFile(p_asSource, p_asDestination, m_BlockDevice)){
        return "ok";
      }

      return "error";
    }

  public String rename(String[] p_asSource,String[] p_asDestination)
    {
      boolean success = fileTree.renameFile(p_asSource, p_asDestination);
      if (success) {
        System.out.print("Renaming file ");
        dumpArray(p_asSource);
        System.out.print(" to ");
        dumpArray(p_asDestination);
      }
      else
        System.out.println("Something went wrong...");

      return "";
    }

  public String mkdir(String[] p_asPath)
    {
      System.out.print("Creating directory ");
      dumpArray(p_asPath);
      System.out.print("");

      if (fileTree.createDirectory(p_asPath)) {
        return "Directory created";
      }
      else {
        return "Something went wrong";
      }
    }



  public String cd(String[] p_asPath)
    {
      System.out.print("Changing directory to ");
      dumpArray(p_asPath);
      System.out.print("");

      if (fileTree.cd(p_asPath)){
        return "";
      }
      else {
        return "cd: " + mergeToString(p_asPath) + ": No such directory";
      }
    }

  public String pwd()
    {
      return fileTree.currentPath();
    }

  private void dumpArray(String[] p_asArray)
    {
      for(int nIndex=0;nIndex<p_asArray.length;nIndex++)
        {
          System.out.print(p_asArray[nIndex]+"=>");
        }
    }


  private String[] mergeStringArr(String[] arr1, String[] arr2) {
    String[] both = new String[arr1.length + arr2.length];

    int index = 0;
    for (String str : arr1) {
      both[index++] = str;
    }
    for (String str : arr2) {
      both[index++] = str;
    }

    return both;
  }

  private String mergeToString(String[] arr) {
    String merged = "";

    for (String s : arr){
      merged += s;
    }

    return merged;
  }



}
