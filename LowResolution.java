import ij.ImagePlus;
import ij.IJ;
import ij.process.ImageProcessor;
import ij.process.ColorProcessor;
import ij.plugin.filter.PlugInFilter;
import java.awt.Color;
import java.util.ArrayList;

public class LowResolution implements PlugInFilter{
    
    ImagePlus inputImage;

    public int setup(String args, ImagePlus im) {
		inputImage = im;
		return DOES_RGB;
	}

    public void run(ImageProcessor ip) 
    {
        String inputTitle = inputImage.getShortTitle();
        int width = ip.getWidth(), height = ip.getHeight();
        ImageProcessor outputIP = new ColorProcessor(width / 5, height / 5);
        
        int r, g, b;
        Color color;
        int colOut, rowOut;
        ArrayList<Integer> meanR = new ArrayList<Integer>();
        ArrayList<Integer> meanG = new ArrayList<Integer>();
        ArrayList<Integer> meanB = new ArrayList<Integer>();
        int row = 0, col = 0;
        
        int sumr = 0, sumg = 0, sumb = 0;
        int meanr = 0, meang = 0, meanb = 0;

        while (col < width)
        {
            while (row < height)
            {
                for (int coln = col; coln < col + 5; coln++)
                {
                    for (int rown = row; rown < row + 5; rown++)
                    {
                        color = new Color(ip.getPixel(coln, rown));
				        r = color.getRed();
				        g = color.getGreen();
				        b = color.getBlue();

                        sumr += r;
                        sumg += g;
                        sumb += b;
                    }
                }
                row += 5;
                meanr = sumr / 25;
                meang = sumg / 25;
                meanb = sumb / 25;

                meanR.add(meanr);
                meanG.add(meang);
                meanB.add(meanb);

                sumr = 0;
                sumg = 0;
                sumb = 0;
                meanr = 0;
                meang = 0;
                meanb = 0;
            }
            col += 5;
            row = 0;
        }

        int l = 0;
        for (colOut = 0; colOut < width / 5; colOut++)
            for (rowOut = 0; rowOut < height / 5; rowOut++)
            {
                int[] rgb = {meanR.get(l), meanG.get(l), meanB.get(l)};
                outputIP.putPixel(colOut, rowOut, rgb);
                l++;
            }

        (new ImagePlus("low_res" + " " + inputTitle, outputIP)).show(); 
    }

}
