public class MandelProcessListing extends javax.swing.JPanel {
    private String processName;
    private int threadsNo;
    private javax.swing.JLabel processNameLabel;
    private javax.swing.JProgressBar[] progressBars;
    private javax.swing.JButton cancelButton;
    private java.awt.GridBagLayout layout;
    private java.awt.GridBagConstraints c;
    private java.awt.GridBagConstraints outerConstraints;
    private boolean processing;
    private MandelProcessor.ComputeMode computeMode;

    public MandelProcessListing(String name, MandelProcessor.ComputeMode computeMode, MandelBatchGUI g, java.awt.GridBagConstraints outerConstraints) {
	this.outerConstraints = outerConstraints;
	processName = name;
	this.computeMode = computeMode;
	threadsNo = (this.computeMode==MandelProcessor.ComputeMode.JAVA_SINGLE?1:g.getProcessors());
	processNameLabel = new javax.swing.JLabel();
	progressBars = new javax.swing.JProgressBar[threadsNo];
	cancelButton = new javax.swing.JButton();
	layout = new java.awt.GridBagLayout();
	c = new java.awt.GridBagConstraints();

	setBorder(javax.swing.BorderFactory.createRaisedBevelBorder());
	
	processNameLabel.setText(processName);

	setLayout(layout);
	c.gridwidth = 2;
	c.anchor = java.awt.GridBagConstraints.LINE_START;
	c.insets = new java.awt.Insets(3, 7, 3, 3);
	add(processNameLabel, c);


	c.insets = new java.awt.Insets(3, 3, 3, 3);
	c.gridwidth = 1;
	c.fill = java.awt.GridBagConstraints.BOTH;
	c.weightx = 1.0;
	for (int i = 0; i < progressBars.length; i++) {
	    progressBars[i] = new javax.swing.JProgressBar();
	    c.gridy = i+1;
	    add(progressBars[i], c);
	}

	c.gridx = 1;
	c.gridheight = 2;
	c.fill = java.awt.GridBagConstraints.NONE;
	c.anchor = java.awt.GridBagConstraints.LINE_END;
	cancelButton.setText("Cancel");
	java.awt.Dimension d = new java.awt.Dimension(70, 27);
	cancelButton.setMinimumSize(d);
	cancelButton.setMaximumSize(d);
	cancelButton.setPreferredSize(d);
	add(cancelButton, c);
	
    }

    public void setBarProgress(int progress, int bar) {
	if (bar < progressBars.length) {
	    //System.out.println(processName + " at " + progress + "%");
	    progressBars[bar].setValue(progress);
	}
    }
    
    public void setProcessing() {
	processing = true;
	updateButton();
    }

    public void setIdle() {
	processing = false;
	updateButton();
    }

    private void updateButton() {
	cancelButton.setText(processing?"Cancel":"OK");
    }

    public javax.swing.JButton getCancelButton() {
	return cancelButton;
    }

    public boolean getProcessing() {
	return processing;
    }

    public MandelProcessor.ComputeMode getComputeMode() {
	return computeMode;
    }

    public java.awt.GridBagConstraints getOuterGridBagConstraints() {
	return outerConstraints;
    }

}