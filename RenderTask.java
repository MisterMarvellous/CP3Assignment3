/*public class RenderTask extends javax.swing.SwingWorker<Void, Integer> {
    private MandelSetting o;
    private MandelProcessor.ComputeMode computeMode;
    private MandelProcessListing l;
    private MandelProcessor mandelProcessor;
    public RenderTask(MandelSetting o, MandelProcessor.ComputeMode computeMode, MandelProcessListing l, MandelProcessor mp) {
	this.o = o;
	this.computeMode = computeMode;
	this.l = l;
	mandelProcessor = mp;
    }
    
    protected Void doInBackground() {
	mandelProcessor.compute(o, computeMode, this);
	o.setImage(mandelProcessor.getLastImage());
	return null;
    }
    
    public void showValue(int value) {
	System.out.println("helloooooo");
	publish(value);
    }

    protected void process(Integer value) {
	l.setBarProgress(value, 0);
    }
}*/