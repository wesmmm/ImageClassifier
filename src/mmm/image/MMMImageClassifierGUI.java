
package mmm.image;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javax.media.jai.InterpolationNearest; 
import javax.media.jai.JAI;
import javax.media.jai.iterator.RandomIter;
import javax.media.jai.iterator.RandomIterFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.sun.media.jai.widget.DisplayJAI;
/**
 * This class uses a very simple, naive similarity algorithm to compare an image
 * with all others in the same directory.
 */
public class MMMImageClassifierGUI extends JFrame
  {
  // The reference image "signature" (25 representative pixels, each in R,G,B).
  // We use instances of Color to make things simpler.
  private Color[][] signature;
  // The base size of the images.
  private static final int BASESIZE = 300;
  // These should be the same value 5 and not larger
  private static final int SAMPLING_PIXEL_WIDTH = 5;
  private static final int SAMPLING_PIXEL_HEIGHT = SAMPLING_PIXEL_WIDTH;

  
  private static final double PIXEL_COLOR_WEIGHT_DEFAULT = 1.00;
  private static final double EYE_WIDTH_WEIGHT_DEFAULT = 0.0;
  private static final double NOSE_HEIGHT_WEIGHT_DEFAULT = 0.0;

  
 /*
  * The constructor, which creates the GUI and start the image processing task.
  */
  public MMMImageClassifierGUI(File reference) throws IOException
    {
    // Create the GUI
    super("MMM Image Classifier ... ");
    Container cp = getContentPane();
    cp.setLayout(new BorderLayout());
    // Put the reference, scaled, in the left part of the UI.
    RenderedImage ref = rescale(ImageIO.read(reference), BASESIZE);
    cp.add(new DisplayJAI(ref), BorderLayout.WEST);
    // Calculate the signature vector for the reference.
    signature = calcSignature(ref, SAMPLING_PIXEL_HEIGHT, SAMPLING_PIXEL_WIDTH);
    // Now we need a component to store X images in a stack, where X is the
    // number of images in the same directory as the original one.
    File[] others = getOtherImageFiles(reference);
    JPanel otherPanel = new JPanel(new GridLayout(others.length, 2));
    cp.add(new JScrollPane(otherPanel), BorderLayout.CENTER);
    // For each image, calculate its signature and its distance from the
    // reference signature.
    RenderedImage[] rothers = new RenderedImage[others.length];
    double[] distances = new double[others.length];
    for (int o = 0; o < others.length; o++)
      {
      rothers[o] = rescale(ImageIO.read(others[o]), BASESIZE);
      distances[o] = calcDistance(rothers[o], SAMPLING_PIXEL_HEIGHT, SAMPLING_PIXEL_WIDTH);
      }
    // Sort those vectors *together*.
    for (int p1 = 0; p1 < others.length - 1; p1++)
      for (int p2 = p1 + 1; p2 < others.length; p2++)
        {
        if (distances[p1] > distances[p2])
          {
          double tempDist = distances[p1];
          distances[p1] = distances[p2];
          distances[p2] = tempDist;
          RenderedImage tempR = rothers[p1];
          rothers[p1] = rothers[p2];
          rothers[p2] = tempR;
          File tempF = others[p1];
          others[p1] = others[p2];
          others[p2] = tempF;
          }
        }
    // Add them to the UI.
    for (int o = 0; o < others.length; o++)
      {
      otherPanel.add(new DisplayJAI(rothers[o]));
      JLabel ldist = new JLabel("<html>" + others[o].getName() + "<br>"
          + String.format("% 13.3f", distances[o]) + "</html>");
      ldist.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 36));
      System.out.println("Image Name: " + others[o].getName() + ", distance to query: " + distances[o]);
      
      otherPanel.add(ldist);
      }
    // More GUI details.
    pack();
    setVisible(true);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

  
  /*
   * The constructor, which creates the GUI and start the image processing task.
   * Construct GUI using weighted features. These features include the eye_width 
   * and the nose_height - of the image being queried or the reference image
   */
   public MMMImageClassifierGUI(File reference, double reference_eye_width, double reference_nose_height) throws IOException
     {
     // Create the GUI
     super("MMM Image Classifier ... ");
     Container cp = getContentPane();
     cp.setLayout(new BorderLayout());
     // Put the reference, scaled, in the left part of the UI.
     RenderedImage ref = rescale(ImageIO.read(reference), BASESIZE);
     cp.add(new DisplayJAI(ref), BorderLayout.WEST);
     // Calculate the signature vector for the reference.
     signature = calcSignature(ref, SAMPLING_PIXEL_HEIGHT, SAMPLING_PIXEL_WIDTH);
     // Now we need a component to store X images in a stack, where X is the
     // number of images in the same directory as the original one.
     File[] others = getOtherImageFiles(reference);
     JPanel otherPanel = new JPanel(new GridLayout(others.length, 2));
     cp.add(new JScrollPane(otherPanel), BorderLayout.CENTER);
     // For each image, calculate its signature and its distance from the
     // reference signature.
     RenderedImage[] rothers = new RenderedImage[others.length];
     double[] distances = new double[others.length];
     double rother_eye_width = 0.0;
     double rother_nose_height = 0.0;
    		 
     for (int o = 0; o < others.length; o++)
       {
    	 // for each image calculate or extract the feature values of eye width and nose height
    	 // remember these should not be scaled as done in the rescale method below
    	 rothers[o] = rescale(ImageIO.read(others[o]), BASESIZE);
    	 distances[o] = calcDistanceWithWeightedFeatures(rothers[o], SAMPLING_PIXEL_HEIGHT, SAMPLING_PIXEL_WIDTH, reference_eye_width, 
    		   reference_nose_height, rother_eye_width, rother_nose_height);
       }
     // Sort those vectors *together*.
     for (int p1 = 0; p1 < others.length - 1; p1++)
       for (int p2 = p1 + 1; p2 < others.length; p2++)
         {
         if (distances[p1] > distances[p2])
           {
           double tempDist = distances[p1];
           distances[p1] = distances[p2];
           distances[p2] = tempDist;
           RenderedImage tempR = rothers[p1];
           rothers[p1] = rothers[p2];
           rothers[p2] = tempR;
           File tempF = others[p1];
           others[p1] = others[p2];
           others[p2] = tempF;
           }
         }
     // Add them to the UI.
     for (int o = 0; o < others.length; o++)
       {
       otherPanel.add(new DisplayJAI(rothers[o]));
       JLabel ldist = new JLabel("<html>" + others[o].getName() + "<br>"
           + String.format("% 13.3f", distances[o]) + "</html>");
       ldist.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 36));
       System.out.println("Image Name: " + others[o].getName() + ", distance to query: " + distances[o]);
       
       otherPanel.add(ldist);
       }
     // More GUI details.
     pack();
     setVisible(true);
     setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     }
   
 /*
  * This method rescales an image to 300,300 pixels using the JAI scale
  * operator.
  */
  private RenderedImage rescale(RenderedImage i, int basesize)
    {
    float scaleW = ((float) basesize) / i.getWidth();
    float scaleH = ((float) basesize) / i.getHeight();
    // Scales the original image
    ParameterBlock pb = new ParameterBlock();
    pb.addSource(i);
    pb.add(scaleW);
    pb.add(scaleH);
    pb.add(0.0F);
    pb.add(0.0F);
    pb.add(new InterpolationNearest());
    // Creates a new, scaled image and uses it on the DisplayJAI component
    return JAI.create("scale", pb);
    }
  
 /*
  * This method calculates and returns signature vectors for the input image.
  */
  private Color[][] calcSignature(RenderedImage i, int pixel_height, int pixel_width)
    {
    // Get memory for the signature.
    Color[][] sig = new Color[pixel_height][pixel_width];
    // For each of the 25 signature values average the pixels around it.
    // Note that the coordinate of the central pixel is in proportions.
    float[] prop = new float[]
      {1f / 10f, 3f / 10f, 5f / 10f, 7f / 10f, 9f / 10f};
    for (int x = 0; x < pixel_height; x++)
      for (int y = 0; y < pixel_width; y++)
        sig[x][y] = averageAround(i, prop[x], prop[y]);
    return sig;
    }

 /*
  * This method averages the pixel values around a central point and return the
  * average as an instance of Color. The point coordinates are proportional to
  * the image.
  */
  private Color averageAround(RenderedImage i, double px, double py)
    {
    // Get an iterator for the image.
    RandomIter iterator = RandomIterFactory.create(i, null);
    // Get memory for a pixel and for the accumulator.
    double[] pixel = new double[3];
    double[] accum = new double[3];
    // The size of the sampling area.
    int sampleSize =  3 * SAMPLING_PIXEL_WIDTH;
    int numPixels = 0;
    // Sample the pixels.
    for (double x = px * BASESIZE - sampleSize; x < px * BASESIZE + sampleSize; x++)
      {
      for (double y = py * BASESIZE - sampleSize; y < py * BASESIZE + sampleSize; y++)
        {
        iterator.getPixel((int) x, (int) y, pixel);
        accum[0] += pixel[0];
        accum[1] += pixel[1];
        accum[2] += pixel[2];
        numPixels++;
        }
      }
    // Average the accumulated values.
    accum[0] /= numPixels;
    accum[1] /= numPixels;
    accum[2] /= numPixels;
    return new Color((int) accum[0], (int) accum[1], (int) accum[2]);
    }

 /*
  * This method calculates the distance between the signatures of an image and
  * the reference one. The signatures for the image passed as the parameter are
  * calculated inside the method.
  */
  private double calcDistance(RenderedImage other, int pixel_height, int pixel_width)
    {
    // Calculate the signature for that image.
    Color[][] sigOther = calcSignature(other, pixel_height, pixel_width);
    // There are several ways to calculate distances between two vectors,
    // we will calculate the sum of the distances between the RGB values of
    // pixels in the same positions.
    double dist = 0;
    for (int x = 0; x < pixel_height; x++)
      for (int y = 0; y < pixel_width; y++)
        {
        int r1 = signature[x][y].getRed();
        int g1 = signature[x][y].getGreen();
        int b1 = signature[x][y].getBlue();
        int r2 = sigOther[x][y].getRed();
        int g2 = sigOther[x][y].getGreen();
        int b2 = sigOther[x][y].getBlue();
        double tempDist = Math.sqrt((r1 - r2) * (r1 - r2) + (g1 - g2)
            * (g1 - g2) + (b1 - b2) * (b1 - b2));
        dist += tempDist;
        }
    return dist;
    }

  /*
   * This method calculates the distance between the signatures of an image and
   * the reference one. The signatures for the image passed as the parameter are
   * calculated inside the method.
   * 
   * In addition it also adds in the distance between the reference image and the current one for the eye width and the nose height
   * These are the weighted features
   * 
   * Then scales the distance based on the weights of pixel_color, eye_width, and nose_height.  The totals of these weights should be 1.00. 
   * 
   * 
   */
   private double calcDistanceWithWeightedFeatures(RenderedImage other, int pixel_height, int pixel_width, double reference_eye_width, 
		   double reference_nose_height, double rother_eye_width, double rother_nose_height)
     {
     // Calculate the signature for that image.
     Color[][] sigOther = calcSignature(other, pixel_height, pixel_width);
     // There are several ways to calculate distances between two vectors,
     // we will calculate the sum of the distances between the RGB values of
     // pixels in the same positions.
     double dist = 0;
     for (int x = 0; x < pixel_height; x++)
       for (int y = 0; y < pixel_width; y++)
         {
         int r1 = signature[x][y].getRed();
         int g1 = signature[x][y].getGreen();
         int b1 = signature[x][y].getBlue();
         int r2 = sigOther[x][y].getRed();
         int g2 = sigOther[x][y].getGreen();
         int b2 = sigOther[x][y].getBlue();
         double tempDist = Math.sqrt((r1 - r2) * (r1 - r2) + (g1 - g2)
             * (g1 - g2) + (b1 - b2) * (b1 - b2));
         dist += tempDist;
         }
     
     double pixel_color_dist_with_weights =  (dist * PIXEL_COLOR_WEIGHT_DEFAULT);
     double eye_width_with_weights = Math.abs(reference_eye_width - rother_eye_width) * EYE_WIDTH_WEIGHT_DEFAULT;
     double nose_height_with_weights = Math.abs(reference_nose_height - rother_nose_height) * NOSE_HEIGHT_WEIGHT_DEFAULT;
     System.out.println("Calcualted weighted Pixel Color: " + pixel_color_dist_with_weights);
     System.out.println("Calculated weighted Eye Width: " + eye_width_with_weights);
     System.out.println("Calculated weighted Nose Height: " + nose_height_with_weights);
     return (pixel_color_dist_with_weights + eye_width_with_weights + nose_height_with_weights);
     }

   
 /*
  * This method get all image files in the same directory as the reference.
  * Just for kicks include also the reference image.
  */
  private File[] getOtherImageFiles(File reference)
    {
    File dir = new File(reference.getParent());
    // List all the image files in that directory.
    //File[] others = dir.listFiles(new JPEGImageFileFilter());
    File[] others = dir.listFiles(new MultiImageFileFilter());

    return others;
    }


  
  }
