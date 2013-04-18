
    package mmm.image;
     
    import java.io.File;
    
   import javax.swing.filechooser.FileFilter;
    
   /*
  14  * This class implements a generic file name filter that allows the listing/selection
  15  * of JPEG files.
  16  */
   public class PNGImageFileFilter extends FileFilter implements java.io.FileFilter
     {
     public boolean accept(File f)
       {
       if (f.getName().toLowerCase().endsWith(".png")) return true;
       if (f.getName().toLowerCase().endsWith(".ping")) return true;
       return false;
       }
     public String getDescription()
       {
       return "JPEG files";
       }
   
    }