package flinders.mandelbrot;

import java.awt.image.BufferedImage;

/**
 * Mandelbrot renderer.
 */
public class MandelProcessor {
    
    public enum ComputeMode { JAVA_SINGLE, JAVA_MULTI };
    
    private BufferedImage image;
        
    public void compute(MandelSetting s, ComputeMode m) {
        switch (m) {
            case JAVA_SINGLE:
                compute_java_single(s);
                break;
            case JAVA_MULTI:
                compute_java_multi(s);
                break;           
        }
    }
    
    private void compute_java_single(MandelSetting s) {
        System.out.println("Rendering Mandelbrot set with: R in [" + s.getMinReal() + "," + s.getMaxReal()
                           + "], I in [" + s.getMinImaginary() + "," + s.getMaxImaginary() 
		           + "], maximum iterations: " + s.getMaxIterations() + " ...");

        image = new BufferedImage(s.getWidth(), s.getHeight(), BufferedImage.TYPE_INT_RGB);

        // generate array of C values that are to be processed
        for (int y = 0; y < s.getHeight(); y++) {
            for (int x = 0; x < s.getWidth(); x++) {
                double C_real = s.getMinReal() + (x * (s.getMaxReal() - s.getMinReal())) / (s.getWidth() - 1);
                double C_imaginary = s.getMinImaginary() + (y * (s.getMaxImaginary() - s.getMinImaginary())) / (s.getHeight() - 1);
                ComplexNumber C = new ComplexNumber(C_real, C_imaginary);
                ComplexNumber Z = new ComplexNumber(0, 0);
                int iterations = 0;
                while ((Z.abs() <= 2) && (iterations < s.getMaxIterations())){
                    Z.multiply(Z); // Z = Z*Z
                    Z.add(C); // Z = Z+C
                    iterations++;
                }
                if (iterations != s.getMaxIterations()) { // convert to colour image (change this if you don't like the colours)
                    double c = (double)(iterations - log2(log2(Z.abs())))/s.getMaxIterations();
                    int rgb = (int)(Math.sin(c*8*Math.PI) * 127) + 128 << 16;
                    rgb |= (int)(Math.sin(c*8*Math.PI+5) * 127) + 128 << 8;
                    rgb |= (int)(Math.sin(c*8*Math.PI+10) * 127) + 128;
                    image.setRGB(x, y, rgb);
                }
            }
        }
        
        System.out.println("Render complete (" + s.getWidth() + "x" + s.getHeight() + " pixels)");
    }
    
    private void compute_java_multi(MandelSetting s) {
        System.err.println("Java Multithreaded Code: NOT IMPLEMENTED");
        image = null;
    }
    
    // utlity code ---
    
    BufferedImage getLastImage() {
        return image;
    }
    
    double log2(double x) {
        return Math.log(x)/Math.log(2);
    }
    
}
