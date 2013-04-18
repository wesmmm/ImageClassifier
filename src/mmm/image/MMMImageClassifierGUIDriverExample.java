package mmm.image;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class MMMImageClassifierGUIDriverExample {

	  // Where are all the files?
	  private static final String basePath = 
	    "/Users/wes/Documents/project/data/BB_IMAGES";
	  
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
	    	
	    	MMMImageClass ic_asian_wide_eyes = new MMMImageClass(basePath + "/asian_wide_eyes.png", "Asian_Wide_Eyes");
	    	MMMImageClass ic_asian_wide_eyes_long_face = new MMMImageClass(basePath + "/asian_wide_eyes_long_face.png", "Asian_Wide_Eyes_Long_Face");
	    	MMMImageClass ic_asian_wide_mouth = new MMMImageClass(basePath + "/asian_wide_mouth.png", "Asian_Wide_Mouth");
	    	MMMImageClass ic_black_indian_1 = new MMMImageClass(basePath + "/black_indian_1.png", "Black_Indian_1");
	    	MMMImageClass ic_black_round_lips = new MMMImageClass(basePath + "/black_round_lips.png", "Black_Round_Lips");
	    	MMMImageClass ic_black_wide_nose = new MMMImageClass(basePath + "/black_wide_nose.png", "Black_Wide_Nose");
	    	MMMImageClass ic_blonde_hsm = new MMMImageClass(basePath + "/blonde_hair_short_mouth.png", "Blonde_Hair_Short_Mouth");
	    	MMMImageClass ic_blonde_l = new MMMImageClass(basePath + "/blonde_long.png", "Blonde_Long");
	    	MMMImageClass ic_blonde_mn = new MMMImageClass(basePath + "/blonde_medium_nose.png", "Blonde_Medium_Nose");
	    	MMMImageClass ic_blonde_mn2 = new MMMImageClass(basePath + "/blonde_medium_nose_2.png", "Blonde_Medium_Nose_2");
	    	MMMImageClass ic_blonde_wm = new MMMImageClass(basePath + "/blonde_wide_mouth.png", "Blonde_Wide_Mouth");
	    	MMMImageClass ic_blonde_wm2 = new MMMImageClass(basePath + "/blonde_wide_mouth_2.png", "Blonde_Wide_Mouth_2");
	    	MMMImageClass ic_brunette_ne = new MMMImageClass(basePath + "/brunette_narrow_eyes.png", "Brunette_Narrow_Eyes");
	    	MMMImageClass ic_brunette_nn = new MMMImageClass(basePath + "/brunette_narrow_nose.png", "Brunette_Narrow_Nose");
	    	MMMImageClass ic_brunette_wm2 = new MMMImageClass(basePath + "/brunette_wide_mouth_2.png", "Brunette_Wide_Mouth_2");
	    	MMMImageClass ic_curly_hbf = new MMMImageClass(basePath + "/curly_hair_black_face.png", "Curly_Hair_Black_face");
	    	MMMImageClass ic_dark_hl = new MMMImageClass(basePath + "/dark_hair_long.png", "Dark_Hair_Long");
	    	MMMImageClass ic_dark_short_wn = new MMMImageClass(basePath + "/dark_short_hair_wide_nose.png", "Dark_Short_Hair_Wide_Nose");
	    	MMMImageClass ic_grey_hair_b = new MMMImageClass(basePath + "/grey_hair_black.png", "Grey_Hair_Black");
	    	MMMImageClass ic_grey_hair_we = new MMMImageClass(basePath + "/grey_hair_wide_eyes.png", "Grey_Hair_Wide_Eyes");
	    	MMMImageClass ic_red_hbf = new MMMImageClass(basePath + "/red_hair_black_face.png", "Red_Hair_Black_Face");
	    	MMMImageClass ic_red_h = new MMMImageClass(basePath + "/red_head.png", "Red_Head");

	    	File file = fc.getSelectedFile();
	    	new MMMImageClassifierGUI(file);
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
