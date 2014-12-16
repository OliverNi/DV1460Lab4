package Lab4;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Filesystem
{
  private BlockDevice m_BlockDevice;
  public static int BLOCK_SIZE = 512;
  public Filesystem(BlockDevice p_BlockDevice)
    {
      m_BlockDevice=p_BlockDevice;
    }

  public String format()
    {
      m_BlockDevice = new MemoryBlockDevice();
      return "Diskformat sucessfull";
    }

  public String ls(String[] p_asPath)
    {
      System.out.print("Listing directory ");
      dumpArray(p_asPath);
      System.out.print("");
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
      return "";
    }

  public String cd(String[] p_asPath)
    {
      System.out.print("Changing directory to ");
      dumpArray(p_asPath);
      System.out.print("");
      return "";
    }

  public String pwd()
    {
      return "/unknown/";
    }

  private void dumpArray(String[] p_asArray)
    {
      for(int nIndex=0;nIndex<p_asArray.length;nIndex++)
        {
          System.out.print(p_asArray[nIndex]+"=>");
        }
    }

  private boolean setName(byte[] block, String name){
    return false;
  }

  private boolean setSize(byte[] sizeInBytes, int size){
    return false;
  }
  /**
   * Assign root
   */
  private void createRoot(MemoryBlockDevice m_abContents){
    byte[] header = new byte[4];
    byte[] name = new byte[40];
    byte[] sizeInBytes = new byte[5];
    byte[] data = new byte[471];
    //Set root as map
    header[0] = 2;
    //Set parentId - No parent (0)
    header[1] = 0;
    //Set id
    header[2] = 1;
    //Set nextBlock (No next block)
    header[3] = 0;
    //Set mapName
    setName(name, "/");
    //Set size (one block)
    setSize(sizeInBytes, BLOCK_SIZE);

    //@TODO Maybe assign data


    ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
    try {
      outputStream.write(header);
      outputStream.write(name);
      outputStream.write(sizeInBytes);
      outputStream.write(data);
    } catch (IOException e) {
      e.printStackTrace();
    }

    m_abContents.writeBlock(0, outputStream.toByteArray());
  }

}