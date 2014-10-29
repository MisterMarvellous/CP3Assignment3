import java.awt.image.BufferedImage;

/**
 * Mandelbrot renderer.
 */
public class MandelProcessor {
    
    public enum ComputeMode { JAVA_SINGLE, JAVA_MULTI };
    
    private BufferedImage image;
    private int counter;
    private MandelProcessListing l;
    private MandelBatchGUI g;
        
    public void compute(MandelSetting s, ComputeMode m, MandelProcessListing l, MandelBatchGUI g) {
        switch (m) {
            case JAVA_SINGLE:
                compute_java_single(s);
                break;
            case JAVA_MULTI:
                compute_java_multi(s);
                break;           
        }
	this.l = l;
	this.g = g;
	counter = 0;
    }

    private void compute_java_single(MandelSetting s) {

	final MandelSetting sf = s;

	javax.swing.SwingWorker<Void, Integer> renderWorker = new javax.swing.SwingWorker<Void, Integer>() { 
	    
	    protected Void doInBackground() {

		System.out.println("Rendering Mandelbrot set with: R in [" + sf.getMinReal() + "," + sf.getMaxReal()
				   + "], I in [" + sf.getMinImaginary() + "," + sf.getMaxImaginary() 
				   + "], maximum iterations: " + sf.getMaxIterations() + " ...");
		
		image = new BufferedImage(sf.getWidth(), sf.getHeight(), BufferedImage.TYPE_INT_RGB);
		
		// generate array of C values that are to be processed
		for (int y = 0; y < sf.getHeight(); y++) {
		    for (int x = 0; x < sf.getWidth(); x++) {
			double C_real = sf.getMinReal() + (x * (sf.getMaxReal() - sf.getMinReal())) / (sf.getWidth() - 1);
			double C_imaginary = sf.getMinImaginary() + (y * (sf.getMaxImaginary() - sf.getMinImaginary())) / (sf.getHeight() - 1);
			ComplexNumber C = new ComplexNumber(C_real, C_imaginary);
			ComplexNumber Z = new ComplexNumber(0, 0);
			int iterations = 0;
			while ((Z.abs() <= 2) && (iterations < sf.getMaxIterations())){
			    Z.multiply(Z); // Z = Z*Z
			    Z.add(C); // Z = Z+C
			    iterations++;
			}
			if (iterations != sf.getMaxIterations()) { // convert to colour image (change this if you don't like the colours)
			    double c = (double)(iterations - log2(log2(Z.abs())))/sf.getMaxIterations();
			    int rgb = (int)(Math.sin(c*8*Math.PI) * 127) + 128 << 16;
			    rgb |= (int)(Math.sin(c*8*Math.PI+5) * 127) + 128 << 8;
			    rgb |= (int)(Math.sin(c*8*Math.PI+10) * 127) + 128;
			    image.setRGB(x, y, rgb);
			}
			counter++;
			System.out.println("Counter: " + counter);
			publish((counter)/(sf.getHeight()+sf.getWidth()));
			System.out.println("Got to show value");
		    }
		}
		
		System.out.println("Render complete (" + sf.getWidth() + "x" + sf.getHeight() + " pixels)");
		sf.setImage(image);
		return null;
	    }
	    
	    protected void process(Integer value) {
		l.setBarProgress(value, 0);
	    }

	    protected void done() {
		g.refreshImage();
	    }
	};

	renderWorker.execute();	
        
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
