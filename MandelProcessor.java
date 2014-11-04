import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Mandelbrot renderer.
 */
public class MandelProcessor {
    
    public enum ComputeMode { JAVA_SINGLE, JAVA_MULTI };
    
    private BufferedImage image;
    private MandelProcessListing l;
    private MandelBatchGUI g;
    private long t;
    private int tasksComplete;
    private static java.util.concurrent.ExecutorService threadPool;
        
    public MandelProcessor(long beginTime) {
	t = beginTime;
    }

    public void compute(MandelSetting s, ComputeMode m, MandelProcessListing listing, MandelBatchGUI g, java.util.concurrent.ExecutorService pool) {
	l = listing;
	this.g = g;
	threadPool = pool;
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

	final MandelSetting sf = s;

	final javax.swing.SwingWorker<Void, Integer> renderWorker = new javax.swing.SwingWorker<Void, Integer>() {
	    
	    protected Void doInBackground() {

		l.setProcessing();

		int counter = 0;

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
			if (isCancelled()) { break; }
			counter++;
			publish((counter*100)/(sf.getHeight()*sf.getWidth()));
			//System.out.println("Got to show value");
		    }
		    if (isCancelled()) { break; }
		}
		if (isCancelled()) { System.out.println("Render cancelled at " + (counter*100)/(sf.getHeight()*sf.getWidth()) + "%"); }
		else {
		    System.out.println("Render complete (" + sf.getWidth() + "x" + sf.getHeight() + " pixels)");
		    sf.setImage(image);
		}
		return null;
	    }
	    
	    protected void process(List<Integer> progress) {
		int value = progress.get(progress.size()-1);
		l.setBarProgress(value, 0);
	    }

	    protected void done() {
		g.updateWithTime(System.currentTimeMillis()-t);
		g.refreshImage();
		l.setIdle();
	    }
	};

	renderWorker.execute();	
        
	l.getCancelButton().addActionListener(new java.awt.event.ActionListener() {
	    public void actionPerformed(java.awt.event.ActionEvent e) {
		if (l.getProcessing()) {
		    renderWorker.cancel(false);
		}
		l.setVisible(false);
	    }
	});
    }
    
    private void compute_java_multi(MandelSetting s) {
	
	final MandelSetting sf = s;
	tasksComplete = 0;
	image = new BufferedImage(sf.getWidth(), sf.getHeight(), BufferedImage.TYPE_INT_RGB);
	final Object imageLock = new Object();

	class RenderMultiTask extends javax.swing.SwingWorker<Void, Integer> { 
	 
	    private int offset;
	    private int width;
	    private int threadNo;
	    
	    public RenderMultiTask(int offset, int width, int threadNo) {
		this.offset = offset;
		this.width = width;
		this.threadNo = threadNo;
	    }
   
	    protected Void doInBackground() {
		
		l.setProcessing();
		
		int counter = 0;
		
		System.out.println("Rendering Mandelbrot set with: R in [" + sf.getMinReal() + "," + sf.getMaxReal()
				   + "], I in [" + sf.getMinImaginary() + "," + sf.getMaxImaginary() 
				   + "], maximum iterations: " + sf.getMaxIterations() + " ...");
				
		// generate array of C values that are to be processed
		for (int y = (offset/sf.getWidth()); y < sf.getHeight(); y++) {
		    for (int x = (counter==0?(offset-(y*sf.getWidth())):0); x < sf.getWidth(); x++) {
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
			if (isCancelled() || ++counter>=width) { break; }
			publish((counter*100)/(width));
			//System.out.println("Got to show value");
		    }
		    if (isCancelled() || counter>=width) { break; }
		}
		if (isCancelled()) { System.out.println("Render cancelled at " + (counter*100)/(sf.getHeight()*sf.getWidth()) + "%"); }
		else {
		    System.out.println("Render segment complete of (" + sf.getWidth() + "x" + sf.getHeight() + " pixels)");
		}
		return null;
	    }
	    
	    protected void process(List<Integer> progress) {
		int value = progress.get(progress.size()-1);
		l.setBarProgress(value, threadNo);
	    }
	    
	    protected void done() {
		g.updateWithTime(System.currentTimeMillis()-t);
		if (increaseTasksComplete() >= g.getProcessors()) {
		    sf.setImage(image);
		    g.refreshImage();
		    l.setIdle();
		}
	    }
	}
	
	final RenderMultiTask renderWorkers[] = new RenderMultiTask[g.getProcessors()];
	int o;
	int w;
	for (int i = 0; i < renderWorkers.length; i++) {
	    o = (i*(sf.getWidth()*sf.getHeight()))/renderWorkers.length;
	    w = (((i+1)*(sf.getWidth()*sf.getHeight()))/renderWorkers.length)-o;
	    renderWorkers[i] = new RenderMultiTask(o, w, i); 
	    threadPool.submit(renderWorkers[i]);
	}
        
	l.getCancelButton().addActionListener(new java.awt.event.ActionListener() {
	    public void actionPerformed(java.awt.event.ActionEvent e) {
		if (l.getProcessing()) {
		    for (int i = 0; i < renderWorkers.length; i++) {
			renderWorkers[i].cancel(false);
		    }
		}
		l.setVisible(false);
	    }
	});
        /*System.err.println("Java Multithreaded Code: NOT IMPLEMENTED");
	  image = null;*/
    }
    
    // utlity code ---
    
    BufferedImage getLastImage() {
        return image;
    }

    private int increaseTasksComplete() {
	return ++tasksComplete;
    }
    
    double log2(double x) {
        return Math.log(x)/Math.log(2);
    }
    
}
