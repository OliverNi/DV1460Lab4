package Lab4;

import Tree.FileTree;

import java.util.ArrayList;

public class Filesystem
{
  private BlockDevice m_BlockDevice;
  private FileTree fileTree;
  public static int BLOCK_SIZE = 512;
  private String currentDir = "/";
  public Filesystem(BlockDevice p_BlockDevice)
    {
      m_BlockDevice=p_BlockDevice;
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
      ArrayList<String> lsList = fileTree.getChildren(mergeStringArr(currentDir.split("/"), p_asPath));
      for (int i = 0; i < lsList.size(); i++){
        if (i != 0 && (i % 5) == 0)
          System.out.println();
        System.out.print(lsList.get(i));
      }
      return "";
    }


  public String create(String[] p_asPath,byte[] p_abContents)
    {
      System.out.print("Creating file ");
      dumpArray(p_asPath);
      System.out.print("");
      return "";
    }

  public String cat(String[] p_asPath)
    {
      System.out.print("Dumping contents of file ");
      dumpArray(p_asPath);
      System.out.print("");
      return "";
    }

  public String save(String p_sPath)
    {
      System.out.print("Saving blockdevice to file "+p_sPath);
      return "";
    }

  public String read(String p_sPath)
    {
      System.out.print("Reading file "+p_sPath+" to blockdevice");
      return "";
    }

  public String rm(String[] p_asPath)
    {
      System.out.print("Removing file ");
      dumpArray(p_asPath);
      System.out.print("");
      return "";
    }

  public String copy(String[] p_asSource,String[] p_asDestination)
    {
      System.out.print("Copying file from ");
      dumpArray(p_asSource);
      System.out.print(" to ");
      dumpArray(p_asDestination);
      System.out.print("");
      return "";
    }

  public String append(String[] p_asSource,String[] p_asDestination)
    {
      System.out.print("Appending file ");
      dumpArray(p_asSource);
      System.out.print(" to ");
      dumpArray(p_asDestination);
      System.out.print("");
      return "";
    }

  public String rename(String[] p_asSource,String[] p_asDestination)
    {
      System.out.print("Renaming file ");
      dumpArray(p_asSource);
      System.out.print(" to ");
      dumpArray(p_asDestination);
      System.out.print("");
      return "";
    }

  public String mkdir(String[] p_asPath)
    {
      System.out.print("Creating directory ");
      dumpArray(p_asPath);
      System.out.print("");

      String[] path = mergeStringArr(currentDir.split("/"), p_asPath);

      if (fileTree.createDirectory(path)) {
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

      String[] path = mergeStringArr(currentDir.split("/"), p_asPath);


      if (fileTree.isValidPath(path)){
        currentDir = "";
        for (String p : path) {
          currentDir += "/" + p;
        }
        fileTree.cd(p_asPath);
        return "";
      }

      else {
        return "cd: " + mergeToString(p_asPath) + ": No such directory";
      }
    }

  public String pwd()
    {
      return currentDir;
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
