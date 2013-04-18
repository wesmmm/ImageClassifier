package mmm.image;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class MMMImageClassifierWeightedFeaturesGUIDriverExample {

	  // Where are all the files?
	  private static final String basePath = 
	    "/Users/wes/Documents/project/data/BB_IMAGES";
	  
	  private static final double eyes_width_in_mm = 62.3;
	  private static final double nose_height_in_mm =  39.5;
	  
	  // http://en.wikipedia.org/wiki/Interpupillary_distance
	  
	 /*
	  * The entry point for the application, which opens a file with an image that
	  * will be used as reference and starts the application.
	  */
	  public static void main(String[] args) throws IOException
	    {
	    JFileChooser fc = new JFileChooser(basePath);
	    //fc.setFileFilter(new JPEGImageFileFilter());
	    fc.setFileFilter(new MultiImageFileFilter());
	    int res = fc.showOpenDialog(null);
	    // We have an image!
	    if (res == JFileChooser.APPROVE_OPTION)
	      {
	      File file = fc.getSelectedFile();
	      new MMMImageClassifierGUI(file, eyes_width_in_mm, nose_height_in_mm);
	      }
	    // Oops!
	    else
	      {
	      JOptionPane.showMessageDialog(null,
	          "You must select one image to be the reference query.", "Aborting...",
	          JOptionPane.WARNING_MESSAGE);
	      }
	    }
}
